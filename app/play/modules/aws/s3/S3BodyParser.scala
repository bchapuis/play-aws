/*
import java.util._
import java.io._
import scala.io._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.iteratee._
import play.api.libs.iteratee.Input._
import play.api.libs.iteratee.Parsing._
import play.modules.aws.S3
import com.amazonaws.services.s3._
import com.amazonaws.services.s3.model._
import scala.collection.immutable.Stream

package play.modules.aws {
  
	abstract class S3PartHandler(bucketName:String, objectKey:String, objectMetadata:ObjectMetadata) extends BodyParsers.parse.Multipart.PartHandler[MultipartFormData.FilePart[UploadPartRequest]] {
	  val s3:S3 = new S3()
	  val client:AmazonS3Client = s3.client()
	  val request:InitiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucketName, objectKey)
	  val result:InitiateMultipartUploadResult = client.initiateMultipartUpload(request)
	  var parts:ArrayList[PartETag] = new ArrayList[PartETag]()
	  var counter:Int = 0
	  def this() {
		  this("test", "test", new ObjectMetadata());
	  }
	  BodyParsers.parse.Multipart.handleFilePart {
	      case BodyParsers.parse.Multipart.FileInfo(partName, filename, contentType) =>
	        Iteratee.fold[Array[Byte], UploadPartRequest](new UploadPartRequest()) { (req:UploadPartRequest, data) =>
	          counter = counter + 1
	          req.setBucketName(bucketName)
	          req.setKey(objectKey)
	          req.setPartSize(data.length)
	          req.setInputStream(new ByteArrayInputStream(data))
	          req.setPartNumber(counter)
	          req.setUploadId(result.getUploadId())
	          client.uploadPart(req)
	          req
	        }.mapDone { os =>
	          val compRequest = new CompleteMultipartUploadRequest(bucketName, objectKey, result.getUploadId(), parts);
	          client.completeMultipartUpload(compRequest)
	        }
	    }
	}
  
	object S3BodyParser {
	  def handleFilePartS3Object: BodyParsers.parse.Multipart.PartHandler[MultipartFormData.FilePart[UploadPartRequest]] = new S3PartHandler()
	}
}
*/
