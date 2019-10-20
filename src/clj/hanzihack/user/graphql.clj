(ns hanzihack.user.graphql)


(defn me [ctx _ _]
  {:id 0
   :name "Ziko"
   :email "email@domain.com"
   :photo "https://graph.facebook.com/3080860635262374/picture"})