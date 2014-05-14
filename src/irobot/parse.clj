(ns irobot.parse
  (:require [clj-antlr.core :as antlr]
            [clojure.set]
            [clojure.tools.logging :refer [trace debug info warn error fatal]]
            [clojure.zip :as zip]
            [irobot.io :refer [stringify]]))


(def ^{:private true :doc "Location of the ANTLR grammar for robots.txt files"}
  ^String robots-grammar-filename "robots.g4")


(def ^{:private true :doc "ANTLR parser generated from the grammar file"}
  parser (antlr/parser (slurp (clojure.java.io/resource robots-grammar-filename))))


(defn parse
  "Parse some robots.txt content using the ANTLR grammar"
  [^String s]
  (parser s))


(defn- find-nodes-in-tree
  "Given a tree of lists, return all nodes whose name is case-insensitively equal to the node text.
Or, given a name and value, return THE FIRST nodes whose name is case-insensitively equal to the node text, and whose value is case-insensitively equal to the value of the next sibling node"
  ([t ^String name]
     (loop [loc (zip/seq-zip t)
            found #{}]
       (let [name (stringify name)
             cur-name (stringify (zip/node loc))
             cur-val (zip/node (zip/next (zip/next loc)))
             match (= name cur-name)]

         (if (zip/end? loc)
           found
           (if match
             (recur (zip/next loc) (clojure.set/union #{cur-val} found))
             (recur (zip/next loc) found))))))

  ([t ^String name ^String value]
     (loop [loc (zip/seq-zip t)]
       (let [name  (stringify name)
             cur-name  (stringify (zip/node loc))
             val (stringify value)
             cur-val (stringify (zip/node (zip/next loc)))
             match (and (= name cur-name) (= val cur-val))]

         (if match
           (zip/node (zip/up (zip/up loc)))
           
           (if (zip/end? loc)       
             []
             (recur (zip/next loc))))))))


(defn find-record-by-ua
  "Find a robots.txt record (USer-AGent and series of rules) by user agent name"
  [t ^String ua]
  (find-nodes-in-tree t "User-agent:" ua))


(defn find-allows-in-record
  "Find the allow-rules in a robots.txt record"
  [rec]
  (find-nodes-in-tree rec :allow))


(defn find-disallows-in-record
  "Find the disallow-rules in a robots.txt record"
  [rec]
  (find-nodes-in-tree rec :disallow))


(defn find-crawl-delay-in-record
  "Find the crawl delay in a robots.txt record"
  [rec]
  (let [v (first (find-nodes-in-tree rec :crawldelay))]
    (if v (read-string v) nil)))
