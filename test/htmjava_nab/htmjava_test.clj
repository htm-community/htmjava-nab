(ns htmjava-nab.htmjava-test
  (:require [clojure.test :refer :all]
            [htmjava-nab.htmjava :refer :all]
            [rx.lang.clojure.core :as rx])
  (:import rx.Observable))

(defn fail [] (is (= 0 1)))

(deftest network-region-layer-tm
  (testing "Network->Region->Layer->TM reset()"
    (let [network (create-network "test" default-parameters)
          tm (temporal-memory)
          layer (create-layer "l1" default-parameters)
          region (create-region "r1")]
      (do
        (->> tm (add-to! layer) (add-to! region) (add-to! network))
        (is (-> network (reset-it!) (lookup-in ["r1" "l1"]) (. hasTemporalMemory)))))))

(deftest network-region-layer-sp
  (testing "Network->Region->Layer->SP reset()"
    (let [network (create-network "test" default-parameters)
          sp (spatial-pooler)
          layer (create-layer "l1" default-parameters)
          region (create-region "r1")]
      (do
        (->> sp (add-to! layer) (add-to! region) (add-to! network))
        (is (not (-> network (reset-it!) (lookup-in ["r1" "l1"]) (. hasTemporalMemory))))))))

(deftest reset-record-num
  (testing "Network->Region->Layer->TM reset-record-num"
    (let [network (create-network "test" default-parameters)
          tm (temporal-memory)
          layer (create-layer "l1" default-parameters)
          region (create-region "r1")]
      (do
        (->> tm (add-to! layer) (add-to! region) (add-to! network))
        (-> network (compute! [2 3 4] :ints) (compute! [2 3 4] :ints))
        (is (= 1 (-> network (lookup-in ["r1" "l1"]) record-num)))
        (is (= 0 (-> network (reset-it!) (lookup-in ["r1" "l1"]) record-num)))))))

(defn many-sp-regions [n p]
  (mapv (fn [i] (->> (spatial-pooler)
                   (add-to! (create-layer "l" p))
                   (add-to! (create-region (str "r" (inc i))))))
       (range n)))

(deftest add-many-regions
  (testing "Network with many regions"
    (let [n 5
          network (create-network "test" default-parameters)
          regions (many-sp-regions n default-parameters)
          lookup-region (fn [i] (look-up network (str "r" (inc i))))]
      (do
        (is (not (some (set regions) (map lookup-region (range n)))))
        (doseq [r regions] (add-to! network  r))
        (doseq [i (range n)] (is (= (regions i) (lookup-region i))))))))

(deftest connect-many-regions
  (testing "Network with many connected regions"
    (let [n 5
          network (create-network "test" default-parameters)
          regions (many-sp-regions n default-parameters)]
      (do
        (try
          (do (connect! network "r1" "r2") (fail))
          (catch Exception e
            (is (= "Region with name: r2 not added to Network." (.getMessage e)))))
        (doseq [r regions] (add-to! network  r))
        (doseq [i (range 1 n)] (connect! network (str "r" i) (str "r" (inc i))))
        (loop [upstream (regions 0)
               tail upstream]
          (if-let [new-tail (upstream-region tail)]
            (recur tail new-tail)
            (is (= upstream (regions (- n 2))))))
        (loop [downstream (regions (dec n))
               the-head downstream]
          (if-let [new-head (downstream-region the-head)]
            (recur the-head new-head)
            (do (is (= the-head (regions 0)))
              (is (= the-head (head network))))))))))

(deftest test-subscribe
  (testing "Test subscribe"
    (let [network (create-network "test" default-parameters)
          tm (temporal-memory)
          layer (create-layer "l1" default-parameters)
          region (create-region "r1")
          dump-record (fn [output]
                        (println (str "Record: " (output-vector output [record-num sdr-str]))))
          show-error (fn [e] (println (str "Opps! " (.getMessage e))) (.printStackTrace e))
          report-complete (fn [] (println "Done!"))]
      (do
        (->> tm (add-to! layer) (add-to! region) (add-to! network))
        (rx/subscribe (observe network) dump-record show-error report-complete)
        (-> network (compute! [2 3 4] :ints) (compute! [2 3 4] :ints))
        (is (= 1 (-> network (lookup-in ["r1" "l1"]) record-num)))
        (is (= 0 (-> network (reset-it!) (lookup-in ["r1" "l1"]) record-num)))))))


(comment "

")
