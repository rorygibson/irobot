(defproject irobot "0.1.1-SNAPSHOT"
  :description "A minimal Clojure library for parsing and intepreting robots.txt files"

  :url "http://github.com/rorygibson/irobot"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.logging "0.2.6"]
                 [clj-antlr "0.2.2"]]

  :profiles {
             :dev {
                   :resource-paths ["test/resources"]
                   :dependencies [[midje "1.6.3"]
                                  [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                                     javax.jms/jms
                                                                     com.sun.jdmk/jmxtools
                                                                     com.sun.jmx/jmxri]]]
                   :plugins      [[lein-ancient "0.5.4" :exclusions [org.clojure/clojure commons-codec org.clojure/data.xml]]
                                  [lein-midje "3.1.1"]]}
             })
