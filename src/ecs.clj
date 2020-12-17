(ns ecs)

(defn create-fargate-cluster [cluster-name]
  {
   "Type" "AWS::ECS::Cluster"
   "Properties" {
                 "ClusterName" cluster-name
                 "CapacityProviders" ["FARGATE"]            ; + FARGATE_SPOT
                 "DefaultCapacityProviderStrategy" [
                                                    {
                                                     "CapacityProvider" "FARGATE"
                                                     "Weight" 1
                                                     }]
                 }
   })

; ecs-cluster-resource: Name of ecs cluster resource
; container-image: "amazon/amazon-ecs-sample"
; TODO task role ?
; TODO task execution role to pull container images & publish container logs to CloudWatch
(defn create-task-definition [ecs-cluster-resource-name app-name container-image app-container-port app-host-port]
  {
   "Type" "AWS::ECS::TaskDefinition"
   "Properties" {
                 "ExecutionRoleArn" "arn:aws:iam::290183655974:role/ecsTaskExecutionRole"
                 ;                 "RequiresCompatibilities" ["EC2", "FARGATE"]
                 "ContainerDefinitions" [
                                         {
                                          "Name" app-name
                                          "MountPoints" [
                                                         ;{
                                                         ; "SourceVolume" "my-vol"
                                                         ; "ContainerPath" "/var/www/my-wol"
                                                         ; }
                                                         ]
                                          "Image" container-image
                                          "Cpu" 256
                                          "PortMappings" [
                                                          {
                                                           "ContainerPort" app-container-port
                                                           "HostPort" app-host-port
                                                           }]
                                          "EntryPoint" []
                                          "Memory" 512,
                                          "Essential" true
                                          }
                                         ]
                 "Volumes" [
                            ;{
                            ; "Host" { "SourcePath" "/var/lib/docker/vfs/dir" }
                            ; "Name" "my-vol"
                            ; }
                            ]
                 }
   "DependsOn" ecs-cluster-resource-name
   })

(defn create-service [cluster-name desired-count task-definition]
  {
   "Type" "AWS::ECS::Service"
   "Properties" {
                 "Cluster" cluster-name
                 "DesiredCount" desired-count
                 "TaskDefinition" task-definition
                 "AwsvpcConfiguration" {
                                        "AssignPublicIp" "ENABLED"
                                        }
                 }
   })