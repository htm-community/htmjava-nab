(defproject htmjava-nab "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://github.com/nupic-community/htmjava-nab"
  :license {:name "GNU Affero General Public Licence, Version 3"
            :url "http://www.gnu.org/licenses/agpl-3.0.en.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.numenta/htm.java "0.6.5"]
                 [io.reactivex/rxclojure "1.0.0"]]
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

