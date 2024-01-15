(ns schemas.aula3
  (:require [clojure.pprint :refer [pprint]]
            [schema.core :as s]))

(s/set-fn-validation! true)

(def PosInt (s/pred pos-int? 'inteiro-positivo))

(def Paciente
  {:id PosInt
   :nome s/Str})

(s/defn novo-paciente :- Paciente
  [id :- PosInt
   nome :- s/Str]
  {:id id
   :nome nome})

(novo-paciente 15 "guilherme")
;; => {:id 15, :nome "guilherme"}

(novo-paciente -5 "guilherme")
; Execution error (ExceptionInfo) at schemas.aula3/eval8984$novo-paciente (REPL:13).
; Input to novo-paciente does not match schema: 
;    [(named (not (inteiro-positivo -5)) id) nil]  

(defn maior-ou-igual-a-zero
  [x]
  (>= x 0))

(def ValorFinanceiro (s/constrained s/Num
                                    maior-ou-igual-a-zero
                                    'financeiro-maior-ou-igual-a-zero))

(def Pedido
  {:paciente     Paciente
   :valor        ValorFinanceiro
   :procedimento s/Keyword})

(s/defn novo-pedido :- Pedido
  [paciente     :- Paciente
   valor        :- ValorFinanceiro
   procedimento :- s/Keyword]
  {:paciente paciente
   :valor valor
   :procedimento procedimento})

(novo-pedido (novo-paciente 15 "guilherme")
             15.53
             :raio-x)
;; => {:paciente {:id 15, :nome "guilherme"}, :valor 15.53, :procedimento :raio-x}

(novo-pedido (novo-paciente 15 "guilherme")
             -12.98
             :raio-x)
; Execution error (ExceptionInfo) at schemas.aula3/eval9012$novo-pedido (REPL:40).
; Input to novo-pedido does not match schema: 
;	   [nil (named (not (financeiro-maior-ou-igual-a-zero -12.98)) valor) nil]  

;; ---------

(def Numeros [s/Num])

(s/validate Numeros [15])
;; => [15]

(s/validate Numeros [15 0 12])
;; => [15 0 12]

(s/validate Numeros [12 1.56 -1])
;; => [12 1.56 -1]

(s/validate Numeros [])
;; => []

(s/validate Numeros [nil])
; Execution error (ExceptionInfo) at schema.core/validator$fn (core.clj:155).
; Value does not match schema: [(not (instance? java.lang.Number nil))]

(s/validate Numeros nil)
;; => nil
;; ^ nil é como uma sequencia vazia por isso é considerado um vetor de Numeros valido
;;   isso vem do nil pulling

(def Plano [s/Keyword])

(def PacienteV2
  {:id    PosInt
   :nome  s/Str
   :plano Plano})

(s/validate PacienteV2 {:id 15
                        :nome "Guilherme"
                        :plano [:raio-x :ultrassom]})
;; => {:id 15, :nome "Guilherme", :plano [:raio-x :ultrassom]}

(s/validate PacienteV2 {:id 15
                        :nome "Guilherme"
                        :plano [:raio-x]})
;; => {:id 15, :nome "Guilherme", :plano [:raio-x]}

(s/validate PacienteV2 {:id 15
                        :nome "Guilherme"
                        :plano []})
;; => {:id 15, :nome "Guilherme", :plano []}

(s/validate PacienteV2 {:id 15
                        :nome "Guilherme"
                        :plano nil})
;; => {:id 15, :nome "Guilherme", :plano nil}

(s/validate PacienteV2 {:id 15
                        :nome "Guilherme"
                        :plano nil})

(s/validate PacienteV2 {:id 15
                        :nome "Guilherme"})
; Execution error (ExceptionInfo) at schema.core/validator$fn (core.clj:155).
; Value does not match schema: {:plano missing-required-key}
; ^ gostaríamos que :plano fosse opcional

