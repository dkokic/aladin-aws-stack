(ns ecr)

; repo-name: string
; user-arns: vec[arn]
(defn create-ecr [repo-name user-arns]
  {
   "Type"       "AWS::ECR::Repository"
   "Properties" {
                 "RepositoryName"       repo-name
                 "RepositoryPolicyText" {
                                         "Version"   "2008-10-17"
                                         "Statement" [
                                                      {
                                                       "Sid"       "AllowPushPull"
                                                       "Effect"    "Allow"
                                                       "Principal" {
                                                                    "AWS" user-arns
                                                                    }
                                                       "Action"    [
                                                                    "ecr:GetDownloadUrlForLayer",
                                                                    "ecr:BatchGetImage",
                                                                    "ecr:BatchCheckLayerAvailability",
                                                                    "ecr:PutImage",
                                                                    "ecr:InitiateLayerUpload",
                                                                    "ecr:UploadLayerPart",
                                                                    "ecr:CompleteLayerUpload"
                                                                    ]
                                                       }
                                                      ]
                                         }
                 }
   }
  )