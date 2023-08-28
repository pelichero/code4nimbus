(ns clojure-studies.alura.loja.db)

(def pedido1 {:usuario 15
              :itens {:mochila {:id :mochila, :quantidade 2, :preco-unitario 80}
                      :camiseta {:id :camiseta, :quantidade 3, :preco-unitario 40}
                      :tenis {:id :tenis, :quantidade 1}}})

(def pedido2 {:usuario 1
              :itens {:mochila {:id :mochila, :quantidade 2, :preco-unitario 80}
                      :camiseta {:id :camiseta, :quantidade 3, :preco-unitario 40}
                      :tenis {:id :tenis, :quantidade 1}}})

(def pedido3 {:usuario 15
              :itens {:mochila {:id :mochila, :quantidade 2, :preco-unitario 80}
                      :camiseta {:id :camiseta, :quantidade 3, :preco-unitario 40}
                      :tenis {:id :tenis, :quantidade 1}}})

(def pedido4 {:usuario 15
              :itens {:mochila {:id :mochila, :quantidade 2, :preco-unitario 80}
                      :camiseta {:id :camiseta, :quantidade 3, :preco-unitario 40}
                      :tenis {:id :tenis, :quantidade 1}}})

(def pedido5 {:usuario 10
              :itens {:mochila {:id :mochila, :quantidade 20, :preco-unitario 80}
                      :camiseta {:id :camiseta, :quantidade 3, :preco-unitario 40}
                      :tenis {:id :tenis, :quantidade 1}}})

(defn todos-pedidos []
  [pedido1 pedido2 pedido3 pedido4 pedido5])