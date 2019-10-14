(ns hanzihack.page
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]

    [syn-antd.button :as button]
    [syn-antd.input :as input]
    [syn-antd.tag :refer [tag]]
    [syn-antd.table :refer [table table-column table-column-group]]

    [hanzihack.routes :as routes]

    [reagent.session :as session]
    [reitit.frontend :as reitit]
    [accountant.core :as accountant]
    [clojure.string :as string]))

;; -------------------------
;; Page components

(defn home-page []
  (fn []
    [:span.main
     [:h1 "Welcome"]
     [:ul
      [:li [:a {:href (routes/path-for :items)} "Items list"]]
      [:li [:a {:href "/broken/link"} "Broken link"]]]]))



(defn items-page []
  (fn []
    [:span.main
     [:h1 "The items"]
     [:ul (map (fn [item-id]
                 [:li {:name (str "item-" item-id) :key (str "item-" item-id)}
                  [:a {:href (routes/path-for :item {:item-id item-id})} "Item: " item-id]])
               (range 1 10))]]))


(defn item-page []
  (fn []
    (let [routing-data (session/get :route)
          item (get-in routing-data [:route-params :item-id])]
      [:span.main
       [:h1 (str "Item " item " of {{name}}")]
       [:p [:a {:href (routes/path-for :items)} "Back to the list of items"]]])))





(defn not-found-page []
  (fn [] [:span.main
          [:h1 "You seem lost"]]))


;; -------------------------
;; Page mounting component

(defn current-page []
  (fn []
    (let [page (:current-page (session/get :route))]
      [:div
       [:header
        [:p [:a {:href (routes/path-for :index)} "Home"] " | "
         [:a {:href (routes/path-for :actors)} "Actors"]]]
       [page]])))



