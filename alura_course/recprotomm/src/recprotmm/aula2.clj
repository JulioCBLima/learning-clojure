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

;; https://www.youtube.com/watch?v=kQhOlWXXl2I
(defprotocol Dateable
  (to-ms [this]))

(extend-type java.lang.Number
  Dateable
  (to-ms
    [this]
    this))

(extend-type java.util.Date
  Dateable
  (to-ms
    [this]
    (.getTime this)))

(extend-type java.util.Calendar
  Dateable
  (to-ms
    [this]
    (-> (.getTime this) ; returns a Date
        to-ms)))

(to-ms 56)
;; => 56

(to-ms (java.util.Date.))
;; => 1613408474635

(to-ms (java.util.GregorianCalendar.))
;; => 1613408578309
