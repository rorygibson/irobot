(ns irobot.parse
  (:require [clj-antlr.core :as antlr]
            [clojure.set]
            [clojure.zip :as zip]))


(def robots-grammar-filename "resources/robots.g4")


(defn create-grammar
  [filename]
  (antlr/parser filename))


(def parser (create-grammar robots-grammar-filename))


(defn parse
  "Parse some robots.txt content using the ANTLR grammar"
  [^String s]
  (parser s))

  
;; TODO never exits if UA not found
;; TODO only handles a single instance of a UA in the file - returns
;;   on first find of the UA in the tree

(defn find-record-in-tree 
  "Given an AST, find the parent node of the node with the given name AND value"
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
