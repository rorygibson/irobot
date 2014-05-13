(ns irobot.paths
  (:require [clojure.tools.logging :refer [trace trace info warn error fatal]]))


(defn matching-path
  "Given a single Path and a set of Paths, return any elements in Paths which startWith Path"
  [path paths]
  (let [filters [ #(= path (.toString %)) #(.startsWith path (.toString %))]
        filtered (filter (apply some-fn filters) paths)
        res (not (empty? filtered))]
    (trace "Matching" path "against" paths "-->" filtered "-->" res)
    res))  


(defn matching-paths
  "Return the subset of paths matching (having a shared prefix with) path"
  [path paths]
  (let [filters [ #(= path (.toString %)) #(.startsWith path (.toString %))]
        filtered (filter (apply some-fn filters) paths)]
    (trace "Matching" path "against" paths "-->" filtered)
    filtered))  


(defn count-longest
  "Return the length of the longest x in xs"
  [xs]
  (count (first (sort #(> (count %1) (count %2)) xs))))
