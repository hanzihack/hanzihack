(ns hanzihack.marilyn.initial.graphql
  (:require
    [hanzihack.marilyn.initial.db :as db]))

(defn get-list [ctx args value]
  (let [user-id (:user-id ctx)]
    (db/get-list user-id args)))
