(ns irobot.t-core
  (:use [midje.sweet])
  (:require [irobot.core :refer [load-from allows?]]))


(def normal-records
  "User-Agent: *
Allow: /

User-Agent: MyBot
Allow: /
Disallow: /private

User-Agent: OtherA
Disallow: /semi-private

User-Agent: OtherB
Disallow: /")
 

(def normal-robots (load-from normal-records))
(def empty-robots (load-from ""))
(def blocks-all-robots (load-from "User-Agent: *\nDisallow: /"))



(fact "empty robots file allows crawling by any UA on any path"
  (allows? empty-robots "NaughtyBot 1.0" "/random/path") => true)


(fact "allows"
  (allows? normal-robots "MyBot" "/") => true)


(fact "allows UA when we have an allow / and a disallow /"
  (allows? normal-robots "OtherA" "/") => truthy)


(fact "disallowing / for all UA prevents accessing any path"
  (allows? blocks-all-robots "MyBot 2.3" "/some/path") => false)

