(ns hanzihack.core
  (:require
    [day8.re-frame.http-fx]
    [reagent.core :as r]
    [re-frame.core :as rf]
    [goog.events :as events]
    [goog.history.EventType :as HistoryEventType]
    [markdown.core :refer [md->html]]
    [hanzihack.ajax :as ajax]
    [hanzihack.events]
    [reagent.session :as session]
    [reitit.frontend :as reitit]
    [accountant.core :as accountant]
    [clojure.string :as string])
  (:import goog.History))

(def router
  (reitit/router
    [["/" :index]
     ["/items"
      ["" :items]
      ["/:item-id" :item]]
     ["/about" :about]]))

(defn path-for [route & [params]]
  (if params
    (:path (reitit/match-by-name router route params))
    (:path (reitit/match-by-name router route))))

;; -------------------------
;; Page components

(defn home-page []
  (fn []
    [:span.main
     [:h1 "Welcome to {{name}}"]
     [:ul
      [:li [:a {:href (path-for :items)} "Items of {{name}}"]]
      [:li [:a {:href "/broken/link"} "Broken link"]]]]))



(defn items-page []
  (fn []
    [:span.main
     [:h1 "The items of {{name}}"]
     [:ul (map (fn [item-id]
                 [:li {:name (str "item-" item-id) :key (str "item-" item-id)}
                  [:a {:href (path-for :item {:item-id item-id})} "Item: " item-id]])
               (range 1 10))]]))


(defn item-page []
  (fn []
    (let [routing-data (session/get :route)
          item (get-in routing-data [:route-params :item-id])]
      [:span.main
       [:h1 (str "Item " item " of {{name}}")]
       [:p [:a {:href (path-for :items)} "Back to the list of items"]]])))


(defn about-page []
  (fn [] [:span.main
          [:h1 "About {{name}}"]]))

(defn not-found-page []
  (fn [] [:span.main
          [:h1 "You seem lost"]]))


;; -------------------------
;; Translate routes -> page components

(defn page-for [route]
  (case route
    :index #'home-page
    :about #'about-page
    :items #'items-page
    :item #'item-page
    #'not-found-page))


;; -------------------------
;; Page mounting component

(defn current-page []
  (fn []
    (let [page (:current-page (session/get :route))]
      [:div
       [:header
        [:p [:a {:href (path-for :index)} "Home"] " | "
         [:a {:href (path-for :about)} "About {{name}}"]]]
       [page]])))


;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [current-page] (.getElementById js/document "app")))

(defn init! []
   (accountant/configure-navigation!
      {:nav-handler
       (fn [path]
           (let [match (reitit/match-by-path router path)
                 current-page (:name (:data  match))
                 route-params (:path-params match)]
             (session/put! :route {:current-page (page-for current-page)
                                   :route-params route-params})))
       :path-exists?
       (fn [path]
           (boolean (reitit/match-by-path router path)))})
   (accountant/dispatch-current!)
   (mount-root))