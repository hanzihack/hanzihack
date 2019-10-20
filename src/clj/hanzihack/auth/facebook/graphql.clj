(ns hanzihack.auth.facebook.graphql)

(defn login [ctx args _]
  (println :fb/login args)
  args)


