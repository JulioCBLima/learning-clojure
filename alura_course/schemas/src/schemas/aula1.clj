(ns schemas.aula1
  (:require [clojure.pprint :refer [pprint]]
            [schema.core :as s]))

(defn adiciona-paciente
  [pacientes paciente]
  (if-let [id (:id paciente)]
    (assoc pacientes id paciente)
    (throw (ex-info "Paciente não possui id" {:paciente paciente}))))

(defn adiciona-visita
  [visitas paciente novas-visitas]
  (if (contains? visitas paciente)
    (update visitas paciente concat novas-visitas)
    (assoc visitas paciente novas-visitas)))

(defn imprime-relatorio-de-paciente
  [visitas paciente]
  (get visitas paciente))

(let [guilherme {:id 15
                 :nome "Guilherme"}
      daniela   {:id 20
                 :nome "Daniela"}
      paulo     {:id 25
                 :nome "Paulo"}
      pacientes (reduce adiciona-paciente {} [guilherme daniela paulo])]
  pacientes)
;; => {15 {:id 15, :nome "Guilherme"}, 20 {:id 20, :nome "Daniela"}, 25 {:id 25, :nome "Paulo"}}

(let [visitas   (-> {}
                    (adiciona-visita 15 ["01/01/2019"])
                    (adiciona-visita 20 ["01/02/2019" "01/01/2020"])
                    (adiciona-visita 15 ["01/03/2019"]))]
  visitas)
;; => {15 ("01/01/2019" "01/03/2019"), 20 ["01/02/2019" "01/01/2020"]}

(let [guilherme {:id 15
                 :nome "Guilherme"}
      daniela   {:id 20
                 :nome "Daniela"}
      paulo     {:id 25
                 :nome "Paulo"}
      pacientes (reduce adiciona-paciente {} [guilherme daniela paulo])
      visitas   (-> {}
                    (adiciona-visita 15 ["01/01/2019"])
                    (adiciona-visita 20 ["01/02/2019" "01/01/2020"])
                    (adiciona-visita 15 ["01/03/2019"]))]
  (imprime-relatorio-de-paciente visitas guilherme))
;; => nil
;; ^ o problema aqui é paciente tem vários schemas distintos (mapas, números, etc).
;;   nesse caso precisávamos usar o número, mas usamos o mapa inteiro

;; -------------------------
;; schemas

(s/validate Long 15)
;; => 15

(s/validate Long "Guilherme") ;; => Execution Error...


(s/defn teste-simples
  [x :- Long]
  x)

(teste-simples 30)
;; => 30

(teste-simples "guilherme")
;; => "guilherme"

(s/set-fn-validation! true)

(teste-simples 30)
;; => 30

(teste-simples "guilherme") ;; => Execution Error...

;;-----------------
;;definindo funcoes anteriores com schema

(s/defn imprime-relatorio-de-paciente
  [visitas 
   paciente :- Long]
  (get visitas paciente))

(let [guilherme {:id 15
                 :nome "Guilherme"}
      daniela   {:id 20
                 :nome "Daniela"}
      paulo     {:id 25
                 :nome "Paulo"}
      pacientes (reduce adiciona-paciente {} [guilherme daniela paulo])
      visitas   (-> {}
                    (adiciona-visita 15 ["01/01/2019"])
                    (adiciona-visita 20 ["01/02/2019" "01/01/2020"])
                    (adiciona-visita 15 ["01/03/2019"]))]
  (imprime-relatorio-de-paciente visitas guilherme))
;; => ; Execution error (ExceptionInfo) at schemas.aula1/eval16310$imprime-relatorio-de-paciente (REPL:83).
;; Input to imprime-relatorio-de-paciente does not match schema: 
;; [nil (named (not (instance? java.lang.Long a-clojure.lang.PersistentArrayMap)) paciente)]
