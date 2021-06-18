(ns test.logic-test
  (:require [clojure.test :refer :all]
            [schema.core :as s]
            [generative.logic :as logic]
            [generative.model :as model]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]))

(deftest cabe-na-fila-test?
  (testing "Que cabe numa fila vazia"
    (is (= true
           (logic/cabe-na-fila? {:espera []} :espera))))

  (testing "Que cabe pessoas em filas de tamanho até 4 inclusive"
    (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4) 100)]
      (is (logic/cabe-na-fila? {:espera fila} :espera))))
  
  (testing "Não cabe na fila quando a fila está cheia"
    (is (not (logic/cabe-na-fila? {:espera [1 2 3 4 5]} :espera))))

  (testing "Que não cabe na fila quando tem mais do que uma fila cheia"
    (is (not (logic/cabe-na-fila? {:espera [1 2 3 4 5 6]} :espera))))

  (testing "Que cabe na fila quando tem um pouco menos do que uma fila cheia"
    (is (logic/cabe-na-fila? {:espera [1 2 3 4]} :espera))
    (is (logic/cabe-na-fila? {:espera [1 2]} :espera)))
  
  (testing "Que ... quando o departamento não existe"
    (is (nil? (logic/cabe-na-fila? {:espera [1 2 3 4]} :raio-x)))))

(defspec coloca-uma-pessoa-em-filas-menores-que-5 10
  (prop/for-all [fila (gen/vector gen/string-alphanumeric 0 4)
                 pessoa gen/string-alphanumeric]
    (is (= {:espera (conj fila pessoa)}
           (logic/chega-em {:espera fila} :espera pessoa)))))

(def nome-aleatorio 
  (gen/fmap clojure.string/join
    (gen/vector gen/char-alphanumeric 5 10)))

(defn vetor->fila [vetor]
  (reduce conj model/fila-vazia vetor))

(def fila-nao-cheia-gen
  (gen/fmap
   vetor->fila
   (gen/vector nome-aleatorio 0 4)))

(defn transfere-ignorando-erro [hospital para]
  (try
    (logic/transfere hospital :espera para)
    (catch clojure.lang.ExceptionInfo e
      (cond
        (= :fila-cheia (:type (ex-data e))) hospital
        :else (throw e))
      )
    (catch java.lang.AssertionError _e
      hospital)))

(defspec transfere-tem-que-manter-a-quantidade-de-pessoas 10
  (prop/for-all [espera (gen/fmap vetor->fila (gen/vector nome-aleatorio 0 10))
                 raio-x fila-nao-cheia-gen
                 ultrasom fila-nao-cheia-gen
                 vai-para (gen/vector (gen/elements [:raio-x :ultrasom]) 10 50)]
    (let [hospital-inicial {:espera espera
                            :raio-x raio-x
                            :ultrasom ultrasom}
          hospital-final   (reduce transfere-ignorando-erro hospital-inicial vai-para)]
      (is (= (logic/total-de-pacientes hospital-inicial)
             (logic/total-de-pacientes hospital-final))))))
