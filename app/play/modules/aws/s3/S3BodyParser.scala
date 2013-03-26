package play.modules.aws.s3

import play.api.mvc.BodyParser
import play.api.libs.iteratee.Iteratee
import com.amazonaws.services.s3.model._
import java.io.{PipedOutputStream, PipedInputStream, OutputStream}
import com.amazonaws.services.s3.transfer.Upload
import concurrent.{Future, Promise}

object S3BodyParser {

  def transfer(to: OutputStream): BodyParser[OutputStream] = BodyParser("transfer") { request =>
    Iteratee.fold[Array[Byte], OutputStream](to) { (os, data) =>
      os.write(data)
      os
    }.mapDone { os =>
      os.close()
      Right(to)
    }
  }

  def proxy(bucket: String, key: String): BodyParser[Future[S3Object]] = BodyParser("proxy") { request =>
    lazy val transferManager = S3.transferManager();

    val metadata = new ObjectMetadata
    val output = new PipedOutputStream
    val input = new PipedInputStream(output)

    val p = scala.concurrent.promise[S3Object]
    val f = p.future
    transferManager.upload(bucket, key, input, metadata).addProgressListener(new ProgressListener {
      def progressChanged(event: ProgressEvent) {
        if (event.getEventCode == ProgressEvent.COMPLETED_EVENT_CODE) {
          p.success(S3.client().getObject(bucket, key))
        }
      }
    })

    transfer(output)(request).mapDone(_ => {
      Right(f)
    })
  }

}
