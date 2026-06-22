# Virtual Threads — Demo do Loom (Plataforma vs. Virtual)

Runbook passo a passo para demonstrar a diferença entre **platform threads**
(threads tradicionais da JVM) e **virtual threads** (Project Loom, JDK 21) sob
carga concorrente de I/O bloqueante.

## O que a demo mostra

Dois serviços idênticos rodam o mesmo endpoint de carga, mas com pools de threads
diferentes:

- **`clojure-api`** (porta `9000`, `THREAD_MODE=platform`): cada tarefa bloqueante
  consome uma **platform thread** real do sistema operacional. Sob alta
  concorrência, o número de threads explode e a latência cresce, pois o pool fica
  saturado (limitado a 200 threads) e as tarefas precisam esperar por uma thread
  livre.
- **`clojure-api-vt`** (porta `9002`, `THREAD_MODE=virtual`): cada tarefa roda em
  uma **virtual thread**. Milhares de tarefas bloqueantes podem rodar
  simultaneamente sem inflar o número de threads de plataforma, porque a JVM
  desacopla a virtual thread da carrier thread durante o bloqueio de I/O.

O endpoint usado nos dois serviços é:

```
GET /load?mode=platform|virtual&tasks=<int>&sleepMs=<int>
```

Ele dispara `tasks` jobs bloqueantes (cada um dorme `sleepMs` milissegundos) no
pool de threads selecionado por `mode`, simulando I/O bloqueante concorrente, e
bloqueia até o lote inteiro terminar (`invokeAll`). A implementação está em
`clojure-api/src/com/code4nimbus/clojureapi/logic/loom.clj`:

- `mode=platform` → `Executors/newFixedThreadPool` (máx. 200 threads de SO).
- `mode=virtual`  → `Executors/newVirtualThreadPerTaskExecutor` (1 virtual thread
  por job).

> Importante: só o **fan-out interno** dos jobs usa o executor configurável. O
> servidor HTTP (httpkit) é o mesmo nos dois serviços — ou seja, a demo isola a
> diferença no ponto onde o I/O bloqueante acontece.

## Limite de CPU (para ver throttling)

No `docker-compose.yml`, ambos os serviços têm `cpus: 1.0` (1 núcleo). Isso é o
que torna o **CPU throttling** visível sob carga: quando o container estoura seu
núcleo, o kernel (CFS) o bloqueia, e o cAdvisor reporta esse tempo bloqueado.

## Observabilidade

O `docker-compose.yml` sobe a stack de métricas:

- **Prometheus** (`9090`) — coleta as métricas dos dois serviços e do **cAdvisor**.
- **cAdvisor** (`8081`) — expõe métricas de cgroup por container (CPU, memória e,
  principalmente, **CPU throttling**). Precisa do socket do Docker montado para
  resolver os nomes dos containers (`/var/run/docker.sock`).
- **Grafana** (`3000`) — dashboard **"Virtual Threads — Loom Demo"**
  (uid `loom-threads`), provisionado de `grafana/dashboards/loom_threads.json`.

## Pré-requisitos

- **Docker** (com `docker compose`).
- **vegeta** para gerar a carga — veja https://github.com/tsenart/vegeta.
- **JDK 21** já vem dentro dos containers; não é necessário instalar
  Java/Clojure/lein localmente.

## 1. Build e subida dos serviços

```sh
docker compose up --build clojure-api clojure-api-vt prometheus cadvisor grafana
```

Os serviços dependem de `kafka`, `zookeeper` e `datomic-transactor` (conforme o
`docker-compose.yml`), iniciados automaticamente como dependências.

Serviços relevantes após a subida:

- `clojure-api`      → http://localhost:9000 (platform threads)
- `clojure-api-vt`   → http://localhost:9002 (virtual threads)
- Prometheus         → http://localhost:9090
- cAdvisor           → http://localhost:8081
- Grafana            → http://localhost:3000 (admin/admin)

## 2. Rodar a carga

Com os serviços no ar, dispare os dois ataques **em paralelo** (platform e
virtual ao mesmo tempo) com o script:

```sh
etc/load_test/run-both.sh
```

O script:

- verifica se o `vegeta` está instalado;
- roda os dois ataques simultaneamente (`&` + `wait`);
- grava os resultados binários (`results-platform.bin`, `results-virtual.bin`,
  ignorados pelo git);
- imprime o `vegeta report` de cada um ao final.

É possível ajustar a intensidade via variáveis de ambiente:

```sh
RATE=40 DURATION=120s etc/load_test/run-both.sh
```

## 3. Abrir o Grafana

Acesse http://localhost:3000 (usuário `admin`, senha `admin`) e abra o dashboard
**"Virtual Threads — Loom Demo"** (uid `loom-threads`), ou diretamente:
http://localhost:3000/d/loom-threads

## 4. O que observar

Compare os dois serviços lado a lado enquanto a carga roda. Painéis do dashboard:

**Threads e tarefas**
- `jvm_threads_current` — platform threads da JVM. **Dispara** no modo platform;
  fica **estável e baixo** no virtual (poucas carriers carregam milhares de
  virtual threads).
- `loom_inflight_tasks{mode}` — tarefas concorrentes em andamento por modo.
- `loom_batch_seconds{mode}` — tempo de execução do lote por modo.

**JVM por container**
- Heap usado, memória residente (RSS) e tempo em GC — o lado virtual mantém mais
  tarefas vivas ao mesmo tempo, então tende a usar mais heap/RSS.

**CPU e throttling (cAdvisor)**
- **CPU % do limite (1 core)** — `100 * rate(container_cpu_usage_seconds_total) /
  (quota/period)`. 100% = container saturou o núcleo.
- **CPU throttling (s/s)** — `rate(container_cpu_cfs_throttled_seconds_total)`.
- **% de períodos CFS throttled** — fração dos períodos de agendamento em que o
  container bateu no teto de CPU.

**HTTP**
- **HTTP por endpoint + status** — `sum by (job,path,status)
  (rate(http_requests_total[1m]))`.
- **Status class por container (2XX/4XX/5XX)** — proporção de sucesso vs. erro.

## 5. Resultados observados

Medições em um MacBook (Docker Desktop, 6 vCPUs disponíveis à VM).

### Carga leve, sem limite de CPU — 20 req/s × 30s (600 req/lado)

| Métrica          | Platform        | Virtual        |
| ---------------- | --------------- | -------------- |
| Sucesso          | 21,67% (130)    | **100% (600)** |
| Throughput       | 2,17 req/s      | **17,38 req/s**|
| Latência média   | 26,6 s          | **1,94 s**     |
| Timeouts (30s)   | 470/600         | **0**          |

O pool de 200 platform threads satura imediatamente (cada requisição enfileira
500 jobs); o virtual absorve toda a carga sem erro. Durante o pico, o scrape do
`clojure-api` chega a ficar **DOWN** no Prometheus — o container está saturado
demais para responder até o `/metrics`.

### Carga pesada, com limite de 1 core — 40 req/s × 120s (4800 req/lado)

| Métrica          | Platform       | Virtual         |
| ---------------- | -------------- | --------------- |
| Sucesso          | 3,06% (147)    | **21,60% (1037)** |
| Throughput       | 0,98 req/s     | **6,91 req/s**  |
| Timeouts (30s)   | 4653/4800      | 3763/4800       |

Picos no meio do teste (cAdvisor):

| Métrica                 | Platform   | Virtual |
| ----------------------- | ---------- | ------- |
| CPU % do limite         | **58,8%**  | 44,7%   |
| CPU throttling (s/s)    | **0,152**  | 0,028   |
| % de períodos throttled | **17,1%**  | 8,7%    |

Com o mesmo limite de CPU, os dois lados saturam — mas o virtual entrega **~7×
mais requisições com sucesso**. O platform **queima mais CPU** (mais
context-switch entre centenas de threads de SO bloqueadas) para fazer **menos**
trabalho útil, e por isso é **throttled com ~2× mais frequência**.

> Observação: a 40 req/s × 500 jobs chegam ~20.000 jobs/s; o virtual também
> começa a estourar o timeout de 30s do vegeta porque o gargalo deixa de ser o
> número de threads e passa a ser CPU/coordenação. Para um contraste "limpo"
> (virtual perto de 100%, platform afundando), use uma taxa intermediária, ex.
> `RATE=25 DURATION=60s`.

## 6. Ajuste fino (tuning)

Os "botões" da demo controlam a intensidade e o formato da carga:

- **`tasks`** (em `etc/load_test/attack-*.txt`): número de jobs bloqueantes por
  requisição. Aumente para acentuar a saturação do pool de platform threads.
- **`sleepMs`** (em `etc/load_test/attack-*.txt`): duração do bloqueio de cada
  job. Valores maiores prolongam o I/O simulado e ampliam a diferença entre os
  modos.
- **`RATE`** (env do `run-both.sh`): requisições por segundo do vegeta.
- **`DURATION`** (env do `run-both.sh`): duração de cada ataque. Use durações
  maiores para enxergar o regime estacionário nos gráficos.

> Dica: para alterar `tasks`/`sleepMs`, edite as linhas em
> `etc/load_test/attack-platform.txt` e `etc/load_test/attack-virtual.txt`
> (mantendo `mode=platform` na porta 9000 e `mode=virtual` na porta 9002).
