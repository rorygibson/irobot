(defproject irobot "0.1.0-SNAPSHOT"
  :description "FIXME: write description"

  :url "http://example.com/FIXME"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-antlr "0.2.2"]]

  :main irobot.core
  
  :profiles {
             :dev {
                   :dependencies [[midje "1.6.3"]]
                   :plugins      [[lein-ancient "0.5.4" :exclusions [org.clojure/clojure commons-codec org.clojure/data.xml]]
                                  [lein-midje "3.1.1"]]}
             })
