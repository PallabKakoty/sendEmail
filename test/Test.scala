import akka.actor.ActorSystem
import dao.HelloActor
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.Play

class Test extends PlaySpec with OneAppPerSuite {

  "EmailService" should {
    "Send email to user" in {
//      import play.api.Play.current
//      val path = Play.application.path
//
//      println(path)

      //val currentDirectory = new java.io.File(".").getCanonicalPath
      //println(currentDirectory)

      //println(System.getProperty("user.dir"))

    }
  }

  "ActorHello" should {
    "SayHello" in {
      implicit val helloActor: HelloActor = app.injector.instanceOf[HelloActor]
      implicit val system: ActorSystem = app.injector.instanceOf[ActorSystem]

      val helloActorNew = system.actorOf(HelloActor.props, "hello-actor")
      helloActorNew ! ("Hello")
    }
  }



}
