(ns map-playground.map-playround
  (:gen-class))

(def map03 {:key 3
            :next-token nil})

(def map02 {:key1 2
            :next-token :map03})

(def map01 {:key1 1
            :next-token :map02})

(def map-of-maps {:map01 map01
                  :map02 map02
                  :map03 map03})
map-of-maps
;; => {:map01 {:key1 1, :next-token :map02}, :map02 {:key1 2, :next-token :map03}, :map03 {:key 3, :next-token nil}}

(defn get-map-from-map-of-maps
  [map-key]
  (map-of-maps map-key))

(conj (conj [] map02) map01)
;; => [{:key1 2, :next-token :map03} {:key1 1, :next-token :map02}]


(defn map-recursive-build
  ([map]
   (map-recursive-build map []))
  ([map acc]
   (let [conj-map (conj acc map)
         next-token (:next-token map)]
     (if next-token
       (recur (get-map-from-map-of-maps next-token) conj-map)
       conj-map))))

(map-recursive-build map01)
;; => [{:key1 1, :next-token :map02} {:key1 2, :next-token :map03} {:key 3, :next-token nil}]
