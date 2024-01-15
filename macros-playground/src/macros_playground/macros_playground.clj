(ns macros-playground.macros-playground
  (:gen-class))

(defn on-rollout? [id fn-name rollout]
  (let [rollout-set (get rollout fn-name)]
    (boolean (contains? rollout-set id))))

(defn skip? [fn-name replaced-by executed-validations id rollout]
  (and (contains? executed-validations replaced-by)
       (on-rollout? id (keyword fn-name) rollout)))

(defmacro my-defn [name args & body]
  `(defn ~(vary-meta name assoc :some-key :some-value) ~args
     (println "Function" '~name "called with arguments:" ~args "body: " '~body)
     (let [replaced-by# (:replaced-by (meta '~name))
           executed-validations# (-> ~args first :as :executed-validations)
           id# (-> ~args first :as :id)
           rollout# (-> ~args first :as :rollout)]
       (if (and (:can-skip? (meta '~name))
                (skip? '~name replaced-by# executed-validations# id# rollout#))
         (do (println "Skipped!")
             nil)
         (do (println "Executed!")
             ~@body)))))

(my-defn ^{:can-skip? true :replaced-by :inc} vai-um
  [{:keys [n] :as _args}]
  (+ n 1))

(def rollout {:vai-um   #{1 3 5}
              :vai-dois #{2 4 6}})

(vai-um {:n 2 :id 1 :executed-validations #{:inc :dec} :rollout rollout})

(meta #'vai-um)
(meta (var vai-um))


(:can-skip? (meta (var vai-um)))

(merge {:n 1} {:as `_args#})

(defmacro ohmy [name args & body]
  `(let [allargs# (gensym)
         #_#_our-params# (merge (first ~args) {:as allargs#})]
     (defn ~(vary-meta name assoc :some-key :some-value) {:n 1}
       (println "Function" '~name "called with arguments:"  "body: " '~body)
       (do (println "Executed!")
           ~@body))))

(defmacro ohmy [name args & body]
  `(defn ~(vary-meta name assoc :some-key :some-value) ~args
     (println "Function" '~name "called with arguments:" ~args "body: " '~body)
     (let [allargs# (gensym)]
       (println "merginz " (merge (first ~args) {:as allargs#}))
       (println "allargs " (keys 'allargs#)))
     (do (println "Executed!")
         ~@body)))

(ohmy ^{:can-skip? true :replaced-by :inc} vai-um
  [{:keys [n]}]
  (+ n 1))

(vai-um {:n 1 :x 2})

(defmacro my-defn [name args & body]
  `(defn ~(vary-meta name assoc :some-key :some-value) ~args
     (println "Function" '~name "called with arguments:" ~args "body: " '~body)
     (let [replaced-by# (:replaced-by (meta '~name))
           executed-validations# (-> ~args first :as :executed-validations)
           id# (-> ~args first :as :id)
           rollout# (-> ~args first :as :rollout)]
       (if (and (:can-skip? (meta '~name))
                (skip? '~name replaced-by# executed-validations# id# rollout#))
         (do (println "Skipped!")
             nil)
         (do (println "Executed!")
             ~@body)))))

(my-defn ^{:can-skip? true :replaced-by :inc} vai-um
         [{:keys [n] :as _args}]
         (+ n 1))

(def rollout {:vai-um   #{1 3 5}
              :vai-dois #{2 4 6}})

(vai-um {:n 2 :id 1 :executed-validations #{:inc :dec} :rollout rollout})

(defmacro defbanana  [name & fn-declaration]
  (let [{:keys [args body]} (extract-fn-definition fn-declaration)
        argzera# (gensym "args")]
    `(clojure.core/defn ~name [~argzera#]
       (clojure.core/let [~(first args) ~argzera#]
         (println ~argzera#)
         (do ~@body)))))
