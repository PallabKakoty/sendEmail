import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.Play

class Test extends PlaySpec with OneAppPerSuite {

  "EmailService" should {
    "Send email to user" in {
//      import play.api.Play.current
//      val path = Play.application.path
//
//      println(path)

      val currentDirectory = new java.io.File(".").getCanonicalPath
      println(currentDirectory)

      println(System.getProperty("user.dir"))

    }
  }



}
