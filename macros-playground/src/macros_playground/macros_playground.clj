(ns macros-playground.macros-playground
  (:gen-class))

(defn on-rollout? [id fn-name rollout]
  (let [rollout-set (get rollout fn-name)]
    (boolean (contains? rollout-set id))))

(defn skip? [fn-name replaced-by {:keys [id executed-validations rollout]}]
  (println "fn-name: " fn-name)
  (println "replaced-by: " replaced-by)
  (println "id: " id)
  (println "executed-validations: " executed-validations)
  (and (not (contains? executed-validations replaced-by))
       (on-rollout? id fn-name rollout)))

(defmacro my-defn [name args & body]
  `(defn ~(vary-meta name assoc :some-key :some-value) ~args
     (if (and (:can-skip? (meta '~name))
              (skip? (:name (meta '~name)) (:replaced-by (meta '~name)) '~args))
       (do (println "Skipped!")
           nil)
       (do (println "Executed!")
           ~@body))))

(my-defn ^{:can-skip? true :replaced-by :inc} vai-um
  [{:keys [n]}]
  (+ n 1))

(def rollout {:vai-um   #{1 3 5}
              :vai-dois #{2 4 6}})

(vai-um {:n 2 :id 2 :executed-validations #{:inc :dec} :rollout rollout})

(meta #'vai-um)
(meta (var vai-um))


(:can-skip? (meta (var vai-um)))

