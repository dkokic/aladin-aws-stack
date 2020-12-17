(ns documentdb)

; subnet-ids: ["subnet-123", "subnet-456"]
(defn create-documentdb-subnet-group [name description subnet-ids]
  {
   "Type" "AWS::DocDB::DBSubnetGroup"
   "Properties" {
                 "DBSubnetGroupName" name
                 "DBSubnetGroupDescription" description
                 "SubnetIds" subnet-ids
                 }
   })

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

(defn create-documentdb-cluster [security-group-resource-id documentdb-subnet-group-resource-id
                                 cluster-name paramgroup-resource-name master-username master-password]
  {
   "Type"           "AWS::DocDB::DBCluster"
   "DeletionPolicy" "Delete"
   "Properties"     {
                     "DBClusterIdentifier"         cluster-name
                     "DBClusterParameterGroupName" {"Ref" paramgroup-resource-name}
                     "DBSubnetGroupName" {"Ref" documentdb-subnet-group-resource-id}
                     "MasterUsername"              master-username
                     "MasterUserPassword"          master-password
                     "EngineVersion"               "4.0.0"
                     "Port" 27017
                     "VpcSecurityGroupIds"         [{"Ref" security-group-resource-id}]
                     }
   "DependsOn"      [security-group-resource-id documentdb-subnet-group-resource-id paramgroup-resource-name]
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