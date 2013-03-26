package play.modules.aws.s3;

import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.PipedInputStream;

public class S3UploadWorker extends Thread {

    public PipedInputStream pis;

    public String bucket;

    public String key;

    public ObjectMetadata metadata = new ObjectMetadata();


    public S3UploadWorker(String bucket, String key, ObjectMetadata metadata, PipedInputStream pis) {
        super();
        this.bucket = bucket;
        this.key = key;
        this.metadata = metadata;
        this.pis = pis;
    }

    public void run() {
        try {
            S3.client().putObject(bucket, key, pis, metadata);
            pis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {pis.close();} catch (Exception ex2) {}
        }
    }
}
