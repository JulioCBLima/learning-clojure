(ns recprotmm.aula3
  (:require [clojure.pprint :as pprint]
            [recprotmm.logic :as logic]))

(defn carrega-paciente
  [id]
  (println "Carregando" id)
  (Thread/sleep (rand 3000))
  {:id id
   :carregado-em (logic/agora)})

;; (carrega-paciente 15)
;; => {:id 30, :carregado-em 1613481548694}

;; (carrega-paciente 30)
;; => {:id 30, :carregado-em 1613481557361}

;; função pura ;)
(defn carrega-se-nao-existe
  [cache id carregadora]
  (if (contains? cache id)
    cache
    (let [paciente (carregadora id)]
      (assoc cache id paciente))))

;; (carrega-se-nao-existe {} 15 carrega-paciente)
;; => {15 {:id 15, :carregado-em 1613482898224}}

;; (carrega-se-nao-existe {15 {:id 15}} 15 carrega-paciente)
;; => {15 {:id 15}}

(defprotocol Carregavel
  (carrega! 
   [this id]))

(defrecord Cache
  [cache carregadora]

  Carregavel

  (carrega!
   [this id]
   (swap! cache carrega-se-nao-existe id carregadora)
   #_(swap! (.cache this) carrega-se-nao-existe id (.carregadora this))
   (get @cache id)))

(def pacientes (->Cache (atom {}) 
                        carrega-paciente))

pacientes
;; => {:cache #<Atom@6fee7a6d: {}>,
;;     :carregadora #object[recprotmm.aula3$carrega_paciente 0x5cff45 "recprotmm.aula3$carrega_paciente@5cff45"]}

(carrega! pacientes 15)
;; => {:id 15, :carregado-em 1613483492146}

(carrega! pacientes 15)
;; => {:id 15, :carregado-em 1613483492146}

