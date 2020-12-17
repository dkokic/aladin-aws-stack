(ns documentdb)

(defn create-documentdb-cluster-paramgroup [paramgroup-name]
  {
   "Type"       "AWS::DocDB::DBClusterParameterGroup"
   "Properties" {
                 "Description" "description"
                 "Family"      "docdb4.0"
                 "Name"        paramgroup-name
                 "Parameters"  {
                                "tls" "disabled"
                                }
                 }
   })

(defn create-documentdb-cluster [cluster-name paramgroup-resource-name master-username master-password]
  {
   "Type"           "AWS::DocDB::DBCluster"
   "DeletionPolicy" "Delete"
   "Properties"     {
                     "DBClusterIdentifier"         cluster-name
                     "DBClusterParameterGroupName" {"Ref" paramgroup-resource-name}
                     "MasterUsername"              master-username
                     "MasterUserPassword"          master-password
                     "EngineVersion"               "4.0.0"
                     }
   "DependsOn"      paramgroup-resource-name
   })

; instance-class: one of https://docs.aws.amazon.com/documentdb/latest/developerguide/db-instance-classes.html#db-instance-classes-by-region
(defn create-documentdb-instance [cluster-resource-name instance-name instance-class]
  {
   "Type"       "AWS::DocDB::DBInstance"
   "Properties" {
                 "DBClusterIdentifier"  {"Ref" cluster-resource-name}
                 "DBInstanceIdentifier" instance-name
                 "DBInstanceClass"      instance-class
                 }
   "DependsOn"  cluster-resource-name
   })