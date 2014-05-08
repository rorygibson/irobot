(ns irobot.core
  (:require [irobot.parse :refer [create-robots-grammar]]
            [clojure.zip :as zip]))


(def parser (create-robots-grammar))


(defn parse
  [s]
  (parser s))


;; TODO never exits if UA not found
;; TODO only handles a single instance of a UA in the file - returns
;;   on first find of the UA in the tree

(defn find-record-in-tree
  [t name val]
  (loop [loc (zip/seq-zip t)]
    (let [cur-name (zip/node loc)
          cur-val (zip/node (zip/next loc))]
      (if (and (= name cur-name) (= val cur-val))
        (zip/node (zip/up (zip/up loc)))
        (if (not (zip/end? loc))
          (recur (zip/next loc))
          (zip/root loc))))))


(defn rules-from-record
  [rec]
  (let [rules-list (flatten (drop 2 rec))
        filters [ #(not= :record %) #(not= :agent %) #(not= :rule %) #(not= "Allow" %) #(not= "Disallow" %) #(not= ":" %) ]
        filtered (filter (apply every-pred filters) rules-list)
        pairs (partition 2 filtered)
        pairs-of-maps (map (fn [[k v]] {k v}) pairs)
        in-sets (apply merge-with (fn [& args] (set args)) pairs-of-maps)
        in-sets (map (fn [[k v]] (if (set? v) {k v} {k #{v}})) in-sets)]
    
;    (println pairs "-->" pairs-of-maps "-->" in-sets)
 
    (apply merge in-sets)))


(defn rules-for
  [ua robots]
  (let [specific (rules-from-record (find-record-in-tree robots "User-Agent:" ua))
        star  (rules-from-record (find-record-in-tree robots "User-Agent:" "*"))]
;    (println specific)
;    (println star)
    (merge-with clojure.set/union specific star)))


(defn matching-path
  [path paths]
  (let [filters [ #(= path %) #(.startsWith path %)]
        filtered (filter (apply some-fn filters) paths)
        res (not (empty? filtered))]
    (println "Path" path "Paths" paths "Filtered" filtered " --> " res)
    res))  



(defn allows?
  "Work out if a path is allowed.
First determines the correct record (based on UA).
Then considers the Allow and Disallow rules.

If Disallow is / and there is no Allow matching the first part of Path, DISALLOW
If Disallow matches the start of Path and there is no longer Allow matching the start of the path, DISALLOW
Else ALLOW"
  [robots ua path]
  (let [rules (rules-for ua robots)
        allows (:allow rules)
        disallows (:disallow rules)
        disallow-root (or (= disallows "/") (some #{"/"} disallows))
        allow-matching-path (matching-path path allows)]
    (if (and disallow-root (not allow-matching-path))
      false
      true)))
