package play.modules.aws.s3;

import play.Application;
import play.Configuration;
import play.Logger;
import play.Plugin;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMapper;
import com.amazonaws.services.s3.AmazonS3Client;

public class S3Plugin extends Plugin {
	
	private final Application application;
	
    private AmazonS3Client client;
    
    public S3Plugin(Application application) {
        this.application = application;
    }
    
    @Override
    public void onStart() {
        Configuration aws = Configuration.root().getConfig("aws");
        Configuration s3 = Configuration.root().getConfig("s3");
        if (aws != null && s3 != null) {
            String accesskey = aws.getString("accesskey");
            String secretkey = aws.getString("secretkey");
            String endpoint = s3.getString("endpoint");
            if (accesskey != null && secretkey != null && endpoint != null) {
                AWSCredentials credentials = new BasicAWSCredentials(accesskey, secretkey);
                client = new AmazonS3Client(credentials);
            	client.setEndpoint(endpoint);
            }
        }
        Logger.info("S3Plugin has started");
    }

    @Override
    public void onStop() {
        Logger.info("S3Plugin has stopped");
    }
	
	public AmazonS3Client client() {
        return client;
    }

}
