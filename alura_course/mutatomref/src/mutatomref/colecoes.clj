(ns mutatomref.colecoes)

(def vetor [111 222])

(conj vetor 333)
;; => [111 222 333]

(conj vetor 444)
;; => [111 222 444]

(pop vetor)
;; => [111]

(def lista-ligada '(111 222))

(conj lista-ligada 333)
;; => (333 111 222)

(conj lista-ligada 444)
;; => (444 111 222)

(pop lista-ligada)
;; => (222)

(def conjunto #{111 222})

(conj conjunto 333)
;; => #{222 111 333}

(conj conjunto 444)
;; => #{222 111 444}

(pop conjunto)
;; Execution error

(defn cria-fila
  ([]
   (clojure.lang.PersistentQueue/EMPTY))
  ([coll]
   (reduce conj clojure.lang.PersistentQueue/EMPTY coll)))

(def fila (cria-fila [111 222]))

(conj fila 333)
;; => <-(111 222 333)-<

(conj fila 444)
;; => <-(111 222 444)-<

(pop fila)
;; => <-(222)-<

(peek fila)
;; => 111
