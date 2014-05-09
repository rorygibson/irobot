(ns irobot.paths)


(defn matching-path
  "Given a single Path and a set of Paths, return any elements in Paths which startWith Path"
  [path paths]
  (let [filters [ #(= path %) #(.startsWith path %)]
        filtered (filter (apply some-fn filters) paths)
        res (not (empty? filtered))]
    res))  
