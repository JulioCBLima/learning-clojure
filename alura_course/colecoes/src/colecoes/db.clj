(ns colecoes.db)


(def pedido1 {:usuario 15
              :itens {:mochila {:id :mochila
                                :quantidade 2
                                :preco-unitario 80}
                      :camiseta {:id :camiseta
                                 :quantidade 3
                                 :preco-unitario 40}
                      :tenis {:id :tenis
                              :quantidade 1}}})

(def pedido2 {:usuario 15
              :itens {:mochila {:id :mochila
                                :quantidade 2
                                :preco-unitario 80}
                      :camiseta {:id :camiseta
                                 :quantidade 3
                                 :preco-unitario 40}
                      :tenis {:id :tenis
                              :quantidade 1}}})

(def pedido3 {:usuario 16
              :itens {:mochila {:id :mochila
                                :quantidade 2
                                :preco-unitario 80}
                      :camiseta {:id :camiseta
                                 :quantidade 3
                                 :preco-unitario 40}
                      :tenis {:id :tenis
                              :quantidade 1}}})

(def pedido4 {:usuario 17
              :itens {:mochila {:id :mochila
                                :quantidade 2
                                :preco-unitario 80}
                      :camiseta {:id :camiseta
                                 :quantidade 3
                                 :preco-unitario 40}
                      :tenis {:id :tenis
                              :quantidade 1}}})

(def pedido5 {:usuario 16
              :itens {:mochila {:id :mochila
                                :quantidade 2
                                :preco-unitario 80}
                      :camiseta {:id :camiseta
                                 :quantidade 3
                                 :preco-unitario 40}
                      :tenis {:id :tenis
                              :quantidade 1}}})

(def pedido6 {:usuario 15
              :itens {:mochila {:id :mochila
                                :quantidade 2
                                :preco-unitario 80}
                      :camiseta {:id :camiseta
                                 :quantidade 3
                                 :preco-unitario 40}
                      :tenis {:id :tenis
                              :quantidade 1}}})

(def pedido7 {:usuario 17
              :itens {:mochila {:id :mochila
                                :quantidade 2
                                :preco-unitario 80}
                      :camiseta {:id :camiseta
                                 :quantidade 3
                                 :preco-unitario 40}
                      :tenis {:id :tenis
                              :quantidade 1}}})

(def pedido8 {:usuario 16
              :itens {:mochila {:id :mochila
                                :quantidade 2
                                :preco-unitario 80}
                      :camiseta {:id :camiseta
                                 :quantidade 3
                                 :preco-unitario 40}
                      :tenis {:id :tenis
                              :quantidade 1}}})

(def pedidos [pedido1 pedido2 pedido3 pedido4 pedido5 pedido6 pedido7 pedido8])

(group-by :usuario pedidos)
;; => {15
;;     [{:usuario 15,
;;       :itens
;;       {:mochila {:id :mochila, :quantidade 2, :preco-unitario 80},
;;        :camiseta {:id :camiseta, :quantidade 3, :preco-unitario 40},
;;        :tenis {:id :tenis, :quantidade 1}}}
;;      {:usuario 15,
;;       :itens
;;       {:mochila {:id :mochila, :quantidade 2, :preco-unitario 80},
;;        :camiseta {:id :camiseta, :quantidade 3, :preco-unitario 40},
;;        :tenis {:id :tenis, :quantidade 1}}}
;;      {:usuario 15,
;;       :itens
;;       {:mochila {:id :mochila, :quantidade 2, :preco-unitario 80},
;;        :camiseta {:id :camiseta, :quantidade 3, :preco-unitario 40},
;;        :tenis {:id :tenis, :quantidade 1}}}],
;;     16
;;     [{:usuario 16,
;;       :itens
;;       {:mochila {:id :mochila, :quantidade 2, :preco-unitario 80},
;;        :camiseta {:id :camiseta, :quantidade 3, :preco-unitario 40},
;;        :tenis {:id :tenis, :quantidade 1}}}
;;      {:usuario 16,
;;       :itens
;;       {:mochila {:id :mochila, :quantidade 2, :preco-unitario 80},
;;        :camiseta {:id :camiseta, :quantidade 3, :preco-unitario 40},
;;        :tenis {:id :tenis, :quantidade 1}}}
;;      {:usuario 16,
;;       :itens
;;       {:mochila {:id :mochila, :quantidade 2, :preco-unitario 80},
;;        :camiseta {:id :camiseta, :quantidade 3, :preco-unitario 40},
;;        :tenis {:id :tenis, :quantidade 1}}}],
;;     17
;;     [{:usuario 17,
;;       :itens
;;       {:mochila {:id :mochila, :quantidade 2, :preco-unitario 80},
;;        :camiseta {:id :camiseta, :quantidade 3, :preco-unitario 40},
;;        :tenis {:id :tenis, :quantidade 1}}}
;;      {:usuario 17,
;;       :itens
;;       {:mochila {:id :mochila, :quantidade 2, :preco-unitario 80},
;;        :camiseta {:id :camiseta, :quantidade 3, :preco-unitario 40},
;;        :tenis {:id :tenis, :quantidade 1}}}]}

(->> pedidos
     (group-by :usuario)
     vals
     (map count))
;; => (3 3 2)

(defn conta-total-por-usuario
  [[usuario pedidos]]
  {:usuario-id usuario
   :total-de-pedidos (count pedidos)})

(->> pedidos
     (group-by :usuario)
     (map conta-total-por-usuario))
;; => ({:usuario-id 15, :total-de-pedidos 3} {:usuario-id 16, :total-de-pedidos 3} {:usuario-id 17, :total-de-pedidos 2})

(defn total-do-item
  [[_ detalhes]]
  (* (get detalhes :quantidade 0) (get detalhes :preco-unitario 0)))

(defn total-do-pedido
  [pedido]
  (reduce + (map total-do-item pedido)))

(defn total-dos-pedidos
  [pedidos]
  (->> pedidos
       (map :itens)
       (map total-do-pedido)
       (reduce +)))

(defn quantia-de-pedidos-e-gasto-total-por-usuario
  [[usuario pedidos]]
  {:usuario-id usuario
   :total-de-pedidos (count pedidos)
   :gasto-total (total-dos-pedidos pedidos)})

(->> pedidos
     (group-by :usuario)
     (map quantia-de-pedidos-e-gasto-total-por-usuario))
;; => ({:usuario-id 15, :total-de-pedidos 3, :gasto-total 840}
;;     {:usuario-id 16, :total-de-pedidos 3, :gasto-total 840}
;;     {:usuario-id 17, :total-de-pedidos 2, :gasto-total 560})
