(defproject phage "0.1.0-SNAPSHOT"
  :description "A game"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.303.0-886421-alpha"]
                 [org.clojure/clojurescript "0.0-2227"]

                 [compojure "1.1.8"]
                 [garden "1.2.1"]
                 [hiccup "1.0.5"]
                 [http-kit "2.1.16"]
                 [reagent "0.4.2"]
                 [ring/ring-core "1.1.8"]
                 [ring/ring-devel "1.1.8"]]

  :plugins [[lein-garden "0.2.0"]
            [lein-cljsbuild "1.0.3"]]

  :hooks [leiningen.cljsbuild leiningen.garden]

  :cljsbuild {:builds [{:source-paths ["src"]
                        :compiler {:preamble ["reagent/react.js"]
                                   :output-to "target/classes/public/js/phage.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]}

  :garden {:builds [{:stylesheet phage.styles/screen
                     :compiler {:output-to "target/classes/public/css/screen.css"
                                :pretty-print? false}}]}

  :main phage.routes)
