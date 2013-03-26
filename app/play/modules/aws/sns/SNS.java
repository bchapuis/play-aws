package play.modules.aws.sns;

import com.amazonaws.services.sns.AmazonSNSClient;
import play.Application;
import play.Play;

public class SNS {

	public static AmazonSNSClient client() {
        Application app = Play.application();
        if (app == null) {
            throw new RuntimeException("No application running");
        }
        SNSPlugin plugin = app.plugin(SNSPlugin.class);
        AmazonSNSClient client = plugin.client();
        if (client == null) {
            throw new RuntimeException("No AmazonSNSClient configured");
        }
        return client;
    }
	
}
