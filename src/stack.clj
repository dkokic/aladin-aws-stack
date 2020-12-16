(ns stack
  (:require [cheshire.core :as cheshire]
            [clojure.pprint]))

(def res-docdb-cluster "DocDbCluster")
(def res-docdb-instance "DocDbInstance")
(def res-docdb-cluster-paramgroup "DocDbClusterParamGroup")

(def docdb-cluster-name "MyCluster")
(def docdb-instance-name "MyDocDbInstance")
(def docdb-cluster-paramgroup-name "MyClusterParameterGroup")
(def docdb-master-username "admin1234567890")
(def docdb-master-password "admin1234567890")

; https://docs.aws.amazon.com/documentdb/latest/developerguide/db-instance-classes.html#db-instance-classes-by-region
(def docdb-instance-class "db.t3.medium")

(def stack
  {"Resources" {
                res-docdb-cluster-paramgroup {
                                              "Type"       "AWS::DocDB::DBClusterParameterGroup"
                                              "Properties" {
                                                            "Description" "description"
                                                            "Family"      "docdb4.0"
                                                            "Name"        docdb-cluster-paramgroup-name
                                                            "Parameters"  {
                                                                           "tls" "disabled"
                                                                           }
                                                            }
                                              }
                res-docdb-cluster            {
                                              "Type"           "AWS::DocDB::DBCluster"
                                              "DeletionPolicy" "Delete"
                                              "Properties"     {
                                                                "DBClusterIdentifier"         docdb-cluster-name
                                                                "DBClusterParameterGroupName" {"Ref" res-docdb-cluster-paramgroup}
                                                                "MasterUsername"              docdb-master-username
                                                                "MasterUserPassword"          docdb-master-password
                                                                "EngineVersion"               "4.0.0"
                                                                }
                                              "DependsOn"      res-docdb-cluster-paramgroup
                                              }
                res-docdb-instance           {
                                              "Type"       "AWS::DocDB::DBInstance"
                                              "Properties" {
                                                            "DBClusterIdentifier"  {"Ref" res-docdb-cluster}
                                                            "DBInstanceIdentifier" docdb-instance-name
                                                            "DBInstanceClass"      docdb-instance-class
                                                            }
                                              "DependsOn"  res-docdb-cluster
                                              }
                }
   })

(defn gen [args]
  (let [output (cheshire/generate-string stack {:pretty true})]
    (clojure.pprint/pprint stack)
    (spit "stack.json" output)))