(defproject htmjava-nab "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://github.com/nupic-community/htmjava-nab"
  :license {:name "GNU Affero General Public Licence, Version 3"
            :url "http://www.gnu.org/licenses/agpl-3.0.en.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.numenta/htm.java "0.6.5"]
                 [io.reactivex/rxclojure "1.0.0"]
                 [incanter/incanter-core "1.9.0"]
                 [incanter/incanter-io "1.9.0"]
                 [org.clojure/data.csv "0.1.3"]
                 [org.clojure/data.json "0.2.6"]
                 [clj-time "0.11.0"]]
  :main ^:skip-aot htmjava-nab.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[midje "1.8.2"]
                                  [criterium "0.4.3"]]}}
  :plugins [[michaelblume/lein-marginalia "0.9.0"]
            [lein-midje "3.0.1"]]
  :repositories [["sonatype" {:url "http://oss.sonatype.org/content/repositories/releases"
                              ;; Disable signing releases deployed to this repo.
                              ;; (Not recommended.)
                              :sign-releases false
                              ;; You can also set the policies for how to handle
                              ;; :checksum failures to :fail, :warn, or :ignore.
                              :checksum :fail
                              ;; How often should this repository be checked for
                              ;; snapshot updates? (:daily, :always, or :never)
                              :update :daily}]]

  :documentation {:files {"doc/index"
                          {:input "test/htmjava_nab/docs.clj"
                           :title "HTM.java NAB"
                           :sub-title "Clojure Library for HTM.java and the Numenta Anomaly Benchmark"
                           :author "Fergal Byrne"
                           :email  "fergalbyrnedublin@gmail.com"
                           :tracking "UA-44409012-2"}}}
  :eval-in-leiningen true)

