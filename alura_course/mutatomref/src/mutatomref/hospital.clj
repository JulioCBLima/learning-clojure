(ns mutatomref.hospital
  (:require [mutatomref.model :as h.model]))

(def hospital-do-gui (h.model/novo-hospital))
hospital-do-gui
;; => {:espera <-()-<, :laboratorio1 <-()-<, :laboratorio2 <-()-<, :laboratorio3 <-()-<}

