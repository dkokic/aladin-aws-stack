(ns stack
  (:require [cheshire.core :as cheshire]))

(def docdb-resource-cluster-name "MyDbCluster")
(def docdb-resource-instance-name "MyDBInstance")
(def docdb-cluster-name "MyCluster")
(def docdb-instance-name "MyDocDbInstance")
(def docdb-master-username "admin1234567890")
(def docdb-master-password "admin1234567890")

; https://docs.aws.amazon.com/documentdb/latest/developerguide/db-instance-classes.html#db-instance-classes-by-region
(def docdb-instance-class "db.t3.medium")

(def stack
  {
   "Resources" {
                docdb-resource-cluster-name  {
                                              "Type"           "AWS::DocDB::DBCluster"
                                              "DeletionPolicy" "Delete"
                                              "Properties"     {
                                                                "DBClusterIdentifier" docdb-cluster-name
                                                                "MasterUsername"      docdb-master-username
                                                                "MasterUserPassword"  docdb-master-password
                                                                "EngineVersion"       "4.0.0"
                                                                }
                                              }
                docdb-resource-instance-name {
                                              "Type"       "AWS::DocDB::DBInstance"
                                              "Properties" {
                                                            "DBClusterIdentifier" {"Ref" docdb-cluster-name}
                                                            "DBInstanceIdentifier" docdb-instance-name
                                                            "DBInstanceClass"      docdb-instance-class
                                                            }
                                              "DependsOn"  docdb-resource-cluster-name
                                              }
                }
   })

(defn gen [args]
  (let [output (cheshire/generate-string stack {:pretty true})]
    (clojure.pprint/pprint stack)
    (spit "stack.json" output)))