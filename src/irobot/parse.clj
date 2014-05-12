(ns irobot.parse
  (:require [clj-antlr.core :as antlr]
            [clojure.set]
            [clojure.zip :as zip]))



(def robots-grammar-filename "resources/robots.g4")


(def parser (antlr/parser robots-grammar-filename))


(defn parse
  "Parse some robots.txt content using the ANTLR grammar"
  [^String s]
  (parser s))


(defn stringify
  [s]
  (-> s (.toString)
      (.toLowerCase)))


(defn find-nodes-in-tree
  ([t name]
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
  
  ([t name value]
     (let [node  (loop [loc (zip/seq-zip t)]
                   (let [name  (stringify name)
                         cur-name  (stringify (zip/node loc))
                         val (stringify value)
                         cur-val (stringify (zip/node (zip/next loc)))
                         match (and (= name cur-name) (= val cur-val))]

                     (if match
                       (zip/node (zip/up (zip/up loc)))
                       
                       (if (zip/end? loc)       
                         []
                         (recur (zip/next loc))))))]
       node )))


(defn find-record-by-ua
  [t ua]
  (find-nodes-in-tree t "User-agent:" ua))



(defn find-allows-in-record
  [rec]
  (find-nodes-in-tree rec :allow))


(defn find-disallows-in-record
  [rec]
  (find-nodes-in-tree rec :disallow))


