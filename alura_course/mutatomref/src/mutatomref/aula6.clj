(ns mutatomref.aula6
  (:use [clojure pprint])
  (:require [mutatomref.model :as h.model]
            [mutatomref.logic :as h.logic]))

(defn cabe-na-fila?
  [fila]
  (-> fila
      count
      (< 5)))

(defn chega-em
  [fila pessoa]
  (if (cabe-na-fila? fila)
    (conj fila pessoa)
    (throw (ex-info "Fila já está cheia" {:tentando-adicionar pessoa}))))

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
     (chega-em! hospital "111")
     (chega-em! hospital "222")
     (chega-em! hospital "333")
     (chega-em! hospital "444")
     (chega-em! hospital "555")
     #_(chega-em! hospital "666")
     )))

#_(simula-um-dia)

(defn async-chega-em!
  [hospital pessoa]
  (future 
    (Thread/sleep (rand 5000))
           (dosync
            (println "Tentando o código sincronizado" pessoa)
            (chega-em! hospital pessoa))))

(defn simula-um-dia-async
  []
  (let [hospital {:espera       (ref h.model/fila-vazia)
                  :laboratorio1 (ref h.model/fila-vazia)
                  :laboratorio2 (ref h.model/fila-vazia)
                  :laboratorio3 (ref h.model/fila-vazia)}]
    #_(dotimes [pessoa 10]
      (async-chega-em! hospital pessoa))
    #_(Thread/sleep 10000)
    (future
      (Thread/sleep 10000)
      (pprint hospital))
    (pprint hospital)
    (mapv #(async-chega-em! hospital %) (range 10))))

(def dia (simula-um-dia-async))

dia
(pprint dia)