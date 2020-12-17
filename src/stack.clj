(ns stack
  (:require [cheshire.core :as cheshire]
            [clojure.pprint]
            [ecr]
            [documentdb]))

; DocumentDB
(def res-docdb-cluster "MyDocDbCluster")
(def res-docdb-instance "MyDocDbInstance")
(def res-docdb-cluster-paramgroup "MyDocDbClusterParamGroup")
(def docdb-cluster-name "MyCluster")
(def docdb-instance-name "MyDocDbInstance")
(def docdb-instance-class "db.t3.medium")
(def docdb-cluster-paramgroup-name "MyClusterParameterGroup")
(def docdb-master-username "admin1234567890")
(def docdb-master-password "admin1234567890")

; ECR
(def res-ecr "MyEcr")
(def ecr-repo-name "my-ecr-repository")                     ; must be kebab-case
(def ecr-user-arns ["arn:aws:iam::290183655974:user/dkokic", "arn:aws:iam::290183655974:user/tom"])

(def stack
  {"Resources" {
                res-docdb-cluster-paramgroup (documentdb/create-documentdb-cluster-paramgroup docdb-cluster-paramgroup-name)
                res-docdb-cluster            (documentdb/create-documentdb-cluster docdb-cluster-name res-docdb-cluster-paramgroup
                                                                                   docdb-master-username docdb-master-password)
                res-docdb-instance           (documentdb/create-documentdb-instance res-docdb-cluster docdb-instance-name docdb-instance-class)
                res-ecr                      (ecr/create-ecr ecr-repo-name ecr-user-arns)
                }
   })

(defn gen [args]
  (let [
        output (cheshire/generate-string stack {:pretty true})]
    (clojure.pprint/pprint stack)
    (spit "stack.json" output)))