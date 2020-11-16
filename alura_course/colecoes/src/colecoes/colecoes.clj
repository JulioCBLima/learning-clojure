(ns colecoes.colecoes
  (:gen-class))

(def nomes ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"])

(map println nomes)

(first nomes)
;; => "daniela"
(rest nomes)
;; => ("guilherme" "carlos" "paulo" "lucia" "ana")
(next nomes)
;; => ("guilherme" "carlos" "paulo" "lucia" "ana")
(rest [])
;; => ()
(next [])
;; => nil

(seq [])
;; => nil
(seq [1 2 3])
;; => (1 2 3)

(defn meu-mapa
  [func sequencia]
  (let [primeiro (first nomes)]
    (func primeiro)
    (meu-mapa (rest nomes))))
