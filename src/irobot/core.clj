(ns irobot.core
  (:require [irobot.parse :refer [create-robots-grammar]]))


(def r (create-robots-grammar))
