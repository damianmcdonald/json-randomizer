package jsonrandomizer

import org.scalatest.FunSpec
import net.liftweb.json._
import scala.annotation.tailrec

class JsonRandomizerComplexTypesTest extends FunSpec with JsonRandomizer with JsonData {

  implicit val formats = net.liftweb.json.DefaultFormats

  describe("A Json Object containing String, Int, Long, Double, Boolean, null and Array values") {
    it("should return a valid Json response") {
      val m = Map(
        "testString" -> JsonString(10),
        "testInt" -> JsonInt(5, 20),
        "testLong" -> JsonLong(542525, 635235323543223L),
        "testDouble" -> JsonDouble(523, 78906, 4),
        "testBoolean" -> JsonBoolean(),
        "testNull" -> JsonNull(),
        "testArrayString" -> JsonArray(JsonString(10), 15),
        "testArrayBoolean" -> JsonArray(JsonBoolean(), 15),
        "testArrayInt" -> JsonArray(JsonInt(1, 50), 15),
        "testArrayLong" -> JsonArray(JsonLong(73646, 36436436436436L), 15),
        "testArrayDouble" -> JsonArray(JsonDouble(443214, 654636342, 4), 15),
        "testArrayNull" -> JsonArray(JsonNull(), 15)
      )
      val json = randomizeAndConvertToJson(m)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)

      val valueString = (parsedJson \ "testString").extract[String]
      assert(valueString.length == 10, "parsed json String value should have a length of 10")

      val valueInt = (parsedJson \ "testInt").extract[Int]
      assert((valueInt >= 5 && valueInt <= 20), "parsed json Int value should be between 5 and 20")

      val valueLong = (parsedJson \ "testLong").extract[Long]
      assert((valueLong >= 542525 && valueLong <= 635235323543223L), "parsed json Long value should be between 542525 and 635235323543223")

      val valueDouble = (parsedJson \ "testDouble").extract[Double]
      assert((valueDouble >= 523 || valueDouble <= 78906), "parsed json Double value should be between 523 and 78906")

      val valueBoolean = (parsedJson \ "testBoolean").extract[Boolean]
      assert((valueBoolean == true || valueBoolean == false), "parsed json Boolean value should be true or false")

      val valueArrString = for { JString(x) <- (parsedJson \ "testArrayString") } yield x
      assert(valueArrString.length == 15, "parsed json value should contain 15 elements")
      valueArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

      val valueArrBoolean = for { JBool(x) <- (parsedJson \ "testArrayBoolean") } yield x
      assert(valueArrBoolean.length == 15, "parsed json value should contain 15 elements")
      valueArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

      val valueArrInt = for { JInt(x) <- (parsedJson \ "testArrayInt") } yield x
      assert(valueArrInt.length == 15, "parsed json value should contain 15 elements")
      valueArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

      val valueArrLong = for { JInt(x) <- (parsedJson \ "testArrayLong") } yield x
      assert(valueArrLong.length == 15, "parsed json value should contain 15 elements")
      valueArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

      val valueArrDouble = for { JDouble(x) <- (parsedJson \ "testArrayDouble") } yield x
      assert(valueArrDouble.length == 15, "parsed json value should contain 15 elements")
      valueArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))
    }
  }

  describe("A Json Object containing String, Int, Long, Double, Boolean, null, Array and Object values") {
    it("should return a valid Json response") {
      val m = Map(
        "testString" -> JsonString(10),
        "testInt" -> JsonInt(5, 20),
        "testLong" -> JsonLong(542525, 635235323543223L),
        "testDouble" -> JsonDouble(523, 78906, 4),
        "testBoolean" -> JsonBoolean(),
        "testNull" -> JsonNull(),
        "testArrayString" -> JsonArray(JsonString(10), 15),
        "testArrayBoolean" -> JsonArray(JsonBoolean(), 15),
        "testArrayInt" -> JsonArray(JsonInt(1, 50), 15),
        "testArrayLong" -> JsonArray(JsonLong(73646, 36436436436436L), 15),
        "testArrayDouble" -> JsonArray(JsonDouble(443214, 654636342, 4), 15),
        "testArrayNull" -> JsonArray(JsonNull(), 15),
        "testObject" -> JsonObject(finalMap)
      )
      val json = randomizeAndConvertToJson(m)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)

      /* Tests for top level object */
      val topLevelString = (parsedJson \ "testString").extract[String]
      assert(topLevelString.length == 10, "parsed json String value should have a length of 10")

      val topLevelInt = (parsedJson \ "testInt").extract[Int]
      assert((topLevelInt >= 5 && topLevelInt <= 20), "parsed json Int value should be between 5 and 20")

      val topLevelLong = (parsedJson \ "testLong").extract[Long]
      assert((topLevelLong >= 542525 && topLevelLong <= 635235323543223L), "parsed json Long value should be between 542525 and 635235323543223")

      val topLevelDouble = (parsedJson \ "testDouble").extract[Double]
      assert((topLevelDouble >= 523 || topLevelDouble <= 78906), "parsed json Double value should be between 523 and 78906")

      val topLevelBoolean = (parsedJson \ "testBoolean").extract[Boolean]
      assert((topLevelBoolean == true || topLevelBoolean == false), "parsed json Boolean value should be true or false")

      val topLevelArrString = for { JString(x) <- (parsedJson \ "testArrayString") } yield x
      assert(topLevelArrString.length == 15, "parsed json array should contain 15 elements")
      topLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

      val topLevelArrBoolean = for { JBool(x) <- (parsedJson \ "testArrayBoolean") } yield x
      assert(topLevelArrBoolean.length == 15, "parsed json array should contain 15 elements")
      topLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

      val topLevelArrInt = for { JInt(x) <- (parsedJson \ "testArrayInt") } yield x
      assert(topLevelArrInt.length == 15, "parsed json array should contain 15 elements")
      topLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

      val topLevelArrLong = for { JInt(x) <- (parsedJson \ "testArrayLong") } yield x
      assert(topLevelArrLong.length == 15, "parsed json array should contain 15 elements")
      topLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

      val topLevelArrDouble = for { JDouble(x) <- (parsedJson \ "testArrayDouble") } yield x
      assert(topLevelArrDouble.length == 15, "parsed json array should contain 15 elements")
      topLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))

      /* Test for second level object */
      val secondLevelString = (parsedJson \ "testObject" \ "innerString").extract[String]
      assert(secondLevelString.length == 10, "parsed json String value should have a length of 10")

      val secondLevelInt = (parsedJson \ "testObject" \ "innerInt").extract[Int]
      assert((secondLevelInt >= 5 && secondLevelInt <= 20), "parsed json Int value should be between 5 and 20")

      val secondLevelLong = (parsedJson \ "testObject" \ "innerLong").extract[Long]
      assert((secondLevelLong >= 542525 && secondLevelLong <= 635235323543223L), "parsed json Long value should be between 542525 and 635235323543223")

      val secondLevelDouble = (parsedJson \ "testObject" \ "innerDouble").extract[Double]
      assert((secondLevelDouble >= 523 || secondLevelDouble <= 78906), "parsed json Double value should be between 523 and 78906")

      val secondLevelBoolean = (parsedJson \ "testObject" \ "innerBoolean").extract[Boolean]
      assert((secondLevelBoolean == true || secondLevelBoolean == false), "parsed json Boolean value should be true or false")

      val secondLevelArrString = for { JString(x) <- (parsedJson \ "testObject" \ "innerArrayString") } yield x
      assert(secondLevelArrString.length == 15, "parsed json array should contain 15 elements")
      secondLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

      val secondLevelArrBoolean = for { JBool(x) <- (parsedJson \ "testObject" \ "innerArrayBoolean") } yield x
      assert(secondLevelArrBoolean.length == 15, "parsed json array should contain 15 elements")
      secondLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

      val secondLevelArrInt = for { JInt(x) <- (parsedJson \ "testObject" \ "innerArrayInt") } yield x
      assert(secondLevelArrInt.length == 15, "parsed json array should contain 15 elements")
      secondLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

      val secondLevelArrLong = for { JInt(x) <- (parsedJson \ "testObject" \ "innerArrayLong") } yield x
      assert(secondLevelArrLong.length == 15, "parsed json array should contain 15 elements")
      secondLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

      val secondLevelArrDouble = for { JDouble(x) <- (parsedJson \ "testObject" \ "innerArrayDouble") } yield x
      assert(secondLevelArrDouble.length == 15, "parsed json array should contain 15 elements")
      secondLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))
    }
  }

  describe("A nested Json Object containing String, Int, Long, Double, Boolean, null, Array and Object values") {
    it("should return a valid Json response") {
      val json = randomizeAndConvertToJson(topMap)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)

      /* Tests for top level object */
      val topLevelString = (parsedJson \ "testString").extract[String]
      assert(topLevelString.length == 10, "parsed json String value should have a length of 10")

      val topLevelInt = (parsedJson \ "testInt").extract[Int]
      assert((topLevelInt >= 5 && topLevelInt <= 20), "parsed json Int value should be between 5 and 20")

      val topLevelLong = (parsedJson \ "testLong").extract[Long]
      assert((topLevelLong >= 542525 && topLevelLong <= 635235323543223L), "parsed json Long value should be between 542525 and 635235323543223")

      val topLevelDouble = (parsedJson \ "testDouble").extract[Double]
      assert((topLevelDouble >= 523 || topLevelDouble <= 78906), "parsed json Double value should be between 523 and 78906")

      val topLevelBoolean = (parsedJson \ "testBoolean").extract[Boolean]
      assert((topLevelBoolean == true || topLevelBoolean == false), "parsed json Boolean value should be true or false")

      val topLevelArrString = for { JString(x) <- (parsedJson \ "testArrayString") } yield x
      assert(topLevelArrString.length == 15, "parsed json array should contain 15 elements")
      topLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

      val topLevelArrBoolean = for { JBool(x) <- (parsedJson \ "testArrayBoolean") } yield x
      assert(topLevelArrBoolean.length == 15, "parsed json array should contain 15 elements")
      topLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

      val topLevelArrInt = for { JInt(x) <- (parsedJson \ "testArrayInt") } yield x
      assert(topLevelArrInt.length == 15, "parsed json array should contain 15 elements")
      topLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

      val topLevelArrLong = for { JInt(x) <- (parsedJson \ "testArrayLong") } yield x
      assert(topLevelArrLong.length == 15, "parsed json array should contain 15 elements")
      topLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

      val topLevelArrDouble = for { JDouble(x) <- (parsedJson \ "testArrayDouble") } yield x
      assert(topLevelArrDouble.length == 15, "parsed json array should contain 15 elements")
      topLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))

      /* Test for second level object */
      val secondLevelString = (parsedJson \ "testObject" \ "outTestString").extract[String]
      assert(secondLevelString.length == 10, "parsed json String value should have a length of 10")

      val secondLevelInt = (parsedJson \ "testObject" \ "outTestInt").extract[Int]
      assert((secondLevelInt >= 5 && secondLevelInt <= 20), "parsed json Int value should be between 5 and 20")

      val secondLevelLong = (parsedJson \ "testObject" \ "outTestLong").extract[Long]
      assert((secondLevelLong >= 542525 && secondLevelLong <= 635235323543223L), "parsed json Long value should be between 542525 and 635235323543223")

      val secondLevelDouble = (parsedJson \ "testObject" \ "outTestDouble").extract[Double]
      assert((secondLevelDouble >= 523 || secondLevelDouble <= 78906), "parsed json Double value should be between 523 and 78906")

      val secondLevelBoolean = (parsedJson \ "testObject" \ "outTestBoolean").extract[Boolean]
      assert((secondLevelBoolean == true || secondLevelBoolean == false), "parsed json Boolean value should be true or false")

      val secondLevelArrString = for { JString(x) <- (parsedJson \ "testObject" \ "outTestArrayString") } yield x
      assert(secondLevelArrString.length == 15, "parsed json array should contain 15 elements")
      secondLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

      val secondLevelArrBoolean = for { JBool(x) <- (parsedJson \ "testObject" \ "outTestArrayBoolean") } yield x
      assert(secondLevelArrBoolean.length == 15, "parsed json array should contain 15 elements")
      secondLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

      val secondLevelArrInt = for { JInt(x) <- (parsedJson \ "testObject" \ "outTestArrayInt") } yield x
      assert(secondLevelArrInt.length == 15, "parsed json array should contain 15 elements")
      secondLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

      val secondLevelArrLong = for { JInt(x) <- (parsedJson \ "testObject" \ "outTestArrayLong") } yield x
      assert(secondLevelArrLong.length == 15, "parsed json array should contain 15 elements")
      secondLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

      val secondLevelArrDouble = for { JDouble(x) <- (parsedJson \ "testObject" \ "outTestArrayDouble") } yield x
      assert(secondLevelArrDouble.length == 15, "parsed json array should contain 15 elements")
      secondLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))

      /* Test for third level object */
      val thirdLevelString = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1String").extract[String]
      assert(thirdLevelString.length == 10, "parsed json String value should have a length of 10")

      val thirdLevelInt = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1Int").extract[Int]
      assert((thirdLevelInt >= 5 && thirdLevelInt <= 20), "parsed json Int value should be between 5 and 20")

      val thirdLevelLong = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1Long").extract[Long]
      assert((thirdLevelLong >= 542525 && thirdLevelLong <= 635235323543223L), "parsed json Long value should be between 542525 and 635235323543223")

      val thirdLevelDouble = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1Double").extract[Double]
      assert((thirdLevelDouble >= 523 || thirdLevelDouble <= 78906), "parsed json Double value should be between 523 and 78906")

      val thirdLevelBoolean = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1Boolean").extract[Boolean]
      assert((thirdLevelBoolean == true || thirdLevelBoolean == false), "parsed json Boolean value should be true or false")

      val thirdLevelArrString = for { JString(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1ArrayString") } yield x
      assert(thirdLevelArrString.length == 15, "parsed json array should contain 15 elements")
      thirdLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

      val thirdLevelArrBoolean = for { JBool(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1ArrayBoolean") } yield x
      assert(thirdLevelArrBoolean.length == 15, "parsed json array should contain 15 elements")
      thirdLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

      val thirdLevelArrInt = for { JInt(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1ArrayInt") } yield x
      assert(thirdLevelArrInt.length == 15, "parsed json array should contain 15 elements")
      thirdLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

      val thirdLevelArrLong = for { JInt(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1ArrayLong") } yield x
      assert(thirdLevelArrLong.length == 15, "parsed json array should contain 15 elements")
      thirdLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

      val thirdLevelArrDouble = for { JDouble(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1ArrayDouble") } yield x
      assert(thirdLevelArrDouble.length == 15, "parsed json array should contain 15 elements")
      thirdLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))

      /* Test for fourth level object */
      val fourthLevelString = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2String").extract[String]
      assert(fourthLevelString.length == 10, "parsed json String value should have a length of 10")

      val fourthLevelInt = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2Int").extract[Int]
      assert((fourthLevelInt >= 5 && fourthLevelInt <= 20), "parsed json Int value should be between 5 and 20")

      val fourthLevelLong = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2Long").extract[Long]
      assert((fourthLevelLong >= 542525 && fourthLevelLong <= 635235323543223L), "parsed json Long value should be between 542525 and 635235323543223")

      val fourthLevelDouble = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2Double").extract[Double]
      assert((fourthLevelDouble >= 523 || fourthLevelDouble <= 78906), "parsed json Double value should be between 523 and 78906")

      val fourthLevelBoolean = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2Boolean").extract[Boolean]
      assert((fourthLevelBoolean == true || fourthLevelBoolean == false), "parsed json Boolean value should be true or false")

      val fourthLevelArrString = for { JString(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2ArrayString") } yield x
      assert(fourthLevelArrString.length == 15, "parsed json array should contain 15 elements")
      fourthLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

      val fourthLevelArrBoolean = for { JBool(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2ArrayBoolean") } yield x
      assert(fourthLevelArrBoolean.length == 15, "parsed json array should contain 15 elements")
      fourthLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

      val fourthLevelArrInt = for { JInt(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2ArrayInt") } yield x
      assert(fourthLevelArrInt.length == 15, "parsed json array should contain 15 elements")
      fourthLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

      val fourthLevelArrLong = for { JInt(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2ArrayLong") } yield x
      assert(fourthLevelArrLong.length == 15, "parsed json array should contain 15 elements")
      fourthLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

      val fourthLevelArrDouble = for { JDouble(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2ArrayDouble") } yield x
      assert(fourthLevelArrDouble.length == 15, "parsed json array should contain 15 elements")
      fourthLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))

      /* Test for fifth level object */
      val fifthLevelString = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3String").extract[String]
      assert(fifthLevelString.length == 10, "parsed json String value should have a length of 10")

      val fifthLevelInt = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3Int").extract[Int]
      assert((fifthLevelInt >= 5 && fifthLevelInt <= 20), "parsed json Int value should be between 5 and 20")

      val fifthLevelLong = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3Long").extract[Long]
      assert((fifthLevelLong >= 542525 && fifthLevelLong <= 635235323543223L), "parsed json Long value should be between 542525 and 635235323543223")

      val fifthLevelDouble = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3Double").extract[Double]
      assert((fifthLevelDouble >= 523 || fifthLevelDouble <= 78906), "parsed json Double value should be between 523 and 78906")

      val fifthLevelBoolean = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3Boolean").extract[Boolean]
      assert((fifthLevelBoolean == true || fifthLevelBoolean == false), "parsed json Boolean value should be true or false")

      val fifthLevelArrString = for { JString(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3ArrayString") } yield x
      assert(fifthLevelArrString.length == 15, "parsed json array should contain 15 elements")
      fifthLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

      val fifthLevelArrBoolean = for { JBool(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3ArrayBoolean") } yield x
      assert(fifthLevelArrBoolean.length == 15, "parsed json array should contain 15 elements")
      fifthLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

      val fifthLevelArrInt = for { JInt(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3ArrayInt") } yield x
      assert(fifthLevelArrInt.length == 15, "parsed json array should contain 15 elements")
      fifthLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

      val fifthLevelArrLong = for { JInt(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3ArrayLong") } yield x
      assert(fifthLevelArrLong.length == 15, "parsed json array should contain 15 elements")
      fifthLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

      val fifthLevelArrDouble = for { JDouble(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3ArrayDouble") } yield x
      assert(fifthLevelArrDouble.length == 15, "parsed json array should contain 15 elements")
      fifthLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))

      /* Test for sixth level object */
      val sixthLevelString = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4String").extract[String]
      assert(sixthLevelString.length == 10, "parsed json String value should have a length of 10")

      val sixthLevelInt = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4Int").extract[Int]
      assert((sixthLevelInt >= 5 && sixthLevelInt <= 20), "parsed json Int value should be between 5 and 20")

      val sixthLevelLong = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4Long").extract[Long]
      assert((sixthLevelLong >= 542525 && sixthLevelLong <= 635235323543223L), "parsed json Long value should be between 542525 and 635235323543223")

      val sixthLevelDouble = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4Double").extract[Double]
      assert((sixthLevelDouble >= 523 || sixthLevelDouble <= 78906), "parsed json Double value should be between 523 and 78906")

      val sixthLevelBoolean = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4Boolean").extract[Boolean]
      assert((sixthLevelBoolean == true || sixthLevelBoolean == false), "parsed json Boolean value should be true or false")

      val sixthLevelArrString = for { JString(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4ArrayString") } yield x
      assert(sixthLevelArrString.length == 15, "parsed json array should contain 15 elements")
      sixthLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

      val sixthLevelArrBoolean = for { JBool(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4ArrayBoolean") } yield x
      assert(sixthLevelArrBoolean.length == 15, "parsed json array should contain 15 elements")
      sixthLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

      val sixthLevelArrInt = for { JInt(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4ArrayInt") } yield x
      assert(sixthLevelArrInt.length == 15, "parsed json array should contain 15 elements")
      sixthLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

      val sixthLevelArrLong = for { JInt(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4ArrayLong") } yield x
      assert(sixthLevelArrLong.length == 15, "parsed json array should contain 15 elements")
      sixthLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

      val sixthLevelArrDouble = for { JDouble(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4ArrayDouble") } yield x
      assert(sixthLevelArrDouble.length == 15, "parsed json array should contain 15 elements")
      sixthLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))
    }
  }

  describe("A List of nested Json Objects containing String, Int, Long, Double, Boolean, null, Array and Object values") {
    it("should return a valid Json response") {
      val jsonList: List[String] = {
        @tailrec
        def inner(xs: List[String], cnt: Int): List[String] = {
          xs match {
            case _ if cnt <= 0 => xs
            case Nil => inner(List(randomizeAndConvertToJson(topMap)) ::: Nil, cnt - 1)
            case head :: tail => inner(List(randomizeAndConvertToJson(topMap)) ::: tail, cnt - 1)
          }
        }
        inner(Nil, 50)
      }
      assert(jsonList.isInstanceOf[List[String]], "json should be of type List[String]")

      jsonList foreach (json => {
        // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
        val parsedJson = parse(json)

        /* Tests for top level object */
        val topLevelString = (parsedJson \ "testString").extract[String]
        assert(topLevelString.length == 10, "parsed json String value should have a length of 10")

        val topLevelInt = (parsedJson \ "testInt").extract[Int]
        assert((topLevelInt >= 5 && topLevelInt <= 20), "parsed json Int value should be between 5 and 20")

        val topLevelLong = (parsedJson \ "testLong").extract[Long]
        assert((topLevelLong >= 542525 && topLevelLong <= 635235323543223L), "parsed json Long value should be between 542525 and 635235323543223")

        val topLevelDouble = (parsedJson \ "testDouble").extract[Double]
        assert((topLevelDouble >= 523 || topLevelDouble <= 78906), "parsed json Double value should be between 523 and 78906")

        val topLevelBoolean = (parsedJson \ "testBoolean").extract[Boolean]
        assert((topLevelBoolean == true || topLevelBoolean == false), "parsed json Boolean value should be true or false")

        val topLevelArrString = for { JString(x) <- (parsedJson \ "testArrayString") } yield x
        assert(topLevelArrString.length == 15, "parsed json array should contain 15 elements")
        topLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

        val topLevelArrBoolean = for { JBool(x) <- (parsedJson \ "testArrayBoolean") } yield x
        assert(topLevelArrBoolean.length == 15, "parsed json array should contain 15 elements")
        topLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

        val topLevelArrInt = for { JInt(x) <- (parsedJson \ "testArrayInt") } yield x
        assert(topLevelArrInt.length == 15, "parsed json array should contain 15 elements")
        topLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

        val topLevelArrLong = for { JInt(x) <- (parsedJson \ "testArrayLong") } yield x
        assert(topLevelArrLong.length == 15, "parsed json array should contain 15 elements")
        topLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

        val topLevelArrDouble = for { JDouble(x) <- (parsedJson \ "testArrayDouble") } yield x
        assert(topLevelArrDouble.length == 15, "parsed json array should contain 15 elements")
        topLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))

        /* Test for second level object */
        val secondLevelString = (parsedJson \ "testObject" \ "outTestString").extract[String]
        assert(secondLevelString.length == 10, "parsed json String value should have a length of 10")

        val secondLevelInt = (parsedJson \ "testObject" \ "outTestInt").extract[Int]
        assert((secondLevelInt >= 5 && secondLevelInt <= 20), "parsed json Int value should be between 5 and 20")

        val secondLevelLong = (parsedJson \ "testObject" \ "outTestLong").extract[Long]
        assert((secondLevelLong >= 542525 && secondLevelLong <= 635235323543223L), "parsed json Long value should be between 542525 and 635235323543223")

        val secondLevelDouble = (parsedJson \ "testObject" \ "outTestDouble").extract[Double]
        assert((secondLevelDouble >= 523 || secondLevelDouble <= 78906), "parsed json Double value should be between 523 and 78906")

        val secondLevelBoolean = (parsedJson \ "testObject" \ "outTestBoolean").extract[Boolean]
        assert((secondLevelBoolean == true || secondLevelBoolean == false), "parsed json Boolean value should be true or false")

        val secondLevelArrString = for { JString(x) <- (parsedJson \ "testObject" \ "outTestArrayString") } yield x
        assert(secondLevelArrString.length == 15, "parsed json array should contain 15 elements")
        secondLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

        val secondLevelArrBoolean = for { JBool(x) <- (parsedJson \ "testObject" \ "outTestArrayBoolean") } yield x
        assert(secondLevelArrBoolean.length == 15, "parsed json array should contain 15 elements")
        secondLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

        val secondLevelArrInt = for { JInt(x) <- (parsedJson \ "testObject" \ "outTestArrayInt") } yield x
        assert(secondLevelArrInt.length == 15, "parsed json array should contain 15 elements")
        secondLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

        val secondLevelArrLong = for { JInt(x) <- (parsedJson \ "testObject" \ "outTestArrayLong") } yield x
        assert(secondLevelArrLong.length == 15, "parsed json array should contain 15 elements")
        secondLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

        val secondLevelArrDouble = for { JDouble(x) <- (parsedJson \ "testObject" \ "outTestArrayDouble") } yield x
        assert(secondLevelArrDouble.length == 15, "parsed json array should contain 15 elements")
        secondLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))

        /* Test for third level object */
        val thirdLevelString = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1String").extract[String]
        assert(thirdLevelString.length == 10, "parsed json String value should have a length of 10")

        val thirdLevelInt = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1Int").extract[Int]
        assert((thirdLevelInt >= 5 && thirdLevelInt <= 20), "parsed json Int value should be between 5 and 20")

        val thirdLevelLong = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1Long").extract[Long]
        assert((thirdLevelLong >= 542525 && thirdLevelLong <= 635235323543223L), "parsed json Long value should be between 542525 and 635235323543223")

        val thirdLevelDouble = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1Double").extract[Double]
        assert((thirdLevelDouble >= 523 || thirdLevelDouble <= 78906), "parsed json Double value should be between 523 and 78906")

        val thirdLevelBoolean = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1Boolean").extract[Boolean]
        assert((thirdLevelBoolean == true || thirdLevelBoolean == false), "parsed json Boolean value should be true or false")

        val thirdLevelArrString = for { JString(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1ArrayString") } yield x
        assert(thirdLevelArrString.length == 15, "parsed json array should contain 15 elements")
        thirdLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

        val thirdLevelArrBoolean = for { JBool(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1ArrayBoolean") } yield x
        assert(thirdLevelArrBoolean.length == 15, "parsed json array should contain 15 elements")
        thirdLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

        val thirdLevelArrInt = for { JInt(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1ArrayInt") } yield x
        assert(thirdLevelArrInt.length == 15, "parsed json array should contain 15 elements")
        thirdLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

        val thirdLevelArrLong = for { JInt(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1ArrayLong") } yield x
        assert(thirdLevelArrLong.length == 15, "parsed json array should contain 15 elements")
        thirdLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

        val thirdLevelArrDouble = for { JDouble(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1ArrayDouble") } yield x
        assert(thirdLevelArrDouble.length == 15, "parsed json array should contain 15 elements")
        thirdLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))

        /* Test for fourth level object */
        val fourthLevelString = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2String").extract[String]
        assert(fourthLevelString.length == 10, "parsed json String value should have a length of 10")

        val fourthLevelInt = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2Int").extract[Int]
        assert((fourthLevelInt >= 5 && fourthLevelInt <= 20), "parsed json Int value should be between 5 and 20")

        val fourthLevelLong = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2Long").extract[Long]
        assert((fourthLevelLong >= 542525 && fourthLevelLong <= 635235323543223L), "parsed json Long value should be between 542525 and 635235323543223")

        val fourthLevelDouble = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2Double").extract[Double]
        assert((fourthLevelDouble >= 523 || fourthLevelDouble <= 78906), "parsed json Double value should be between 523 and 78906")

        val fourthLevelBoolean = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2Boolean").extract[Boolean]
        assert((fourthLevelBoolean == true || fourthLevelBoolean == false), "parsed json Boolean value should be true or false")

        val fourthLevelArrString = for { JString(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2ArrayString") } yield x
        assert(fourthLevelArrString.length == 15, "parsed json array should contain 15 elements")
        fourthLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

        val fourthLevelArrBoolean = for { JBool(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2ArrayBoolean") } yield x
        assert(fourthLevelArrBoolean.length == 15, "parsed json array should contain 15 elements")
        fourthLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

        val fourthLevelArrInt = for { JInt(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2ArrayInt") } yield x
        assert(fourthLevelArrInt.length == 15, "parsed json array should contain 15 elements")
        fourthLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

        val fourthLevelArrLong = for { JInt(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2ArrayLong") } yield x
        assert(fourthLevelArrLong.length == 15, "parsed json array should contain 15 elements")
        fourthLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

        val fourthLevelArrDouble = for { JDouble(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2ArrayDouble") } yield x
        assert(fourthLevelArrDouble.length == 15, "parsed json array should contain 15 elements")
        fourthLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))

        /* Test for fifth level object */
        val fifthLevelString = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3String").extract[String]
        assert(fifthLevelString.length == 10, "parsed json String value should have a length of 10")

        val fifthLevelInt = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3Int").extract[Int]
        assert((fifthLevelInt >= 5 && fifthLevelInt <= 20), "parsed json Int value should be between 5 and 20")

        val fifthLevelLong = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3Long").extract[Long]
        assert((fifthLevelLong >= 542525 && fifthLevelLong <= 635235323543223L), "parsed json Long value should be between 542525 and 635235323543223")

        val fifthLevelDouble = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3Double").extract[Double]
        assert((fifthLevelDouble >= 523 || fifthLevelDouble <= 78906), "parsed json Double value should be between 523 and 78906")

        val fifthLevelBoolean = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3Boolean").extract[Boolean]
        assert((fifthLevelBoolean == true || fifthLevelBoolean == false), "parsed json Boolean value should be true or false")

        val fifthLevelArrString = for { JString(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3ArrayString") } yield x
        assert(fifthLevelArrString.length == 15, "parsed json array should contain 15 elements")
        fifthLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

        val fifthLevelArrBoolean = for { JBool(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3ArrayBoolean") } yield x
        assert(fifthLevelArrBoolean.length == 15, "parsed json array should contain 15 elements")
        fifthLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

        val fifthLevelArrInt = for { JInt(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3ArrayInt") } yield x
        assert(fifthLevelArrInt.length == 15, "parsed json array should contain 15 elements")
        fifthLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

        val fifthLevelArrLong = for { JInt(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3ArrayLong") } yield x
        assert(fifthLevelArrLong.length == 15, "parsed json array should contain 15 elements")
        fifthLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

        val fifthLevelArrDouble = for { JDouble(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3ArrayDouble") } yield x
        assert(fifthLevelArrDouble.length == 15, "parsed json array should contain 15 elements")
        fifthLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))

        /* Test for sixth level object */
        val sixthLevelString = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4String").extract[String]
        assert(sixthLevelString.length == 10, "parsed json String value should have a length of 10")

        val sixthLevelInt = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4Int").extract[Int]
        assert((sixthLevelInt >= 5 && sixthLevelInt <= 20), "parsed json Int value should be between 5 and 20")

        val sixthLevelLong = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4Long").extract[Long]
        assert((sixthLevelLong >= 542525 && sixthLevelLong <= 635235323543223L), "parsed json Long value should be between 542525 and 635235323543223")

        val sixthLevelDouble = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4Double").extract[Double]
        assert((sixthLevelDouble >= 523 || sixthLevelDouble <= 78906), "parsed json Double value should be between 523 and 78906")

        val sixthLevelBoolean = (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4Boolean").extract[Boolean]
        assert((sixthLevelBoolean == true || sixthLevelBoolean == false), "parsed json Boolean value should be true or false")

        val sixthLevelArrString = for { JString(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4ArrayString") } yield x
        assert(sixthLevelArrString.length == 15, "parsed json array should contain 15 elements")
        sixthLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

        val sixthLevelArrBoolean = for { JBool(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4ArrayBoolean") } yield x
        assert(sixthLevelArrBoolean.length == 15, "parsed json array should contain 15 elements")
        sixthLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

        val sixthLevelArrInt = for { JInt(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4ArrayInt") } yield x
        assert(sixthLevelArrInt.length == 15, "parsed json array should contain 15 elements")
        sixthLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

        val sixthLevelArrLong = for { JInt(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4ArrayLong") } yield x
        assert(sixthLevelArrLong.length == 15, "parsed json array should contain 15 elements")
        sixthLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

        val sixthLevelArrDouble = for { JDouble(x) <- (parsedJson \ "testObject" \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4ArrayDouble") } yield x
        assert(sixthLevelArrDouble.length == 15, "parsed json array should contain 15 elements")
        sixthLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))
      })
    }
  }

}