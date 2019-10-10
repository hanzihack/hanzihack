(ns hanzihack.handler
  (:require
    [hanzihack.middleware :as middleware]
    [hanzihack.layout :refer [error-page]]
    [hanzihack.routes.home :refer [home-routes]]
    [hanzihack.routes.services :refer [service-routes]]
    [hanzihack.layout :as layout]
    [reitit.swagger-ui :as swagger-ui]
    [reitit.ring :as ring]
    [ring.middleware.content-type :refer [wrap-content-type]]
    [ring.middleware.webjars :refer [wrap-webjars]]
    [hanzihack.env :refer [defaults]]
    [mount.core :as mount]))

(mount/defstate init-app
  :start ((or (:init defaults) (fn [])))
  :stop  ((or (:stop defaults) (fn []))))

(defn home-page [request]
  (layout/render request "base.html"))

(mount/defstate app-routes
  :start
  (ring/ring-handler
    (ring/router
      [
       (home-routes)
       [""
        ["/" {:get home-page}]]
       (service-routes)])
    (ring/routes
      (swagger-ui/create-swagger-ui-handler
        {:path   "/swagger-ui"
         :url    "/api/swagger.json"
         :config {:validator-url nil}})
      (ring/create-resource-handler
        {:path "/"})
      (wrap-content-type
        (wrap-webjars (constantly nil)))
      (ring/create-default-handler
        {:not-found
         home-page
         #_(constantly (error-page {:status 404, :title "404 - Page not found"}))
         :method-not-allowed
         (constantly (error-page {:status 405, :title "405 - Not allowed"}))
         :not-acceptable
         (constantly (error-page {:status 406, :title "406 - Not acceptable"}))}))))

(defn app []
  (middleware/wrap-base #'app-routes))
