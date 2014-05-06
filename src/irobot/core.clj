(ns irobot.core
  (:require [clj-antlr.core :as antlr]))

(defn create-grammar
  [filename]
  (antlr/parser filename))
