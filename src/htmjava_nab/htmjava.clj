(ns htmjava-nab.htmjava
  (require [htmjava-nab.parameters :refer :all])
  (import [org.numenta.nupic.network Network Region Layer PALayer]
          [org.numenta.nupic.network.sensor FileSensor Sensor SensorParams
           SensorParams$Keys SensorParams$Keys$Args SensorFactory]
          [org.numenta.nupic.datagen ResourceLocator]
          [org.numenta.nupic.algorithms TemporalMemory SpatialPooler Anomaly]
          [org.numenta.nupic Parameters]
          [java.util Arrays]
          [java.util.function.Supplier]
          [java.util.function.UnaryOperator]))

; wrapper API

(defn look-up [thing by]
  (. thing (lookup by)))

(defn lookup-in [root path]
  (reduce look-up root path))

(defn add-to! [added-to added]
  (. added-to (add added)))

(defn observe [observed]
  (. observed observe))

(defn reset-it! [thing]
  (. thing reset)
  thing)

(defn connect! [thing name1 name2]
  (. thing (connect name1 name2))
  thing)

(defmulti prepare second)

(defmethod prepare :ints [[data _]]
  (int-array data))

(defn compute! [thing data hint]
  (. thing (compute (prepare [data hint])))
  thing)

(defn create-layer [name params]
  (.. Network (createLayer name params)))

(defn create-region [name]
  (.. Network (createRegion name)))

(defn create-network [name params]
  (Network. name params))

(def fs-creator (reify org.numenta.nupic.network.sensor.SensorFactory
                  (create [_ params] (FileSensor/create params))))

(defn create-file-sensor [f]
  (let [res-path (ResourceLocator/path f)
        path-key  (into-array String ["FILE" "PATH"])

        path-vals (into-array String ["" res-path])
        sensor-params (SensorParams/create path-key path-vals)
        ]
      (Sensor/create fs-creator sensor-params)))

(defn temporal-memory [] (TemporalMemory.))

(defn spatial-pooler [] (SpatialPooler.))

(defn create-anomaly [] (Anomaly/create))

(defn record-num [thing] (. thing getRecordNum))
(defn encoding [thing] (. thing getEncoding))
(defn sdr [thing] (. thing getSDR))
(defn classifier-input [thing field]
  (.. thing getClassifierInput (get field) (get "inputValue")))

(defn encoding-str [thing] (. Arrays (toString (encoding thing))))
(defn sdr-str [thing] (. Arrays (toString (sdr thing))))

(defn head [thing] (. thing getHead))

(defn upstream-region [thing] (. thing getUpstreamRegion))
(defn downstream-region [thing] (. thing getDownstreamRegion))

(def default-parameters (. Parameters getAllDefaultParameters))

(defn fresh-parameters [] (. Parameters getAllDefaultParameters))

; output.getRecordNum()  Arrays.toString(output.getEncoding())) Arrays.toString(output.getSDR()) + ", " + output.getAnomalyScore());
(defn output-vector [output ks]
  (mapv #(% output) ks))

(defn key->KEY [k]
  (first (k params-map)))

(defn set-param! [p k v]
  (.setParameterByKey p (key->KEY k) v)
  p)
(defn get-param [p k]
  (.getParameterByKey p (key->KEY k)))

(defn alter-param! [thing k v]
  (.alterParameter thing (key->KEY k) v)
  thing)
; client code

(comment "
  .add(Network.createRegion("r1")
                .add(Network.createLayer("l1", p).add(new TemporalMemory())));
  network.reset();
            assertTrue(network.lookup("r1").lookup("l1").hasTemporalMemory());
  Parameters.getAllDefaultParameters
import org.numenta.nupic.Parameters;
import org.numenta.nupic.Parameters.KEY;
import org.numenta.nupic.algorithms.Anomaly;
import org.numenta.nupic.algorithms.CLAClassifier;
import org.numenta.nupic.algorithms.PASpatialPooler;
import org.numenta.nupic.algorithms.TemporalMemory;
import org.numenta.nupic.datagen.ResourceLocator;
import org.numenta.nupic.encoders.Encoder;
import org.numenta.nupic.encoders.MultiEncoder;
import org.numenta.nupic.network.Inference;
import org.numenta.nupic.network.PALayer;
import org.numenta.nupic.network.Network;
import org.numenta.nupic.network.Region;
import org.numenta.nupic.network.sensor.FileSensor;
import org.numenta.nupic.network.sensor.Sensor;
import org.numenta.nupic.network.sensor.SensorParams;
import org.numenta.nupic.network.sensor.SensorParams.Keys;
")
