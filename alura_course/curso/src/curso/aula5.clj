(ns curso.aula5)

(def estoque {"Mochila" 10
              "Camiseta" 5})

estoque

(count estoque)
;; => 2

(keys estoque)
;; => ("Mochila" "Camiseta")


(vals estoque)
;; => (10 5)

(def kestoque {:mochila 10
               :camiseta 5})

(keys kestoque)
;; => (:mochila :camiseta)

(vals kestoque)
;; => (10 5)

(count kestoque)
;; => 2

(assoc kestoque :cadeira 3)
;; => {:mochila 10, :camiseta 5, :cadeira 3}

(assoc kestoque :mochila 1)
;; => {:mochila 1, :camiseta 5}

(update kestoque :mochila inc)
;; => {:mochila 11, :camiseta 5}

(merge kestoque {:mesa 8})
;; => {:mochila 10, :camiseta 5, :mesa 8}

(merge kestoque {:mochila 5})
;; => {:mochila 5, :camiseta 5}

(dissoc kestoque :mochila)
;; => {:camiseta 5}

;;
;; ------------------
;;

(def pedido {:mochila  {:quantidade 2
                        :preco 80}
             :camiseta {:quantidade 3
                        :preco 40}})

pedido
;; => {:mochila {:quantidade 2, :preco 80}, :camiseta {:quantidade 3, :preco 40}}

(assoc pedido :chaveiro {:quantidade 1
                         :preco 10})
;; => {:mochila {:quantidade 2, :preco 80}, :camiseta {:quantidade 3, :preco 40}, :chaveiro {:quantidade 1, :preco 10}}

(pedido :mochila)
;; => {:quantidade 2, :preco 80}

(get pedido :mochila)
;; => {:quantidade 2, :preco 80}

(get pedido :cadeira)
;; => nil

(get pedido :cadeira {})
;; => {}

(:mochila pedido)
;; => {:quantidade 2, :preco 80}

(:cadeira pedido)
;; => nil

(:cadeira pedido {})
;; => {}

(:quantidade (:mochila pedido))
;; => 2

(-> pedido :mochila :quantidade)
;; => 2

(update-in pedido [:mochila :quantidade] inc)
;; => {:mochila {:quantidade 3, :preco 80}, :camiseta {:quantidade 3, :preco 40}}
