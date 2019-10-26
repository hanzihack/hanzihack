(ns hanzihack.marilyn.actor.svc
  (:require [clojure.java.jdbc :as jdbc]
            [hanzihack.db.core :refer [*db*]]))


(defn create [{:keys [name note image initial_id user_id] :as actor}]
  (first (jdbc/insert! *db* :marilyn.actors actor)))
