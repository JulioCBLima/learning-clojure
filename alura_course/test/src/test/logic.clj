(ns test.logic)

(defn cabe-na-fila?
  [hospital departamento]
  (some-> hospital
          departamento
          count
          (< 5))) ; (not= 5); => bug

(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "Não cabe ninguém neste departamento" {:paciente pessoa}))))
;; ^ não é uma boa jogar uma exception genérica para tratar erros
