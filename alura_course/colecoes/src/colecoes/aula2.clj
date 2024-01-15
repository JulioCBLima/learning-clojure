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

(defn conta-2
  [elementos]
  (loop [total-ate-agora 0
         elementos-restantes elementos]
    (if (seq elementos-restantes)
      (recur (inc total-ate-agora) (next elementos-restantes))
      total-ate-agora)))

(conta-2 vetor)


(take 100 (for [x (range 100000000) y (range 100000)
                :while (< y x)]
            [x y]))


(take 100 (for [x (range 100000000) y (range 1000000) :while (< y x)] [x y]))

(take 10
      (for [x (range 100000)
            :let [y (* x 3)]
            :when (even? y)]
        y))

