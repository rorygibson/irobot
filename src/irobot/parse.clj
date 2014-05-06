(ns irobot.parse
  (:require [clj-antlr.core :as antlr]))


(def robots-grammar-filename "resources/robots.g4")


(defn create-grammar
  [filename]
  (antlr/parser filename))


(defn create-robots-grammar
  []
  (create-grammar robots-grammar-filename))
