(defproject htmjava-nab "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.numenta/htm.java "0.6.5"]]
  :main ^:skip-aot htmjava-nab.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :repositories [["sonatype" {:url "http://oss.sonatype.org/content/repositories/releases"
                              ;; Disable signing releases deployed to this repo.
                              ;; (Not recommended.)
                              :sign-releases false
                              ;; You can also set the policies for how to handle
                              ;; :checksum failures to :fail, :warn, or :ignore.
                              :checksum :fail
                              ;; How often should this repository be checked for
                              ;; snapshot updates? (:daily, :always, or :never)
                              :update :daily}]])

