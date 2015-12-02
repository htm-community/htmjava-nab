(ns htmjava-nab.hotgym
  (:require [clojure.test :refer :all]
            [htmjava-nab.htmjava :refer :all]
            [rx.lang.clojure.core :as rx]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clj-time.core :as tc]
            [clj-time.format :as tf])
  (:import [rx Observable]
           [org.numenta.nupic.util MersenneTwister]))

(def opf-timestamp-re #"(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):([0-9.]+)")
(defn strip-leading-zeros [s] (clojure.string/replace-first s #"^0+([1-9.])" "$1"))

(defn old-parse-opf-date
	[s]
	(let [m (re-matches opf-timestamp-re s)]
	  (if m (let [rev (reverse (map strip-leading-zeros (rest m)))
                  secs (java.lang.Double/parseDouble (first rev))
                  items (map #(. Integer parseInt %) (rest rev))
                  ]
        (apply tc/date-time (reverse (conj items secs)))))))

(def opf-format (tf/formatter "yyyy-MM-dd HH:mm:ss.SS"))
;(tf/parse opf-format "16:13:49:06 on 2013-04-06")
(defn parse-opf-date [s] (tf/parse opf-format s))

(defn parse-opf-item
	"converts a CSV item (a string) into a Clojure value"
    [v t]
    (condp = t
	  "datetime" (parse-opf-date v)
	  "float" (double (read-string v))
	  v))

(defn safe-parse-opf-item
	"converts a CSV item (a string) into a Clojure value. catches and throws exceptions"
    [v t]
    (try (parse-opf-item v t)
	(catch Exception e (do (println (str "caught exception for value " v)) (throw e)))))

(defn parse-opf-row
	[line & {:keys [fields types flags]}]
	(vec (for [i (range (count line))]
	  (let [^String v (line i) ^String t (types i) ^String field (fields i) ^String flag (flags i)
	    parsed (parse-opf-item v t)
	    opf-meta {:raw v :type t :field field :flag flag}]
	    (with-meta
		  [parsed]
		  {:opf-meta opf-meta})))))

; type-map (apply hash-map (vec (interleave line types)))
(defn parse-opf-data
  "parse OPF data from CSV test rows"
  [raw-csv & {:keys [fields types flags]}]
  (mapv #(parse-opf-row % :fields fields :types types :flags flags) (drop 3 raw-csv)))

(defn load-opf-data [data & n]
  (let [raw-csv (if n (vec (take (first n) data))
                  (vec data))
        fields (raw-csv 0)
        types (raw-csv 1)
        flags (raw-csv 2)
        ;encoders (make-encoders fields types)
        ;opf-map {:fields fields :types types :flags flags :encoders encoders}
        parsed-data (parse-opf-data raw-csv :fields fields :types types :flags flags)
        ]
    (println "load-opf-data: parsed" (count raw-csv) "lines")
    {:raw-csv raw-csv :fields fields :types types :flags flags
     :parsed-data parsed-data
     ;:encoders encoders
     ;:bits (reduce + (:bits encoders))
     ;:on (reduce + (:on encoders))
     }))

(defn load-opf-file [config]
  (let [f (:file config)
        n (:read-n-records config)
        fileio 	(with-open [in-file (io/reader f)]
	                  (vec (doall (csv/read-csv in-file))))
        n (if (and n (not= n :all)) n (count fileio))]
    (println "load-opf-file: loaded" (count fileio) "lines")
       (load-opf-data fileio n)))

      (def hotgym-config {:file "resources/hotgym.csv"
                          :read-n-records :all
                          :fields ["gym" {:type :string
                                          :doc "Name of this Gym"
                                          :encoder {:type :hash-encoder
                                                    :bits 32
                                                    :on 8}
                                          :sequence-flag? true}
                                   "address" {:type :string
                                              :doc "Address of this Gym"
                                              :encoder {:type :hash-encoder
                                                        :bits 32
                                                        :on 8}}
                                   "timestamp" {:type :datetime
                                                :doc "Timestamp of this data record"
                                                :subencode [{:field :day-of-year}
                                                            {:field :day-of-week}
                                                            {:field :time-of-day}
                                                            {:field :weekday?}]}
                                   ]})
      (def hotgym (load-opf-file hotgym-config))

(deftest hotgym-loaded
  (testing "hotgym data loaded"

(with-open [out-file (io/writer "hotgym-full.csv")]
  (csv/write-csv out-file
                 [["abc" "def"]
                  ["ghi" "jkl"]]))

    (is (= 87840 (count (:parsed-data hotgym))))))

