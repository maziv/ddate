(ns ddate.core-test
  (:require [clojure.test :refer :all]
            [ddate.ddate :refer :all]
            [ddate.core :refer :all]))
(require '[java-time :as t])

(def refdate (t/local-date 2011 11 1))
(def mungday (t/local-date 2000 1 5))
(def sttibs (t/local-date 2000 2 29))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 0))))

(deftest gimmedate-test 
  (testing "gimmeddate defaultformat"
    (is (= "Today is Setting Orange, the 13th day of The Aftermath in the YOLD 3177" (ddate.ddate/gimmeddate ddate.ddate/defaultDateFormat refdate)))
    )
  (testing "gimmeddate celebrate Mungday" 
    (is (="Today is Setting Orange, the 5th day of Chaos in the YOLD 3166\nCelebrate Mungday" (ddate.ddate/gimmeddate ddate.ddate/defaultDateFormat mungday)))
    )
  (testing "gimmeddate st tibs"
    (is (="Today is St. Tib's Day in the YOLD 3166" (ddate.ddate/gimmeddate ddate.ddate/defaultDateFormat sttibs)))
    ))

