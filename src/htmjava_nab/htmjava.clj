(ns htmjava.clj
  (import [org.numenta.nupic.network Network Region Layer PALayer]
          [org.numenta.nupic.algorithms TemporalMemory]
          [org.numenta.nupic Parameters]))


(defn look-up [thing by]
  (. thing (lookup by)))

(defn add-to! [added-to added]
  (. added-to (add added)))

(defn reset-it! [thing]
  (. thing reset)
  thing)

(defn create-layer [name params]
  (.. Network (createLayer name params)))

(defn create-region [name]
  (.. Network (createRegion name)))

(def parameters (. Parameters getAllDefaultParameters))

(def network (Network. "test" parameters))

(def layer (->> (TemporalMemory.)
             (add-to! (create-layer "l1" parameters))))

(def region (let [r (create-region "r1")]
             (add-to! r layer)
              (add-to! network r)))

(-> network (reset-it!) (look-up "r1") (look-up "l1") (. hasTemporalMemory))

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
