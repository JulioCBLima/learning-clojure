(ns recprotmm.aula2
  (:use clojure.pprint))

(defrecord PacientePlanoDeSaude [id nome nascimento plano])

(defrecord PacienteParticular [id nome nascimento])

(defprotocol Cobravel
  (deve-assinar-pre-autorizacao?
    [paciente procedimento valor]))

(extend-type PacienteParticular
  Cobravel
  (deve-assinar-pre-autorizacao?
    [paciente procedimento valor]
    (>= valor 50)))

;; alternativa para definir tudo junto
;; (defrecord PacientePlanoDeSaude [...]
;;   Cobravel
;;   (deve-assinar-pre-autorizacao? [...]
;;     ...))

(extend-type PacientePlanoDeSaude
  Cobravel
  (deve-assinar-pre-autorizacao?
    [paciente procedimento valor]
    (let [plano (:plano paciente)]
      (not (some #(= % procedimento) plano)))))

(let [particular (->PacienteParticular 15
                                       "Guilherme"
                                       "18/9/1981")]
  (deve-assinar-pre-autorizacao? particular
                                 :raio-x
                                 500))
;; => true

(let [particular (->PacienteParticular 15
                                       "Guilherme"
                                       "18/9/1981")]
  (deve-assinar-pre-autorizacao? particular
                                 :raio-x
                                 40))
;; => false

(let [pacienteplano (->PacientePlanoDeSaude 15
                                            "Guilherme"
                                            "18/9/1981"
                                            [:raio-x :ultrassom])]
  (deve-assinar-pre-autorizacao? pacienteplano
                                 :raio-x
                                 40))
;; => false

(let [pacienteplano (->PacientePlanoDeSaude 15
                                            "Guilherme"
                                            "18/9/1981"
                                            [:raio-x :ultrassom])]
  (deve-assinar-pre-autorizacao? pacienteplano
                                 :colete-de-sangue
                                 40))
;; => true
