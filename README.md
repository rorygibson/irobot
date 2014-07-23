# I, Robot

Parses and handles robots.txt files.
Does its' best to obey Asimov's Three Laws.


## Features
+ Loads robots.txt files from string data
+ Has a simple API for determining if a robots.txt allows acccess for a given user-agent to a specific path in the site
+ Natively understands allow, disallow, crawl-delay and sitemap rules
+ Uses the ANTLR library and a custom grammar to precisely model the robots.txt spec
+ Minimal dependencies
+ Well tested (using Midje)

## Usage

Add the dependency to your project.clj.

You'll probably also want a dependency on something like [https://github.com/dakrone/clj-http](clj-http) for actually fetching the robots.txt from the target webserver.

```clojure
[irobot "0.1.1-SNAPSHOT"]
[clj-http "0.9.1"]
```


Then, in a REPL

```clojure
(require '[clj-http.client :as client])
(require '[irobot.core :as irobot])

(def fb (irobot/robots (:body (client/get "http://www.facebook.com/robots.txt"))))

(irobot/allows? fb "MyUserAgent" "/policy.php")
=> false

(irobot/allows? fb "GoogleBot" "/policy.php")
=> true

```

## Must-do TODOs:
+ Better handling of parsing errors

## Nice-to-have TODOs:
+ Support ':' in URLs (see www.linkedin.com/robots.txt for an example)
+ Wildcard globbing in allow & disallow directives (see github's robots for an example)
+ Support Host: directive
+ Defined behaviour for empty directives (key but no value)
+ Normalisation of URL encoded allow/disallow URLs prior to comparison so that eg  /a%3CD.html = /a%3cd.html (BUT /a%2fb.html != /a/b.html - don't decode /)


## License

Copyright © 2014 Rory Gibson

Distributed under the Eclipse Public License version 1.0.
