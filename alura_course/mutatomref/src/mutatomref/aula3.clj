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
;; ----------

(defn chega-em-malvado!
  [hospital pessoa]
  (swap! hospital h.logic/chega-em-pausado :espera pessoa)
  (println "após inserir" pessoa))

(defn simula-um-dia-em-paralelo
  []

  (let [hospital (atom (h.model/novo-hospital))]
    (.start (Thread. (fn [] (chega-em-malvado! hospital "111"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "222"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "333"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "444"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "555"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "666"))))
    (.start (Thread. (fn [] (Thread/sleep 8000) (pprint hospital))))))

(simula-um-dia-em-paralelo)

(defn chega-sem-malvado!
  [hospital pessoa]
  (swap! hospital h.logic/chega-em :espera pessoa)
  (println "após inserir" pessoa))

(defn simula-um-dia-em-paralelo-bonzinho
  []

  (let [hospital (atom (h.model/novo-hospital))]
    (.start (Thread. (fn [] (chega-sem-malvado! hospital "111"))))
    (.start (Thread. (fn [] (chega-sem-malvado! hospital "222"))))
    (.start (Thread. (fn [] (chega-sem-malvado! hospital "333"))))
    (.start (Thread. (fn [] (chega-sem-malvado! hospital "444"))))
    (.start (Thread. (fn [] (chega-sem-malvado! hospital "555"))))
    (.start (Thread. (fn [] (chega-sem-malvado! hospital "666"))))
    (.start (Thread. (fn [] (Thread/sleep 1000) (pprint hospital))))))

(simula-um-dia-em-paralelo-bonzinho)