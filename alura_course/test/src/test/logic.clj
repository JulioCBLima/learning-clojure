(ns test.logic)

(defn cabe-na-fila?
  [hospital departamento]
  (some-> hospital
          departamento
          count
          (< 5))) ; (not= 5); => bug

(defn- tenta-colocar-na-fila
  [hospital departamento pessoa]
  (when (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)))

(defn chega-em
  [hospital departamento pessoa]
  (if-let [novo-hospital (tenta-colocar-na-fila hospital departamento pessoa)]
    {:hospital novo-hospital
     :resultado :sucesso}
    {:hospital hospital
     :resultado :impossivel-colocar-pessoa-na-fila}))
