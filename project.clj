(defproject ddate "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            }
  :dependencies [[org.clojure/clojure "1.7.0"] [clojure.java-time "0.3.0"] [org.clojure/math.numeric-tower "0.0.4"]]
  :main ^:skip-aot ddate.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
