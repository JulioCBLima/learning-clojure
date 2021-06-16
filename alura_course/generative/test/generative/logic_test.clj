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

(deftest chega-em-test
  (testing "Que é colocada uma pessoa em filas menores que 5"
    (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4) 10)
            pessoa (gen/sample gen/string-alphanumeric)]
      )))
