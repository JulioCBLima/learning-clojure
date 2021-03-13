(ns schemas.aula2
  (:require [clojure.pprint :refer [pprint]]
            [schema.core :as s]))

;; (s/defrecord Paciente
;;   [id :- Long
;;    nome :- s/Str])

;; (Paciente. 15 "Guilherme")
;; ;; => {:id 15, :nome "Guilherme"}

;; ;; (->Paciente 15 "Guilherme")
;; ;; => {:id 15, :nome "Guilherme"}

;; ;; (Paciente. "15" "Guilherme")
;; ;; => {:id "15", :nome "Guilherme"}
;; ;; ^ não liga para o schema :(

;; ;; (->Paciente "15" "Guilherme")
;; ;; => {:id "15", :nome "Guilherme"}
;; ;; ^ não liga para o schema :(

;; ;; (map->Paciente {15 "Guilherme"})
;; ;; => {:id nil, :nome nil, 15 "Guilherme"}
;; ;; O mapa está errado. Criou um objeto onde a key 15 tem o value "Guilherme"

;; (map->Paciente {"15" "Guilherme"})
;; ;; => {:id nil, :nome nil, "15" "Guilherme"}
;; ;; O mapa está errado. Criou um objeto onde a key "15" tem o value "Guilherme"

;; (map->Paciente {:id 15
;;                 :nome "Guilherme"})
;; ;; => {:id 15, :nome "Guilherme"}
;; ;; ^ Parece ok

;; (map->Paciente {:id "15"
;;                 :nome "Guilherme"})
;; ;; => {:id "15", :nome "Guilherme"}
;; ;; ^ também não liga para o schema :(

;;----------------------------------

(def Paciente
  "Schema de um paciente"
  {:id s/Num
   :nome s/Str})

(s/explain Paciente)
;; => {:id Num, :nome Str}

(s/validate Paciente {:id 15
                      :nome "Guilherme"})
;; => {:id 15, :nome "Guilherme"}

(s/validate Paciente {:id 15
                      :name "Guilherme"})
; Execution error (ExceptionInfo) at schema.core/validator$fn (core.clj:155).
; Value does not match schema: {:nome missing-required-key, :name disallowed-key}

(s/validate Paciente {:id 15})
; Execution error (ExceptionInfo) at schema.core/validator$fn (core.clj:155).
; Value does not match schema: {:nome missing-required-key}

(s/validate Paciente {:id "15"
                      :nome "Guilherme"})
; Execution error (ExceptionInfo) at schema.core/validator$fn (core.clj:155).
; Value does not match schema: {:id (not (instance? java.lang.Number "15"))}

(s/validate Paciente {:id "15"
                      :nome :guilherme})
; Execution error (ExceptionInfo) at schema.core/validator$fn (core.clj:155).
; Value does not match schema: {:id (not (instance? java.lang.Number "15")), :nome (not (instance? java.lang.String :guilherme))}

(s/defn novo-paciente :- Paciente
  [id :- s/Num
   nome :- s/Str]
  {:id id
   :nome nome})

(novo-paciente 15 "Guilherme")
;; => {:id 15, :nome "Guilherme"}

(novo-paciente "15" "Guilherme")
;; => {:id "15", :nome "Guilherme"}

(s/set-fn-validation! true)

(novo-paciente "15" "Guilherme")
; Execution error (ExceptionInfo) at schemas.aula2/eval16496$novo-paciente (REPL:74).
; Input to novo-paciente does not match schema: 
; [(named (not (instance? java.lang.Number "15")) id) nil]
