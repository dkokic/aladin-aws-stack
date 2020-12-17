(ns codebuild)

(defn create-codebuild [project-name project-description]
  {
   "Type"       "AWS::CodeBuild::Project"
   "Properties" {
                 "Name"             project-name
                 "Description"      project-description
                 "ServiceRole"      {"Fn::GetAtt" ["ServiceRole", "Arn"]}
                 "Artifacts"        {"Type" "no_artifacts"}
                 "Environment"      {
                                     "Type"                 "LINUX_CONTAINER"
                                     "ComputeType"          "BUILD_GENERAL1_SMALL"
                                     "Image"                "aws/codebuild/java:openjdk-8",
                                     "EnvironmentVariables" [
                                                             {"Name"  "varName"
                                                              "Type"  "varType"
                                                              "Value" "varValue"}
                                                             ]
                                     }
                 "Source"           {
                                     "Location" "codebuild-demo-test/0123ab9a371ebf0187b0fe5614fbb72c"
                                     "Type"     "S3"
                                     }
                 "TimeoutInMinutes" 10
                 }
   }
  )