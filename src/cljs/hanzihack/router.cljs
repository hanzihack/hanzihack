(ns hanzihack.router
  (:require
    [reagent.session :as session]
    [reitit.frontend :as reitit]
    [accountant.core :as accountant]
    [hanzihack.routes :as routes]
    [hanzihack.page :as p]
    [hanzihack.marilyn.initial.page :as i])
  (:import goog.History))


;; -------------------------
;; Translate routes -> page components

(defn page-for [route]
  (case route
    :index #'p/home-page
    :actors #'i/actors-page
    :actor #'i/actor-page
    :items #'p/items-page
    :item #'p/item-page
    #'p/not-found-page))


;; -------------------------
;; Initialize router

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
      (let [match (reitit/match-by-path routes/routes path)
            current-page (:name (:data  match))
            route-params (:path-params match)]
        (session/put! :route {:current-page (page-for current-page)
                              :route-params route-params})))
     :path-exists?
     (fn [path]
      (boolean (reitit/match-by-path routes/routes path)))})
  (accountant/dispatch-current!))

