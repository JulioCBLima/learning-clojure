(ns generative.logic
  (:require [generative.model :as model]
            [schema.core :as s]))

(defn cabe-na-fila?
  [hospital departamento]
  (some-> hospital
          departamento
          count
          (< 5))) ; (not= 5); => bug

(defn- tenta-colocar-na-fila
  [hospital departamento pessoa]
  (when (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)))

(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "Não cabe ninguém neste departamento" 
                    {:paciente pessoa
                     :tipo :impossivel-colocar-pessoa-na-fila}))))
; ^ antes de fazer swap chega-em vai ter que tratar o resultado

(s/defn atende :- model/Hospital
  [hospital :- model/Hospital
   departamento :- model/DepartamentoId]
  (update hospital departamento pop))

(s/defn proxima :- model/PacienteId
  [hospital :- model/Hospital
   departamento :- model/DepartamentoId]
  (-> hospital
      departamento
      peek))

(defn- mesmo-tamanho?
  [hospital outro-hospital de para]
  (= (+ (count (get outro-hospital de)) (count (get outro-hospital para)))
             (+ (count (get hospital de)) (count (get hospital para)))))

(s/defn transfere :- model/Hospital
  [hospital :- model/Hospital
   de :- model/DepartamentoId
   para :- model/DepartamentoId]
  {:pre  [(contains? hospital de)
          (contains? hospital para)]
   :post [(mesmo-tamanho? % hospital de para)]}
  (let [pessoa (proxima hospital de)]
    (-> hospital
        (atende de)
        (chega-em para pessoa))))
