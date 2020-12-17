(ns stack
  (:require [cheshire.core :as cheshire]
            [clojure.pprint]
            [ecr]
            [documentdb]
            [ecs]
            [codebuild]
            [ec2]))

; DocumentDB
(def res-docdb-cluster "MyDocDbCluster")
(def res-docdb-instance "MyDocDbInstance")
(def res-docdb-cluster-paramgroup "MyDocDbClusterParamGroup")
(def res-docdb-subnet-group "MyDocDbSubnetGroup")
(def docdb-cluster-name "MyCluster")
(def docdb-instance-name "MyDocDbInstance")
(def docdb-instance-class "db.t3.medium")
(def docdb-cluster-paramgroup-name "MyClusterParameterGroup")
(def docdb-master-username "admin1234567890")
(def docdb-master-password "admin1234567890")

; VPC & Security Group
(def res-vpc "MyVpc")
(def res-vpc-internet-gateway "MyVpcInternetGateway")
(def res-vpc-attach-gateway "MyVpcAttachInternetGateway")
(def res-vpc-route-table "MyRouteTable")
(def res-security-group "MySecurityGroup")
(def res-subnet1 "MySubnet1")
(def res-subnet2 "MySubnet2")

; ECR
(def res-ecr "MyEcr")
(def ecr-repo-name "my-ecr-repository")                     ; must be kebab-case
(def ecr-user-arns ["arn:aws:iam::290183655974:user/dkokic", "arn:aws:iam::290183655974:user/tom"])

; Fargate
(def res-fargate "MyFargate")
(def fargate-cluster-name "MyFargateCluster")

; CodeBuild
(def res-codebuild "MyCodeBuild")
(def codebuild-project-name "MyCodeBuildProject")
(def codebuild-project-description "My project description")

; App
(def res-app-task-definition "MyAppTaskDefinition")
(def res-app-service "MyAppService")
(def app-name "MyAppName")

(def stack
  {"Resources" {
                res-vpc                      (ec2/create-vpc "MyVpcName2" "10.0.0.0/16")
                res-vpc-internet-gateway     (ec2/create-internet-gateway res-vpc)
                res-vpc-attach-gateway       (ec2/create-vpc-gateway-attachment res-vpc res-vpc-internet-gateway)
                res-vpc-route-table          (ec2/create-route-table res-vpc)
                res-security-group           (ec2/create-security-group res-vpc "MySecurityGroupName" "My security group description")
                res-subnet1                  (ec2/create-subnet res-vpc "eu-central-1a" "10.0.0.0/24")
                res-subnet2                  (ec2/create-subnet res-vpc "eu-central-1b" "10.0.1.0/24")
                "assoc1"                     (ec2/create-subnet-route-table-association res-subnet1 res-vpc-route-table)
                "assoc2"                     (ec2/create-subnet-route-table-association res-subnet2 res-vpc-route-table)
                "MyInternetRoute"            (ec2/create-route-internet-gateway res-vpc-route-table "0.0.0.0/0" res-vpc-internet-gateway)

                res-docdb-subnet-group       (documentdb/create-documentdb-subnet-group "MyDocDbSubnetGroupName" "My doc db subnet group desc"
                                                                                        [{"Ref" res-subnet1} {"Ref" res-subnet2}])
                res-docdb-cluster-paramgroup (documentdb/create-documentdb-cluster-paramgroup docdb-cluster-paramgroup-name)
                res-docdb-cluster            (documentdb/create-documentdb-cluster res-security-group res-docdb-subnet-group
                                                                                   docdb-cluster-name res-docdb-cluster-paramgroup
                                                                                   docdb-master-username docdb-master-password)
                res-docdb-instance           (documentdb/create-documentdb-instance res-docdb-cluster docdb-instance-name docdb-instance-class)
                ;res-ecr                      (ecr/create-ecr ecr-repo-name ecr-user-arns)

                ; res-fargate (ecs/create-fargate-cluster fargate-cluster-name)
                ;res-app-task-definition (ecs/create-task-definition res-fargate app-name "nginx:latest" 8080 80)
                ;res-app-service (ecs/create-service fargate-cluster-name 1 )

                ; res-codebuild (codebuild/create-codebuild codebuild-project-name codebuild-project-description)
                }
   })

(defn gen [args]
  (let [
        output (cheshire/generate-string stack {:pretty true})]
    (clojure.pprint/pprint stack)
    (spit "stack.json" output)))