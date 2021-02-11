(ns recprotmm.aula1)

(defn adiciona-paciente
  "pacientes = {15 {paciente 15}, 25 {paciente 23}}"
  [pacientes paciente]
  (if-let [id (:id paciente)]
    (assoc pacientes id paciente)
    (throw (ex-info "Paciente não possui id"
                    {:paciente paciente}))))

(defn testa-uso-de-pacientes
  []
  (let [pacientes {}
        guilherme {:id 15
                   :nome "Guilherme"
                   :nascimento "18/9/1981"}
        daniela   {:id 20
                   :nome "Daniela"
                   :nascimento "18/9/1982"}
        paulo     {:nome "Paulo"
                   :nascimento "18/10/1983"}]
    (adiciona-paciente pacientes guilherme) ;ok
    (adiciona-paciente pacientes daniela) ; ok
    (adiciona-paciente pacientes paulo) ; nok
    ))

(testa-uso-de-pacientes)

(defrecord Paciente [id nome nascimento])

(def p (->Paciente 15 "Guilherme" "18/9/1981"))
p
;; => {:id 15, :nome "Guilherme", :nascimento "18/9/1981"}
(type p)
;; => recprotmm.aula1.Paciente
(record? p)
;; => true

(.nome p)
;; => "Guilherme"

(Paciente. 15 "Guilherme" "18/9/1981")
;; => {:id 15, :nome "Guilherme", :nascimento "18/9/1981"}

(defrecord PacienteV2 [^Long id nome nascimento])

(map->Paciente {:id 15
                :nome "Guilherme"
                :nascimento "18/9/1981"})
;; => {:id 15, :nome "Guilherme", :nascimento "18/9/1981"}

(let [guilherme (->Paciente 15 "Guilherme" "18/9/1981")]
  (println (:id guilherme)) ; => 15
  (println (vals guilherme)) ; => (15 Guilherme 18/9/1981)
  )

(map->Paciente {:id 15
                :nome "Guilherme"
                :nascimento "18/9/1981"
                :rg "222222"})
;; => {:id 15, :nome "Guilherme", :nascimento "18/9/1981", :rg "222222"}

(map->Paciente {:nome "Guilherme"
                :nascimento "18/9/1981"
                :rg "222222"})
;; => {:id nil, :nome "Guilherme", :nascimento "18/9/1981", :rg "222222"}

#_(->Paciente "Guilherme" "18/9/1981")
;; => arity error

(assoc (Paciente. nil "guilherme" "18/9/1981") :id 38)
;; => {:id 38, :nome "Guilherme", :nascimento "18/9/1981"}

(class (assoc (Paciente. nil "guilherme" "18/9/1981") :id 38))
;; => recprotmm.aula1.Paciente

(= 
 (map->Paciente {:id 15
                :nome "Guilherme"
                :nascimento "18/9/1981"})
 (map->Paciente {:id 15
                :nome "Guilherme"
                :nascimento "18/9/1981"}))
;; => true

(= 
 (map->Paciente {:id 15
                :nome "Guilherme"
                :nascimento "18/9/1981"})
 (map->Paciente {:id 18
                :nome "Guilherme"
                :nascimento "18/9/1981"}))
;; => false
