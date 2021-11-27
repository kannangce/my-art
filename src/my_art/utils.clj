(ns my-art.utils)

(defn deg->radian [point]
  (Math/toRadians point))

(defn distance [[x1 y1] [x2 y2]]
  "Gets the distance between 2 different points"
  (let [dx (- x1 x2)
        dy (- y1 y2)]
    (Math/sqrt (* dx dx) (* dy dy))))

(defn polar->cartesian
  "Gets te cartesian coordinate for the given polar coordinate"
  [radius angle]
  [(* radius (Math/cos angle))
   (* radius (Math/sin angle))])

(defn translate-coordinates
  "Translates the given point [x,y] from the origin [from-x, from-y] to [to-x to-y]"
  [[from-x from-y] [to-x to-y] [x y]]
  (let [dx (- to-x from-x)
        dy (- to-y from-y)]
    [(+ x dx) (+ y dy)]))