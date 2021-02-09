(ns mutatomref.aula3
  (:use [clojure pprint])
  (:require [mutatomref.logic :as h.logic]
            [mutatomref.model :as h.model]))

(defn testa-atomao
  []
  (let [hospital-silveira (atom {:espera h.model/fila-vazia})]
    (println hospital-silveira)
    (pprint hospital-silveira)
    (pprint (deref hospital-silveira))
    (pprint @hospital-silveira)
    
    (pprint (assoc @hospital-silveira :laboratorio1 h.model/fila-vazia))
    (pprint @hospital-silveira)
    ;; ^ esse assoc retorna um novo hospital com assoc do que tem dentro do atom 
    ;;   (o conteudo do atom continua o mesmo)
    
    (swap! hospital-silveira assoc :laboratorio1 h.model/fila-vazia)
    (pprint @hospital-silveira)
    (swap! hospital-silveira assoc :laboratorio2 h.model/fila-vazia)
    (pprint @hospital-silveira)
    
    (swap! hospital-silveira update :laboratorio1 conj "111")
    (pprint @hospital-silveira)))

(testa-atomao)
