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
        Configuration conf = Configuration.root().getConfig("aws");
        if (conf != null) {
            String accesskey = conf.getString("accesskey");
            String secretkey = conf.getString("secretkey");
            if (accesskey != null && secretkey != null) {
                AWSCredentials credentials = new BasicAWSCredentials(accesskey, secretkey);
                client = new AmazonS3Client(credentials);
                String endpoint = conf.getString("endpoint");
                if (endpoint != null) {
                    client.setEndpoint(endpoint);
                }
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
