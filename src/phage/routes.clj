(ns phage.routes
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [org.httpkit.server :refer [run-server]]
            [phage.views :refer [index-page]]
            [ring.middleware.reload :as reload]
            [ring.util.response :refer [redirect]]))

(defroutes main-routes
  (GET "/" [] (index-page))
  (route/resources "/")
  (route/not-found "Page not found"))

(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@server :timeout 100)
    (reset! server nil)))

(defn -main [& args]
  (let [port 8080
        handler (reload/wrap-reload #'main-routes)]
    ;; The #' is useful, when you want to hot-reload code
    ;; You may want to take a look: https://github.com/clojure/tools.namespace
    ;; and http://http-kit.org/migration.html#reload
    (reset! server (run-server handler {:port port}))
    (println (str "Started server on http://localhost:" port))))
