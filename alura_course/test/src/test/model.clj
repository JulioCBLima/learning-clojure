(ns test.model
  (:require [schema.core :as s]))

(def fila-vazia clojure.lang.PersistentQueue/EMPTY)

(defn novo-hospital
  []
  {:espera       fila-vazia
   :laboratorio1 fila-vazia
   :laboratorio2 fila-vazia
   :laboratorio3 fila-vazia})

(s/def PacienteId (s/pred pos-int? 'inteiro-positivo))

(s/def DepartamentoId s/Keyword)

(s/def Departamento (s/queue PacienteId))

(s/def Hospital {DepartamentoId Departamento})
