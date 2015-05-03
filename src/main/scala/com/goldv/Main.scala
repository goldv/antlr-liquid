package com.goldv

import java.io.StringWriter

import akka.actor.ActorSystem
import com.goldv.context.Context
import com.goldv.io.DefaultTemplateLoader
import com.goldv.visitor.{TemplateRenderer, AjaxRenderer}
import spray.http.{MediaTypes, MediaType}
import spray.routing.SimpleRoutingApp
import spray.json._





/**
 * Created by vince on 02/05/15.
 */

object M2eeJsonProtocol extends DefaultJsonProtocol {

  implicit object MapJsonFormat extends JsonFormat[Map[String, Any]] { // 1
  def write(m: Map[String, Any]) = {
    JsObject(m.mapValues {                  // 2
      case v: String => JsString(v)         // 3
      case v: Int => JsNumber(v)
      case v: Map[_, _] => write(v.asInstanceOf[Map[String, Any]])  // 4
      case v: Any => JsString(v.toString)   // 5
    })
  }

    def read(value: JsValue) = ???            // 6
  }
}

object Main extends App with SimpleRoutingApp{

  implicit val system = ActorSystem("cms-system")

  val items = 2 :: 31 :: 32 :: Nil

  val scope: Map[String, Any] = Map("item" -> "Item List", "array" -> items)


  import spray.httpx.SprayJsonSupport._
  import M2eeJsonProtocol._

  val loader = new DefaultTemplateLoader("src/main/resources")
  loader.intitialize

  startServer(interface = "localhost", port = 8080) {
    pathSingleSlash {
      get {
        respondWithMediaType(MediaTypes.`text/html`) {
          complete {
            loader.forName("test").map { t =>

              val sw = new StringWriter
              t.render(new TemplateRenderer(new Context(scope), loader, sw))
              sw.toString
            }
          }
        }
      }
    } ~
    path("cms"){
      get {
        respondWithMediaType(MediaTypes.`text/html`) {
          complete {
            loader.forName("test").map { t =>

              val sw = new StringWriter
              t.render(new AjaxRenderer(new Context(scope), loader, sw))
              sw.toString
            }
          }
        }
      }
    } ~
    path("rest"){
      get {
        complete {
          scope.toJson
        }
      }
    }
  }



}


