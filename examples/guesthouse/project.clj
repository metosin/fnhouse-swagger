(defproject guesthouse "0.1.0-SNAPSHOT"
  :description "Example guestbook project for demonstrating fnhouse"
  :url "https://github.com/Prismatic/fnhouse/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[prismatic/plumbing "0.3.5"]
                 [prismatic/fnhouse "0.1.1"]
                 [metosin/fnhouse-swagger "0.4.0" :exclusions [org.clojure/clojure]]
                 [metosin/ring-swagger "0.14.0" :exclusions [org.clojure/clojure]]
                 [metosin/ring-swagger-ui "2.0.17"]
                 [org.clojure/clojure "1.6.0"]
                 [clj-http "1.0.1"]
                 [ring/ring-core "1.3.1"]
                 [ring/ring-jetty-adapter "1.3.1"]
                 [ring/ring-json "0.3.1"]])
