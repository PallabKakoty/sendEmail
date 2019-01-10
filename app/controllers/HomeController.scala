package controllers

import javax.inject._
import play.api.Logger
import globals.Configurations
import play.api.mvc._
import java.io.File
import dao.SendMailer
import scala.io.Source

@Singleton
class HomeController @Inject()(configurations: Configurations, sendMailer: SendMailer) extends Controller {

  implicit val logAddress = "controllers.HomeController"
  val csvType = Array("text/csv","application/csv","application/vnd.ms-excel","application/x-filler","text/comma-separated-values")

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index("File Upload"))
  }

  def fileUploadAndSendMail= Action(parse.multipartFormData) { request =>
    val requestBody = request.body
    val emailIds = requestBody.dataParts("email").mkString.replaceAll(" ","").split(",").filter(_.nonEmpty).toList
    val ccemailIds = requestBody.dataParts("ccemail").mkString.replaceAll(" ","").split(",").filter(_.nonEmpty).toList
    val bccmailIds = requestBody.dataParts("bccemail").mkString.replaceAll(" ","").split(",").filter(_.nonEmpty).toList
    val radioBtn = requestBody.dataParts("sendType").toList.map(chk => chk).mkString
    val subject: String = requestBody.dataParts("subject").toList.mkString
    val body: String = requestBody.dataParts("body").toList.mkString

    val csvMailIds: Option[List[String]] = request.body.file("fileUpload").map { uploadedFile =>
      val fileName = uploadedFile.filename
      val contentType = uploadedFile.contentType.get
      println(contentType)
      if (csvType contains contentType) {
        val currentDirectory = new java.io.File(".").getCanonicalPath + "/public/uploadedFile/"
        val newPath = uploadedFile.ref.moveTo(new File(currentDirectory + uploadedFile.filename))
        val fileData = Source.fromFile(newPath.getAbsolutePath).getLines.drop(1).toList
        fileData.map ( mailIds => mailIds.split(",")(0))
      } else List.empty
    }
    val res = sendMailer.send(csvMailIds.getOrElse(List.empty), emailIds, ccemailIds, bccmailIds, subject, body, radioBtn)
    Ok("<center><div style='margin-top:50px;'>Status -> "+res+"</div></center>").as("text/html")
  }


}
