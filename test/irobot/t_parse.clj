(ns irobot.t-parse
  (:use [midje.sweet])
  (:require [irobot.parse :refer [parser rules-for-ua]]))


;; TODO
;; Case insensitive "User-agent" and its value
;; URL decode URLs prior to comparison so that /a%3CD.html = /a%3cd.html
;; Change so that we don't conflate * and <specific UA> - otherwise you can't exlude UAs, can you?
;; BUT - /a%2fb.html != /a/b.html (don't decode /)

(fact "single record with no rule line"
      (parser "User-Agent:thingy\n") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]]])


(fact "User-agent value is case-insensitive")
 
(fact "User-Agent directive is lexed case insensitively"
  (parser "UsEr-aGent:thingy\n") =>
  [:records
   [:record
    [:agent "UsEr-aGent:" "thingy"]]])


(fact "User agent should match on a substring")


(fact "a path of / works"
      (parser
"User-Agent:thingy
Allow:/") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]
        [:rule [:allow "Allow" ":" "/"]]]]) 


(fact "robots with one record, one allow"
      (parser
"User-Agent:thingy
Allow: /") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]
        [:rule [:allow "Allow" ":" "/"]]]])
 

(fact "paths don't have to start with a slash"
      (parser
"User-Agent: thingy
Allow: Foo") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]
        [:rule [:allow "Allow" ":" "Foo"]]]]) 



(fact "end-of-line comments are allowed"
       (parser
"User-Agent: thingy # some comment
Allow: /foo") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy" "# some comment"]
        [:rule [:allow "Allow" ":" "/foo"]]]])


(fact "comments are allowed before the records"
      (parser
"# first line
# second comment line
User-Agent: thingy
Allow: /bar") =>
      [:records
       "# first line" "# second comment line"
       [:record
        [:agent "User-Agent:" "thingy"]
        [:rule [:allow "Allow" ":" "/bar"]]]]) 


(fact "comments are allowed after the records (but they'll get gobbled greedily)"
      (parser
"User-Agent: thingy
Allow: /baz
# first line
# second comment line") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]
        [:rule [:allow "Allow" ":" "/baz" "# first line" "# second comment line"]]]]) 


(fact "comments are allowed within the records (but they'll get gobbled greedily)"
      (parser
"User-Agent: thingy
# first line
Allow: /~rory") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy" "# first line"]
        [:rule [:allow "Allow" ":" "/~rory"]]]]) 


(fact "robots with one record, one disallow"
      (parser "User-Agent:thingy\nDisallow:/~rory") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]
        [:rule [:disallow "Disallow" ":" "/~rory"]]]])
 

(fact "arbitrary extensions can be specified"
      (parser "User-Agent:thingy\nDisallow:/\nSOMETHING: foobar") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]
        [:rule [:disallow "Disallow" ":" "/"]]
        [:rule [:extension "SOMETHING" ":" "foobar"]]]])
 

(fact "can have more than one record"
      (parser
"User-Agent: foo
Allow: /
User-Agent: bar
Disallow: /") =>
      [:records
       [:record
        [:agent "User-Agent:" "foo"]
        [:rule [:allow "Allow" ":" "/"]]] 
       [:record
        [:agent "User-Agent:" "bar"]
        [:rule [:disallow "Disallow" ":" "/"]] ]])
 

(fact "multiple records, with multiple allows & disallows"
      (parser
"User-Agent: foo
Allow: /
User-Agent: bar 
Disallow: /
Allow: /") =>
      [:records
       [:record
        [:agent "User-Agent:" "foo"]
        [:rule [:allow "Allow" ":" "/"]]]
       [:record
        [:agent "User-Agent:" "bar"]
        [:rule [:disallow "Disallow" ":" "/"]]
        [:rule [:allow "Allow" ":" "/"]]]]) 


(def combined-robots
  "User-Agent: *
  Allow: /

  User-Agent: MyBot
  Disallow: /private")

(fact "rules for a UA include exact match and those for *"
  (rules-for-ua "MyBot" (parser combined-robots)) => {:allow #{"/"} :disallow #{ "/private"}}) 
 
