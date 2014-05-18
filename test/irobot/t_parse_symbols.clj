(ns irobot.t-parse-symbols
  (:use [midje.sweet])
  (:require [irobot.parse :refer :all]))



(fact "Allows may include globs"
    (parse "User-agent:foo\nAllow:/a/*/c")
  => [:records
      [:record
       [:agent "User-agent:" "foo"]
       [:allow "Allow:" "/a/*/c"]]])
 

(fact "Disallows may include globs"
    (parse "User-agent:foo\nDisallow:/a/*/c")
  => [:records
      [:record
       [:agent "User-agent:" "foo"]
       [:disallow "Disallow:" "/a/*/c"]]])


(fact "Paths may contain the ? and = symbols"
  (parse "User-agent:foo\nAllow:/a/b/c?d=e")
  => [:records
      [:record
       [:agent "User-agent:" "foo"]
       [:allow "Allow:" "/a/b/c?d=e"]]])


(fact "URLs may contain the $ sign"
  (parse "User-Agent:Foo\nAllow:/some-url-with-a-$/")
  => [:records
      [:record
       [:agent "User-Agent:" "Foo"]
       [:allow "Allow:" "/some-url-with-a-$/"]]])


(fact "URLs may contain the , symbol"
  (parse "User-Agent:Foo\nAllow:/some-url-with-a-,-in-it/")
  => [:records
      [:record
       [:agent "User-Agent:" "Foo"]
       [:allow "Allow:" "/some-url-with-a-,-in-it/"]]])


(fact "URLs may contain the % symbol"
  (parse "User-Agent:Foo\nAllow:/some-url-with-a-%-in-it/")
  => [:records
      [:record
       [:agent "User-Agent:" "Foo"]
       [:allow "Allow:" "/some-url-with-a-%-in-it/"]]])


(fact "URLs may contain the @ symbol"
  (parse "User-Agent:Foo\nAllow:/some-url-with-a-@-in-it/")
  => [:records
      [:record
       [:agent "User-Agent:" "Foo"]
       [:allow "Allow:" "/some-url-with-a-@-in-it/"]]])


(fact "URLs may contain the + symbol"
  (parse "User-Agent:Foo\nAllow:/some-url-with-a-+-in-it/")
  => [:records
      [:record
       [:agent "User-Agent:" "Foo"]
       [:allow "Allow:" "/some-url-with-a-+-in-it/"]]])


(fact "URLs may contain the ! symbol"
  (parse "User-Agent:Foo\nAllow:/some-url-with-a-!-in-it/")
  => [:records
      [:record
       [:agent "User-Agent:" "Foo"]
       [:allow "Allow:" "/some-url-with-a-!-in-it/"]]])


(fact "URLs may contain the \" symbol"
  (parse "User-Agent:Foo\nAllow:/some-url-with-a-\"-in-it/")
  => [:records
      [:record
       [:agent "User-Agent:" "Foo"]
       [:allow "Allow:" "/some-url-with-a-\"-in-it/"]]])


(fact "URLs may contain the ' symbol"
  (parse "User-Agent:Foo\nAllow:/some-url-with-a-'-in-it/")
  => [:records
      [:record
       [:agent "User-Agent:" "Foo"]
       [:allow "Allow:" "/some-url-with-a-'-in-it/"]]])


(fact "URLs may contain the ( and ) symbols"
  (parse "User-Agent:Foo\nAllow:/some-url-with-a-()-in-it/")
  => [:records
      [:record
       [:agent "User-Agent:" "Foo"]
       [:allow "Allow:" "/some-url-with-a-()-in-it/"]]])


(fact "URLs may contain the | symbol"
  (parse "User-Agent:Foo\nAllow:/some-url-with-a-|-in-it/")
  => [:records
      [:record
       [:agent "User-Agent:" "Foo"]
       [:allow "Allow:" "/some-url-with-a-|-in-it/"]]])


(fact "URLs may contain the ; symbol"
  (parse "User-Agent:Foo\nAllow:/some-url-with-a-;-in-it/")
  => [:records
      [:record
       [:agent "User-Agent:" "Foo"]
       [:allow "Allow:" "/some-url-with-a-;-in-it/"]]])


(fact "URLs may contain the # symbol"
  (parse "User-Agent:Foo\nAllow:/some-url-with-a-#-in-it/")
  => [:records
      [:record
       [:agent "User-Agent:" "Foo"]
       [:allow "Allow:" "/some-url-with-a-#-in-it/"]]])
