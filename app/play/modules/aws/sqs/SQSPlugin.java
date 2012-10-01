package play.modules.aws.sqs;

import java.util.List;

import play.Application;
import play.Configuration;
import play.Logger;
import play.Plugin;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;

public class SQSPlugin extends Plugin {
	
	private final Application application;

    private AmazonSQSClient client;

    public SQSPlugin(Application application) {
        this.application = application;
    }
    
    @Override
    public void onStart() {
        Configuration aws = Configuration.root().getConfig("aws");
        Configuration sqs = Configuration.root().getConfig("sqs");
        if (aws != null && sqs != null) {
            String accesskey = aws.getString("accesskey");
            String secretkey = aws.getString("secretkey");
            String endpoint = sqs.getString("endpoint");
            if (accesskey != null && secretkey != null) {
                AWSCredentials credentials = new BasicAWSCredentials(accesskey, secretkey);
                client = new AmazonSQSClient(credentials);
	            client.setEndpoint(endpoint);
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
