(ns hanzihack.routes.home
  (:require
    [hanzihack.layout :as layout]
    [clojure.java.io :as io]
    [hanzihack.middleware :as middleware]
    [ring.util.http-response :as response]))

(defn home-page [request]
  (layout/render request "home.html"))

(defn app-page [request]
  (layout/render request "app.html"))


(defn base-page [request]
  (layout/render request "base.html"))


(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/app" {:get app-page}]
   ["/base" {:get base-page}]
   ["/graphiql" {:get (fn [request] (layout/render request "graphiql.html"))}]
   ["/docs" {:get (fn [_]
                    (-> (response/ok (-> "docs/docs.md" io/resource slurp))
                        (response/header "Content-Type" "text/plain; charset=utf-8")))}]])

