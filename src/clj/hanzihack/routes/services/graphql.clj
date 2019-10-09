(ns hanzihack.routes.services.graphql
  (:require
    [com.walmartlabs.lacinia.util :refer [attach-resolvers]]
    [com.walmartlabs.lacinia.schema :as schema]
    [com.walmartlabs.lacinia :as lacinia]
    [com.walmartlabs.lacinia.resolve :as resolve]
    [hanzihack.marilyn.initial.graphql :as initial]
    [clojure.data.json :as json]
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [ring.util.http-response :refer :all]
    [mount.core :refer [defstate]]))

(defn get-hero [context args value]
  (let [data  [{:id 1000
                :name "Luke"
                :home_planet "Tatooine"
                :appears_in ["NEWHOPE" "EMPIRE" "JEDI"]}
               {:id 2000
                :name "Lando Calrissian"
                :home_planet "Socorro"
                :appears_in ["EMPIRE" "JEDI"]}]]
    (first data)))

(defn get-initial [{:keys [:initial_id]} args value]
  (println :get-initial args)
  {:id initial_id
   :name "a"
   :pinyin "a"})



(defn list-actor [ctx args value]
  (println :list-actor args)
  (resolve/with-context
    [{:id 1
      :name "actor"
      :initial_id 2}]
    {:initial_id 99}))

(defn list-final [ctx args value]
  [{:id 1
    :sound "(u)an"
    :pinyin "uan"}])

(defn list-location [ctx args value]
  (print :list-location)
  [{:id 1
    :name "Esplanade"
    :final_id 3}])

(defn get-actor [ctx args value]
  (println :get-actor args)
  {:id 2
   :name "get-actor"})

(defn get-final [ctx args value]
  (println :get-final args)
  {:id 2
   :sound "(u)an"
   :pinyin "uan"})

(comment
  (try
    (-> "graphql/schema.edn"
        io/resource
        slurp
        edn/read-string
        (attach-resolvers {})
        schema/compile)
    (catch Exception e (ex-data e))))

(defstate compiled-schema
  :start
  (-> "graphql/schema.edn"
      io/resource
      slurp
      edn/read-string
      (attach-resolvers {;; objects
                         :initial/resolve   get-initial
                         :actor/resolve     get-actor
                         :final/resolve     get-final
                         :location/resolve  (constantly {})
                         :area/resolve      (constantly {})
                         :character/resolve (constantly {})

                         ; queries
                         :initial/resolve-list initial/get-list
                         :actor/resolve-list   list-actor
                         :final/resolve-list   list-final
                         :location/resolve-list list-location

                         :get-hero get-hero
                         :get-droid (constantly {})})
      schema/compile))

(defn format-params [query]
   (let [parsed (json/read-str query)] ;;-> placeholder - need to ensure query meets graphql syntax
     (str "query { hero(id: \"1000\") { name appears_in }}")))

(defn execute-request [query]
  (let [vars nil
        context {:user-id 1}]
    (-> (lacinia/execute compiled-schema query vars context)
        (json/write-str))))
