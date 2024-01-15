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

(novo-paciente 14 "julio")

;; --------------------

(defn estritamente-positivo?
  [x]
  (> x 0))

(def EstritamentePositivo (s/pred estritamente-positivo?))

(s/validate EstritamentePositivo 15)
;; => 15

(s/validate EstritamentePositivo 0)
; Execution error (ExceptionInfo) at schema.core/validator$fn (core.clj:155).
; Value does not match schema: (not (schemas.aula2/estritamente-positivo? 0))

(s/validate EstritamentePositivo -15)
; Execution error (ExceptionInfo) at schema.core/validator$fn (core.clj:155).
; Value does not match schema: (not (schemas.aula2/estritamente-positivo? -15))

(def EstritamentePositivoV2 (s/pred estritamente-positivo? 'estritamente-positivo))

(s/validate EstritamentePositivoV2 -15)
; Execution error (ExceptionInfo) at schema.core/validator$fn (core.clj:155).
; Value does not match schema: (not (estritamente-positivo -15))

;; -------------------
(def PacienteV2
  "Schema de um paciente"
  {:id (s/constrained s/Int estritamente-positivo?)
   :nome s/Str})

(s/validate PacienteV2 {:id 15
                        :nome "guilherme"})
;; => {:id 15, :nome "guilherme"}

(s/validate PacienteV2 {:id 0
                        :nome "guilherme"})
; Execution error (ExceptionInfo) at schema.core/validator$fn (core.clj:155).
; Value does not match schema: {:id (not (schemas.aula2/estritamente-positivo? 0))}

(s/validate PacienteV2 {:id -15
                        :nome "guilherme"})
; Execution error (ExceptionInfo) at schema.core/validator$fn (core.clj:155).
; Value does not match schema: {:id (not (schemas.aula2/estritamente-positivo? -15))}

(def PacienteV3
  "Schema de um paciente"
  {:id (s/constrained s/Int pos-int?)
   :nome s/Str})

(s/validate PacienteV3 {:id 15
                        :nome "guilherme"})
;; => {:id 15, :nome "guilherme"}

(s/validate PacienteV3 {:id 0
                        :nome "guilherme"})
; Execution error (ExceptionInfo) at schema.core/validator$fn (core.clj:155).
; Value does not match schema: {:id (not (schemas.aula2/estritamente-positivo? 0))}

(s/validate PacienteV3 {:id -15
                        :nome "guilherme"})
; Execution error (ExceptionInfo) at schema.core/validator$fn (core.clj:155).
; Value does not match schema: {:id (not (schemas.aula2/estritamente-positivo? -15))}

(def PacienteV4
  "Schema de um paciente"
  {:id (s/constrained s/Int #(> % 0))
   :nome s/Str})

(s/validate PacienteV4 {:id 15
                        :nome "guilherme"})
;; => {:id 15, :nome "guilherme"}

(s/validate PacienteV4 {:id 0
                        :nome "guilherme"})
; Execution error (ExceptionInfo) at schema.core/validator$fn (core.clj:155).
; Value does not match schema: {:id (not (schemas.aula2/fn--8848 0))}

(def PacienteV5
  "Schema de um paciente"
  {:id (s/constrained s/Int #(> % 0) 'estritamente-positivo)
   :nome s/Str})

(s/validate PacienteV5 {:id 15
                        :nome "guilherme"})
;; => {:id 15, :nome "guilherme"}

(s/validate PacienteV5 {:id 0
                        :nome "guilherme"})
; Execution error (ExceptionInfo) at schema.core/validator$fn (core.clj:155).
; Value does not match schema: {:id (not (estritamente-positivo 0))}
