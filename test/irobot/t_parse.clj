(ns irobot.t-parse
  (:use [midje.sweet])
  (:require [irobot.parse :refer [create-robots-grammar]]))


(def r (create-robots-grammar))


(fact "single record with no rule line"
      (r "User-Agent:thingy\n") =>
      [:records
       [:record
        [:agentline "User-Agent:" "thingy"]]])

 
(fact "a path of / works"
      (r
"User-Agent:thingy
Allow:/") =>
      [:records
       [:record
        [:agentline "User-Agent:" "thingy"]
        [:ruleline [:allowexpr "Allow" ":" "/"]]]]) 


(fact "robots with one record, one allow"
      (r
"User-Agent:thingy
Allow:*") =>
      [:records
       [:record
        [:agentline "User-Agent:" "thingy"]
        [:ruleline [:allowexpr "Allow" ":" "*"]]]]) 


(fact "end-of-line comments are allowed"
       (r
"User-Agent: thingy # some comment
Allow:*") =>
      [:records
       [:record
        [:agentline "User-Agent:" "thingy" "# some comment"]
        [:ruleline [:allowexpr "Allow" ":" "*"]]]])


(fact "comments are allowed before the records"
      (r
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
      (r
"User-Agent: thingy
Allow: *
# first line
# second comment line") =>
      [:records
       [:record
        [:agentline "User-Agent:" "thingy"]
        [:ruleline [:allowexpr "Allow" ":" "*" "# first line" "# second comment line"]]]]) 


(fact "comments are allowed within the records (but they'll get gobbled greedily)"
      (r
"User-Agent: thingy
# first line
Allow: *") =>
      [:records
       [:record
        [:agentline "User-Agent:" "thingy" "# first line"]
        [:ruleline [:allowexpr "Allow" ":" "*"]]]]) 


(fact "robots with one record, one disallow"
      (r "User-Agent:thingy\nDisallow:*") =>
      [:records
       [:record
        [:agentline "User-Agent:" "thingy"]
        [:ruleline [:disallowexpr "Disallow" ":" "*"]]]])


(fact "arbitrary extensions can be specified"
      (r "User-Agent:thingy\nDisallow:*\nSOMETHING: foobar") =>
      [:records
       [:record
        [:agentline "User-Agent:" "thingy"]
        [:ruleline [:disallowexpr "Disallow" ":" "*"]]
        [:ruleline [:extline "SOMETHING" ":" "foobar"]]]])


(fact "can have more than one record"
      (r
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
      (r
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

