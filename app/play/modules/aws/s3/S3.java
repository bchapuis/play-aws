package play.modules.aws.s3;

import play.Application;
import play.Play;

import com.amazonaws.services.s3.AmazonS3Client;

public class S3 {

	public AmazonS3Client client() {
        Application app = Play.application();
        if (app == null) {
            throw new RuntimeException("No application running");
        }
        S3Plugin plugin = app.plugin(S3Plugin.class);
        AmazonS3Client client = plugin.client();
        if (client == null) {
            throw new RuntimeException("No AmazonS3Client configured");
        }
        return client;
    }
	
}
