(ns test.logic)

(defn cabe-na-fila?
  [hospital departamento]
  (some-> hospital
          departamento
          count
          (< 5))) ; (not= 5); => bug

(defn chega-em
  [hospital departamento pessoa]
  (update hospital departamento conj pessoa))