(ns phage.routes
  (:require [chord.http-kit :refer [wrap-websocket-handler]]
            [clojure.core.async :refer [<! >! put! close! go-loop go timeout]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [org.httpkit.server :refer [run-server]]
            [phage.core :refer [start game-over? move moves to-string player-turn]]
            [phage.prep :refer [prep]]
            [phage.views :refer [index-page]]
            [ring.middleware.reload :as reload]
            [ring.util.response :refer [redirect]]))

(def clients (atom {}))

(defn ws-handler
  "Handler for Websockets connection"
  [{:keys [ws-channel] :as req}]
  (let [addr (:remote-addr req)]
    (swap! clients assoc addr true)
    (println "Opened connection from " addr)
    (go-loop [match start]
      (when (>! ws-channel (prep match))
        (when-not (game-over? match)
          (let [mv (if (= 1 (player-turn match))
                     (do
                       (println "Pausing before picking random move")
                       (<! (timeout 1000))
                       (rand-nth (moves match)))
                     (do
                       (println "Waiting for user move")
                       (<! ws-channel)))]
            (println "Move: " mv)
            (recur (move match mv))))))
    (println "Client disconnected: " addr)
    (swap! clients dissoc addr)))

(defroutes main-routes
  (GET "/" [] (index-page))
  (GET "/ws" [] (-> ws-handler (wrap-websocket-handler)))
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
    ;; The #' is useful when you want to hot-reload code
    ;; You may want to take a look: https://github.com/clojure/tools.namespace
    ;; and http://http-kit.org/migration.html#reload
    (reset! server (run-server handler {:port port}))
    (println (str "Started server on http://localhost:" port))))
