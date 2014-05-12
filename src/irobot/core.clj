(ns irobot.core
  (:require [irobot.parse :refer :all]
            [irobot.paths :refer [matching-path matching-paths count-longest]]
            [irobot.io :refer [string-from]])
  (:import [java.io InputStream]))



(defn allowed-path?
  "Work out if a path is allowed.

   If Disallow is / and there is no Allow matching the first part of Path, DISALLOW
   If Disallow matches the start of Path and there is no longer Allow matching the start of the path, DISALLOW
   Else ALLOW"
  [rec path]
  (println "*********************")
  (println "allowed-path?" rec "Path" path)

  (let [allows (find-allows-in-record rec)
        disallows (find-disallows-in-record rec)

        disallow-matching-path (matching-path path disallows)
        allow-matching-path (matching-path path allows)
        simple-pass (and allow-matching-path (not disallow-matching-path))
        
        longest-matching-allow (count-longest (matching-paths path allows))
        longest-matching-disallow (count-longest (matching-paths path disallows))
        complex-pass (>= longest-matching-allow longest-matching-disallow)]
    
    (println "Record" rec) 
    (println "Disallows:" disallows)

    (println "Allows:" allows)
    (println "Path to test" path)
    (println "Simple pass:" simple-pass)
    (println "Longest disallow:" longest-matching-disallow)
    (println "Longest allow:" longest-matching-allow)
    (println "Complex pass:" complex-pass)
    (or simple-pass complex-pass)))


(defn allows?
  [robots ua path]
  (println "Searching for record for UA" ua)
  (let [rec (find-record-by-ua robots ua)]
    (if (not (empty? rec)) 
      (do
        (println "Found record for UA" ua "-->" rec)
        (allowed-path? rec path))
      (do
        (println "Couldn't find record for UA" ua)
      (allowed-path? (find-record-by-ua robots "*") path)))))


(defmulti robots
  "Load and parse a robots.txt. Returns a representation of a Robots file that can be used with allows? etc"
  class)
 

(defmethod robots
  String [txt]
  (parse txt))


(defmethod robots
  InputStream [st]
  (parse (string-from st)))
  
