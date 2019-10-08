(ns hanzihack.marilyn.initial.db
  (:require
   [honeysql.core :as sql]
   [hanzihack.db.core :refer [*db*]]
   [clojure.java.jdbc :as jdbc]
   [db-helper.db :refer [where equal]]))


(defn get-list [user-id {:keys [sound group limit]}]
  (let [query (sql/format 
                (where
                  {:select [:i.* [(sql/call :row_to_json :a.*) :actor]]
                   :from [[:marilyn.initials :i]]
                   :join [[:marilyn.actors :a] [:= :i.id :a.initial_id]]
                   :limit limit}
                  [(equal :i.sound sound)
                   (equal (sql/raw "i.\"group\"") group)
                   (equal :a.user-id user-id)]))]

     (jdbc/query *db* query)))

