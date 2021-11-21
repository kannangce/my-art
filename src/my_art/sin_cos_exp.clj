(ns my-art.sin-cos-exp
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def size 1200)


(defn equation
  [[x y]]
  (- (Math/sin (Math/sin (* x (- (Math/sin y) (Math/cos x))))) (Math/cos (Math/cos (* y (- (Math/cos x) (Math/sin y)))))))

(defn degrees->radians [point]
  (mapv #(Math/toRadians %) point))

(defn radians->degrees [point]
  (mapv #(Math/round (Math/toDegrees %)) point))

(defn get-all-points []
  (for [x (range size) y (range size)]
    [x y]))


(defn get-plottable-points
  []
  (->> (get-all-points)
       (map #(degrees->radians %))
       (map #(hash-map :point %1 :value (equation %1)))
       (filter #(< (Math/abs (:value %)) 0.0001))
       (map :point)
       (map #(radians->degrees %))
       ))



(defn setup
  []
  {:points (get-plottable-points)
   })

(defn draw
  [state]
  (println state)
  (doseq [point (:points state)]
    ;(q/ellipse (first point) (last point) 1 1)
    (apply q/point point)))

;(println (get-plottable-points))
;
(q/defsketch my
             :host "host"
             :size [size size]
             :draw draw
             :setup setup
             :middleware [m/fun-mode identity])