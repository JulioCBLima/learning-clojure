(ns mutatomref.aula4
  (:use [clojure pprint])
  (:require [mutatomref.logic :as h.logic]
            [mutatomref.model :as h.model]))

(defn chega-em-malvado!
  [hospital pessoa]
  (swap! hospital h.logic/chega-em-pausado :espera pessoa)
  (println "após inserir" pessoa))

(defn starta-thread-de-chegada
  [hospital pessoa]
  (.start (Thread. (fn [] (chega-em-malvado! hospital pessoa)))))

(defn preparadinha
  [hospital]
  (fn [pessoa] (starta-thread-de-chegada hospital pessoa)))

(defn simula-um-dia-em-paralelo
  []
  (let [hospital (atom (h.model/novo-hospital))
        pessoas ["111" "222" "333" "444" "555" "666"]
        starta (partial starta-thread-de-chegada hospital)]
    (run! starta pessoas)
    (.start (Thread. (fn [] (Thread/sleep 8000) (pprint hospital))))))

(simula-um-dia-em-paralelo)
