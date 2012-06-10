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
        Configuration conf = Configuration.root().getConfig("aws");
        if (conf != null) {
            String accesskey = conf.getString("accesskey");
            String secretkey = conf.getString("secretkey");
            if (accesskey != null && secretkey != null) {
                AWSCredentials credentials = new BasicAWSCredentials(accesskey, secretkey);
                client = new AmazonSimpleEmailServiceClient(credentials);
                String endpoint = conf.getString("endpoint");
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
