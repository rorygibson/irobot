(ns irobot.t-parse
  (:use [midje.sweet])
  (:require [irobot.parse :refer [create-robots-grammar]]))


(def r (create-robots-grammar))


(fact "single record with no rule line"
      (r "User-Agent:thingy\n") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]]])

 
(fact "a path of / works"
      (r
"User-Agent:thingy
Allow:/") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]
        [:rule [:allow "Allow" ":" "/"]]]]) 


(fact "robots with one record, one allow"
      (r
"User-Agent:thingy
Allow:*") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]
        [:rule [:allow "Allow" ":" "*"]]]]) 


(fact "end-of-line comments are allowed"
       (r
"User-Agent: thingy # some comment
Allow:*") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy" "# some comment"]
        [:rule [:allow "Allow" ":" "*"]]]])


(fact "comments are allowed before the records"
      (r
"# first line
# second comment line
User-Agent: thingy
Allow:*") =>
      [:records
       "# first line" "# second comment line"
       [:record
        [:agent "User-Agent:" "thingy"]
        [:rule [:allow "Allow" ":" "*"]]]]) 


(fact "comments are allowed after the records (but they'll get gobbled greedily)"
      (r
"User-Agent: thingy
Allow: *
# first line
# second comment line") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]
        [:rule [:allow "Allow" ":" "*" "# first line" "# second comment line"]]]]) 


(fact "comments are allowed within the records (but they'll get gobbled greedily)"
      (r
"User-Agent: thingy
# first line
Allow: *") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy" "# first line"]
        [:rule [:allow "Allow" ":" "*"]]]]) 


(fact "robots with one record, one disallow"
      (r "User-Agent:thingy\nDisallow:*") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]
        [:rule [:disallow "Disallow" ":" "*"]]]])


(fact "arbitrary extensions can be specified"
      (r "User-Agent:thingy\nDisallow:*\nSOMETHING: foobar") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]
        [:rule [:disallow "Disallow" ":" "*"]]
        [:rule [:extension "SOMETHING" ":" "foobar"]]]])


(fact "can have more than one record"
      (r
"User-Agent: foo
Allow: *
User-Agent: bar
Disallow: *") =>
      [:records
       [:record
        [:agent "User-Agent:" "foo"]
        [:rule [:allow "Allow" ":" "*"]]] 
       [:record
        [:agent "User-Agent:" "bar"]
        [:rule [:disallow "Disallow" ":" "*"]] ]])


(fact "multiple records, with multiple allows & disallows"
      (r
"User-Agent: foo
Allow: *  
User-Agent: bar 
Disallow: *
Allow: *") =>
      [:records
       [:record
        [:agent "User-Agent:" "foo"]
        [:rule [:allow "Allow" ":" "*"]]]
       [:record
        [:agent "User-Agent:" "bar"]
        [:rule [:disallow "Disallow" ":" "*"]]
        [:rule [:allow "Allow" ":" "*"]]]])

