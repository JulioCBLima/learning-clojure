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