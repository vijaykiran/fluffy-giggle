(ns fluffy-giggle.core
  (:require [cheshire.core :as json]
            [clojure.string :as str]
            [clojure.tools.cli :refer [parse-opts]]
            [org.httpkit.client :as http])
  (:gen-class))

(def cli-options
  [["-a" "--arena" "Arena Mode"]
   ["-b" "--browse" "Open game in a browser"]
   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["Vijay's Vindinium Client"
        ""
        "Usage: program-name [options] action"
        ""
        "Options:"
        options-summary
        ""]
       (str/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (str/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 (usage summary)))))
