(ns curso.aula4)

(def precos [30 700 1000])

(update precos 0 #(+ % 1))

(defn aplica-desconto?
  [valor-bruto]
  (> valor-bruto 100))

(defn valor-descontado
  [valor-bruto]
  (if (aplica-desconto? valor-bruto)
    (let [taxa-de-desconto (/ 10 100)
          desconto (* valor-bruto taxa-de-desconto)]
      (- valor-bruto desconto))
    valor-bruto))

(map valor-descontado precos)

(filter aplica-desconto? precos)

(map valor-descontado (filter aplica-desconto? precos))

(reduce + precos)