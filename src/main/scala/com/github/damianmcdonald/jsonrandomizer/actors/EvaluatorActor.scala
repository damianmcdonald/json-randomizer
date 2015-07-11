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

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorSelection.toScala

/**
 * Object defining the messages that are evaluated in
 * [[com.github.damianmcdonald.jsonrandomizer.actors.EvaluatorActor]]
 */
object Evaluator {
  sealed trait EvaluatorMesaage
  case class Success(sha1: String, expected: Int) extends EvaluatorMesaage
  case class Failure(sha1: String, expected: Int) extends EvaluatorMesaage
}

/**
 * Actor that tracks the request completions and failures in an API request set
 */
class EvaluatorActor extends Actor with ActorLogging {
  // import the messages to evaluate
  import Evaluator._

  // the map that holds the SHA-1 id of the API request set
  // and the number of completions for that set
  val m = new scala.collection.mutable.HashMap[String, Int]

  /**
   * Receives messages and tracks the request completions and failures in an
   * API request set. On API request set completion, the actor sends a Terminate
   * message to [[com.github.damianmcdonald.jsonrandomizer.actors.TerminatorActor]]
   */
  override def receive: PartialFunction[Any, Unit] = {
    case s: Success => {
      val cnt = m.getOrElseUpdate(s.sha1, 1)
      if (cnt + 1 == s.expected) {
        val total = cnt + 1
        log.debug("Expected completions are finished, {} of {}", total, s.expected)
        context.actorSelection("/user/terminator") ! Terminator.Terminate(s.sha1)
      } else {
        val updatedCnt = cnt + 1
        m.update(s.sha1, updatedCnt)
        log.debug("Incremented count. Count = {} of {}", updatedCnt, s.expected)
      }
    }
    case f: Failure => {
      log.error("!!!Failure!!! in: " + f.sha1)
      val cnt = m.getOrElseUpdate(f.sha1, 1)
      if (cnt + 1 == f.expected) {
        val total = cnt + 1
        log.debug("Expected completions are finished, {} of {}", total, f.expected)
        context.actorSelection("/user/terminator") ! Terminator.Terminate(f.sha1)
      } else {
        val updatedCnt = cnt + 1
        m.update(f.sha1, updatedCnt)
        log.debug("Incremented count. Count = {} of {}", updatedCnt, f.expected)
      }
    }
    case whatever => log.error("Not match found for Success or Failure")
  }

}
