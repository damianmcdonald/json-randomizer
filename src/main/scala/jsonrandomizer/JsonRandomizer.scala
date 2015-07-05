package jsonrandomizer

import scala.util.Random
import java.text.DecimalFormat
import scala.annotation.tailrec

sealed trait JsonDataType
sealed trait JsonNonStringType
sealed trait JsonProvidedType
case class JsonString(
  max: Int,
  f: (Int) => String = (max: Int) => {
    val x = Random.alphanumeric
    x take max mkString ("")
  }
) extends JsonDataType
case class JsonBoolean() extends JsonDataType with JsonNonStringType
case class JsonInt(startRange: Int, endRange: Int) extends JsonDataType with JsonNonStringType
case class JsonLong(startRange: Long, endRange: Long) extends JsonDataType with JsonNonStringType
case class JsonDouble(startRange: Int, endRange: Int, decimalPlaces: Int) extends JsonDataType with JsonNonStringType
case class JsonArray(typeof: JsonDataType, length: Int) extends JsonDataType
case class JsonObject(idToValue: Map[String, JsonDataType]) extends JsonDataType
case class JsonNull() extends JsonDataType with JsonNonStringType
case class JsonObjectList(list: List[Any]) extends JsonDataType
// TODO implement the provided types and array of object types handler

private final class Null

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

abstract class Randomizer[T1, T2] { def randomize(x: T1): T2 }

trait JsonRandomizer {

  import jsonrandomizer.JsonChar._

  def randomizeAndConvertToJson(m: Map[String, Any]): String = {
    toJson(m)
  }

  private def randomize[T1, T2](x: T1)(implicit randomizer: Randomizer[T1, T2]): T2 = randomizer.randomize(x)

  private implicit object JsonStringRandomizer extends Randomizer[JsonString, String] {
    def randomize(dataType: JsonString): String = {
      val x = Random.alphanumeric
      x take dataType.max mkString ("")
    }
  }

  private implicit object JsonBooleanRandomizer extends Randomizer[JsonBoolean, Boolean] {
    def randomize(dataType: JsonBoolean): Boolean = {
      Random.nextBoolean
    }
  }

  private implicit object JsonIntRandomizer extends Randomizer[JsonInt, Int] {
    def randomize(dataType: JsonInt): Int = {
      Random.nextInt((dataType.endRange - dataType.startRange) + 1) + dataType.startRange
    }
  }

  private implicit object JsonLongRandomizer extends Randomizer[JsonLong, Long] {
    def randomize(dataType: JsonLong): Long = {
      (dataType.startRange + (Random.nextDouble() * (dataType.endRange - dataType.startRange))).toLong
    }
  }

  private implicit object JsonDoubleRandomizer extends Randomizer[JsonDouble, Double] {
    def randomize(dataType: JsonDouble): Double = {
      val double = (dataType.startRange + (dataType.endRange - dataType.startRange)) * Random.nextDouble
      BigDecimal(double).setScale(dataType.decimalPlaces, BigDecimal.RoundingMode.CEILING).toDouble
    }
  }

  private implicit object JsonNullRandomizer extends Randomizer[JsonNull, Null] {
    def randomize(dataType: JsonNull): Null = {
      new Null
    }
  }

  private implicit object JsonArrayRandomizer extends Randomizer[JsonArray, List[Any]] {
    def randomize(dataType: JsonArray): List[Any] = {
      val length = dataType.length
      dataType.typeof match {
        case s: JsonString => {
          1 to length map (e => QUOTE + JsonStringRandomizer.randomize(s) + QUOTE) toList
        }
        case n: JsonInt => {
          1 to length map (e => JsonIntRandomizer.randomize(n)) toList
        }
        case n: JsonLong => {
          1 to length map (e => JsonLongRandomizer.randomize(n)) toList
        }
        case n: JsonDouble => {
          1 to length map (e => JsonDoubleRandomizer.randomize(n)) toList
        }
        case n: JsonBoolean => {
          1 to length map (e => JsonBooleanRandomizer.randomize(n)) toList
        }
        case o: JsonNull => {
          1 to length map (e => "null") toList
        }
        case _ => throw new RuntimeException("couldn't find match")
      }
    }
  }

  private implicit object JsonObjectRandomizer extends Randomizer[JsonObject, JsonObjectList] {
    def randomize(dataType: JsonObject): JsonObjectList = {
      val xs = dataType.idToValue.toList
      @tailrec
      def inner(xxs: List[Any], ys: List[Any]): List[Any] = {
        xxs match {
          case Nil => ys
          case (id: String, s: JsonString) :: tail => {
            val accum = produceJson(id, JsonStringRandomizer.randomize(s), Nil, 0)
            inner(tail, accum :: ys)
          }
          case (id: String, n: JsonNonStringType) :: tail => {
            val accum = {
              n match {
                case typ: JsonBoolean => produceJson(id, JsonBooleanRandomizer.randomize(typ), Nil, 0)
                case typ: JsonInt => produceJson(id, JsonIntRandomizer.randomize(typ), Nil, 0)
                case typ: JsonLong => produceJson(id, JsonLongRandomizer.randomize(typ), Nil, 0)
                case typ: JsonDouble => produceJson(id, JsonDoubleRandomizer.randomize(typ), Nil, 0)
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
        }
      }
      new JsonObjectList(inner(xs, Nil))
    }
  }

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
      case whatever => throw new RuntimeException("couldn't find match for : " + whatever.getClass)
    }
  }

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

  private def toJson(m: Map[String, Any], acc: String = JsonChar.CURLY_OPEN, close: Boolean = true): String = {
    val xs = m.toList
    @tailrec
    def inner(xxs: List[Any], accum: String, cnt: Int): String = {
      xxs match {
        case Nil if close => accum + JsonChar.CURLY_CLOSE
        case Nil => accum
        case (id: String, dataType: JsonString) :: tail => {
          val v = randomize(dataType)
          val json = produceJson(id, v, tail, cnt)
          val next = preIterate(accum + json, tail == Nil, cnt)
          inner(tail, next.acc, next.count)
        }
        case (id: String, dataType: JsonNonStringType) :: tail => {
          val v = {
            dataType match {
              case typ: JsonBoolean => randomize(typ)
              case typ: JsonInt => randomize(typ)
              case typ: JsonLong => randomize(typ)
              case typ: JsonDouble => randomize(typ)
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
      }
    }
    inner(xs, acc, 0)
  }

}
