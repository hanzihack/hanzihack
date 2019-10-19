(ns hanzihack.marilyn.final.db
  (:require
   [honeysql.core :as sql]
   [honeysql.helpers :as hh]
   [hanzihack.db.core :refer [*db*]]
   [clojure.java.jdbc :as jdbc]
   [db-helper.db :refer [equal]]))


(defn get-list [{:keys [user-id]}]
  (let [query (sql/format
                (->
                  {:select [:f.*
                            [(sql/call :row_to_json :la.*) :location]]
                   :from [[:marilyn.finals :f]]
                   :left-join [[{:select [:l.* :ja.areas]
                                 :where [:= :l.user_id user-id]
                                 :from [[:marilyn.locations :l]]
                                 :left-join [[{:select [:location_id
                                                        [(sql/call :json_agg :marilyn.areas.*) :areas]]
                                               :from [:marilyn.areas]
                                               :group-by [:location_id]} :ja] [:= :l.id :ja.location_id]]} :la]
                               [:= :f.id :la.final_id]]}))]

    (jdbc/query *db* query)))

