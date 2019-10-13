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

    [syn-antd.button :as button]
    [syn-antd.input :as input]
    [syn-antd.tag :refer [tag]]
    [syn-antd.table :refer [table table-column table-column-group]]

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
     ["/actors"
      ["" :actors]
      ["/:actor-id" :actor]]]))

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


(defn render [d]
  (if d
    (let [{:keys [id group pinyin actor]} (js->clj d :keywordize-keys true)]
      (r/as-element [:div
                     [:a {:href (str "/actors/" id)} pinyin]
                     [:div {} (str actor)]]))
    (r/as-element [:div "-"])))


(def cols
  [
   {:title "Sound"
    :dataIndex "sound"
    :key "sound"}
   {:title "Group A"
    :dataIndex "a"
    :key "a"
    :render render}
   {:title "Group I"
    :dataIndex "i"
    :render render
    :key "i"}
   {:title "Group U"
    :dataIndex "u"
    :render render
    :key "u"}
   {:title "Group UV"
    :dataIndex "ü"
    :render render
    :key "ü"}])

(def data
  (for [i (range 500)]
   {:key i
    :name "Ziko"
    :age (str (* i 10))}))


(defn actors-page []
  (rf/dispatch [:initial/fetch-table])
  (let [data (rf/subscribe [:initial/table])]
    (fn []
      [:div.main
       [:h1 "Actors"]
       [:div
        [:div.row
         [:div.col-md
           [table {:columns cols
                   :dataSource @data
                   :pagination {:pageSize 100}}]]]
        [:div.row
         [button/button
          {:type     "primary"
           :on-click #(println "hello world")}
          "Reset input to 'Test'"]]]])))

(defn actor-page []
  (fn []
    (let [routing-data (session/get :route)
          actor-id (get-in routing-data [:route-params :actor-id])]
      [:span.main
       [:h1 (str "Actor " actor-id)]
       [:p [:a {:href (path-for :actors)} "Back to the list of items"]]])))


(defn not-found-page []
  (fn [] [:span.main
          [:h1 "You seem lost"]]))


;; -------------------------
;; Translate routes -> page components

(defn page-for [route]
  (case route
    :index #'home-page
    :actors #'actors-page
    :actor #'actor-page
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
  (ajax/load-interceptors!)
  (mount-components))


