package jsonrandomizer

import org.scalatest.FunSpec
import net.liftweb.json._
import scala.util.matching.Regex

class JsonRandomizerBasicTypesTest extends FunSpec with JsonRandomizer {

  implicit val formats = net.liftweb.json.DefaultFormats

  describe("A simple Json String object") {
    it("should return a valid Json response") {
      val m = Map("testString" -> JsonString(10))
      val json = randomizeAndConvertToJson(m)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = (parsedJson \ "testString").extract[String]
      assert(value.length == 10, "parsed json value should have a length of 10")
      intercept[NumberFormatException] {
        // expected this exception as String can not be parsed to Int
        value.toInt
      }
    }
  }

  describe("A simple Json Int object") {
    it("should return a valid Json response") {
      val m = Map("testInt" -> JsonInt(5, 20))
      val json = randomizeAndConvertToJson(m)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = (parsedJson \ "testInt").extract[Int]
      assert((value >= 5 && value <= 20), "parsed json value should be between 5 and 20")
    }
  }

  describe("A simple Json Long object") {
    it("should return a valid Json response") {
      val m = Map("testLong" -> JsonLong(542525, 635235323543223L))
      val json = randomizeAndConvertToJson(m)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = (parsedJson \ "testLong").extract[Long]
      assert((value >= 542525 && value <= 635235323543223L), "parsed json value should be between 542525 and 635235323543223")
    }
  }

  describe("A simple Json Double object") {
    it("should return a valid Json response") {
      val m = Map("testDouble" -> JsonDouble(523, 78906, 4))
      val json = randomizeAndConvertToJson(m)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = (parsedJson \ "testDouble").extract[Double]
      assert((value >= 523 || value <= 78906), "parsed json value should be between 523 and 78906")
      val pattern = "^\\d+(\\.\\d{0,4})?$".r
      pattern findFirstIn value.toString match {
        case Some(s) => // Matched
        case None => throw new RuntimeException("returned double: " + value + " does not meet expceted pattern ^\\d+(\\.\\d{4})?$")
      }
    }
  }

  describe("A simple Json Boolean object") {
    it("should return a valid Json response") {
      val m = Map("testBoolean" -> JsonBoolean())
      val json = randomizeAndConvertToJson(m)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = (parsedJson \ "testBoolean").extract[Boolean]
      assert((value == true || value == false), "parsed json value should be true or false")
    }
  }

  describe("A simple Json Null object") {
    it("should return a valid Json response") {
      val m = Map("testNull" -> JsonNull())
      val json = randomizeAndConvertToJson(m)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
    }
  }

  describe("A simple Json Array[JsonString] object") {
    it("should return a valid Json response") {
      val m = Map("testArray" -> JsonArray(JsonString(10), 15))
      val json = randomizeAndConvertToJson(m)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = for { JString(x) <- (parsedJson \ "testArray") } yield x
      assert(value.length == 15, "parsed json value should contain 15 elements")
      value.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))
    }
  }

  describe("A simple Json Array[JsonBoolean] object") {
    it("should return a valid Json response") {
      val m = Map("testArray" -> JsonArray(JsonBoolean(), 15))
      val json = randomizeAndConvertToJson(m)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = for { JBool(x) <- (parsedJson \ "testArray") } yield x
      assert(value.length == 15, "parsed json value should contain 15 elements")
      value.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))
    }
  }

  describe("A simple Json Array[JsonInt] object") {
    it("should return a valid Json response") {
      val m = Map("testArray" -> JsonArray(JsonInt(1, 50), 15))
      val json = randomizeAndConvertToJson(m)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = for { JInt(x) <- (parsedJson \ "testArray") } yield x
      assert(value.length == 15, "parsed json value should contain 15 elements")
      value.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))
    }
  }

  describe("A simple Json Array[JsonLong] object") {
    it("should return a valid Json response") {
      val m = Map("testArray" -> JsonArray(JsonLong(73646, 36436436436436L), 15))
      val json = randomizeAndConvertToJson(m)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = for { JInt(x) <- (parsedJson \ "testArray") } yield x
      assert(value.length == 15, "parsed json value should contain 15 elements")
      value.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))
    }
  }

  describe("A simple Json Array[JsonDouble] object") {
    it("should return a valid Json response") {
      val m = Map("testArray" -> JsonArray(JsonDouble(443214, 654636342, 4), 15))
      val json = randomizeAndConvertToJson(m)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = for { JDouble(x) <- (parsedJson \ "testArray") } yield x
      assert(value.length == 15, "parsed json value should contain 15 elements")
      value.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Double"))
    }
  }

  describe("A simple Json Array[JsonNull] object") {
    it("should return a valid Json response") {
      val m = Map("testArray" -> JsonArray(JsonNull(), 15))
      val json = randomizeAndConvertToJson(m)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = (for { JArray(x) <- (parsedJson \ "testArray") } yield x).head
      assert(value.length == 15, "parsed json value should contain 15 elements")
    }
  }

  describe("A simple Json Object object") {
    it("should return a valid Json response") {
      val obj: Map[String, JsonDataType] = Map(
        "stringVal" -> JsonString(15),
        "intVal" -> JsonInt(5, 25),
        "longVal" -> JsonLong(424324, 324324324324324L),
        "doubleVal" -> JsonDouble(67875, 9678656, 4),
        "booleanVal" -> JsonBoolean(),
        "nullVal" -> JsonNull(),
        "arrayVal" -> JsonArray(JsonString(20), 6)
      )
      val m = Map("testObject" -> JsonObject(obj))
      val json = randomizeAndConvertToJson(m)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)

      val valueStr = (parsedJson \\ "stringVal").extract[String]
      assert(valueStr.length == 15, "parsed json value should have a length of 15")
      intercept[NumberFormatException] {
        // expected this exception as String can not be parsed to Int
        valueStr.toInt
      }

      val valueInt = (parsedJson \\ "intVal").extract[Int]
      assert((valueInt >= 5 && valueInt <= 25), "parsed json value should be between 5 and 25")

      val valueLong = (parsedJson \\ "longVal").extract[Long]
      assert((valueLong >= 424324 && valueLong <= 324324324324324L), "parsed json value should be between 424324 and 324324324324324")

      val valueDouble = (parsedJson \\ "doubleVal").extract[Double]
      assert((valueDouble >= 67875 || valueDouble <= 9678656), "parsed json value should be between 67875 and 9678656")
      val pattern = "^\\d+(\\.\\d{0,4})?$".r
      pattern findFirstIn valueDouble.toString match {
        case Some(s) => // Matched
        case None => throw new RuntimeException("returned double: " + valueDouble + " does not meet expceted pattern ^\\d+(\\.\\d{4})?$")
      }

      val valueBoolean = (parsedJson \\ "booleanVal").extract[Boolean]
      assert((valueBoolean == true || valueBoolean == false), "parsed json value should be true or false")

      val valueArr = for { JString(x) <- (parsedJson \\ "arrayVal") } yield x
      assert(valueArr.length == 6, "parsed json value should contain 6 elements")
      valueArr.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))
    }
  }

}