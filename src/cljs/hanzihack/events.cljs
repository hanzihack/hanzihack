(ns hanzihack.events
  (:require
    [re-frame.core :as rf]
    [ajax.core :as ajax]
    [reitit.frontend.easy :as rfe]
    [reitit.frontend.controllers :as rfc]))

;;dispatchers

(rf/reg-event-db
  :navigate
  (fn [db [_ match]]
    (let [old-match (:common/route db)
          new-match (assoc match :controllers
                                 (rfc/apply-controllers (:controllers old-match) match))]
      (assoc db :route new-match))))

(rf/reg-fx
  :navigate-fx!
  (fn [[k & [params query]]]
    (rfe/push-state k params query)))

(rf/reg-event-fx
  :navigate!
  (fn [_ [_ url-key params query]]
    {:navigate-fx! [url-key params query]}))

(rf/reg-event-db
  :set-docs
  (fn [db [_ docs]]
    (assoc db :docs docs)))

(rf/reg-event-fx
  :fetch-docs
  (fn [_ _]
    {:http-xhrio {:method          :get
                  :uri             "/docs"
                  :response-format (ajax/raw-response-format)
                  :on-success       [:set-docs]}}))

(rf/reg-event-db
  :common/set-error
  (fn [db [_ error]]
    (assoc db :common/error error)))

(rf/reg-event-fx
  :page/init-home
  (fn [_ _]
    {:dispatch [:fetch-docs]}))

;;subscriptions

(rf/reg-sub
  :route
  (fn [db _]
    (-> db :route)))

(rf/reg-sub
  :page-id
  :<- [:route]
  (fn [route _]
    (-> route :data :name)))

(rf/reg-sub
  :page
  :<- [:route]
  (fn [route _]
    (-> route :data :view)))

(rf/reg-sub
  :docs
  (fn [db _]
    (:docs db)))

(rf/reg-sub
  :common/error
  (fn [db _]
    (:common/error db)))

;; ----------------

(rf/reg-event-db
  :initial/set-table
  (fn [db [_ table]]
    (assoc db :initials {:loading false
                         :items (get-in table [:data :initials])})))

(rf/reg-event-fx
  :initial/fetch-table
  (fn [{:keys [db] :as cofx} _]
    {:db         (assoc db :initials {:loading true
                                      :items ()})
     :http-xhrio {:method          :post
                  :uri             "/api/graphql"
                  :headers         {:content-type "application/graphql"}
                  :body            "query getInitialTables{initials:initials{...initialWithActor}} fragment initialWithActor on initial{id,sound,group,pinyin,actor{id,name,image}}"
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [:initial/set-table]}}))


(rf/reg-sub
  :initial/table
  (fn [db [_ arg]]
    (println :arg arg)
    (let [{:keys [loading items]} (get-in db [:initials])]
      {:loading loading
       :items (vals
                (sort
                  (apply merge-with merge
                   (for [[sound [group data]] (map (juxt (comp keyword :sound) (fn [e] [((comp keyword :group) e) e])) items)]
                    {sound {:sound sound
                            :key sound
                            group data}}))))})))


(comment
  @(rf/subscribe [:initial/table]))