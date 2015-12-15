(ns fluffy-giggle.client
  "vindinium interaction functions"
  (:require [cheshire.core :as json]
            [clojure.string :as str]
            [fluffy-giggle.moves :refer [random-dir]]
            [org.httpkit.client :as http]))

(def server-url "http://vindinium.org/api")
(def training-url (str server-url "/training"))
(def arena-url (str server-url "/arena"))

(defn- create-game
  "get a new game from the server"
  [url key]
  (http/post url {:form-params {:key key}}))

(defn- finished?
  "returns true if game finished, false otherwise"
  [game]
  (let [turn (get-in game ["game" "turn"])
        max-turns (get-in game ["game" "maxTurns"])
        finished (get-in game ["game" "finished"])]
    (or finished (= turn max-turns))))

(defn- parse-game
  "parse response from a game post"
  [resp]
  (json/parse-string (:body resp)))

(defn- post-move
  "post a move in 'dir' to the server at url with key,
  and with a call-back 'play-fn"
  [play-url game-key dir play-fn]
  (http/post play-url {:form-params {:key game-key :dir dir}} play-fn))

(defn- play [response]
  (let [game (parse-game response)
        play-url (get game "playUrl")]
    (if (finished? game)
      {:status :finished :result "lost"}
      (post-move play-url game-key (random-dir game) play))))

(defn play-game [server-url game-key next-move-fn]
  (let [game (parse-game @(create-game server-url game-key))
        play-url (get game "playUrl")]
    (println (str "game is at: " (get game "viewUrl")))
    (post-move play-url game-key (next-move-fn game) play)))
