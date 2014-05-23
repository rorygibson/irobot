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
    
    (trace "[allowed-path?] Disallows:" disallows "Allows" allows)
    (or simple-pass complex-pass)))


(defn allows?
  "Determine whether, according to a robots.txt, a specific User Agent is allowed to access a specific path"
  [robots ua path]
  (trace "[allows?] Searching for record for UA" ua)
  (let [rec (find-record-by-ua robots ua)]
    (if (not (empty? rec)) 
      (do
        (trace "[allows?] Found record for UA" ua "-->" rec)
        (allowed-path? rec path))
      (do
        (trace "[allows?] Couldn't find record for UA" ua)
        (allowed-path? (find-record-by-ua robots "*") path)))))


(defn crawl-delay
  "Return the appropriate crawl-delay for a given UA in a robots.txt (to be used eg in rate-limiting by the user of this library)"
  [robots ua]
  (trace "[crawl-delay] searching for record for UA" ua)
  (let [rec (find-record-by-ua robots ua)]
    (if (not (empty? rec)) 
      (do
        (trace "[crawl-delay] Found record for UA" ua "-->" rec)
        (find-crawl-delay-in-record rec))
      (do
        (trace "[crawl-delay] record not found")
        (find-crawl-delay-in-record (find-record-by-ua robots "*"))))))


(defn sitemaps
  "Return the set of all sitemap file locations listed in this robots.txt"
  [robots]
  (let [sms (find-sitemaps-in-tree robots)]
    (trace "[sitemaps] returning list of" (count sms) "sitemaps")
    sms))


(defmulti robots
  "Load and parse a robots.txt. Returns a representation of a Robots file that can be used with allows?"
  class)


(defmethod robots
  String [txt]
  (parse txt))


(defmethod robots
  InputStream [st]
  (parse (string-from st)))

