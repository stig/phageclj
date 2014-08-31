(ns phage.render
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(def match (atom {:cells (vec (range 4))}))

(println "Hello world!")

(defn phage-match []
  [:p [:strong "Insert match here."]])

(reagent/render-component [phage-match] (.getElementById js/document "main"))
