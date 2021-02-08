(ns mutatomref.aula1
  (:require [mutatomref.model :as h.model]
            [mutatomref.logic :as h.logic]))

(defn simula-um-dia
  [])

(def hospital (h.model/novo-hospital))

(def hospital (h.logic/chega-em hospital :espera "111"))
hospital
;; => {:espera <-("111")-<, :laboratorio1 <-()-<, :laboratorio2 <-()-<, :laboratorio3 <-()-<}

(def hospital (h.logic/chega-em hospital :espera "222"))
hospital
;; => {:espera <-("111" "222")-<, :laboratorio1 <-()-<, :laboratorio2 <-()-<, :laboratorio3 <-()-<}

(def hospital (h.logic/chega-em hospital :espera "333"))
hospital
;; => {:espera <-("111" "222" "333")-<, :laboratorio1 <-()-<, :laboratorio2 <-()-<, :laboratorio3 <-()-<}

;; ^ root binding -> símbolo global sendo alterado a rodo para gerar dor de cabeça

(def hospital (h.logic/chega-em hospital :laboratorio1 "444"))
(def hospital (h.logic/chega-em hospital :laboratorio3 "555"))
hospital
;; => {:espera <-("111" "222" "333")-<, :laboratorio1 <-("444")-<, :laboratorio2 <-()-<, :laboratorio3 <-("555")-<}

;; ^ continuando com a alteração global a rodo

(def hospital (h.logic/atende hospital :laboratorio1))
hospital
;; => {:espera <-("111" "222" "333")-<, :laboratorio1 <-()-<, :laboratorio2 <-()-<, :laboratorio3 <-("555")-<}


(def hospital (h.logic/atende hospital :espera))
hospital
;; => {:espera <-("222" "333")-<, :laboratorio1 <-()-<, :laboratorio2 <-()-<, :laboratorio3 <-("555")-<}

;; ^ mutacao de variável com root binding

(def hospital (h.logic/chega-em hospital :espera "666"))
hospital
;; => {:espera <-("222" "333" "666")-<, :laboratorio1 <-()-<, :laboratorio2 <-()-<, :laboratorio3 <-("555")-<}

(def hospital (h.logic/chega-em hospital :espera "777"))
hospital
;; => {:espera <-("222" "333" "666" "777")-<, :laboratorio1 <-()-<, :laboratorio2 <-()-<, :laboratorio3 <-("555")-<}

(def hospital (h.logic/chega-em hospital :espera "888"))
hospital
;; => {:espera <-("222" "333" "666" "777" "888")-<, :laboratorio1 <-()-<, :laboratorio2 <-()-<, :laboratorio3 <-("555")-<}

(def hospital (h.logic/chega-em hospital :espera "999"))
;; Execution Error (ExceptionInfo) Fila já está cheia


;; --------------

