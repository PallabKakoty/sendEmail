package dao

import akka.actor.ActorSystem
import dao.SendMailActor.sendmail
import javax.inject.{Inject, Singleton}
import play.api.libs.mailer.{Email, MailerClient}

@Singleton
class SendMailer @Inject() ( system: ActorSystem, mailerClient: MailerClient) {
  def send(csvemailIds: List[String], emailIds: List[String], ccEmailIds: List[String], bccEmailIds: List[String], subject: String, body: String, sendType: String):String = {

    val mailActor = system.actorOf(SendMailActor.props(mailerClient), "mailer-actor"+ System.nanoTime())

    if (csvemailIds.isEmpty && emailIds.isEmpty) {
      "Email id not found to send mail. Please try again."
    } else {
      val ids = csvemailIds ::: emailIds
      if (sendType=="one") {
        ids.map { id =>
          val mail = Email(subject, "testMail@test.com", Seq(id), Some(body), None, None, ccEmailIds.seq, bccEmailIds.seq)
          mailActor ! sendmail(mail)
        }
        "Sending mail one by one"
      } else if (sendType=="all") {
        mailerClient.send(Email(subject, "testMail@test.com",ids.seq, Some(body), None, None, ccEmailIds.seq, bccEmailIds.seq))
        "Sending mail to all"
      } else {
        "SendType not found. Unable to send mail."
      }
    }
  }
}

