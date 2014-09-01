(ns phage.views
  (:require
    [hiccup.page :refer [html5 include-js]]))

(defn index-page
  []
  (html5
   [:head
    [:title "Phage Match"]]
   [:body
    [:h1 "Phage Match"]
    [:div#main]
    (include-js "/js/phage.js")]))
