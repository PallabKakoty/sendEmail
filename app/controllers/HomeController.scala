package controllers

import dao.{SendMail, UserDao}
import javax.inject._
import play.api.Logger
import globals.Configurations
import play.api.mvc._
import java.io.File

import utils.Tools

import scala.io.Source

@Singleton
class HomeController @Inject()(configurations: Configurations, userDao: UserDao, tools: Tools, sendMail: SendMail) extends Controller {

  implicit val logAddress = "controllers.HomeController"

  def index() = Action { implicit request: Request[AnyContent] =>
    Logger.debug("Hello")
    Ok(views.html.index("File Upload"))
  }

  def validateUser(verifyToken: String) = Action {
    if (verifyToken != null) {
      println("+++++++=> "+verifyToken)

      //val email = Email("Test", "pallabkakoty@gmail.com",Seq("pallabkakoty407@gmail.com"))
      //mailerClient.send(email)

      Ok("")
    } else NotFound("invalid verification token. please try again later")
  }

  val csvType = Array("text/csv","application/csv","application/vnd.ms-excel","application/x-filler","text/comma-separated-values")

  def uploadFile = Action(parse.multipartFormData) { request =>

    val requestBody = request.body
    println(requestBody.dataParts("name"))


    request.body.file("fileUpload").map { uploadedFile =>
      val fileName = uploadedFile.filename
      val contentType = uploadedFile.contentType.get
      if (contentType contains csvType) {
        val currentDirectory = new java.io.File(".").getCanonicalPath + "/public/uploadedFile/"
        //val newPath = uploadedFile.ref.moveTo(new File(currentDirectory + uploadedFile.filename))
        //println(newPath.getAbsolutePath)
      } else (NotFound("File tipe doesn't match. Please upload CSV file only."))
      println(fileName)
      println(uploadedFile.contentType.get)
    }.getOrElse {
      //Redirect(routes.Application.index)
    }

    Ok("File has been uploaded")
  }


  def fileUploadAndSendMail= Action(parse.multipartFormData) { request =>
    val requestBody = request.body
    val emailIds = requestBody.dataParts("email").toList.map ( mail => mail.replace(" ",""))
    println(emailIds)
    val ccemailIds = requestBody.dataParts("ccemail").toList.map(mail => mail.replace(" ",""))
    println(ccemailIds)
    val bccmailIds = requestBody.dataParts("bccemail").toList.map(mail => mail.replace(" ", ""))
    println(bccmailIds)

    val subject: String = requestBody.dataParts("subject").toList.mkString
    println("Sub "+subject)
    val body: String = requestBody.dataParts("body").toList.mkString
    println("Body: "+body)

    val ids: Option[List[String]] = request.body.file("fileUpload").map { uploadedFile =>
      val fileName = uploadedFile.filename
      val contentType = uploadedFile.contentType.get
      val csvMails: List[String] = if (csvType contains contentType) {
        val currentDirectory = new java.io.File(".").getCanonicalPath + "/public/uploadedFile/"
        val newPath = uploadedFile.ref.moveTo(new File(currentDirectory + uploadedFile.filename))
        println(newPath)
        val fileData = Source.fromFile(newPath.getAbsolutePath).getLines.drop(1).toList
        println(fileData.length)
        val csvMailIds: List[String] = fileData.map ( mailIds => mailIds.split(",")(10))
        csvMailIds
      } else List.empty
      csvMails
    }
    val res = sendMail.send(ids.getOrElse(List.empty), emailIds, ccemailIds, bccmailIds, subject, body)
    println("ifds: "+res)
    Ok("")
  }


}
