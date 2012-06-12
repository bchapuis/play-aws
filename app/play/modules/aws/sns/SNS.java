package play.modules.aws.sns;

import play.Application;
import play.Play;

import com.amazonaws.services.sns.AmazonSNS;

public class SNS {

	public static AmazonSNS client() {
        Application app = Play.application();
        if (app == null) {
            throw new RuntimeException("No application running");
        }
        SNSPlugin plugin = app.plugin(SNSPlugin.class);
        AmazonSNS client = plugin.client();
        if (client == null) {
            throw new RuntimeException("No AmazonSNSClient configured");
        }
        return client;
    }
	
}
