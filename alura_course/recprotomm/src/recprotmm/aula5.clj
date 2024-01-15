(ns recprotmm.aula5
  (:require [clojure.pprint :as pprint]
            [recprotmm.logic :as logic]))

(defn tipo-de-autorizador
  [pedido]
  (let [paciente (:paciente pedido)
        situacao (:situacao paciente)]
    (cond (= :urgente situacao) :sempre-autorizado
          (contains? paciente :plano) :plano-de-saude
          :else :credito-minimo)))

(defmulti deve-assinar-pre-autorizacao? 
  tipo-de-autorizador)

(defmethod deve-assinar-pre-autorizacao?
  :sempre-autorizado
  [pedido]
  false)

(defmethod deve-assinar-pre-autorizacao?
  :plano-de-saude
  [pedido]
  (not (some #(= % (:procedimento pedido))
             (:plano (:paciente pedido)))))

(defmethod deve-assinar-pre-autorizacao?
  :credito-minimo
  [pedido]
  (>= (:valor pedido 0)
      50))

(let [particular {:id 15
                  :nome "Guilherme"
                  :nascimento "18/9/1981"
                  :situacao :urgente}]
  (deve-assinar-pre-autorizacao? {:paciente particular
                                  :procedimento :raio-x
                                  :valor 500}))
;; => false

(let [particular {:id 15
                  :nome "Guilherme"
                  :nascimento "18/9/1981"
                  :situacao :urgente}]
  (deve-assinar-pre-autorizacao? {:paciente particular
                                  :procedimento :raio-x
                                  :valor 40}))
;; => false

(let [particular {:id 15
                  :nome "Guilherme"
                  :nascimento "18/9/1981"
                  :situacao :normal}]
  (deve-assinar-pre-autorizacao? {:paciente particular
                                  :procedimento :raio-x
                                  :valor 1000}))
;; => true

(let [pacienteplano {:id 15
                     :nome "Guilherme"
                     :nascimento "18/9/1981"
                     :situacao :normal
                     :plano [:raio-x
                             :ultrassom]}]
  (deve-assinar-pre-autorizacao? {:paciente pacienteplano
                                  :procedimento :raio-x
                                  :valor 1000}))
;; => false

(let [pacienteplano {:id 15
                     :nome "Guilherme"
                     :nascimento "18/9/1981"
                     :situacao :normal
                     :plano [:raio-x
                             :ultrassom]}]
  (deve-assinar-pre-autorizacao? {:paciente pacienteplano
                                  :procedimento :colete-de-sangue
                                  :valor 1000}))
;; => true
