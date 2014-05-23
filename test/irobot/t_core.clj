(ns irobot.t-core
  (:use [midje.sweet])
  (:require [irobot.core :refer [robots allows? crawl-delay sitemaps]]))


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


(def with-cd
  "User-Agent:XYZ
Crawl-delay: 10
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
  (allows? (robots "User-Agent:other\nAllow:/") "Me" "/something") => true)


(fact "allows UA when we have an allow / and a disallow /"
  (allows? (robots allow-and-disallow-records) "XYZ" "/") => truthy) 
 

(fact "disallowing a path works" 
  (allows? (robots "User-agent:MyBotX\nDisallow: /") "MyBotX" "/private") => false)

  
(fact "uses the * record when there's no match on UA"
  (allows? normal-robots "UseTheStarBot" "/allowed-by-star") => true)


(fact "uses the * record when there's no match on UA" 
  (allows? normal-robots "UseTheStarBot" "/private/disallowed-by-star") => false)


(fact "User agent should match on a substring"
  (allows? (robots "User-agent:LongBotName\nAllow:/\nDisallow:/hidden") "LongB" "/hidden")
  => true)


(fact "Can retrieve the crawl-delay from a robots.txt"
  (crawl-delay (robots with-cd) "XYZ") => 10N) 


(fact "If there's no explicit crawl-delay it returns nil"
  (crawl-delay normal-robots "MyBot") => nil)


(fact "Crawl-Delay will use the one from the * record if it's not set for the UA"
  (crawl-delay (robots "User-agent:*\nCrawl-Delay:50\nUser-Agent:Foo\nAllow:/") "MyBot")
  => 50N)


(fact "we can access all the sitemap elements"
  (count (sitemaps (robots "Sitemap:/sitemap1.xml\nSitemap:/sitemap2.xml"))) => 2)
