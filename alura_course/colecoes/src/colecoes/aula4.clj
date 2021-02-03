(ns colecoes.aula4
  (:require [colecoes.db :as l.db]
            [colecoes.logic :as l.logic]))

(def resumo (l.logic/resumo-por-usuario l.db/pedidos))
resumo
;; => ({:usuario-id 15, :total-de-pedidos 3, :gasto-total 840}
;;     {:usuario-id 16, :total-de-pedidos 3, :gasto-total 840}
;;     {:usuario-id 17, :total-de-pedidos 2, :gasto-total 560})

(sort-by :gasto-total resumo)
;; => ({:usuario-id 17, :total-de-pedidos 2, :gasto-total 560}
;;     {:usuario-id 15, :total-de-pedidos 3, :gasto-total 840}
;;     {:usuario-id 16, :total-de-pedidos 3, :gasto-total 840})

(reverse (sort-by :gasto-total resumo))
;; => ({:usuario-id 16, :total-de-pedidos 3, :gasto-total 840}
;;     {:usuario-id 15, :total-de-pedidos 3, :gasto-total 840}
;;     {:usuario-id 17, :total-de-pedidos 2, :gasto-total 560})

(get-in l.db/pedidos [0 :itens :mochila :quantidade])
;; => 2

(defn resumo-por-usuario-ordenado
  [pedidos]
  (->> pedidos
       l.logic/resumo-por-usuario
       (sort-by :gasto-total)
       reverse))

(def resumo (resumo-por-usuario-ordenado l.db/pedidos))
resumo
;; => ({:usuario-id 16, :total-de-pedidos 3, :gasto-total 840}
;;     {:usuario-id 15, :total-de-pedidos 3, :gasto-total 840}
;;     {:usuario-id 17, :total-de-pedidos 2, :gasto-total 560})

(first resumo)
;; => {:usuario-id 16, :total-de-pedidos 3, :gasto-total 840}

(second resumo)
;; => {:usuario-id 15, :total-de-pedidos 3, :gasto-total 840}

(rest resumo)
;; => ({:usuario-id 15, :total-de-pedidos 3, :gasto-total 840} {:usuario-id 17, :total-de-pedidos 2, :gasto-total 560})

(count resumo)
;; => 3

(class resumo)
;; => clojure.lang.PersistentList

(nth resumo 1)
;; => {:usuario-id 15, :total-de-pedidos 3, :gasto-total 840}

(take 2 resumo)
;; => ({:usuario-id 16, :total-de-pedidos 3, :gasto-total 840} {:usuario-id 15, :total-de-pedidos 3, :gasto-total 840})

(defn top-2
  [resumo]
  (take 2 resumo))

(top-2 resumo)
;; => ({:usuario-id 16, :total-de-pedidos 3, :gasto-total 840} {:usuario-id 15, :total-de-pedidos 3, :gasto-total 840})
