(ns generative.model
  (:require [schema.core :as s]
            [clojure.test.check.generators :as gen]))

(def fila-vazia clojure.lang.PersistentQueue/EMPTY)

(defn novo-hospital
  []
  {:espera       fila-vazia
   :laboratorio1 fila-vazia
   :laboratorio2 fila-vazia
   :laboratorio3 fila-vazia})

(s/def PacienteId (s/constrained long pos?))
                         
(s/def DepartamentoId s/Keyword)

(s/def Departamento (s/queue PacienteId))

(s/def Hospital {DepartamentoId Departamento})
