(ns my-art.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def size 1000)

(def center [(/ size 2) (/ size 2)])

(def radius 245)

(def d 0.015708)

(def offset (/ size 2))

(defn next-point [step]
  (let [angle (* step d)]
    [(* radius (Math/cos angle))
     (* radius (Math/sin angle))]))

(defn draw-body [{:keys [ref step]}]
  (let [tx (first ref)
        ty (last ref)
        position (next-point step)
        x (first position)
        y (last position)]
    (q/translate (+ x (first ref)) (+ y (last ref)))
    (q/ellipse 0 0 15 15)))


(defn draw-satellite [[x y] step]
  (q/stroke 255)
  (q/fill 220 200 255)
  (q/ellipse (+ offset x) (+ offset y) 15 15))

(defn draw-planet [[x y]]
  (let [c (- 255 (* x y))]
    (q/stroke 255)
    (q/shininess 2)
    (q/fill 220 200 255)
    (q/ellipse (+ offset x) (+ offset y) 15 15)))


(defn draw-planet1
  [planet]
  (draw-body {:ref center :step (:step planet)}))


(defn draw [state]
  (q/background 0)
  (doseq [planet (:planets state)] (draw-planet1 planet)))

(defn move-planet [planet]
  (update-in planet :step inc))

(defn update-fn [state]
  (for [planet (:planets state)]
    (move-planet planet)))

(defn setup []
  (q/frame-rate 100)
  {:planets [{:step   1
              :radius 30
              :size   10
              }]})

(q/defsketch my
             :host "host"
             :size [size size]
             :draw draw
             :setup setup
             :update update-fn
             ;:renderer :p3d
             :middleware [m/fun-mode identity])

(defn -main [& args])