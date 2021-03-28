(ns test.logic-test
  (:require [clojure.test :refer :all]
            [test.logic :refer :all]))

(deftest cabe-na-fila-test?
  (testing "Que cabe numa fila vazia"
    (is (= true
           (cabe-na-fila? {:espera []} :espera))))
  
  (testing "Não cabe na fila quando a fila está cheia"
    (is (= false
           (cabe-na-fila? {:espera [1 2 3 4 5]} :espera))))

  (testing "Que não cabe na fila quando tem mais do que uma fila cheia"
    (is (= false
           (cabe-na-fila? {:espera [1 2 3 4 5 6]} :espera))))

  (testing "Que cabe na fila quando tem um pouco menos do que uma fila cheia"
    (is (= true
           (cabe-na-fila? {:espera [1 2 3 4]} :espera)))
    (is (= true
           (cabe-na-fila? {:espera [1 2]} :espera))))
  
  (testing "Que ... quando o departamento não existe"
    (is (nil? (cabe-na-fila? {:espera [1 2 3 4]} :raio-x)))))

(deftest chega-em-test

  (testing "aceita pessoas enquanto cabem pessoas na fila"
    (is (= {:espera [1 2 3 4 5]}
           (chega-em {:espera [1 2 3 4]} :espera 5)))
    
    (is (= {:espera [1 2 5]}
           (chega-em {:espera [1 2]} :espera 5))))
  
  (testing "não aceita quando não cabe na fila"
    (is (thrown? clojure.lang.ExceptionInfo
                 (chega-em {:espera [1 35 42 64 21]} :espera 76)))))