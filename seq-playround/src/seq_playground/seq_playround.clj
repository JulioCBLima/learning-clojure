(ns seq-playground.seq-playround
  (:gen-class))

;; how lazy-seq works?
(defn even-numbers
  ([] (even-numbers 0))
  ([n] (cons n (lazy-seq (even-numbers (+ n 2))))))

(take 3 (even-numbers))
;; => (0 2 4)

(defn gen-map
  [n]
  {:Value (str n)
   :Next (cond 
           (< n 5) (inc n)
           :else nil)})

(map #(gen-map %1) (range 6))
;; => ({:Value "0", :Next 1}
;;     {:Value "1", :Next 2}
;;     {:Value "2", :Next 3}
;;     {:Value "3", :Next 4}
;;     {:Value "4", :Next 5}
;;     {:Value "5", :Next nil})

(defn lazy-maps
  ([]
   (lazy-maps 0))
  ([next]
   (let [mapa (gen-map next)]
     (cons mapa (lazy-seq (lazy-maps (:Next mapa)))))))

(take 8 (lazy-maps))
;; => ({:Value "0", :Next 1}
;;     {:Value "1", :Next 2}
;;     {:Value "2", :Next 3}
;;     {:Value "3", :Next 4}
;;     {:Value "4", :Next 5}
;;     {:Value "5", :Next nil}) ;; -> error after here

;; maybe take-while could help us here
(take-while #(some? (:Next %)) (lazy-maps))
;; => ({:Value "0", :Next 1} {:Value "1", :Next 2} {:Value "2", :Next 3} {:Value "3", :Next 4} {:Value "4", :Next 5})
;; we need the last map to be returned as well. So take-while is not good :/

;; we can probaly use reduce with reduced to interrumpt the chain *after* we see a nil :Next
(reduce (fn
          [acc mapa]
          (if (some? (:Next mapa))
            (cons mapa acc)
            (reduced (cons mapa acc))))
        [] ;; this is used as first element to avoid the wrong insertion from the first map
        (lazy-maps))
;; => ({:Value "5", :Next nil}
;;     {:Value "4", :Next 5}
;;     {:Value "3", :Next 4}
;;     {:Value "2", :Next 3}
;;     {:Value "1", :Next 2}
;;     {:Value "0", :Next 1})
;; it worked \o/

;; how to avoid two (cons mapa acc) ?
(reduce (fn
          [acc mapa]
          (let [accumulated (cons mapa acc)]
            (if (some? (:Next mapa))
              accumulated
              (reduced accumulated))))
        []
        (lazy-maps))
;; => ({:Value "5", :Next nil}
;;     {:Value "4", :Next 5}
;;     {:Value "3", :Next 4}
;;     {:Value "2", :Next 3}
;;     {:Value "1", :Next 2}
;;     {:Value "0", :Next 1})

;; how about a anom function?
(reduce #(let [acc (cons %2 %1)]
           (if (some? (:Next %2))
             acc
             (reduced acc)))
        []
        (lazy-maps))
;; => ({:Value "5", :Next nil}
;;     {:Value "4", :Next 5}
;;     {:Value "3", :Next 4}
;;     {:Value "2", :Next 3}
;;     {:Value "1", :Next 2}
;;     {:Value "0", :Next 1})
