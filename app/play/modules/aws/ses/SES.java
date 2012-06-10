package play.modules.aws.ses;

import play.Application;
import play.Play;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;

public class SES {

	public AmazonSimpleEmailService client() {
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
