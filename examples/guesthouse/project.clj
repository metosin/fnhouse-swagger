(defproject guesthouse "0.1.0-SNAPSHOT"
  :description "Example guestbook project for demonstrating fnhouse"
  :url "https://github.com/Prismatic/fnhouse/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[prismatic/plumbing "0.2.2"]
                 [prismatic/fnhouse "0.1.0"]
                 [metosin/fnhouse-swagger "0.1.1"]
                 [metosin/ring-swagger "0.8.3"]
                 [metosin/ring-swagger-ui "2.0.12-1"]
                 [org.clojure/clojure "1.5.1"]
                 [clj-http "0.9.1"]
                 [ring/ring-core "1.2.2"]
                 [ring/ring-jetty-adapter "1.2.2"]
                 [ring/ring-json "0.3.0"]])
