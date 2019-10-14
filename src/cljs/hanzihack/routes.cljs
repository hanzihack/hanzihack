(ns hanzihack.routes
  (:require
    [reitit.frontend :as reitit]))

(def routes
  (reitit/router
    [["/" :index]
     ["/items"
      ["" :items]
      ["/:item-id" :item]]
     ["/actors"
      ["" :actors]
      ["/:actor-id" :actor]]]))

(defn path-for [route & [params]]
  (if params
    (:path (reitit/match-by-name routes route params))
    (:path (reitit/match-by-name routes route))))

