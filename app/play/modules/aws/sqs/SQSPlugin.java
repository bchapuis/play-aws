package play.modules.aws.sqs;

import play.Application;
import play.Configuration;
import play.Logger;
import play.Plugin;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMapper;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sqs.AmazonSQSClient;

public class SQSPlugin extends Plugin {
	
	private final Application application;

    private AmazonSQSClient client;

    public SQSPlugin(Application application) {
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
                client = new AmazonSQSClient(credentials);
                String endpoint = conf.getString("endpoint");
                if (endpoint != null) {
                    client.setEndpoint(endpoint);
                }
            }
        }
        Logger.info("SQSPlugin has started");
    }

    @Override
    public void onStop() {
        Logger.info("SQSPlugin has stopped");
    }
	
	public AmazonSQSClient client() {
        return client;
    }

}
