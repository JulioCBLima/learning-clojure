(ns curso.aula6)

(def pedido {:mochila  {:quantidade 2
                        :preco 80}
             :camiseta {:quantidade 3
                        :preco 40}})

(defn get-valor 
  [[chave valor]]
  valor)

(map get-valor pedido)
;; => ({:quantidade 2, :preco 80} {:quantidade 3, :preco 40})

(defn get-chave
  [[chave valor]]
  chave)

(map get-chave pedido)
;; => (:mochila :camiseta)

(defn preco-dos-produtos
  [[chave valor]]
  (* (:quantidade valor) (:preco valor)))

(map preco-dos-produtos pedido)
;; => (160 120)

(reduce + (map preco-dos-produtos pedido))
;; => 280

(defn total-do-pedido
  [pedido]
  (reduce + (map preco-dos-produtos pedido)))
(total-do-pedido pedido)
;; => 280

(defn total-do-pedido-v2
  [pedido]
  (->> pedido
      (map preco-dos-produtos)
      (reduce +)))
(total-do-pedido-v2 pedido)
;; => 280

(defn preco-total-do-produto
  [produto]
  (* (:quantidade produto) (:preco produto)))
(defn total-do-pedido-v3
  [pedido]
  (->> pedido
       vals
       (map preco-total-do-produto)
       (reduce +)))
(total-do-pedido-v3 pedido)
;; => 280

(def pedido-com-brinde (assoc pedido :chaveiro {:quantidade 1}))
pedido-com-brinde
;; => {:mochila {:quantidade 2, :preco 80}, :camiseta {:quantidade 3, :preco 40}, :chaveiro {:quantidade 1}}


(defn gratuito?
  [[_ item]]
  (<= (:preco item 0) 0))

(filter gratuito? pedido-com-brinde)
;; => ([:chaveiro {:quantidade 1}])

(defn gratuito-v2?
  [item]
  (<= (:preco item 0) 0))
(filter (fn [[_ item]] (gratuito-v2? item)) pedido-com-brinde)
;; => ([:chaveiro {:quantidade 1}])

(filter #(gratuito-v2? (second %)) pedido-com-brinde)
;; => ([:chaveiro {:quantidade 1}])

(defn pago?
  [item]
  (not (gratuito-v2? item)))
(filter #(pago? (second %)) pedido-com-brinde)
;; => ([:mochila {:quantidade 2, :preco 80}] [:camiseta {:quantidade 3, :preco 40}])

(def pago-composed? (comp not gratuito-v2?))
(filter #(pago-composed? (second %)) pedido-com-brinde)
;; => ([:mochila {:quantidade 2, :preco 80}] [:camiseta {:quantidade 3, :preco 40}])



(def clientes [
  { :nome "Guilherme"
    :certificados ["Clojure" "Java" "Machine Learning"] }
    { :nome "Paulo"
      :certificados ["Java" "Ciência da Computação"] }
    { :nome "Daniela"
      :certificados ["Arquitetura" "Gastronomia"] }])

(->> clientes
     (map :certificados)
     (map count)
     (reduce +))