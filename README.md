play-aws
========

The intent of this module is to integrate Amazon Web Services with Play 2.0.

## Configuration

To use the module in your application you must first clone this repository and run:
    
    play deploy-local
    
You can then use the plugin in your applications by adding the modules to your dependencies.

    val appDependencies = Seq(
      "play-aws" % "play-aws_2.9.1" % "0.1"
    )


## DynamoDB

To use DynamoDB you must add your aws access and secret keys to your application.conf file.

    aws.accesskey="awsAccessKey"
    aws.secretkey="awsSecretKey
    dynamodb.endpoint="awsEndPoint" 

The following example assumes that a table called User already exists in your configuration.

    package models;

    import com.amazonaws.services.dynamodb.datamodeling.DynamoDBAttribute;
    import com.amazonaws.services.dynamodb.datamodeling.DynamoDBHashKey;
    import com.amazonaws.services.dynamodb.datamodeling.DynamoDBTable;
    import play.modules.aws.DynamoDB;
    import play.modules.aws.DynamoDBModel;

    @DynamoDBTable(tableName = "Users")
    public class User extends DynamoDBModel {

        private String email;
        private String password;

        @DynamoDBHashKey(attributeName = "email")
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @DynamoDBAttribute(attributeName = "password")
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public static User authenticate(String email, String password) {
            User user = DynamoDB.mapper().load(User.class, email);
            if (user.getPassword().equals(password)) {
                return user;
            }
            return null;
        }

        public static User create(String name, String email, String password) {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.save();
            return user;
        }

        public static boolean exists(String email) {
            User user = DynamoDB.mapper().load(User.class, email);
            return user != null;
        }
    }



## S3

Comming soon...

## SQS

Comming soon...

## SNS

Comming soon...