(ns irobot.t-real
  (:use [midje.sweet])
  (:require [irobot.parse :refer :all]
            [irobot.core :refer :all])
  (:import [java.io File]))


(def fbase "test/resources/real-robots/")
(def fdir (File. fbase))
(def filters [ #(not= (.getName %) ".git")
               #(not= (.getName %) "capture.sh")
               #(not (.startsWith (.getName %) "broken-"))])
 
(defn files-in
  "Filter out .git and capture.sh from the list of files in a directory"
  [fdir]
  (println "Listing files in" fbase "[" (.getAbsolutePath fdir) "]")
  (let [fs (.listFiles fdir)]
    (filter (apply every-pred filters) fs)))


(defn loadf
  [f]
  (println "... Loading" (.getName f))
  (robots (slurp f)))


(defn load-all-robots-in
  [d]
  (let [files (files-in (File. d))]
    (println "Loading" (count files) "robots files")
    (map loadf files)))


(fact "Loads all the robots files we know should be working"
  (let [rs (load-all-robots-in fbase)]
    (count rs) => 10))

