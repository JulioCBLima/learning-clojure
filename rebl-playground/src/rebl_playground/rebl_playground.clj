(ns rebl-playground.rebl-playground
  (:gen-class))

(def mapinha {:chave-um       1
              :chave-dois     2
              :chave-composta {:sub-chave-um   1
                               :sub-chave-dois 2}})

(def mapinha2 {:chave-um       1
               :chave-dois     2
               :chave-composta {:sub-chave-um   1
                                :sub-chave-dois 2}})

(def map1+map2 {:mapao1 mapinha
                :mapao2 mapinha2})
