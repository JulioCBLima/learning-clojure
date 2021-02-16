(ns recprotmm.logic
  (:require [recprotmm.model :as model]))

(defn agora
  []
  (model/to-ms (java.util.Date.)))
