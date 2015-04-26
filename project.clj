(defproject metosin/fnhouse-swagger "0.5.2"
  :description "Swagger-support for fnhouse"
  :url "https://github.com/metosin/fnhouse-swagger"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [prismatic/plumbing "0.4.2"]
                 [prismatic/fnhouse "0.1.1"]
                 [metosin/ring-swagger "0.20.1"]
                 [ring/ring-core "1.3.2"]]
  :profiles {:dev {:plugins [[lein-clojars "0.9.1"]
                             [lein-midje "3.1.3"]]
                   :dependencies [[midje "1.7.0-SNAPSHOT"]
                                  [metosin/ring-swagger-ui "2.1.1-M2"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0-alpha4"]]}}
  :aliases {"all" ["with-profile" "dev:dev,1.7"]
            "test-ancient" ["midje"]})
