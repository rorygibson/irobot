(ns irobot.t-matching
  (:use [midje.sweet])
  (:require [irobot.paths :refer [matching-path]]))



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


 
