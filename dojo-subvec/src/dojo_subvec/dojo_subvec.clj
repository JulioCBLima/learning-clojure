(ns dojo-subvec.dojo-subvec
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def vectorzin [10 20 30 40 50 60 70 80 90 100 110 120 130 140 150])



(subvec vectorzin 0 3)
;; => [10 20 30]

(subvec vectorzin 3)
;; => [40 50 60 70 80 90 100 110 120 130 140 150]

(into (subvec vectorzin 0 3)
      (subvec vectorzin 4))
;; => [10 20 30 50 60 70 80 90 100 110 120 130 140 150]








(comment
  ;; = subvec - Example 1 = 
  
  ;; not supplying 'end' returns vector from 'start' to (count vector)
  user=> (subvec [1 2 3 4 5 6 7] 2)
  [3 4 5 6 7]
  
  ;; supplying 'end' returns vector from 'start' to element (- end 1)
  user=> (subvec [1 2 3 4 5 6 7] 2 4)
  [3 4]
  ;; See also:
  vector
  vector?
  )

