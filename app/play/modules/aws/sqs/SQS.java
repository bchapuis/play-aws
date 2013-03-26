package play.modules.aws.sqs;

import com.amazonaws.services.sqs.AmazonSQSClient;
import play.Application;
import play.Play;

public class SQS {

	public static AmazonSQSClient client() {
        Application app = Play.application();
        if (app == null) {
            throw new RuntimeException("No application running");
        }
        SQSPlugin plugin = app.plugin(SQSPlugin.class);
        AmazonSQSClient client = plugin.client();
        if (client == null) {
            throw new RuntimeException("No AmazonSQSClient configured");
        }
        return client;
    }
	
}
