(ns ddate.ddate)
(require '[java-time :as t])
(require '[clojure.math.numeric-tower :as math])

(def days '({:l "Sweetmorn" :s "SM"},
            {:l "Boomtime" :s "BT"},
            {:l "Pungenday" :s "PD"},
            {:l "Prickle-Pricle" :s "PP"},
            {:l "Setting Orange" :s "SO"}))

(def seasons '({:l "Chaos" :s "Chs"},
               {:l "Discord" :s "Dsc"},
               {:l "Confusion" :s "Cfn"},
               {:l "Bureaucracy" :s "Bcy"},
               {:l "The Aftermath" :s "Afm"}))

(def holidays {"Chaos" {5 "Mungday" 50 "Chaoflux"},
               "Discord" {5 "Mojoday" 50 "Discoflux"},
               "Confusion" {5 "Syaday" 50 "Confuflux"},
               "Bureaucracy" {5 "Zaraday" 50 "Bureflux"},
               "The Aftermath" {5 "Maladay" 50 "Afflux"}})

(def defaultDateFormat "Today is %{%A, the %e day of %B} in the YOLD %Y%N%nCelebrate %H")

(defn in? [x coll]
    "Return true if x is in coll, else false. "
    (some #(= x %) coll))


(defn replacer [inputstring replacement date]
  (clojure.string/replace 
    inputstring (get replacement :pattern) ((get replacement :value) date)))



(defn ordinal
    "Converts an integer to its ordinal as a string. 1 is '1st', 2 is '2nd',
       3 is '3rd', etc."
      [num]
      (let [ordinals ["th", "st", "nd", "rd", "th",
                      "th", "th", "th", "th", "th"]
            remainder-100 (rem num 100)
            remainder-10  (rem num 10)]
        (if (in? remainder-100 [11 12 13])
          ;; special case for *11, *12, *13
          (str num (ordinals 0))
          (str num (ordinals remainder-10)))))

(defn dayOfYOLD
  "returns the day of discordian year, St. Tib's not counted"
  [date]
  (let [doy (t/as date :day-of-year), year (t/as date :year)]
    (if (t/leap? date)
      (if (> doy 59) (- doy 1) doy)
      doy)))

(defn stTibs 
  "returns St. Tib's Day if it is St. Tip's Day"
  [date]
  (if (and (t/leap? date)  (= (t/as date :day-of-year) 60))
    "St. Tib's Day"
    ""))

(defn YOLD
  [date]
  (+ (t/as date :year) 1166))

(defn dDayOfSeason
  "returns the day of season of the given date"
  [date]
  (rem (dayOfYOLD date) 73) )

(defn dSeason 
  "returns the current season of the given date"
  ([date] 
   (dSeason date :l))
  ([date size] 
   (get (nth seasons (math/floor (/ (dayOfYOLD date) 73))) size)))

(defn dHoliday 
  "returns a discordian holiday or empty string if given date is not a holiday"
  [date]
  (let [holiday (get (get holidays (dSeason date)) (dDayOfSeason date))]
   (if holiday 
     holiday
     "")))

(defn dWeekday
 "returns the day of week" 
  ([date]
  (dWeekday date :l))
  ([date size]
   (let [dow (rem (dayOfYOLD date) 5)]
   (get (nth days (if (> dow 0) (- dow 1) 4) ) size))))

(defn nline
  [date]
  (str "\n"))

(def options (list 
               {:pattern "%A" :value #(dWeekday % :l)}
               {:pattern "%a" :value #(dWeekday % :s)}
               {:pattern "%B" :value #(dSeason % :l)}
               {:pattern "%b" :value #(dSeason % :s)}
               {:pattern "%d" :value #(str (dDayOfSeason %))}
               {:pattern "%e" :value #(ordinal (dDayOfSeason %))}
               {:pattern "%H" :value #(dHoliday %)}
               {:pattern "%Y" :value #(str (YOLD %))}
               {:pattern "%n" :value #(nline %)}
               ))

(defn sipar 
  "simple parser for simple string replacement using options as pattern and replace source"
  [formatstring date]
  (reduce (fn [x y] (replacer x y date)) formatstring options))

(defn handle%N 
  [formatstring date]
    (if (= (dHoliday date) "") 
      (clojure.string/replace formatstring #"%N.*" "")   
      (clojure.string/replace formatstring "%N" "")))


(defn handleStTibs
  [formatstring date]
  (let [stTibsdate (stTibs date)]
  (if (= stTibsdate "") 
        (clojure.string/replace formatstring #"%\{|\}" "")
        (clojure.string/replace formatstring #"%\{.*\}" stTibsdate))))

(defn gimmeddate 
  "returns discordian date according to the given formatstring"
  [formatstring date]
  (sipar (handle%N (handleStTibs formatstring date) date) date))
