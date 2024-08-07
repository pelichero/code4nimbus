# Clojure and Datomic Studies with Docker and Kafka

<p align="center">
    <img alt="Clojure Icon" height="50" src="https://upload.wikimedia.org/wikipedia/commons/thumb/5/5d/Clojure_logo.svg/256px-Clojure_logo.svg.png?20161016020557" width="50"/>
    <img alt="Datomic Icon" height="50" src="https://www.datomic.com/images/datomic-logo-290x230.png" width="60"/>
    <img alt="Docker Icon" height="50" src="https://seeklogo.com/images/D/docker-logo-6D6F987702-seeklogo.com.png" width="60"/>
    <img alt="Kafka Icon" height="50" src="https://cdn.icon-icons.com/icons2/2699/PNG/512/apache_kafka_logo_icon_167866.png" width="100"/>
</p>

Welcome to my monorepo project dedicated to exploring Clojure and Datomic while leveraging Docker and Kafka technologies. This repository documents my study path and progress as I delve into these exciting areas of software development.

## Some articles I wrote

- [Dockerized Clojure API + Datomic](https://medium.com/@xsquarenix/dockerized-clojure-api-datomic-06b37967feeb)
- [Dependency injection with Clojure](https://medium.com/@felipepelichero/dependency-injection-with-clojure-6ee5dc839c08)
- [Writing a Kafka consumer with Clojure](https://medium.com/@felipepelichero/writing-a-kafka-consumer-with-clojure-6abcd4f58919)

## Project Structure

The repository is organized into the following sections:

- `clojure-api`: Contains Web API projects built with Clojure.
- `clojure-async`: Includes asynchronous Clojure applications that interact with Kafka.
- `clojure-api-bff` : Includes a Clojure API that acts as a BFF (Backend for Frontend) for the Clojure Async application.
- `datomic-datbase`: Includes a sample Datomic database and tools to interact with it.
  - `datomic-console`: Contains Datomic Console configuration files.
- `kafka`: Contains Kafka configuration files and scripts.
  - `zookeeper`: Contains Zookeeper configuration files.
  - `kafka-ui`: Includes a Kafka UI tool to visualize topics and messages.
- `prometheus`: Contains Prometheus configuration files.
  - `kafka-exporter`: Contains Kafka exporter configuration files.
  - `jmx-exporter`: Contains JMX exporter configuration files.
- `grafana`: Contains Grafana configuration files.`

# How to put thing up!.

`TL;DR` To run the entire project, execute the following command:

```sh
docker compose up
```

This command will start the following services:

   - Grafana: http://localhost:3000
   - Prometheus: http://localhost:9090
   - Datomic Console: http://localhost:9090/browse
   - Kafka Topics UI: http://localhost:9099
   - Clojure API: http://localhost:9000/swagger
   - Clojure Async: http://localhost:9001/swagger

# Full explanation: 

- [Grafana](#grafana)
- [Prometheus](#prometheus)
- [Datomic - Storage Services and Transactor](#storage-services-and-transactor)
    - [Dev Mode](#dev-mode)
    - [Datomic Console](#datomic-console)
- [Kafka](#kafka)
- [Clojure API](#clojure-api) 
- [Clojure Async](#clojure-async)
   
## Some useful commands

- Pre requisites:
  - Install [jq](https://stedolan.github.io/jq/download/) to generate JSON payloads.
  - Install [Vegeta](https://github.com/tsenart/vegeta) to generate load tests.

- To produce some messages in Kafka:

```sh
cd etc/load_test && vegeta attack -targets=tmp -rate=10 -duration=60s | tee results.bin | vegeta report
```

## Grafana

Official Documentation: [Grafana](https://grafana.com/docs/grafana/latest/getting-started/getting-started-docker/)

Run Grafana:

```sh
docker compose up grafana
```

http://localhost:3000

![img.png](resources/images/img.png)

## Prometheus

Official Documentation: [Prometheus](https://prometheus.io/docs/prometheus/latest/installation/)

Run Prometheus:

```sh
docker compose up prometheus
```

http://localhost:9090

## Datomic - Storage Services and Transactor

Official Documentation: [Storage Services](https://docs.datomic.com/pro/overview/storage.html)

### Dev Mode

Official Documentation: [Provisioning dev mode](https://docs.datomic.com/pro/overview/storage.html#provisioning-dev-mode)

Run the Datomic Transactor:

```sh
docker compose up datomic-transactor
```

To restore a backup of the [MusicBrainz](https://musicbrainz.org) Sample Database:

```sh
docker compose run datomic-tools ./bin/datomic restore-db file:/usr/mbrainz-1968-1973 "datomic:dev://datomic-transactor:4334/my-datomic?password=unsafe"
````

## Datomic Console

To run [Datomic Console](https://docs.datomic.com/pro/other-tools/console.html):

```sh
docker compose up datomic-console
```

http://localhost:9090/browse

![Screenshot of the Datomic Console interface](https://docs.datomic.com/pro/images/console-window.png "Screenshot of the Datomic Console interface")

## Kafka

Official Documentation: [Kafka Quickstart](https://kafka.apache.org/quickstart)

Run Kafka:

```sh
docker compose up kafka
```

### Kafka Topics UI

Run Kafka UI:

```sh
docker compose up kafka-ui
```

To access the Kafka Topics UI:

http://localhost:9099

![img_1.png](resources/images/img_1.png)

## Clojure API

To run the Web application:

```sh
docker compose up clojure-api
```

You can access the swagger UI through the following link:

http://localhost:9000/swagger

![img_2.png](resources/images/img_2.png)

## Clojure Async

To run the Async Kafka producer/Web application:

```sh
docker compose up clojure-async
```

You can access the swagger UI through the following link:

http://localhost:9001/swagger

![img_3.png](resources/images/img_3.png)

## Study Path

1. **Getting Started with Clojure**
   - Explore basic syntax and functional programming concepts.
   - Implement small coding exercises to solidify understanding.

2. **Diving into Datomic**
   - Study Datomic's data model and architecture.
   - Set up a sample Datomic database and interact with it from a Clojure application.

3. **Working with Docker**
   - Learn Docker fundamentals and containerization concepts.
   - Create Dockerfiles for Clojure applications and Datomic instances.

4. **Building with Kafka**
   - Understand the principles of event streaming and Kafka.
   - Develop a simple event producer/consumer system using Kafka and Clojure.

5. **Integration and Projects**
   - Combine knowledge gained from previous steps to build more complex applications.
   - Experiment with connecting Clojure applications to Datomic using Dockerized environments.
   - Explore advanced Kafka scenarios in Clojure-based microservices.

## Contributions

Contributions to this repository are welcome! Feel free to submit pull requests if you find any issues, have improvements to suggest, or want to add new study resources related to Clojure, Datomic, Docker, or Kafka.

## Resources

- [Clojure Official Documentation](https://clojure.org/guides/learn)
- [Datomic Documentation](https://docs.datomic.com/)
- [Docker Documentation](https://docs.docker.com/)
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)

## Acknowledgments

Icons made by [Iconfinder](https://www.iconfinder.com/) and [Freepik](https://www.freepik.com/) from [www.flaticon.com](https://www.flaticon.com/).

## References

https://github.com/demystifyfp/BlogSamples/blob/0.10/clojure/resultful-crud/src/resultful_crud/core.clj
https://gsfl3101.medium.com/kafka-kraft-monitoring-with-prometheus-and-grafana-1994ef272f48

## Extra

I'm using portainer to manage the containers, you can access it through the following link:

https://docs.portainer.io/start/install/server/setup

You should be able to manage your docker containers through a web interface like this:

![img_4.png](resources/images/img_4.png)

### License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
