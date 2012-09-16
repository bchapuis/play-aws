package play.modules.aws.ses;

import java.util.List;

import play.Application;
import play.Configuration;
import play.Logger;
import play.Plugin;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.VerifyEmailAddressRequest;

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
            List<String> senders = ses.getStringList("senders");
            if (accesskey != null && secretkey != null && endpoint != null) {
                AWSCredentials credentials = new BasicAWSCredentials(accesskey, secretkey);
                client = new AmazonSimpleEmailServiceClient(credentials);
                client.setEndpoint(endpoint);
                for (String sender : senders) {
                	client.verifyEmailAddress(new VerifyEmailAddressRequest().withEmailAddress(sender));
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
