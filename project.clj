(defproject txture "1.0.0-SNAPSHOT"
  :description "A modern, flat-file blog engine"
  :dependencies [[org.clojure/clojure "1.1.0"]
                 [org.clojure/clojure-contrib "1.1.0"]
                 [compojure "0.4.0"]
                 [hiccup "0.2.6"]
                 [ring/ring-jetty-adapter "0.2.3"]]
  :repl-init-script "src/txture/core.clj")
