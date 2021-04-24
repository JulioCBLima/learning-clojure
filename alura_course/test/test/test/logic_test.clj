(ns test.logic-test
  (:require [clojure.test :refer :all]
            [schema.core :as s]
            [test.logic :as logic]
            [test.model :as model]))

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


(deftest chega-em-test

  (testing "aceita pessoas enquanto cabem pessoas na fila"
    (is (= {:espera [1 2 3 4 5]}
           (logic/chega-em {:espera [1 2 3 4]} :espera 5)))

    (is (= {:espera [1 2 5]}
           (logic/chega-em {:espera [1 2]} :espera 5))))

  (testing "não aceita quando não cabe na fila"
    (is (thrown? clojure.lang.ExceptionInfo
                 (logic/chega-em {:espera [1 35 42 64 21]} :espera 76))))

  (testing "não aceita quando não cabe na fila"
    (is (try
          (logic/chega-em {:espera [1 35 42 64 21]} :espera 76)
          false
          (catch clojure.lang.ExceptionInfo e
            (= :impossivel-colocar-pessoa-na-fila (:tipo (ex-data e))))))))

(deftest transfere-teste
  (testing "aceita pessoas se cabe"
    (s/with-fn-validation
      (let [hospital-original {:espera (conj model/fila-vazia 5)
                               :raio-x model/fila-vazia}]
        (is (= {:espera []
                :raio-x [5]}
               (logic/transfere hospital-original
                                :espera
                                :raio-x))))
      (let [hospital-original {:espera (conj model/fila-vazia 51 5)
                               :raio-x (conj model/fila-vazia 13)}]
        (is (= {:espera [5]
                :raio-x [13 51]}
               (logic/transfere hospital-original
                                :espera
                                :raio-x))))))

  (testing "recusa pessoa se não cabe"
    (let [hospital-cheio {:espera (conj model/fila-vazia 5)
                          :raio-x (conj model/fila-vazia 1 2 52 42 13)}]
      (is (thrown? clojure.lang.ExceptionInfo
                   (logic/transfere hospital-cheio
                                    :espera
                                    :raio-x)))))
  
  (testing "Não pode invocar transferência sem hospital"
    (is (thrown? clojure.lang.ExceptionInfo (logic/transfere nil
                                                             :espera
                                                             :raio-x)))))
