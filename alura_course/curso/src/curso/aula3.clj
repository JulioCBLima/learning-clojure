(ns curso.aula3)


(defn aplicar-desconto?
  ([valor-bruto]
   (> valor-bruto 100)))

(aplicar-desconto? 101)

(defn valor-descontado
  "Retorna o valor com desconto de 10% se o valor bruto for estritamente maior do que 100"
  [aplica? valor-bruto]
  (if (aplica? valor-bruto)
    (let [taxa-de-desconto (/ 10 100)
          desconto         (* valor-bruto taxa-de-desconto)]
      (println "Calculando desconto de" desconto)
      (- valor-bruto desconto))
    valor-bruto))

(valor-descontado #(> %1 100) 1000)

(valor-descontado #(> %1 100) 100)

(valor-descontado #(> % 100) 1000)

(valor-descontado #(> % 100) 100)
