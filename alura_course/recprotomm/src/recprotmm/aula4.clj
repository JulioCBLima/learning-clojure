(ns recprotmm.aula4
  (:require [clojure.pprint :as pprint]
            [recprotmm.logic :as logic]))

(defrecord PacienteParticular
  [id nome nascimento situacao])

(defrecord PacientePlanoDeSaude
  [id nome nascimento situacao plano])

(defprotocol Cobravel
  (deve-assinar-pre-autorizacao?
    [paciente procedimento valor]))

(defn nao-eh-urgente?
  [paciente]
  (not= :urgente (:situacao paciente :normal)))

(extend-type PacienteParticular
  Cobravel
  (deve-assinar-pre-autorizacao?
    [paciente procedimento valor]
    (and (>= valor 50)
         (nao-eh-urgente? paciente))))

(extend-type PacientePlanoDeSaude
  Cobravel
  (deve-assinar-pre-autorizacao?
    [paciente procedimento valor]
    (let [plano (:plano paciente)]
      (and (not (some #(= % procedimento) plano))
           (nao-eh-urgente? paciente)))))

(let [particular (->PacienteParticular 15
                                       "Guilherme"
                                       "18/9/1981"
                                       :urgente)]
  (deve-assinar-pre-autorizacao? particular
                                 :raio-x
                                 500))
;; => false

(let [particular (->PacienteParticular 15
                                       "Guilherme"
                                       "18/9/1981"
                                       :normal)]
  (deve-assinar-pre-autorizacao? particular
                                 :raio-x
                                 40))
;; => false

(let [pacienteplano (->PacientePlanoDeSaude 15
                                            "Guilherme"
                                            "18/9/1981"
                                            :normal
                                            [:raio-x :ultrassom])]
  (deve-assinar-pre-autorizacao? pacienteplano
                                 :raio-x
                                 40))
;; => false

(let [pacienteplano (->PacientePlanoDeSaude 15
                                            "Guilherme"
                                            "18/9/1981"
                                            :urgente
                                            [:raio-x :ultrassom])]
  (deve-assinar-pre-autorizacao? pacienteplano
                                 :colete-de-sangue
                                 40))
;; => false

;; usando multimedodo com base em classe
;; sufixo multi não é convencional
(defmulti deve-assinar-pre-autorizacao-multi?
  class)

(defmethod deve-assinar-pre-autorizacao-multi?
  PacienteParticular
  [paciente]
  true)

(defmethod deve-assinar-pre-autorizacao-multi?
  PacientePlanoDeSaude
  [paciente]
  false)

(let [particular (->PacienteParticular 15
                                       "Guilherme"
                                       "18/9/1981"
                                       :urgente)]
  (deve-assinar-pre-autorizacao-multi? particular))
;; => true

(let [plano (->PacientePlanoDeSaude 15
                                    "Guilherme"
                                    "18/9/1981"
                                    :urgente
                                    [:raio-x :ultrassom])]
  (deve-assinar-pre-autorizacao-multi? plano))
;; => false

;; para o multimetodo ficar legal não dá para usar a classe
;; uma função pode ser utilizada para especificar a estratégia
(defn tipo-de-autorizador
  [pedido]
  (let [paciente (:paciente pedido)
        situacao (:situacao paciente)
        urgencia? (= :urgente situacao)]
    (if urgencia?
      :sempre-autorizado
      (class paciente)))) ;; não é bom misturar tipos de retornos

(defmulti deve-assinar-pre-autorizacao-do-pedido? tipo-de-autorizador)

(defmethod deve-assinar-pre-autorizacao-do-pedido?
  :sempre-autorizado
  [pedido]
  false)

(defmethod deve-assinar-pre-autorizacao-do-pedido?
  PacienteParticular
  [pedido]
  (>= (:valor pedido 0) 50))

(defmethod deve-assinar-pre-autorizacao-do-pedido?
  PacientePlanoDeSaude
  [pedido]
  (not (some #(= % (get :procedimento pedido)) 
             (:plano (:paciente pedido)))))

(let [particular (->PacienteParticular 15
                                       "Guilherme"
                                       "18/9/1981"
                                       :urgente)]
  (deve-assinar-pre-autorizacao-do-pedido? {:paciente particular
                                            :valor 1000
                                            :procedimento :colete-de-sangue}))
;; => false

(let [plano (->PacientePlanoDeSaude 15
                                    "Guilherme"
                                    "18/9/1981"
                                    :urgente
                                    [:raio-x :ultrassom])]
  (deve-assinar-pre-autorizacao-do-pedido? {:paciente plano
                                        :valor 1000
                                        :procedimento :colete-de-sangue}))
;; => false

(let [plano (->PacientePlanoDeSaude 15
                                    "Guilherme"
                                    "18/9/1981"
                                    :normal
                                    [:raio-x :ultrassom])]
  (deve-assinar-pre-autorizacao-do-pedido? {:paciente plano
                                        :valor 1000
                                        :procedimento :colete-de-sangue}))
;; => true

(let [particular (->PacienteParticular 15
                                       "Guilherme"
                                       "18/9/1981"
                                       :normal)]
  (deve-assinar-pre-autorizacao-do-pedido? {:paciente particular
                                            :valor 1000
                                            :procedimento :colete-de-sangue}))
;; => true
