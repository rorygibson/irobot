# I, Robot

Parses and handles robots.txt files.
Does its' best to obey Asimov's Three Laws.

Not ready for prime-time yet - need to resolve the must-do TODOs below first.

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
+ Support fetching sitemaps & sitemap-indexes, using mundi library for parsing & link extraction

## Nice-to-have TODOs:
+ Support ':' in URLs (see www.linkedin.com/robots.txt for an example)
+ Wildcard globbing in allow & disallow directives (see github's robots for an example)
+ Support Host: directive
+ Defined behaviour for empty directives (key but no value)
+ Normalisation of URL encoded allow/disallow URLs prior to comparison so that eg  /a%3CD.html = /a%3cd.html (BUT /a%2fb.html != /a/b.html - don't decode /)


## License

Copyright © 2014 Rory Gibson

Distributed under the Eclipse Public License version 1.0.
