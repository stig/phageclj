(ns phage.views
  (:require
    [hiccup.page :refer [html5 include-js]]))

(defn index-page
  []
  (html5
   [:head
    [:title "Hello World"]]
   [:body
    [:h1 "Hello World"]
    [:p "This is a paragraph."]
    [:div#main]
    (include-js "/js/phage.js")]))
