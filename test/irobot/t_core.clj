(ns irobot.t-core
  (:use [midje.sweet])
  (:require [irobot.core :refer :all]))

(def normal-records
  "User-Agent: *
Allow: /

User-Agent: MyBot
Allow: /
Disallow: /

User-Agent: OtherA
Disallow: /

User-Agent: OtherB
Disallow: /")


(def non-root-paths-records
  "User-Agent: MyBot
Disallow: /
Allow: /foobar") 

(def normal-robots (parse normal-records))
(def empty-robots (parse ""))
(def blocks-all-robots (parse "User-Agent: *\nDisallow: /"))
(def non-root-paths-robots (parse non-root-paths-records))
 
(facts "about path matching" 
  
  (fact "Root paths always match"
    (matching-path "/" #{"/"}) => truthy)

  
  (fact "Exact matches always match"
    (matching-path "/foo" #{"/foo"}) => truthy)

  
  (fact "If one of the Paths is a prefix of Path, we match"
    (matching-path "/foobar" #{"/foo"}) => truthy
    (matching-path "/foobar" #{"/fooba"}) => truthy)

  
  (fact "If there is one element in Paths and it's not a prefix, we don't match"
    (matching-path "/foobar" #{"/baz"}) => falsey)


  (fact "If there are multiple Paths and one of them is a prefix of Path, we match"
    (matching-path "/foobar" #{"/bar" "/baz" "/foo"}) => truthy)

  (fact "If there are multiple Paths and NONE of them is a prefix of Path, we DON'T match"
    (matching-path "/foobar" #{"/baz" "/bar"}) => falsey)


  (fact "empty robots file allows crawling by any UA on any path"
    (allows? empty-robots "NaughtyBot 1.0" "/random/path") => true)


  (fact "allows"
    (allows? normal-robots "MyBot" "/") => true)


  (fact "allows UA when we have an allow / and a disallow /"
    (allows? normal-robots "OtherA" "/") => truthy)


  (fact "disallowing / for all UA prevents accessing any path"
    (allows? blocks-all-robots "MyBot 2.3" "/some/path") => false)


  (fact "collates rules by UA"
    (rules-for "MyBot" normal-robots) => {:allow #{"/"} :disallow #{ "/"}}))
