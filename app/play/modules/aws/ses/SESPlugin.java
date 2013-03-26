package play.modules.aws.ses;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import play.Application;
import play.Configuration;
import play.Logger;
import play.Plugin;

public class SESPlugin extends Plugin {
	
	private final Application application;

    private AmazonSimpleEmailService client;

    public SESPlugin(Application application) {
        this.application = application;
    }
    
    @Override
    public void onStart() {
        Configuration aws = Configuration.root().getConfig("aws");
        Configuration ses = Configuration.root().getConfig("ses");
        if (aws != null && ses != null) {
            String accesskey = aws.getString("accesskey");
            String secretkey = aws.getString("secretkey");
            String endpoint = ses.getString("endpoint");
            if (accesskey != null && secretkey != null && endpoint != null) {
                AWSCredentials credentials = new BasicAWSCredentials(accesskey, secretkey);
                client = new AmazonSimpleEmailServiceClient(credentials);
                client.setEndpoint(endpoint);
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
