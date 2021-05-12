(ns test.logic-test
  (:require [clojure.test :refer :all]
            [schema.core :as s]
            [generative.logic :as logic]
            [generative.model :as model]
            [clojure.test.check.generators :as gen]))

(deftest cabe-na-fila-test?
  (testing "Que cabe numa fila vazia"
    (is (= true
           (logic/cabe-na-fila? {:espera []} :espera))))
  
  (testing "Não cabe na fila quando a fila está cheia"
    (is (= false
           (logic/cabe-na-fila? {:espera [1 2 3 4 5]} :espera))))

  (testing "Que não cabe na fila quando tem mais do que uma fila cheia"
    (is (= false
           (logic/cabe-na-fila? {:espera [1 2 3 4 5 6]} :espera))))

  (testing "Que cabe na fila quando tem um pouco menos do que uma fila cheia"
    (is (= true
           (logic/cabe-na-fila? {:espera [1 2 3 4]} :espera)))
    (is (= true
           (logic/cabe-na-fila? {:espera [1 2]} :espera))))
  
  (testing "Que ... quando o departamento não existe"
    (is (nil? (logic/cabe-na-fila? {:espera [1 2 3 4]} :raio-x)))))
