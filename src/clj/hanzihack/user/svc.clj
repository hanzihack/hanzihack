(ns hanzihack.user.svc
  (:require
    [clojure.java.jdbc :as jdbc]
    [clojure.tools.logging :as log]
    [honeysql.core :as sql]
    [hanzihack.db.core :refer [*db*]]))


(defn create [{:keys [email name picture] :as user}]
  (try
    (first (jdbc/insert! *db* :users (select-keys user [:email :name :picture])))
    (catch Exception e
      (log/errorf "create new user - %s" email)
      (first (jdbc/query *db* (sql/format {:select [:*]
                                           :from [:users]
                                           :where [:= :email email]}))))))


(comment
  (create {:email "ziko@email.com"
           :name "ziko"
           :picture "img"}))

