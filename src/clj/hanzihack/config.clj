(ns hanzihack.config
  (:require
    [cprop.core :refer [load-config]]
    [cprop.source :as source]
    [mount.core :refer [args defstate]]))

(defstate env
  :start
  (load-config
    :merge
    [(args)
     (source/from-system-props)
     (source/from-env)]))

(defn port []
  (:port env))

(defn nrepl-bind []
  (:nrep-bind env))

(defn nrepl-port []
  (:nrep-port env))

(defn database-url []
  (:database-url env))

