(ns irobot.core
  (:require [irobot.parse :refer [create-robots-grammar]]
            [clojure.zip :as zip]))


(def parser (create-robots-grammar))


(defn parse
  "Parse some robots.txt content using the ANTLR grammar"
  [^String s]
  (parser s))


;; TODO never exits if UA not found
;; TODO only handles a single instance of a UA in the file - returns
;;   on first find of the UA in the tree

(defn find-record-in-tree
  "Given an AST of a robots.txt, find the parent "
  [t name val]
  (loop [loc (zip/seq-zip t)]
    (let [cur-name (zip/node loc)
          cur-val (zip/node (zip/next loc))]
      (if (and (= name cur-name) (= val cur-val))
        (zip/node (zip/up (zip/up loc)))
        (if (not (zip/end? loc))
          (recur (zip/next loc))
          (zip/root loc))))))


(defn extract-rules-from-record
  "Given a :record from a robots AST, extract the :rule elements.
   A bit hacky, I'm sure there's a much nicer tree-walking way to do this.
   C'est la vie."
  [rec]
  (let [rules-list (flatten (drop 2 rec))
        filters [ #(not= :record %) #(not= :agent %) #(not= :rule %) #(not= "Allow" %) #(not= "Disallow" %) #(not= ":" %) ]
        filtered (filter (apply every-pred filters) rules-list)
        pairs (partition 2 filtered)
        pairs-of-maps (map (fn [[k v]] {k v}) pairs)
        in-sets (apply merge-with (fn [& args] (set args)) pairs-of-maps)
        in-sets (map (fn [[k v]] (if (set? v) {k v} {k #{v}})) in-sets)]
 
    (apply merge in-sets)))


(defn rules-for-ua
  "Find applicable rules for a User Agent in a robots.txt file.
   Returns rules matching the UA precisely (exact string match,
   excluding start / end whitespace, and rules for the special
   User Agent '*' (macthes all)"
  [ua robots]
  (let [specific (extract-rules-from-record (find-record-in-tree robots "User-Agent:" ua))
        star  (extract-rules-from-record (find-record-in-tree robots "User-Agent:" "*"))]
    (merge-with clojure.set/union specific star)))


(defn matching-path
  "Given a single Path and a set of Paths, return any elements in Paths which startWith Path"
  [path paths]
  (let [filters [ #(= path %) #(.startsWith path %)]
        filtered (filter (apply some-fn filters) paths)
        res (not (empty? filtered))]
    res))  


(defn allows?
  "Work out if a path is allowed.
   First determines the correct record (based on UA).
   Then considers the Allow and Disallow rules.

   If Disallow is / and there is no Allow matching the first part of Path, DISALLOW
   If Disallow matches the start of Path and there is no longer Allow matching the start of the path, DISALLOW
   Else ALLOW"
  [robots ua path]
  (let [rules (rules-for-ua ua robots)
        allows (:allow rules)
        disallows (:disallow rules)
        disallow-root (or (= disallows "/") (some #{"/"} disallows))
        allow-matching-path (matching-path path allows)]
    (if (and disallow-root (not allow-matching-path))
      false
      true)))
