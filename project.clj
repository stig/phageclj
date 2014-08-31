(defproject phage "0.1.0-SNAPSHOT"
  :description "A game"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.303.0-886421-alpha"]
                 [org.clojure/clojurescript "0.0-2227"]
                 [http-kit "2.1.16"]
                 [hiccup "1.0.5"]
                 [compojure "1.1.8"]]

  :plugins [[lein-cljsbuild "1.0.3"]]
  :hooks [leiningen.cljsbuild]

  :cljsbuild {:builds [{:source-paths ["src"]
                        :compiler {:output-to "target/classes/public/js/phage.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]}

  :main phage.routes)
