package play.modules.aws.s3

import java.util._
import java.io._
import scala.io._
import play.api._
import play.api.mvc._

import play.api.libs.json._
import play.api.libs.iteratee._
import play.api.libs.iteratee.Input._
import play.api.libs.iteratee.Parsing._
import com.amazonaws.services.s3._
import com.amazonaws.services.s3.model._
import play.modules.aws.s3.S3
import scala.collection.immutable.Stream
import play.mvc.Http

object S3MultiPartHandler {

  case class RequestBody(
      s3Object: Option[MultipartFormData[S3Object]] = None,
      override val isMaxSizeExceeded: Boolean = false) extends Http.RequestBody {

    def asS3Object = {
      s3Object.orNull
    }

  }

  def java(maxLength: Int): BodyParser[Http.RequestBody] = BodyParsers.parse.maxLength(maxLength, parse).map { body =>
    body
      .left.map(_ => RequestBody(isMaxSizeExceeded = true))
      .right.map { s3Object =>
        RequestBody(s3Object = Some(s3Object))
      }.fold(identity, identity)
  }

  def parse: BodyParser[MultipartFormData[S3Object]] = BodyParsers.parse.multipartFormData(handleFilePartAsS3Object)

  def handleFilePartAsS3Object: BodyParsers.parse.Multipart.PartHandler[MultipartFormData.FilePart[S3Object]] = {
    BodyParsers.parse.Multipart.handleFilePart {
      case BodyParsers.parse.Multipart.FileInfo(partName, filename, contentType) =>
        val client:AmazonS3Client = S3.client()
        val bucket:String = "agimem-tmp"
        val key:String = UUID.randomUUID().toString()
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
          parts.add(client.uploadPart(req).getPartETag())
          req
        }.mapDone { req =>
          client.completeMultipartUpload(new CompleteMultipartUploadRequest(bucket, key, result.getUploadId(), parts))
          client.getObject(bucket, key)
        }
    }
  }
  
}

