(ns mutatomref.logic)

(defn chega-em
  [hospital departamento pessoa]
  (update hospital departamento conj pessoa))

(defn atende
  [hospital departamento]
  (let [fila (get hospital departamento)]
    (update hospital departamento pop)))