package play.modules.aws.s3;

import java.io.File;
import java.util.Date;

import play.Application;
import play.Play;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.VersionListing;

public class S3 {

	private static S3Plugin plugin;
	
	public static S3Plugin plugin() {
		if (plugin == null) {
	        Application app = Play.application();
	        if (app == null) {
	            throw new RuntimeException("No application running");
	        }
	        plugin = app.plugin(S3Plugin.class);
		}
		return plugin;
	}
	
	public static AmazonS3Client client() {
        AmazonS3Client client = plugin().client();
        if (client == null) {
            throw new RuntimeException("No AmazonS3Client configured");
        }
        return client;
    }
	
	public static String key() {
		return null;
	}
	
	public static void get(String bucket, String key) {
		client().getObject(bucket, key);
	}
	
	public static void put(File file, String bucket, String key) {
		client().putObject(new PutObjectRequest(bucket, key, file));
	}
	
	public static void delete(String bucket, String key) {
		client().deleteObject(bucket, key);
	}
	
	public static void copy(String sourceBucket, String sourceKey, String destinationBucket, String destinationKey) {
		client().copyObject(sourceBucket, sourceKey, destinationBucket, destinationKey);
	}
	
	public static VersionListing getVersions(String bucket, String prefix) {
		return client().listVersions(bucket, prefix);
	}
	
	public static void deleteVersion(String bucket, String key, String versionId) {
		client().deleteVersion(bucket, key, versionId);
	}
	
	public static void setAcl(String bucket, String key, AccessControlList acl) {
		client().setObjectAcl(bucket, key, acl);
	}
	
	public static void setAcl(String bucket, String key, CannedAccessControlList acl) {
		client().setObjectAcl(bucket, key, acl);
	}
	
	public static void generatePresignedUrl(String bucket, String key, Date expiration) {
		client().generatePresignedUrl(bucket, key, expiration);
	}
	
	public static void generatePresignedUrl(String bucket, String key, Date expiration, HttpMethod method) {
		client().generatePresignedUrl(bucket, key, expiration, method);
	}
	
}
