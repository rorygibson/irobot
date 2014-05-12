(ns irobot.t-parse
  (:use [midje.sweet])
  (:require [irobot.parse :refer :all]))





(def mixed-case
  "User-Agent: MIXEDcaseBOT
   Disallow: /private")


(def one-rec-and-star
  "User-Agent: *
  Allow: /
  Disallow: /private

  User-Agent: MyBot
  Disallow: /foobar")


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
 

(fact "When the UA is not found and there's no *-record, returns empty result"
  (find-record-by-ua (parse "User-Agent:not-me\nAllow:/") "somethingme") => empty?)


(fact "User-agent value is case-insensitive"
  (find-record-by-ua (parse mixed-case) "mixedcasebot") =>
  [:record
   [:agent "User-Agent:" "MIXEDcaseBOT"]
   [:disallow "Disallow" "/private"]])


(fact "User-Agent directive is lexed case insensitively"
  (parser "UsEr-aGent:thingy\n") =>
  [:records
   [:record
    [:agent "UsEr-aGent:" "thingy"]]])


;; TODO fill in
(fact "User agent should match on a substring")


(fact "a path of / works"
      (parser
"User-Agent:thingy
Allow:/") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]
        [:allow "Allow" "/"]]])


(fact "robots with one record, one allow"
      (parser
"User-Agent:thingy
Allow: /") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]
        [:allow "Allow" "/"]]])
 

(fact "paths don't have to start with a slash"
      (parser
"User-Agent: thingy
Allow: Foo") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]
        [:allow "Allow" "Foo"]]])



(fact "end-of-line comments are allowed"
       (parser
"User-Agent: thingy # some comment
Allow: /foo") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy" "# some comment"]
        [:allow "Allow" "/foo"]]])


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
        [:allow "Allow" "/bar"]]]) 


(fact "comments are allowed after the records (but they'll get gobbled greedily)"
      (parser
"User-Agent: thingy
Allow: /baz
# first line
# second comment line") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]
        [:allow "Allow" "/baz" "# first line" "# second comment line"]]]) 


(fact "comments are allowed within the records (but they'll get gobbled greedily)"
      (parser
"User-Agent: thingy
# first line
Allow: /~rory") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy" "# first line"]
        [:allow "Allow" "/~rory"]]]) 


(fact "robots with one record, one disallow"
      (parser "User-Agent:thingy\nDisallow:/~rory") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]
        [:disallow "Disallow" "/~rory"]]])
 

(fact "arbitrary extensions can be specified"
      (parser "User-Agent:thingy\nDisallow:/\nSOMETHING: foobar") =>
      [:records
       [:record
        [:agent "User-Agent:" "thingy"]
        [:disallow "Disallow" "/"]
        [:extension "SOMETHING" "foobar"]]])
 

(fact "can have more than one record"
      (parser
"User-Agent: foo
Allow: /
User-Agent: bar
Disallow: /") =>
      [:records
       [:record
        [:agent "User-Agent:" "foo"]
        [:allow "Allow" "/"]] 
       [:record
        [:agent "User-Agent:" "bar"]
        [:disallow "Disallow" "/"]] ])
 

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
        [:allow "Allow" "/"]]
       [:record
        [:agent "User-Agent:" "bar"]
        [:disallow "Disallow" "/"]
        [:allow "Allow" "/"]]])  


