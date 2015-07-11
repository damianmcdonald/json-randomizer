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

import scala.util.Random
import scala.annotation.tailrec

/** Generic Json data type */
sealed trait JsonDataType

/** Non string Json data type such as boolean, number, null */
sealed trait JsonNonStringType

/**
 * Json String data type
 *
 * ==Default value generation strategy==
 *
 * If a custom String generation function is not provided the default
 * function will be utilized which will produce a random String with a
 * length equal to the max parameter.
 *
 * @constructor creates a random Json String
 * @param max the length of the String to create
 * @param f optional function that can be provided to produce custom Json Strings
 */
case class JsonString(
  max: Int,
  f: (Int) => String = (max: Int) => {
    val x = Random.alphanumeric
    x take max mkString ("")
  }
) extends JsonDataType

/**
 * Json Boolean data type
 *
 * ==Default value generation strategy==
 *
 * If a custom Boolean generation function is not provided the default
 * function will be utilized which will produce a random Boolean.
 *
 * @constructor creates a random Json Boolean
 * @param f optional function that can be provided to produce custom Json Booleans
 */
case class JsonBoolean(f: () => Boolean = Random.nextBoolean) extends JsonDataType with JsonNonStringType

/**
 * Json Int data type
 *
 * ==Default value generation strategy==
 *
 * If a custom Int generation function is not provided the default
 * function will be utilized which will produce a random Int within a
 * given range specified by the min and max parameters.
 *
 * @constructor creates a random Json Int
 * @param min the start of the range
 * @param max the end of the range
 * @param f optional function that can be provided to produce custom Json Ints
 */
case class JsonInt(
  min: Int,
  max: Int,
  f: (Int, Int) => Int = (x: Int, y: Int) => {
    Random.nextInt((y - x) + 1) + x
  }
) extends JsonDataType with JsonNonStringType

/**
 * Json Long data type
 *
 * If a custom Long generation function is not provided the default
 * function will be utilized which will produce a random Long within a
 * given range specified by the min and max parameters.
 *
 * @constructor creates a random Json Long
 * @param min the start of the range
 * @param max the end of the range
 * @param f optional function that can be provided to produce custom Json Longs
 */
case class JsonLong(
  min: Long,
  max: Long,
  f: (Long, Long) => Long = (x: Long, y: Long) => {
    (x + (Random.nextDouble() * (y - x))).toLong
  }
) extends JsonDataType with JsonNonStringType

/**
 * Json Double data type
 *
 * If a custom Double generation function is not provided the default
 * function will be utilized which will produce a random Double within a
 * given range specified by the min and max parameters. The Double value
 * will be set to the number of decimal places defined in the dp parameter.
 *
 * @constructor creates a random Json Double
 * @param min the start of the range
 * @param max the end of the range
 * @param dp the number of decimal places
 * @param f optional function that can be provided to produce custom Json Doubles
 */
case class JsonDouble(
  min: Int,
  max: Int,
  dp: Int,
  f: (Int, Int, Int) => Double = (x: Int, y: Int, z: Int) => {
    val double = (x + (y - x)) * Random.nextDouble
    BigDecimal(double).setScale(z, BigDecimal.RoundingMode.CEILING).toDouble
  }
) extends JsonDataType with JsonNonStringType

/**
 * Json Array data type
 *
 * @constructor creates a random Json Array
 * @param typeof the JsonDataType to create the array with
 * @param length the size of the array to create
 */
case class JsonArray(typeof: JsonDataType, length: Int) extends JsonDataType

/**
 * Json Object data type
 *
 * @constructor creates a random Json Object
 * @param idToValue the Map representing the structure of the custom Json object
 */
case class JsonObject(idToValue: Map[String, JsonDataType]) extends JsonDataType

/**
 * Json Null data type
 *
 * @constructor creates a random Json Null
 */
case class JsonNull() extends JsonDataType with JsonNonStringType

/**
 * Json ObjectList data type
 *
 * This is an internal implementation that does not represent an actual Json data type
 * @constructor creates a ObjectList
 * @param list the List of JsonObject
 */
private final case class JsonObjectList(list: List[Any]) extends JsonDataType

/** Marker class used in pattern matching to represent a Json null value */
private final class Null

/**
 * Represents Json control characters
 */
object JsonChar {
  val BRACKET_OPEN = "["
  val BRACKET_CLOSE = "]"
  val CURLY_OPEN = "{"
  val CURLY_CLOSE = "}"
  val COMMA = ","
  val QUOTE = "\""
  val COLON = ":"
  val NULL = "null"
}

/**
 * Defines a randomize function.
 *
 * ==Usage==
 *
 * private def randomize[T1, T2](x: T1)(implicit randomizer: Randomizer[T1, T2]): T2 = randomizer.randomize(x)
 *
 * private implicit object JsonNullRandomizer extends Randomizer[JsonNull, Null] {
 *    def randomize(dataType: JsonNull): Null = {
 *     new Null
 *   }
 * }
 *
 */
abstract class Randomizer[T1, T2] { def randomize(x: T1): T2 }

/**
 * Mixin that creates random or custom generated data type values for Json structures
 */
trait JsonRandomizer {

  import com.github.damianmcdonald.jsonrandomizer.JsonChar._

  /**
   * Creates random or custom generated data type values for the provided Json structure
   *
   * @param m the Map representing the Json structure
   * @return String the generated Json containing the random/custom values
   */
  def randomizeAndConvertToJson(m: Map[String, Any]): String = {
    toJson(m)
  }

  /** Defines the type signature of the randomize function to generate random values for specific data types */
  private def randomize[T1, T2](x: T1)(implicit randomizer: Randomizer[T1, T2]): T2 = randomizer.randomize(x)

  /**
   * Implicit implementation of the randomize function, applied to JsonNull data type
   *
   * @param dataType the JsonNull type
   * @return Null the marker class representing a Json Null
   */
  private implicit object JsonNullRandomizer extends Randomizer[JsonNull, Null] {
    def randomize(dataType: JsonNull): Null = {
      new Null
    }
  }

  /**
   * Implicit implementation of the randomize function, applied to JsonArray data type
   *
   * @param dataType the JsonArray type
   * @return List the list containing the elements of the randomly generated Json array
   */
  private implicit object JsonArrayRandomizer extends Randomizer[JsonArray, List[Any]] {
    def randomize(dataType: JsonArray): List[Any] = {
      val length = dataType.length
      dataType.typeof match {
        case s: JsonString => {
          1 to length map (e => QUOTE + s.f(s.max) + QUOTE) toList
        }
        case n: JsonInt => {
          1 to length map (e => n.f(n.min, n.max)) toList
        }
        case n: JsonLong => {
          1 to length map (e => n.f(n.min, n.max)) toList
        }
        case n: JsonDouble => {
          1 to length map (e => n.f(n.min, n.max, n.dp)) toList
        }
        case n: JsonBoolean => {
          1 to length map (e => n.f()) toList
        }
        case o: JsonNull => {
          1 to length map (e => "null") toList
        }
        case o: JsonObject => {
          1 to length map (e => CURLY_OPEN + JsonObjectRandomizer.randomize(o).list.mkString(",") + CURLY_CLOSE) toList
        }
        case _ => throw new RuntimeException("FAILURE >>> unable to find match for JsonDataType")
      }
    }
  }

  /**
   * Implicit implementation of the randomize function, applied to JsonObject data type
   *
   * @param dataType the JsonObject type
   * @return JsonObjectList the marker class containing the contents of the custom Json object structure
   */
  private implicit object JsonObjectRandomizer extends Randomizer[JsonObject, JsonObjectList] {
    def randomize(dataType: JsonObject): JsonObjectList = {
      val xs = dataType.idToValue.toList
      @tailrec
      def inner(xxs: List[Any], ys: List[Any]): List[Any] = {
        xxs match {
          case Nil => ys
          case (id: String, s: JsonString) :: tail => {
            val accum = produceJson(id, s.f(s.max), Nil, 0)
            inner(tail, accum :: ys)
          }
          case (id: String, n: JsonNonStringType) :: tail => {
            val accum = {
              n match {
                case typ: JsonBoolean => produceJson(id, typ.f(), Nil, 0)
                case typ: JsonInt => produceJson(id, typ.f(typ.min, typ.max), Nil, 0)
                case typ: JsonLong => produceJson(id, typ.f(typ.min, typ.max), Nil, 0)
                case typ: JsonDouble => produceJson(id, typ.f(typ.min, typ.max, typ.dp), Nil, 0)
                case typ: JsonNull => produceJson(id, JsonNullRandomizer.randomize(typ), Nil, 0)
              }
            }
            inner(tail, accum :: ys)
          }
          case (id: String, n: JsonArray) :: tail => {
            val accum = produceJson(id, JsonArrayRandomizer.randomize(n), Nil, 0)
            inner(tail, accum :: ys)
          }
          case (id: String, o: JsonObject) :: tail => {
            val accum = toJson(Map(id -> o), "", false)
            inner(tail, accum :: ys)
          }
          case _ => throw new RuntimeException("FAILURE >>> unable to find match for data type")
        }
      }
      new JsonObjectList(inner(xs, Nil))
    }
  }

  /**
   * Creates Json compliant Strings
   *
   * @param id the json id
   * @param v the json value
   * @param xs the list used to determine if ending control chars are required
   * @param i the counter used to determine if additional json properties exist. Required to add appropriate control chars.
   * @return String a Json compliant string
   */
  private def produceJson(id: String, v: Any, xs: List[Any], i: Int): String = {
    v match {
      case s: String => {
        if (xs == Nil || i - 1 == 0) {
          QUOTE + id + QUOTE + COLON + QUOTE + s + QUOTE
        } else {
          QUOTE + id + QUOTE + COLON + QUOTE + s + QUOTE + COMMA
        }
      }
      case _: Boolean | _: Int | _: Long | _: Double => {
        if (xs == Nil || i - 1 == 0) {
          QUOTE + id + QUOTE + COLON + v
        } else {
          QUOTE + id + QUOTE + COLON + v + COMMA
        }
      }
      case n: Null => {
        if (xs == Nil || i - 1 == 0) {
          QUOTE + id + QUOTE + COLON + NULL
        } else {
          QUOTE + id + QUOTE + COLON + NULL + COMMA
        }
      }
      case l: List[_] => {
        if (xs == Nil || i - 1 == 0) {
          QUOTE + id + QUOTE + COLON + BRACKET_OPEN + l.mkString(COMMA) + BRACKET_CLOSE
        } else {
          QUOTE + id + QUOTE + COLON + BRACKET_OPEN + l.mkString(COMMA) + BRACKET_CLOSE + COMMA
        }
      }
      case o: JsonObjectList => {
        if (xs == Nil || i - 1 == 0) {
          QUOTE + id + QUOTE + COLON + CURLY_OPEN + o.list.mkString(COMMA) + CURLY_CLOSE
        } else {
          QUOTE + id + QUOTE + COLON + CURLY_OPEN + o.list.mkString(COMMA) + CURLY_CLOSE + COMMA
        }
      }
      case whatever => throw new RuntimeException("FAILURE >>> unable to find match for : " + whatever.getClass)
    }
  }

  /**
   * Deterines the next iteration values
   *
   * @param json the json String to be modified
   * @param end indicates if this is the final part of the Json string
   * @param cnt the number of items remaining in the Json string
   * @return an anonymous Tuple with 2 values;
   * acc which holds the updated Json String and count which contains the number of iterations remaining
   */
  private def preIterate(json: String, end: Boolean, cnt: Int) = {
    if (cnt - 1 == 0 && !end) {
      new {
        val acc = json + CURLY_CLOSE + BRACKET_CLOSE + COMMA
        val count = 0
      }
    } else if (cnt - 1 == 0 && end) {
      new {
        val acc = json + CURLY_CLOSE + BRACKET_CLOSE
        val count = 0
      }
    } else {
      new {
        val acc = json
        val count = cnt - 1
      }
    }
  }

  /**
   * Creates random or custom generated data type values for the provided Json structure
   *
   * @param m the Map representing the Json structure
   * @param acc the accumulator used to build the Json String
   * @param close indicates if the Json String should be closed
   * @return String the generated Json containing the random/custom values
   */
  private def toJson(m: Map[String, Any], acc: String = JsonChar.CURLY_OPEN, close: Boolean = true): String = {
    val xs = m.toList
    @tailrec
    def inner(xxs: List[Any], accum: String, cnt: Int): String = {
      xxs match {
        case Nil if close => accum + JsonChar.CURLY_CLOSE
        case Nil => accum
        case (id: String, dataType: JsonString) :: tail => {
          val v = dataType.f(dataType.max)
          val json = produceJson(id, v, tail, cnt)
          val next = preIterate(accum + json, tail == Nil, cnt)
          inner(tail, next.acc, next.count)
        }
        case (id: String, dataType: JsonNonStringType) :: tail => {
          val v = {
            dataType match {
              case typ: JsonBoolean => typ.f()
              case typ: JsonInt => typ.f(typ.min, typ.max)
              case typ: JsonLong => typ.f(typ.min, typ.max)
              case typ: JsonDouble => typ.f(typ.min, typ.max, typ.dp)
              case typ: JsonNull => randomize(typ)
            }
          }
          val json = produceJson(id, v, tail, cnt)
          val next = preIterate(accum + json, tail == Nil, cnt)
          inner(tail, next.acc, next.count)
        }
        case (id: String, dataType: JsonArray) :: tail => {
          val v = randomize(dataType)
          val json = produceJson(id, v, tail, cnt)
          val next = preIterate(accum + json, tail == Nil, cnt)
          inner(tail, next.acc, next.count)
        }
        case (id: String, dataType: JsonObject) :: tail => {
          val v = randomize(dataType)
          val json = produceJson(id, v, tail, cnt)
          val next = preIterate(accum + json, tail == Nil, cnt)
          inner(tail, next.acc, next.count)
        }
        case _ => throw new RuntimeException("FAILURE >>> unable to find match for data type")
      }
    }
    inner(xs, acc, 0)
  }

}
