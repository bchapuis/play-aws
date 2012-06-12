package play.modules.aws 

import java.util._
import java.io._
import scala.io._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.iteratee._
import play.api.libs.iteratee.Input._
import play.api.libs.iteratee.Parsing._
import play.modules.aws.s3.S3
import com.amazonaws.services.s3._
import com.amazonaws.services.s3.model._
import scala.collection.immutable.Stream

object S3PartHandler {
  def handleFilePartS3Object: BodyParsers.parse.Multipart.PartHandler[MultipartFormData.FilePart[S3Object]] = {
    BodyParsers.parse.Multipart.handleFilePart {
      case BodyParsers.parse.Multipart.FileInfo(partName, filename, contentType) =>
        val client:AmazonS3Client = S3.client()
        val bucket:String = ""
        val key:String = ""
        val request:InitiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucket, key)
      	val result:InitiateMultipartUploadResult = client.initiateMultipartUpload(request)
        var parts:ArrayList[PartETag] = new ArrayList[PartETag]()
        var counter:Int = 0
        Iteratee.fold[Array[Byte], UploadPartRequest](new UploadPartRequest()) { (req:UploadPartRequest, data) =>
          counter = counter + 1
          req.setBucketName(bucket)
          req.setKey(key)
          req.setPartSize(data.length)
          req.setInputStream(new ByteArrayInputStream(data))
          req.setPartNumber(counter)
          req.setUploadId(result.getUploadId())
          client.uploadPart(req)
          req
        }.mapDone { req =>
          client.completeMultipartUpload(new CompleteMultipartUploadRequest(bucket, key, result.getUploadId(), parts))
          client.getObject(bucket, key)
        }
    }
  }
}

