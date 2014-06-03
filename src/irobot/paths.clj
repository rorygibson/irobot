(ns irobot.paths
  (:require [clojure.tools.logging :refer [trace trace info warn error fatal]]))


(defn path=
  [p1 p2]
  (let [p1 (if p1 (.toString p1) "")
        p2 (if p2 (.toString p2) "")]
    (= p1 p2)))


(defn path-starts-with
  [p1 p2]
  (let [p1 (if p1 (.toString p1) "")
        p2 (if p2 (.toString p2) "")]
    (.startsWith p1 p2)))



(defn matching-path
  "Given a single Path and a set of Paths, return any elements in Paths which startWith Path"
  [^String path paths]
  (let [filters [ (partial path= path) (partial path-starts-with path)]
        filtered (filter (apply some-fn filters) paths)
        res (not (empty? filtered))]
    (trace "Matching" path "against" paths "-->" filtered "-->" res)
    res))  


(defn matching-paths
  "Return the subset of paths matching (having a shared prefix with) path"
  [^String path paths]
  (let [filters [(partial path= path) (partial path-starts-with path)]
        filtered (filter (apply some-fn filters) paths)]
    (trace "Matching" path "against" paths "-->" filtered)
    filtered))  


(defn count-longest
  "Return the length of the longest x in xs"
  [xs]
  (count (first (sort #(> (count %1) (count %2)) xs))))
