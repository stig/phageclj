(ns phage.views
  (:require
    [hiccup.page :refer [html5 include-js]]))

(defn index-page
  []
  (html5
   [:head
    [:title "Hello World"]
    (include-js "/js/react-0.10.0.js")
    (include-js "/js/phage.js")]
   [:body
    [:h1 "Hello World"]
    [:p "This is a paragraph."]]))
