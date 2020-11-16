
(ns colecoes.aula1)

(def nomes ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"])

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
  (let [primeiro (first sequencia)]
    (when-not (nil? primeiro) ;; só para em nil (não para em false ou "")
      (func primeiro)
      (meu-mapa func (rest sequencia)))))

(meu-mapa println nomes)
;; => nil

; (meu-mapa println (range 100000))
;; => StackOverflowError

(defn meu-mapa-recur
  [funcao sequencia]
  (let [primeiro (first sequencia)]
    (when-not (nil? primeiro)
        (funcao primeiro)
        (recur funcao (rest sequencia)))))

; (meu-mapa-recur println (range 10000))
;; => sem StackOverflowError

