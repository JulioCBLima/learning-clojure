(ns mutatomref.aula5
  (:use [clojure pprint])
  (:require [mutatomref.logic :as h.logic]
            [mutatomref.model :as h.model]))

(defn chega-em!
  [hospital pessoa]
  (swap! hospital h.logic/chega-em :espera pessoa))

(defn transfere!
  [hospital de para]
  (swap! hospital h.logic/transfere de para))

(defn testa-transferencia
  []
  (let [hospital (atom (h.model/novo-hospital))]
    (doseq [pessoa (range 1 6)]
      (chega-em! hospital pessoa))
    (pprint @hospital)
    
    (transfere! hospital :espera :laboratorio1)
    (pprint @hospital)))

(testa-transferencia)
