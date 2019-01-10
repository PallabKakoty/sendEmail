package dao

import akka.actor.{Actor, Props}
import play.api.libs.mailer.{Email, MailerClient}
import play.api.Logger

object SendMailActor {
  def props(mailerClient: MailerClient) = Props(classOf[SendMailActor], mailerClient)
  case class sendmail(email: Email)
}

class SendMailActor (mailerClient: MailerClient) extends Actor {
  import SendMailActor._
  def receive = {
    case sendmail(email: Email) => {
      mailerClient.send(email)
      Logger.info("Mailer: "+email)
    }
    case _ => Logger.error("Not Found")
  }
}
