(ns ec2)

(defn create-vpc [name cidr-block]
  {
   "Type"       "AWS::EC2::VPC"
   "Properties" {
                 "CidrBlock" cidr-block
                 "Tags"      [
                              {
                               "Key"   "Name"
                               "Value" name
                               }
                              ]
                 }
   })

(defn create-internet-gateway [vpc-resource-id]
  {
   "Type"       "AWS::EC2::InternetGateway"
   "Properties" {}
   "DependsOn"  [vpc-resource-id]
   })

(defn create-vpc-gateway-attachment [vpc-resource-id internet-gateway-resource-id]
  {
   "Type"       "AWS::EC2::VPCGatewayAttachment"
   "Properties" {
                 "VpcId"             {"Ref" vpc-resource-id}
                 "InternetGatewayId" {"Ref" internet-gateway-resource-id}
                 }
   "DependsOn"  [vpc-resource-id internet-gateway-resource-id]
   })

(defn create-route-table [vpc-resource-id]
  {
   "Type"       "AWS::EC2::RouteTable"
   "Properties" {
                 "VpcId" {"Ref" vpc-resource-id}
                 }
   "DependsOn"  [vpc-resource-id]
   })

(defn create-route-internet-gateway [route-table-resource-id dest-cidr-block gateway-resource-id]
  {
   "Type"       "AWS::EC2::Route"
   "Properties" {
                 "RouteTableId"         {"Ref" route-table-resource-id}
                 "DestinationCidrBlock" dest-cidr-block
                 "GatewayId"            {"Ref" gateway-resource-id}
                 }
   "DependsOn"  [route-table-resource-id gateway-resource-id]
   })

(defn create-subnet [vpc-resource-id availability-zone cidr-block]
  {
   "Type"       "AWS::EC2::Subnet"
   "Properties" {
                 "AvailabilityZone" availability-zone
                 "CidrBlock"        cidr-block
                 "VpcId"            {"Ref" vpc-resource-id}
                 }
   "DependsOn"  [vpc-resource-id]
   })

(defn create-subnet-route-table-association [subnet-resource-id route-table-resource-id]
  {
   "Type"       "AWS::EC2::SubnetRouteTableAssociation"
   "Properties" {
                 "SubnetId"     {"Ref" subnet-resource-id}
                 "RouteTableId" {"Ref" route-table-resource-id}
                 }
   "DependsOn" [subnet-resource-id route-table-resource-id]
   })

(defn create-security-group [vpc-resource-id group-name group-description]
  {
   "Type"       "AWS::EC2::SecurityGroup"
   "Properties" {
                 "GroupName"            group-name
                 "GroupDescription"     group-description
                 "VpcId"                {"Ref" vpc-resource-id}
                 "SecurityGroupIngress" [
                                         {
                                          "IpProtocol" "tcp"
                                          "FromPort"   27017
                                          "ToPort"     27017
                                          "CidrIp"     "0.0.0.0/0"
                                          }
                                         ]
                 "SecurityGroupEgress"  []
                 }
   "DependsOn"  [vpc-resource-id]
   })