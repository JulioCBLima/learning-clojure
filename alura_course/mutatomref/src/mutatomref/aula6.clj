(ns mutatomref.aula6
  (:use [clojure pprint])
  (:require [mutatomref.model :as h.model]
            [mutatomref.logic :as h.logic]))

(defn chega-em
  [fila pessoa]
  (conj fila pessoa))

(defn chega-em!
  [hospital pessoa]
  (let [fila (get hospital :espera)]
    #_(dosync (ref-set fila (chega-em @fila pessoa)))
    #_(ref-set fila (chega-em @fila pessoa))
    (alter fila chega-em pessoa)))

(defn simula-um-dia
  []
  (let [hospital {:espera       (ref h.model/fila-vazia)
                  :laboratorio1 (ref h.model/fila-vazia)
                  :laboratorio2 (ref h.model/fila-vazia)
                  :laboratorio3 (ref h.model/fila-vazia)}]
    (dosync
     (chega-em! hospital "111"))))

(simula-um-dia)