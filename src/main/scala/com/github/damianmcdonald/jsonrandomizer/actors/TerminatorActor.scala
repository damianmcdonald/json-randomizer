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

import akka.actor.{ Actor, ActorLogging, actorRef2Scala }

/**
 * Object defining the messages that are evaluated in
 * [[com.github.damianmcdonald.jsonrandomizer.actors.TerminatorActor]]
 */
object Terminator {
  sealed trait TerminatorMesaage
  case class Terminate(sha1: String) extends TerminatorMesaage
  case class AskTerminate(sha1: String) extends TerminatorMesaage
}

/**
 * Actor that evaluates whether an API request set has completed
 */
class TerminatorActor extends Actor with ActorLogging {
  // import the messages to evaluate
  import Terminator._

  // the map that holds the SHA-1 id of the API request set and a boolean
  // representing whether that request set has completed
  val m = new scala.collection.mutable.HashMap[String, Boolean]

  /**
   * Receives messages and evaluates whether an API request set has completed.
   * An ask message is provided,
   * [[com.github.damianmcdonald.jsonrandomizer.actors.Terminator.AskTerminate]],
   * that can be used to check whether the API request set has completed.
   */
  override def receive: PartialFunction[Any, Unit] = {
    case t: Terminate => {
      log.debug("Terminate message received, updating Map for SHA: " + t.sha1)
      m.getOrElseUpdate(t.sha1, true)
      log.debug("Map value for SHA: " + t.sha1 + " after updates is: " + m.get(t.sha1))
    }
    case t: AskTerminate => {
      log.debug("AskTerminate message recieved for SHA: " + t.sha1)
      if (m.getOrElse(t.sha1, false)) sender ! true else sender ! false
    }
    case whatever => log.error("Not match found for Terminate or AskTerminate, {}", whatever)
  }

}
