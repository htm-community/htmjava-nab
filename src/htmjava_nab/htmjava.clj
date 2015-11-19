(ns htmjava.clj
  (import [org.numenta.nupic.network Network Region Layer PALayer]
          [org.numenta.nupic.algorithms TemporalMemory]
          [org.numenta.nupic Parameters]))


(defn look-for [thing by]
  (. thing (lookup by)))


(defn add-to [added-to added]
  (. added-to (add added)))

(def parameters (. Parameters getAllDefaultParameters))
(def network (Network. "test" parameters))
(def layer (let [tm (TemporalMemory.)
                 l (.. Network (createLayer "l1" parameters))]
             (. l (add tm))))
(def region (let [r (.. Network (createRegion "r1"))]
             (add-to r layer)
              (add-to network r)))

(do (. network reset)
  (-> network (look-for "r1") (look-for "l1") (. hasTemporalMemory)))

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
