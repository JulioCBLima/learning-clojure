(ns colecoes.aula2)

(def vetor ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"])

(defn conta
  ([elementos]
   (conta 0 elementos))
  ([total-ate-agora elementos]
   (if (seq elementos)
    (recur (inc total-ate-agora) (next elementos))
     total-ate-agora)))

(conta vetor)