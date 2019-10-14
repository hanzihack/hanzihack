(ns hanzihack.marilyn.initial.page
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]

    [syn-antd.button :as button]
    [syn-antd.input :as input]
    [syn-antd.tag :refer [tag]]
    [syn-antd.table :refer [table table-column table-column-group]]

    [hanzihack.routes :as routes]

    [reagent.session :as session]))


(defn render [d]
  (if d
    (let [{:keys [id group pinyin actor]} (js->clj d :keywordize-keys true)
          {:keys [image]} actor]
      (r/as-element [:div.row
                     [:div.col-md-3
                       [:span.kt-media.kt-margin-r-5.kt-margin-t-5
                        [:img {:src image
                               :alt pinyin}]]]
                     [:div.col-md-9
                       [:a {:href (str "/actors/" id)} pinyin]
                       [:div (:name actor)]]]))
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
  (let [data (rf/subscribe [:initial/table :items])]
    (fn []
      [:div.main
       [:div.kt-portlet
         [:div.kt-portlet__body
           [:h1 "Actors"]
           [:div
            [:div.row
             [:div.col-md
              [table {:columns cols
                      :dataSource (:items @data)
                      :loading (:loading @data)
                      :pagination {:pageSize 100
                                   :hideOnSinglePage true}}]]]
            [:div.row
             [button/button
              {:type     "primary"
               :on-click #(println "hello world")}
              "Reset input to 'Test'"]]]]]])))

(defn actor-page []
  (fn []
    (let [routing-data (session/get :route)
          actor-id (get-in routing-data [:route-params :actor-id])]
      [:span.main
       [:h1 (str "Actor " actor-id)]
       [:p [:a {:href (routes/path-for :actors)} "Back to the list of items"]]])))
