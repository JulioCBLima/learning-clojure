(ns schemas.aula3
  (:require [clojure.pprint :refer [pprint]]
            [schema.core :as s]))


(def Plano [s/Keyword])

(def PosInt (s/pred pos-int? 'inteiro-positivo))

(def Paciente
  {:id                          PosInt
   :nome                        s/Str
   :plano                       Plano
   (s/optional-key :nascimento) s/Str})

(s/validate Paciente {:id         15
                      :nome       "Guilherme"
                      :plano      []
                      :nascimento "18/9/1981"})
;; => {:id 15, :nome "Guilherme", :plano [], :nascimento "18/9/1981"}

(s/validate Paciente {:id    15
                      :nome  "Guilherme"
                      :plano []})
;; => {:id 15, :nome "Guilherme", :plano []}

;; --------------
;; Pacientes
;; { 15 {PACIENTE} 32 {PACIENTE}}
;; esse é um mapa dinâmico
;; o schema não trata as chaves desse mapa como requiridas por saber que são mapas dinâmicos
;; quando usamos keywords nas chaves dos mapas por convenção elas são requiridas e o schema espera isso

(def Pacientes
  {PosInt Paciente})

(s/validate Pacientes {})
;; => {}

(s/validate Pacientes {15 :nadaaver})
; Execution error (ExceptionInfo) at schema.core/validator$fn (core.clj:155).
; Value does not match schema: {15 (not (map? :nadaaver))}

(let [guilherme {:id    15
                 :nome  "Guilherme"
                 :plano [:raio-x]}]
  (s/validate Pacientes {15 guilherme}))
;; => {15 {:id 15, :nome "Guilherme", :plano [:raio-x]}}

(let [guilherme {:id    15
                 :nome  "Guilherme"
                 :plano [:raio-x]}
      daniela   {:id    15
                 :nome  "Guilherme"
                 :plano [:raio-x]}]
  (s/validate Pacientes {15 guilherme
                         16 daniela}))
;; => {15 {:id 15, :nome "Guilherme", :plano [:raio-x]}, 16 {:id 15, :nome "Guilherme", :plano [:raio-x]}}

;; ---------------

(def Visita s/Str)

(def Visitas
  {PosInt [Visita]})

