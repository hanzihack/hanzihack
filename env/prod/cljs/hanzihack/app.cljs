(ns hanzihack.app
  (:require [hanzihack.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
