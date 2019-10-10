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
     ["/actors" :actors]]))

(defn path-for [route & [params]]
  (if params
    (:path (reitit/match-by-name router route params))
    (:path (reitit/match-by-name router route))))

;; -------------------------
;; Page components

(defn home-page []
  (fn []
    [:span.main
     [:h1 "Welcome"]
     [:ul
      [:li [:a {:href (path-for :items)} "Items list"]]
      [:li [:a {:href "/broken/link"} "Broken link"]]]]))



(defn items-page []
  (fn []
    [:span.main
     [:h1 "The items"]
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

(defn card []
 [:div {:class "kt-portlet kt-portlet--height-fluid"}
  [:div {:class "kt-portlet__head kt-portlet__head--noborder"}
   [:div {:class "kt-portlet__head-label"}
    [:h3 {:class "kt-portlet__head-title"}]]
   [:div {:class "kt-portlet__head-toolbar"}
    [:a {:href "#", :class "btn btn-icon", :data-toggle "dropdown"}
     [:i {:class "flaticon-more-1 kt-font-brand"}]]
    [:div {:class "dropdown-menu dropdown-menu-right"}
     [:ul {:class "kt-nav"}
      [:li {:class "kt-nav__item"}
       [:a {:href "#", :class "kt-nav__link"}
        [:i {:class "kt-nav__link-icon flaticon2-line-chart"}]
        [:span {:class "kt-nav__link-text"} "Reports"]]]
      [:li {:class "kt-nav__item"}
       [:a {:href "#", :class "kt-nav__link"}
        [:i {:class "kt-nav__link-icon flaticon2-send"}]
        [:span {:class "kt-nav__link-text"} "Messages"]]]
      [:li {:class "kt-nav__item"}
       [:a {:href "#", :class "kt-nav__link"}
        [:i {:class "kt-nav__link-icon flaticon2-pie-chart-1"}]
        [:span {:class "kt-nav__link-text"} "Charts"]]]
      [:li {:class "kt-nav__item"}
       [:a {:href "#", :class "kt-nav__link"}
        [:i {:class "kt-nav__link-icon flaticon2-avatar"}]
        [:span {:class "kt-nav__link-text"} "Members"]]]
      [:li {:class "kt-nav__item"}
       [:a {:href "#", :class "kt-nav__link"}
        [:i {:class "kt-nav__link-icon flaticon2-settings"}]
        [:span {:class "kt-nav__link-text"} "Settings"]]]]]]]
  [:div {:class "kt-portlet__body"}
   [:div {:class "kt-widget kt-widget--user-profile-2"}
    [:div {:class "kt-widget__head"}
     [:div {:class "kt-widget__media"}
      [:img {:class "kt-widget__img kt-hidden-", :src "assets/media/users/300_19.jpg", :alt "image"}]
      [:div {:class "kt-widget__pic kt-widget__pic--danger kt-font-danger kt-font-boldest  kt-hidden"}]]
     [:div {:class "kt-widget__info"}
      [:a {:href "#", :class "kt-widget__username"} "Charlie Stone"]
      [:span {:class "kt-widget__desc"} "PR Manager"]]]
    [:div {:class "kt-widget__body"}
     [:div {:class "kt-widget__section"} "Lorem ipsum dolor sit amet "
      [:a {:href "#", :class "kt-font-brand kt-link kt-font-transform-u kt-font-bold"} " #xrs-23pq"]", sed"
      [:b "USD342/Annual"]" doloremagna"]
     [:div {:class "kt-widget__item"}
      [:div {:class "kt-widget__contact"}
       [:span {:class "kt-widget__label"} "Email:"]
       [:a {:href "#", :class "kt-widget__data"} "charlie@studiovoila.com"]]
      [:div {:class "kt-widget__contact"}
       [:span {:class "kt-widget__label"} "Phone:"]
       [:a {:href "#", :class "kt-widget__data"} "22(43)64534621"]]
      [:div {:class "kt-widget__contact"}
       [:span {:class "kt-widget__label"} "Location:"]
       [:span {:class "kt-widget__data"} "Italy"]]]]
    [:div {:class "kt-widget__footer"}
     [:button {:type "button", :class "btn btn-label-danger btn-lg btn-upper"} "write message"]]]]])

(defn actor-card []
  [:div {:class "kt-portlet kt-portlet--height-fluid"}
   [:div {:class "kt-portlet__head kt-portlet__head--noborder"}
    [:div {:class "kt-portlet__head-label"}
     [:h3 {:class "kt-portlet__head-title"}]]]
   [:div {:class "kt-portlet__body"}
    [:div {:class "kt-widget kt-widget--user-profile-2"}
     [:div {:class "kt-widget__head"}
      [:div {:class "kt-widget__info"}
       [:a {:href "#", :class "kt-widget__username"} "Charlie Stone"]
       [:span {:class "kt-widget__desc"} "PR Manager"]]]
     [:div {:class "kt-widget__body"}
      [:div {:class "kt-widget__section"} "Lorem ipsum dolor sit amet "]
      [:div {:class "kt-widget__item"}
       [:div {:class "kt-widget__contact"}
        [:span {:class "kt-widget__label"} "Email:"]
        [:a {:href "#", :class "kt-widget__data"} "charlie@studiovoila.com"]]
       [:div {:class "kt-widget__contact"}
        [:span {:class "kt-widget__label"} "Phone:"]
        [:a {:href "#", :class "kt-widget__data"} "22(43)64534621"]]
       [:div {:class "kt-widget__contact"}
        [:span {:class "kt-widget__label"} "Location:"]
        [:span {:class "kt-widget__data"} "Italy"]]]]
     [:div {:class "kt-widget__footer"}
      [:button {:type "button", :class "btn btn-label-danger btn-lg btn-upper"} "write message"]]]]])

(defn actors-page []
  (fn []
    [:div.main
     [:h1 "Actors"]
     [:div
      [:div.row
       [:div.col-md-3
        [actor-card]]
       [:div.col-md-3
        [card]]
       [:div.col-md-3
        [card]]]]]))


(defn not-found-page []
  (fn [] [:span.main
          [:h1 "You seem lost"]]))


;; -------------------------
;; Translate routes -> page components

(defn page-for [route]
  (case route
    :index #'home-page
    :actors #'actors-page
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
         [:a {:href (path-for :actors)} "Actors"]]]
       [page]])))


;; -------------------------
;; Initialize app

(defn ^:dev/after-load mount-components []
  (rf/clear-subscription-cache!)
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
   (mount-components))


