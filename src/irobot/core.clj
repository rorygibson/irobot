(ns irobot.core
  (:require [irobot.parse :refer [parse rules-for-ua]]
            [irobot.paths :refer [matching-path]]
            [irobot.io :refer [string-from]])
  (:import [java.io InputStream]))


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


(defmulti robots
  "Load and parse a robots.txt. Returns a representation of a Robots file that can be used with allows? etc"
  class)


(defmethod robots
  String [txt]
  (parse txt))


(defmethod robots
  InputStream [st]
  (parse (string-from st)))
  
