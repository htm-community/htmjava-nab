(ns htmjava-nab.htmjava
  (import [org.numenta.nupic.network Network Region Layer PALayer]
          [org.numenta.nupic.algorithms TemporalMemory SpatialPooler]
          [org.numenta.nupic Parameters]))

; wrapper API

(defn look-up [thing by]
  (. thing (lookup by)))

(defn lookup-in [root path]
  (reduce look-up root path))

(defn add-to! [added-to added]
  (. added-to (add added)))

(defn reset-it! [thing]
  (. thing reset)
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

(defn temporal-memory [] (TemporalMemory.))

(defn spatial-pooler [] (SpatialPooler.))

(defn record-num [thing] (. thing getRecordNum))

(def default-parameters (. Parameters getAllDefaultParameters))

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
