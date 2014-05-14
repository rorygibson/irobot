(ns irobot.core
  (:require [clojure.tools.logging :refer [trace debug info warn error fatal]]
            [irobot.parse :refer :all]
            [irobot.paths :refer [matching-path matching-paths count-longest]]
            [irobot.io :refer [string-from]])
  (:import [java.io InputStream]))


(defn- allowed-path?
  "Determines if a path is allowed by the rules in a specific Robots.txt record"
  [rec path]

  (let [allows (find-allows-in-record rec)
        disallows (find-disallows-in-record rec)

        disallow-matching-path (matching-path path disallows)
        allow-matching-path (matching-path path allows)
        simple-pass (and allow-matching-path (not disallow-matching-path))
        
        longest-matching-allow (count-longest (matching-paths path allows))
        longest-matching-disallow (count-longest (matching-paths path disallows))
        complex-pass (>= longest-matching-allow longest-matching-disallow)]
    
    (trace "Disallows:" disallows "Allows" allows)
    (or simple-pass complex-pass)))


(defn allows?
  "Determine whether, according to a robots.txt, a specific User Agent is allowed to access a specific path"
  [robots ua path]
  (trace "Searching for record for UA" ua)
  (let [rec (find-record-by-ua robots ua)]
    (if (not (empty? rec)) 
      (do
        (trace "Found record for UA" ua "-->" rec)
        (allowed-path? rec path))
      (do
        (trace "Couldn't find record for UA" ua)
        (allowed-path? (find-record-by-ua robots "*") path)))))


(defmulti robots
  "Load and parse a robots.txt. Returns a representation of a Robots file that can be used with allows?"
  class)


(defmethod robots
  String [txt]
  (parse txt))


(defmethod robots
  InputStream [st]
  (parse (string-from st)))

