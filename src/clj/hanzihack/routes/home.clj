(ns hanzihack.routes.home
  (:require
    [hanzihack.layout :as layout]
    [hanzihack.db.core :as db]
    [clojure.java.io :as io]
    [hanzihack.middleware :as middleware]
    [ring.util.http-response :as response]))

(defn parking-page [request]
  (layout/render request "parking.html"))

(defn app-page [request]
  (layout/render request "app.html"))




(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/park" {:get parking-page}]
   ["/app" {:get app-page}]
   ["/about" {:get home-page}]
   ["/graphiql" {:get (fn [request] (layout/render request "graphiql.html"))}]
   ["/docs" {:get (fn [_]
                    (-> (response/ok (-> "docs/docs.md" io/resource slurp))
                        (response/header "Content-Type" "text/plain; charset=utf-8")))}]])

