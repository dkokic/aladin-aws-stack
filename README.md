# execute
clj -X stack/gen

# aws
aws cloudformation create-stack --stack-name OurTestStack --template-body file://stack.json --disable-rollback
aws cloudformation update-stack --stack-name OurTestStack --template-body file://stack.json
