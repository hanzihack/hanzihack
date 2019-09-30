(ns hanzihack.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[hanzihack started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[hanzihack has shut down successfully]=-"))
   :middleware identity})
