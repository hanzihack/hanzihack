(ns hanzihack.marilyn.initial.graphql
  (:require
    [hanzihack.marilyn.initial.db :as db]))

(defn get-list [ctx args value]
  (db/get-list args))

