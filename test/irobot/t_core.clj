(ns irobot.t-core
  (:use [midje.sweet])
  (:require [irobot.core :refer [create-grammar]]))


(def robots (create-grammar "resources/robots.g4"))


(fact "single record with no rule line"
      (robots "User-Agent:thingy\n") =>
      [:records
       [:record
        [:agentline "User-Agent:" "thingy"]]])

 
(fact "a path of / works"
      (robots
"User-Agent:thingy
Allow:/") =>
      [:records
       [:record
        [:agentline "User-Agent:" "thingy"]
        [:ruleline [:allowexpr "Allow" ":" "/"]]]]) 


(fact "robots with one record, one allow"
      (robots
"User-Agent:thingy
Allow:*") =>
      [:records
       [:record
        [:agentline "User-Agent:" "thingy"]
        [:ruleline [:allowexpr "Allow" ":" "*"]]]]) 


(fact "end-of-line comments are allowed"
       (robots
"User-Agent: thingy # some comment
Allow:*") =>
      [:records
       [:record
        [:agentline "User-Agent:" "thingy" "# some comment"]
        [:ruleline [:allowexpr "Allow" ":" "*"]]]])


(fact "comments are allowed before the records"
      (robots
"# first line
# second comment line
User-Agent: thingy
Allow:*") =>
      [:records
       "# first line" "# second comment line"
       [:record
        [:agentline "User-Agent:" "thingy"]
        [:ruleline [:allowexpr "Allow" ":" "*"]]]]) 


(fact "comments are allowed after the records (but they'll get gobbled greedily)"
      (robots
"User-Agent: thingy
Allow: *
# first line
# second comment line") =>
      [:records
       [:record
        [:agentline "User-Agent:" "thingy"]
        [:ruleline [:allowexpr "Allow" ":" "*" "# first line" "# second comment line"]]]]) 


(fact "comments are allowed within the records (but they'll get gobbled greedily)"
      (robots
"User-Agent: thingy
# first line
Allow: *") =>
      [:records
       [:record
        [:agentline "User-Agent:" "thingy" "# first line"]
        [:ruleline [:allowexpr "Allow" ":" "*"]]]]) 


(fact "robots with one record, one disallow"
      (robots "User-Agent:thingy\nDisallow:*") =>
      [:records
       [:record
        [:agentline "User-Agent:" "thingy"]
        [:ruleline [:disallowexpr "Disallow" ":" "*"]]]])


(fact "can have more than one record"
      (robots
"User-Agent: foo
Allow: *
User-Agent: bar
Disallow: *") =>
      [:records
       [:record
        [:agentline "User-Agent:" "foo"]
        [:ruleline [:allowexpr "Allow" ":" "*"]]] 
       [:record
        [:agentline "User-Agent:" "bar"]
        [:ruleline [:disallowexpr "Disallow" ":" "*"]] ]])


(fact "multiple records, with multiple allows & disallows"
      (robots
"User-Agent: foo
Allow: *  
User-Agent: bar 
Disallow: *
Allow: *") =>
      [:records
       [:record
        [:agentline "User-Agent:" "foo"]
        [:ruleline [:allowexpr "Allow" ":" "*"]]]
       [:record
        [:agentline "User-Agent:" "bar"]
        [:ruleline [:disallowexpr "Disallow" ":" "*"]]
        [:ruleline [:allowexpr "Allow" ":" "*"]]]])

