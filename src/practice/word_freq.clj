(ns practice.word-freq
  (:require
   [clojure.java.io :as io]
   [clojure.string  :as s]))

(defn print-word-freqs [pairs]
  (doseq [pair pairs]
    (println pair)))

(defn word-freqs [fname]
  (let [data (slurp fname)]
    (->> (s/split ^String data #"\n|[  ]")
         (map #(s/lower-case ^String %))
         (frequencies)
         (sort-by val >))))

;; (time (word-freqs "resources/bible-10.txt"))

(defn word-freqs-2 [fname]
  (with-open [rdr (io/reader fname)]
    (->> (line-seq rdr)
         (mapcat #(s/split % #"\n|[ ]"))
         (map #(s/lower-case %))
         (frequencies)
         (sort-by val >))))

;; (time (print-word-freqs (word-freqs-2 "resources/bible-10.txt")))

(defn line-freq [l]
  (->> (s/split l #"\W+")
       (map s/lower-case)
       (frequencies)))

(defn word-freqs-3 [fname]
  (sort-by val >
           (with-open [rdr (io/reader fname)]
             (reduce (fn [a v]
                       (merge-with + a (line-freq v)))
                     {}
                     (line-seq rdr)))))

;; (time (print-word-freqs (word-freqs-3 "resources/bible-10.txt")))

(defn frequencies2
  [xform coll]
  (persistent!
   (transduce xform
              (completing (fn [counts x]
                            (assoc! counts x (inc (get counts x 0)))))
              (transient {}) coll)))

(defn word-freqs-4 [text]
  (with-open [rdr (clojure.java.io/reader text)]
    (->> (frequencies2 (comp (mapcat #(clojure.string/split % #" "))
                             (map #(clojure.string/lower-case %)))
                       (line-seq rdr))
         (sort-by val >))))

;; (time (print-word-freqs (word-freqs-4 "resources/bible-10.txt")))

;; (defn word-freqs-5 [fname]
;;   (let [data (slurp fname)]
;;     (->> (split-at)
;;          (map #(s/lower-case %))
;;          (frequencies)
;;          (sort-by val >))))

;; (time (print-word-freqs (word-freqs-3 "resources/bible-10.txt")))
