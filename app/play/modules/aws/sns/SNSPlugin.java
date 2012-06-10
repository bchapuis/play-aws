package play.modules.aws.sns;

import play.Application;
import play.Configuration;
import play.Logger;
import play.Plugin;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;

public class SNSPlugin extends Plugin {
	
	private final Application application;

    private AmazonSNS client;

    public SNSPlugin(Application application) {
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
                client = new AmazonSNSClient(credentials);
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
	
	public AmazonSNS client() {
        return client;
    }

}
