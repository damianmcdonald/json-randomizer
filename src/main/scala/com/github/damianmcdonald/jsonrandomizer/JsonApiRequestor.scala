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

package com.github.damianmcdonald.jsonrandomizer

import akka.actor.{ ActorSystem, Props }
import com.github.damianmcdonald.jsonrandomizer.actors._
import spray.client.pipelining._
import spray.http.MediaTypes._
import spray.http._

import scala.util.{ Failure, Success }

/**
 * Mixin that provides the ability to send multiple, simultaneous API requests
 * using structured Json with randomly generated values.
 */
trait JsonApiRequestor extends JsonRandomizer {

  // execution context for Futures
  import system.dispatcher

  // SLF4JLogging
  import system.log

  // spin up and name actor system
  implicit val system = ActorSystem("json-api-requestor")

  // spin up evaluator actor
  private val evaluatorActor = system.actorOf(Props[EvaluatorActor], name = "evaluator")

  // spin up terminator actor
  private val terminatorActor = system.actorOf(Props[TerminatorActor], name = "terminator")

  // default number of api requests to make
  private val MAX_API_REQUESTS = 10

  /**
   * Default function that will be executed to evaluate the API response
   *
   * @param res the HttpResponse to evaluate
   * @return Boolean true if the evaluation passed, false if the evaluation failed
   */
  lazy val responseEvaluator = (res: HttpResponse) => res.status == StatusCodes.OK

  /**
   * Default content type: application/json
   * Override this value if a different content type is required
   */
  lazy val contentType = `application/json`

  /**
   * Generates a SHA-1 hash based on an internally generated random alphanumeric String
   */
  private def generateSHA1() = {
    import scala.util.Random
    val LENGTH = 100
    val s = Random.alphanumeric take LENGTH mkString ("")
    val md = java.security.MessageDigest.getInstance("SHA-1")
    md.digest(s.getBytes("UTF-8")).map("%02x".format(_)).mkString
  }

  /**
   * Generates random values for structured Json and sends the entity to an external API.
   *
   * ==Example Usage==
   *
   * val httpVerb = Post("http://localhost/apiroute")   // defines the http method and API route
   * val m = Map(                                       // defines the Json structure to randomize
   *  "item_id" -> JsonString(10),
   *  "in_stock" -> JsonBoolean(),
   *  "price" -> JsonDouble(100, 1000, 2),
   *  "quantity" -> JsonInt(5, 25)
   * )
   * fireAndForgetResponse(httpVerb, m)        // call the method using the default number of requests
   * fireAndForgetResponse(httpVerb, m, 25)    // call the method overriding the number of requests
   *
   * @param httpVerb the http verb (POST, PUT, DELETE) that defines the method and route to the API
   * @param m the Json structure to be randomized
   * @param max optional parameter to define how many API requests to send, default 10
   * @return String the SHA-1 value that represents this set of API requests
   */
  def fireAndForgetResponse(
    httpVerb: HttpRequest,
    m: Map[String, Any],
    max: Int = MAX_API_REQUESTS
  ): String = {
    val sha1 = generateSHA1
    // generate the Json requests and add to parallel collection
    val jsonList = (1 to max map (n => randomizeAndConvertToJson(m))).par
    val pipeline = sendReceive
    jsonList foreach (jsonReq => {
      val response = pipeline(httpVerb.withEntity(HttpEntity(contentType, jsonReq)))
      // handle result/failure
      response.onComplete {
        case Success(response) => {
          log.debug("Request completed with response: " + response.toString)
          evaluatorActor ! Evaluator.Success(sha1, max)
        }
        case Failure(e) => {
          log.error("Request failed: " + e)
          evaluatorActor ! Evaluator.Failure(sha1, max)
        }
      }
    })
    sha1
  }

  /**
   * Generates random values for structured Json and sends the entity to an external API,
   * evaluating the API response.
   *
   * ==Example Usage==
   *
   * val httpVerb = Post("http://localhost/apiroute")   // defines the http method and API route
   * val m = Map(                                       // defines the Json structure to randomize
   *  "item_id" -> JsonString(10),
   *  "in_stock" -> JsonBoolean(),
   *  "price" -> JsonDouble(100, 1000, 2),
   *  "quantity" -> JsonInt(5, 25)
   * )
   * val f = (res: HttpResponse) => {
   *   val isCodeValid = res.status == StatusCodes.OK
   *   val isResponseValid: Boolean = {
   *     OBJECT_ID_REGEX.r findFirstIn res.entity.asString match {
   *       case Some(_) => true
   *       case None => false
   *     }
   *   }
   *   if (isCodeValid && isResponseValid) true else false
   * }
   * fireAndEvaluateResponse(httpVerb, m)         // call the method using the default number of requests
   *
   * // call the method overriding the number of requests and overriding the function to evaluate responses
   * fireAndEvaluateResponse(httpVerb, m, 25, f)
   *
   * @param httpVerb the http verb (POST, PUT, DELETE) that defines the method and route to the API
   * @param m the Json structure to be randomized
   * @param max optional parameter to define how many API requests to send, default 10
   * @param f optional parameter that defines the function that will be used to evaluate the API response
   * @return String the SHA-1 value that represents this set of API requests
   */
  def fireAndEvaluateResponse(
    httpVerb: HttpRequest,
    m: Map[String, Any],
    max: Int = MAX_API_REQUESTS,
    f: HttpResponse => Boolean = responseEvaluator
  ): String = {
    val sha1 = generateSHA1
    // generate the Json requests and add to parallel collection
    val jsonList = (1 to max map (n => randomizeAndConvertToJson(m))).par
    val pipeline = sendReceive
    jsonList foreach (jsonReq => {
      val response = pipeline(httpVerb.withEntity(HttpEntity(contentType, jsonReq)))
      // handle result/failure
      response.onComplete {
        case Success(response) => {
          log.debug("Request completed with response: " + response.toString)
          assert(f(response), "The server returned an invalid response")
          evaluatorActor ! Evaluator.Success(sha1, max)
        }
        case Failure(e) => {
          log.error("Request failed: " + e)
          evaluatorActor ! Evaluator.Failure(sha1, max)
        }
      }
    })
    sha1
  }

}
