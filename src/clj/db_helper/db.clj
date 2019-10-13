(ns db-helper.db
  (:require
    [clojure.java.jdbc :as jdbc]
    [clojure.string :as str]
    [honeysql.core :as sql]))


(defn get-schemas [db]
  (jdbc/query db "SELECT nspname FROM pg_catalog.pg_namespace"))

(defn get-tables [db schema]
  (jdbc/query db ["SELECT table_catalog,table_schema,table_name FROM information_schema.tables WHERE table_schema = ?" schema]))


(defn count-status [db table]
  (jdbc/query db [(format "select status,count(*) FROM %s GROUP BY status ORDER BY status" table)]))

(defn list-value [db table col]
  (jdbc/query db [(format "SELECT DISTINCT %s AS value FROM %s" col table)]))

(defn equal [col val]
  (if (some? val)
    [:= col val]))

(defn like [col ^String val]
  (if-not (str/blank? val)
    [:like col (sql/raw ["'%'||'" val "'||'%'"])]))


(comment
  (count-status nil "table1")
  (list-value nil "table2" "currency"))


(comment
  ;; Move this to test
  (assert (vector? (equal :currency "xzc")) "Must be vector if val not empty")
  (assert (nil? (equal :currency nil)) "Must be nil if val is nil")
  (assert (nil? (equal :currency "")) "Must be nil if val is empty")
  (assert (vector? (like :email "supakorn@")))
  (assert (nil? (like :email "")))

  (equal :value "10")
  (equal :value "")
  (equal :value 10)
  (equal :value nil)

  (like :value "")
  (like :value "val")

  (where {:select :a :from :b} [(equal :v1 1) (like :v2 "2")])
  (where {:select :a :from :b} [(equal :v1 nil) (like :v2 "2")])
  (where {:select :a :from :b} [(equal :v1 nil) (like :v2 "")])

  (sql/format (where {:select [:*] :from [:marilyn.initials]} [(equal (sql/raw "\"group\"") "u") (like :v2 nil)])) 

  :bye)

