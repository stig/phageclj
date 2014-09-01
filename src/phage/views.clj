(ns phage.views
  (:require
    [hiccup.page :refer [html5 include-js include-css]]))

(defn index-page
  []
  (html5
   [:head
    [:title "Phage Match"]
    (include-css "/css/styles.css")]
   [:body
    [:h1 "Phage Match"]
    [:div#main]
    (include-js "/js/phage.js")]))
