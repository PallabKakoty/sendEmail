package dao

import javax.inject.Singleton

@Singleton
class SendMail {

  def send(csvemailIds: List[String], emailIds: List[String], ccEmailIds: List[String], bccEmailIds: List[String], subject: String, body: String) {
    if (csvemailIds.isEmpty && emailIds.isEmpty) {
      "Email id not found to send mail. Please try again."
    } else {
      val ids = csvemailIds ::: emailIds
    }

  }


}

//sendMail.send(csvMailIds, emailIds, ccemailIds, bccmailIds)

/*if (csvType contains contentType) {
  val currentDirectory = new java.io.File(".").getCanonicalPath + "/public/uploadedFile/"
  val newPath = uploadedFile.ref.moveTo(new File(currentDirectory + uploadedFile.filename))
  println(newPath)
  val fileData = Source.fromFile(newPath.getAbsolutePath).getLines.drop(1).toList
  println(fileData.length)
  val csvMailIds = fileData.map { mailIds =>
    mailIds.split(",")(10)
  }
  sendMail.send(csvMailIds, emailIds, ccemailIds, bccmailIds)
  //println(newPath.getAbsolutePath)
} else {
  sendMail.send(List.empty, emailIds, ccemailIds, bccmailIds)
}*/