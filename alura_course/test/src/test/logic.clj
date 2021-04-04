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
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "Não cabe ninguém neste departamento" 
                    {:paciente pessoa
                     :tipo :impossivel-colocar-pessoa-na-fila}))))
; ^ antes de fazer swap chega-em vai ter que tratar o resultado

(defn atende
  [hospital departamento]
  (update hospital departamento pop))

(defn proxima
  [hospital departamento]
  (-> hospital
      departamento
      peek))

(defn transfere
  [hospital de para]
  (let [pessoa (proxima hospital de)]
    (-> hospital
        (atende de)
        (chega-em para pessoa))))
