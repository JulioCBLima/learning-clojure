(ns schemas.aula5
  (:require [clojure.pprint :refer [pprint]]
            [schema.core :as s]))


(def Plano [s/Keyword])

;; (def PosInt (s/pred pos-int? 'inteiro-positivo))

(def PacienteId (s/pred pos-int? 'paciente-id))

(def Paciente
  {:id                          PacienteId
   :nome                        s/Str
   :plano                       Plano
   (s/optional-key :nascimento) s/Str})

(def Pacientes
  {PacienteId Paciente})

(def Visita s/Str)

(def Visitas
  {PacienteId [Visita]})


(s/defn adiciona-paciente :- Pacientes
  [pacientes :- Pacientes
   paciente  :- Paciente]
  (let [id (:id paciente)]
    (assoc pacientes id paciente))) ; não precisa do if-let porque os schemas garantem que id existirá

(s/defn adiciona-visita :- Visitas
  [visitas       :- Visitas
   paciente      :- PacienteId
   novas-visitas :- [Visita]]
  (if (contains? visitas paciente)
    (update visitas paciente concat novas-visitas)
    (assoc visitas paciente novas-visitas)))

(s/defn imprime-relatorio-de-paciente
  [visitas  :- Visitas
   paciente :- PacienteId]
  (str "Visitas do paciente " paciente ": " (get visitas paciente)))

(let [guilherme {:id    15
                 :nome  "Guilherme"
                 :plano [:raio-x]}
      daniela   {:id    20
                 :nome  "Daniela"
                 :plano []}
      paulo     {:id    25
                 :nome  "Paulo"
                 :plano nil}
      pacientes (reduce adiciona-paciente {} [guilherme daniela paulo])
      visitas   (-> {}
                    (adiciona-visita 15 ["01/01/2019"])
                    (adiciona-visita 20 ["01/02/2019" "01/01/2020"])
                    (adiciona-visita 15 ["01/03/2019"]))]
  (imprime-relatorio-de-paciente visitas guilherme))
; Execution error (ExceptionInfo) at schemas.aula5/eval9203$imprime-relatorio-de-paciente (REPL:41).
; Input to imprime-relatorio-de-paciente does not match schema: 
;	   [nil (named (not (paciente-id a-clojure.lang.PersistentArrayMap)) paciente)]  

(let [guilherme {:id    15
                 :nome  "Guilherme"
                 :plano [:raio-x]}
      daniela   {:id    20
                 :nome  "Daniela"
                 :plano []}
      paulo     {:id    25
                 :nome  "Paulo"
                 :plano nil}
      pacientes (reduce adiciona-paciente {} [guilherme daniela paulo])
      visitas   (-> {}
                    (adiciona-visita 15 ["01/01/2019"])
                    (adiciona-visita 20 ["01/02/2019" "01/01/2020"])
                    (adiciona-visita 15 ["01/03/2019"]))]
  (imprime-relatorio-de-paciente visitas 20))
;; => "Visitas do paciente 20: [\"01/02/2019\" \"01/01/2020\"]"

