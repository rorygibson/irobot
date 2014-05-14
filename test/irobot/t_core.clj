(ns irobot.t-core
  (:use [midje.sweet])
  (:require [irobot.core :refer [robots allows?]]))


(def normal-records
  "User-Agent: *
Allow: /
Disallow: /private
Allow: /private/allowed-by-star

User-Agent: MyBot
Allow: /
Disallow: /private

User-Agent: OtherA
Allow: /
Disallow: /

User-Agent: OtherB
Disallow: /")
 

(def allow-and-disallow-records
  "User-Agent:XYZ\n
Allow: /
Disallow: /")


(def normal-robots (robots normal-records))
(def empty-robots (robots ""))
(def blocks-all-robots (robots "User-Agent: *\nDisallow: /"))


(fact "disallowing / for all UA prevents accessing any path"
  (allows? blocks-all-robots "MyBot 2.3" "/some/path") => false)

  
(fact "empty robots file allows crawling by any UA on any path"
  (allows? empty-robots "NaughtyBot 1.0" "/random/path") => true)


(fact "allows access to allowed /"
  (allows? normal-robots "MyBot" "/") => true)


(fact "If there's no matching or * record, we allow"
  (allows? (robots "User-Agent:other\nAllows:/") "Me" "/something") => true)


(fact "allows UA when we have an allow / and a disallow /"
  (allows? (robots allow-and-disallow-records) "XYZ" "/") => truthy) 
 

(fact "disallowing a path works" 
  (allows? (robots "User-agent:MyBotX\nDisallow: /") "MyBotX" "/private") => false)

  
(fact "uses the * record when there's no match on UA"
  (allows? normal-robots "UseTheStarBot" "/allowed-by-star") => true)

(fact "uses the * record when there's no match on UA" 
  (allows? normal-robots "UseTheStarBot" "/private/disallowed-by-star") => false)

(fact "User agent should match on a substring"
  (allows? (robots "User-agent:LongBotName\n:Allows:/\nDisallows:/hidden") "LongB" "/hidden")
  => true)
