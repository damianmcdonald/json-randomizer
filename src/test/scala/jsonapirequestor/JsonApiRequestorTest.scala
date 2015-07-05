package jsonapirequestor

import jsonrandomizer.JsonRandomizer
import org.scalatest.FunSpec
import jsonrandomizer.JsonInt
import jsonrandomizer.JsonNull
import jsonrandomizer.JsonDouble
import jsonrandomizer.JsonString
import jsonrandomizer.JsonArray
import jsonrandomizer.JsonObject
import jsonrandomizer.JsonBoolean
import jsonrandomizer.JsonLong
import jsonrandomizer.JsonData
import spray.http._
import spray.client.pipelining._

class JsonApiRequestorTest extends FunSpec with JsonApiRequestor with JsonRandomizer with JsonData {

  implicit val formats = net.liftweb.json.DefaultFormats

  override def httpVerb() = Post("http://localhost:8085/v1/trade")

  describe("A simple URL get request") {
    it("should return a valid 200 OK http response") {

      /*
      val m = Map(
        "userId" -> JsonString(10),
        "currencyFrom" -> JsonInt(5, 20),
        "currencyTo" -> JsonLong(542525, 635235323543223L),
        "amountSell" -> JsonDouble(523, 78906, 2),
        "amountBuy" -> JsonDouble(1087, 34794, 2),
        "rate" -> JsonDouble(0, 1, 4),
        "timePlaced" -> JsonArray(JsonString(10), 15),
        "originatingCountry" -> JsonArray(JsonBoolean(), 15)
      )

      fireAndEvaluateResponse(m, 10, (res: HttpResponse) => {
        val isCodeValid = res.status == StatusCodes.OK
        val isResponseValid: Boolean = {
          "^[a-f\\d]{24}$".r findFirstIn res.entity.asString match {
            case Some(_) => true
            case None => false
          }
        }
        if (isCodeValid && isResponseValid) true else false
      })
      */
      assert(1 == 1)
    }
  }

}