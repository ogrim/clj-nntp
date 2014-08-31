(defproject clj-nntp/clj-nntp "0.1.0-SNAPSHOT"
  :description "A Clojure NNTP library wrapping Apache Commons Net NNTP"
  :url "https://github.com/ogrim/clj-nntp/"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :global-vars {*warn-on-reflection* true}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [commons-net/commons-net "3.3"]])
