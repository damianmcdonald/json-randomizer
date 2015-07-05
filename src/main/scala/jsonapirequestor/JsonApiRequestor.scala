package jsonapirequestor

import jsonrandomizer.JsonRandomizer
import scala.util.{ Success, Failure }
import akka.actor.{ Props, ActorSystem }
import spray.client._
import spray.http._
import spray.util._
import spray.client.pipelining._
import MediaTypes._
import scala.annotation.tailrec

trait JsonApiRequestor extends JsonRandomizer {

  implicit val system = ActorSystem("json-api-requestor")
  import system.dispatcher
  import system.log

  val MAX_API_REQUESTS = 10

  lazy val responseEvaluator = (res: HttpResponse) => res.status == StatusCodes.OK
  lazy val headers: Map[String, String] = Map("Content-Type" -> "application/json")
  def httpVerb() = Post("http://localhost:8085/v1/trade")

  def fireAndForgetResponse(m: Map[String, Any], iter: Int = MAX_API_REQUESTS): Unit = {
    // generate the Json requests
    val jsonList = getJsonList(m, iter)
    val pipeline = {
      headers.foreach({ case (key, value) => addHeader(key, value) })
      sendReceive
    }
    jsonList foreach (jsonReq => {
      val response = pipeline(httpVerb.withEntity(jsonReq))
      // handle result/failure
      response.onComplete {
        case Success(response) => log.debug("************************ SUCCESS " + response.toString)
        case Failure(e) => log.error("************************ FAILURE " + e)
      }
    })
  }

  def fireAndEvaluateResponse(m: Map[String, Any], iter: Int = MAX_API_REQUESTS, f: HttpResponse => Boolean = responseEvaluator): Unit = {
    // generate the Json requests
    val jsonList = getJsonList(m, iter)
    val pipeline = {
      headers.foreach({ case (key, value) => addHeader(key, value) })
      sendReceive
    }
    jsonList foreach (jsonReq => {
      val response = pipeline(httpVerb.withEntity(jsonReq))
      // handle result/failure
      response.onComplete {
        case Success(response) => {
          log.debug("************************ SUCCESS " + response.toString)
          assert(f(response), "The server returned an invalid response")
        }
        case Failure(e) => log.error("************************ FAILURE " + e)
      }
    })
  }

  private def getJsonList(m: Map[String, Any], iter: Int) = {
    @tailrec
    def inner(xs: List[String], cnt: Int): List[String] = {
      xs match {
        case _ if cnt <= 0 => xs
        case Nil => inner(List(randomizeAndConvertToJson(m)) ::: Nil, cnt - 1)
        case head :: tail => inner(List(randomizeAndConvertToJson(m)) ::: tail, cnt - 1)
      }
    }
    inner(Nil, iter)
  }

}
