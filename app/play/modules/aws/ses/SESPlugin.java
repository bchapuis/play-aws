package play.modules.aws.ses;

import play.Application;
import play.Configuration;
import play.Logger;
import play.Plugin;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.sns.AmazonSNSClient;

public class SESPlugin extends Plugin {
	
	private final Application application;

    private AmazonSimpleEmailService client;

    public SESPlugin(Application application) {
        this.application = application;
    }
    
    @Override
    public void onStart() {
        Configuration aws = Configuration.root().getConfig("aws");
        if (aws != null) {
            String accesskey = aws.getString("accesskey");
            String secretkey = aws.getString("secretkey");
            if (accesskey != null && secretkey != null) {
                AWSCredentials credentials = new BasicAWSCredentials(accesskey, secretkey);
                client = new AmazonSimpleEmailServiceClient(credentials);
                String endpoint = aws.getString("endpoint");
                if (endpoint != null) {
                    client.setEndpoint(endpoint);
                }
            }
        }
        Logger.info("SNSPlugin has started");
    }

    @Override
    public void onStop() {
        Logger.info("SNSPlugin has stopped");
    }
	
	public AmazonSimpleEmailService client() {
        return client;
    }

}
