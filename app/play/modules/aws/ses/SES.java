package play.modules.aws.ses;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import play.Application;
import play.Play;

public class SES {

	public static AmazonSimpleEmailService client() {
        Application app = Play.application();
        if (app == null) {
            throw new RuntimeException("No application running");
        }
        SESPlugin plugin = app.plugin(SESPlugin.class);
        AmazonSimpleEmailService client = plugin.client();
        if (client == null) {
            throw new RuntimeException("No AmazonSNSClient configured");
        }
        return client;
    }
	
}
