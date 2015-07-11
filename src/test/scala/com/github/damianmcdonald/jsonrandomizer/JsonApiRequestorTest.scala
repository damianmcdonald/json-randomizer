/*
 * Copyright 2015 Damian McDonald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.damianmcdonald.jsonrandomizer.actors

import scala.concurrent.Await
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSpec
import com.github.damianmcdonald.jsonrandomizer.JsonApiRequestor
import com.github.damianmcdonald.jsonrandomizer.JsonData
import com.github.damianmcdonald.jsonrandomizer.JsonDouble
import com.github.damianmcdonald.jsonrandomizer.JsonString
import akka.pattern.ask
import akka.util.Timeout
import spray.client.pipelining.Post
import spray.http.HttpResponse
import spray.http.StatusCodes
import akka.actor.PoisonPill
import com.github.damianmcdonald.jsonrandomizer.TestValues._

class JsonApiRequestorTest extends FunSpec with BeforeAndAfter with JsonApiRequestor with JsonData {
  // SLF4JLogging
  import system.log

  // the SHA-1 value of the API request set being tested
  var test1Sha: String = _

  after {
    import scala.concurrent.duration._
    // sleep this thread to give the actor system, started by JsonApiRequestor, a chance to fire up
    Thread.sleep(TIMEOUT)

    // grab a reference to the TerminatorActor
    val terminator = system.actorSelection("/user/terminator")

    // define an implicit Timeout value, required by the ask pattern Future
    implicit val timeout = Timeout(5 seconds)

    /**
     * Checks if the API request set has completed.
     * If completed: shut down the ActorSystem
     * if not completed: sleep the Thread before asking the TerminatorActor again.
     *
     * @param b true means the API set has completed, false means that the API set has not completed
     * @param sha1 the SHA-1 hash that represents the API request set
     * @return Unit
     */
    def checkTerminate(b: Boolean, sha1: String): Unit = {
      b match {
        case true => {
          log.debug("ActorSystem can be shutdown")
          system.actorSelection("/user/*") ! PoisonPill
          system.shutdown
        }
        case false => {
          log.debug("ActorSystem can not be shutdown")
          Thread.sleep(TIMEOUT) // sleep the thread before asking again
          val future = terminator ? Terminator.AskTerminate(sha1)
          val result = Await.result(future, 5 seconds).asInstanceOf[Boolean]
          log.debug("Terimator.AskTerminate response is: " + result)
          checkTerminate(result, sha1)
        }
      }
    }
    // call checkTerminate with initial values
    // checkTerminate(false, test1Sha)  // uncomment to active ActorSystem shutdown hook
  }

  describe("A simple URL get request") {
    it("should return a valid 200 OK http response") {

      val f1 = (max: Int) => {
        import scala.util.Random
        val xs = List("EUR", "AUD", "ARS", "BRL", "CNY", "GBP", "HKD", "INR", "KWD", "NOK")
        val i = Random.nextInt((xs.length - ONE - ZERO) + ONE) + ZERO
        xs(i)
      }

      val f2 = (max: Int) => {
        import scala.util.Random
        val xs = List("USD", "NZD", "CAD", "RUB", "SGD", "ZAR", "CHF", "TRY", "AED", "MXN")
        val i = Random.nextInt((xs.length - ONE - ZERO) + ONE) + ZERO
        xs(i)
      }

      val f3 = (max: Int) => {
        import scala.util.Random
        val xs = List("US", "NZ", "CA", "RU", "SG", "ZA", "CH", "TR", "AE", "MX",
          "ES", "AU", "AR", "BR", "CN", "HKD", "IN", "KW", "NO")
        val i = Random.nextInt((xs.length - ONE - ZERO) + ONE) + ZERO
        xs(i)
      }

      val f4 = (max: Int) => {
        import scala.util.Random
        val xs = List(
          "24-JAN-15 10:27:44",
          "12-FEB-15 18:55:44",
          "13-MAR-15 08:12:44",
          "05-APR-15 09:27:44",
          "15-MAY-15 11:27:44",
          "18-JUN-15 12:27:44",
          "27-JUL-15 13:27:44",
          "22-AUG-15 14:27:44",
          "30-SEP-15 15:27:44",
          "08-OCT-15 16:27:44",
          "09-NOV-15 17:27:44",
          "10-DEC-15 19:27:44"
        )
        val i = Random.nextInt((xs.length - ONE - ZERO) + ONE) + ZERO
        xs(i)
      }

      val m = Map(
        "userId" -> JsonString(STRING_LENGTH),
        "currencyFrom" -> JsonString(3, f1),
        "currencyTo" -> JsonString(3, f2),
        "amountSell" -> JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP),
        "amountBuy" -> JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP),
        "rate" -> JsonDouble(0, 1, 4),
        "timePlaced" -> JsonString(18, f4),
        "originatingCountry" -> JsonString(2, f3)
      )

      /* Uncomment to test a web service. Don't forget to update the API_ROUTE value to match the api you wish to test
      test1Sha = fireAndEvaluateResponse(Post(API_ROUTE), m, REQUESTS, (res: HttpResponse) => {
        val isCodeValid = res.status == StatusCodes.OK
        val isResponseValid: Boolean = {
          OBJECT_ID_REGEX.r findFirstIn res.entity.asString match {
            case Some(_) => true
            case None => false
          }
        }
        if (isCodeValid && isResponseValid) true else false
      })
      */
    }
  }

}
