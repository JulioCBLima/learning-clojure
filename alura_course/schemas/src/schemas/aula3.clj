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
