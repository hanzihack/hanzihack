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
    [hanzihack.router :as rt]
    [hanzihack.page :as p]

    [syn-antd.button :as button]
    [syn-antd.input :as input]
    [syn-antd.tag :refer [tag]]
    [syn-antd.table :refer [table table-column table-column-group]]

    [reagent.session :as session]
    [reitit.frontend :as reitit]
    [accountant.core :as accountant]
    [clojure.string :as string])
  (:import goog.History))

;; -------------------------
;; Initialize app

(defn ^:dev/after-load mount-components []
  (rf/clear-subscription-cache!)
  (r/render [p/current-page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:init-db])
  (rt/init!)
  (ajax/load-interceptors!)
  (mount-components))


