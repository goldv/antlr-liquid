package com.goldv

import java.io.StringWriter

import akka.actor.ActorSystem
import com.goldv.context.Context
import com.goldv.io.DefaultTemplateLoader
import com.goldv.visitor.{TemplateRenderer, AjaxRenderer}
import spray.http.{MediaTypes, MediaType}
import spray.httpx.SprayJsonSupport
import spray.routing.SimpleRoutingApp
import spray.json._





/**
 * Created by vince on 02/05/15.
 */

case class Test(key:String, value: String)

object M2eeJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport{

  implicit object AnyJsonFormat extends JsonFormat[Any] {
    def write(x: Any) = x match {
      case n: Int => JsNumber(n)
      case s: String => JsString(s)
      case x: Seq[_] => seqFormat[Any].write(x)
      case m: Map[String, _] => mapFormat[String, Any].write(m.asInstanceOf[Map[String, Any]])
      case b: Boolean if b == true => JsTrue
      case b: Boolean if b == false => JsFalse
      case x => serializationError("Do not understand object of type " + x.getClass.getName)
    }
    def read(value: JsValue) = value match {
      case JsNumber(n) => n.intValue()
      case JsString(s) => s
      case a: JsArray => listFormat[Any].read(value)
      case o: JsObject => mapFormat[String, Any].read(value)
      case JsTrue => true
      case JsFalse => false
      case x => deserializationError("Do not understand how to deserialize " + x)
    }
  }
}

object Main extends App with SimpleRoutingApp{

  implicit val system = ActorSystem("cms-system")

  val items = 2 :: 31 :: 32 :: Nil

  val scope: Map[String, Any] = Map("item" -> "Item List", "array" -> items)


  import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
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
          scope
        }
      }
    } ~
    pathPrefix("js") { get { getFromResourceDirectory("js") } }
  }



}


