(ns hanzihack.marilyn.initial.graphql
  (:require
    [hanzihack.marilyn.initial.db :as db]))

(defn get-list [ctx args value]
  (println :user-id (:user-id ctx) value)
  (def ccc ctx)
  (map #(assoc % :actor {:id 99})
       (db/get-list args)))


(comment
  (keys ccc)
  (get-list nil nil nil))
