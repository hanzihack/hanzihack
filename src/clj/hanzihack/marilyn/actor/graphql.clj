(ns hanzihack.marilyn.actor.graphql
  (:require [hanzihack.marilyn.actor.svc :as svc]))


(defn create [{:keys [session]} {:keys [name note image initial_id] :as args} _]
  (let [user-id (get-in session [:identity :id])]
    (svc/create (merge args
                       {:user_id user-id}))))
