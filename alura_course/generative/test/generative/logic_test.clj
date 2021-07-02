(ns test.logic-test
  (:require [clojure.test :refer :all]
            [schema.core :as s]
            [generative.logic :as logic]
            [generative.model :as model]
            [schema-generators.generators :as g]
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
        :else (throw e)))))

(defspec transfere-tem-que-manter-a-quantidade-de-pessoas 100
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

(defn adiciona-fila-de-espera [[hospital fila]]
  (assoc hospital :espera fila))

(def hospital-gen (->> (gen/tuple (gen/not-empty (g/generator model/Hospital))
                                  fila-nao-cheia-gen)
                       (gen/fmap adiciona-fila-de-espera)))

(def chega-em-gen
  (gen/tuple (gen/return logic/chega-em)
             (gen/return :espera)
             nome-aleatorio
             (gen/return 1)))

(defn transfere-gen
  [hospital]
  (let [departamentos (keys hospital)]
    (gen/tuple (gen/return logic/transfere)
               (gen/elements departamentos)
               (gen/elements departamentos)
               (gen/return 0))))

(defn acao-gen 
  [hospital]
  (gen/one-of [chega-em-gen (transfere-gen hospital)]))

(defn executa-uma-acao
  [{:keys [hospital delta] :as situacao} [fn param1 param2 param3]]
  (try
    {:hospital (fn hospital param1 param2)
     :delta (+ delta param3)}
    (catch clojure.lang.ExceptionInfo _e
      situacao)))

(defspec simula-um-dia-do-hospita-acumula-pessoas 1
  (prop/for-all [hospital-inicial hospital-gen]
    (let [acoes                        (-> (gen/vector (acao-gen hospital-inicial) 1 100)
                                           gen/not-empty
                                           gen/generate)
          situacao-inicial             {:hospital hospital-inicial :delta 0}
          situacao-final               (reduce executa-uma-acao situacao-inicial acoes)
          total-de-pacientes-inicial   (logic/total-de-pacientes hospital-inicial)
          total-de-pacientes-final     (logic/total-de-pacientes (:hospital situacao-final))]
      (is (>= total-de-pacientes-final total-de-pacientes-inicial))
      (is (= (- total-de-pacientes-final (:delta situacao-final)) total-de-pacientes-inicial)))))
