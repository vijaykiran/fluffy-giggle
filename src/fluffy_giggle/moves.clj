(ns fluffy-giggle.moves)

;; This is the function you should update with custom
;; move calculation
(defn random-dir
  "return next move (one of 'Stay', 'North', 'South', and 'West')
  based on game-state"
  [game-state]
  (rand-nth ["Stay" "North" "East" "South" "West"]))
