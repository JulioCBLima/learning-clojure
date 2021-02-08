(ns colecoes.aula5
  (:require [colecoes.db :as l.db]
            [colecoes.logic :as l.logic]))

(def pedidos l.db/pedidos)

(def resumo (l.logic/resumo-por-usuario pedidos))
resumo
;; => ({:usuario-id 15, :total-de-pedidos 3, :gasto-total 840}
;;     {:usuario-id 16, :total-de-pedidos 3, :gasto-total 840}
;;     {:usuario-id 17, :total-de-pedidos 2, :gasto-total 560})

(defn gastou-bastante?
  [info-do-usuario]
  (> (:gasto-total info-do-usuario) 600))

(keep gastou-bastante? resumo)
;; => (true true false)

(filter gastou-bastante? resumo)
;; => ({:usuario-id 15, :total-de-pedidos 3, :gasto-total 840} {:usuario-id 16, :total-de-pedidos 3, :gasto-total 840})
