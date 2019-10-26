(ns hanzihack.marilyn.location.graphql
  (:require [hanzihack.marilyn.location.svc :as svc]))


(defn create [{:keys [session]} {:keys [name image final_id] :as args} _]
  (let [user-id (get-in session [:identity :id])]
    (svc/create (merge args
                       {:user_id user-id}))))
