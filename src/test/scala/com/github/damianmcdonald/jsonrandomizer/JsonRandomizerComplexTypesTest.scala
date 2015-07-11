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

import org.scalatest.FunSpec
import net.liftweb.json._
import scala.annotation.tailrec
import com.github.damianmcdonald.jsonrandomizer.TestValues._

class JsonRandomizerComplexTypesTest extends FunSpec with JsonRandomizer with JsonData {

  implicit val formats = net.liftweb.json.DefaultFormats

  describe("A Json Object containing String, Int, Long, Double, Boolean, null and Array values") {
    it("should return a valid Json response") {
      val json = randomizeAndConvertToJson(complexJsonObject)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)

      val valueString = (parsedJson \ "testString").extract[String]
      assert(valueString.length == STRING_LENGTH, "parsed json String value should have a length of " + STRING_LENGTH)

      val valueInt = (parsedJson \ "testInt").extract[Int]
      assert((valueInt >= INT_MIN && valueInt <= INT_MAX), "parsed json Int value should be between " + INT_MIN + " and " + INT_MAX)

      val valueLong = (parsedJson \ "testLong").extract[Long]
      assert((valueLong >= LONG_MIN && valueLong <= LONG_MAX), "parsed json Int value should be between " + LONG_MIN + " and " + LONG_MAX)

      val valueDouble = (parsedJson \ "testDouble").extract[Double]
      assert((valueDouble >= DOUBLE_MIN || valueDouble <= DOUBLE_MAX), "parsed json Int value should be between " + DOUBLE_MIN + " and " + DOUBLE_MAX)

      val valueBoolean = (parsedJson \ "testBoolean").extract[Boolean]
      assert((valueBoolean == true || valueBoolean == false), "parsed json Boolean value should be true or false")

      val valueArrString = for { JString(x) <- (parsedJson \ "testArrayString") } yield x
      assert(valueArrString.length == ARRAY_LENGTH, "parsed json value should contain " + ARRAY_LENGTH + " elements")
      valueArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

      val valueArrBoolean = for { JBool(x) <- (parsedJson \ "testArrayBoolean") } yield x
      assert(valueArrBoolean.length == ARRAY_LENGTH, "parsed json value should contain " + ARRAY_LENGTH + " elements")
      valueArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

      val valueArrInt = for { JInt(x) <- (parsedJson \ "testArrayInt") } yield x
      assert(valueArrInt.length == ARRAY_LENGTH, "parsed json value should contain " + ARRAY_LENGTH + " elements")
      valueArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

      val valueArrLong = for { JInt(x) <- (parsedJson \ "testArrayLong") } yield x
      assert(valueArrLong.length == ARRAY_LENGTH, "parsed json value should contain " + ARRAY_LENGTH + " elements")
      valueArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

      val valueArrDouble = for { JDouble(x) <- (parsedJson \ "testArrayDouble") } yield x
      assert(valueArrDouble.length == ARRAY_LENGTH, "parsed json value should contain " + ARRAY_LENGTH + " elements")
      valueArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Double"))

      val valueArrObject = for { JObject(x) <- (parsedJson \ "testArrayObject") } yield x
      assert(valueArrObject.length == ARRAY_LENGTH, "parsed json value should contain " + ARRAY_LENGTH + " elements")
    }
  }

  describe("A Json Object containing String, Int, Long, Double, Boolean, null, Array and Object values") {
    it("should return a valid Json response") {
      val json = randomizeAndConvertToJson(complexJsonObjectWithArray)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)

      /* Tests for top level object */
      val topLevelString = (parsedJson \ "testString").extract[String]
      assert(topLevelString.length == STRING_LENGTH, "parsed json String value should have a length of " + STRING_LENGTH)

      val topLevelInt = (parsedJson \ "testInt").extract[Int]
      assert((topLevelInt >= INT_MIN && topLevelInt <= INT_MAX), "parsed json Int value should be between " + INT_MIN + " and " + INT_MAX)

      val topLevelLong = (parsedJson \ "testLong").extract[Long]
      assert((topLevelLong >= LONG_MIN && topLevelLong <= LONG_MAX), "parsed json Long value should be between " + LONG_MIN + " and " + LONG_MAX)

      val topLevelDouble = (parsedJson \ "testDouble").extract[Double]
      assert((topLevelDouble >= DOUBLE_MIN || topLevelDouble <= DOUBLE_MAX), "parsed json Double value should be between " + DOUBLE_MIN + " and " + DOUBLE_MAX)

      val topLevelBoolean = (parsedJson \ "testBoolean").extract[Boolean]
      assert((topLevelBoolean == true || topLevelBoolean == false), "parsed json Boolean value should be true or false")

      val topLevelArrString = for { JString(x) <- (parsedJson \ "testArrayString") } yield x
      assert(topLevelArrString.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      topLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

      val topLevelArrBoolean = for { JBool(x) <- (parsedJson \ "testArrayBoolean") } yield x
      assert(topLevelArrBoolean.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      topLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

      val topLevelArrInt = for { JInt(x) <- (parsedJson \ "testArrayInt") } yield x
      assert(topLevelArrInt.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      topLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

      val topLevelArrLong = for { JInt(x) <- (parsedJson \ "testArrayLong") } yield x
      assert(topLevelArrLong.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      topLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

      val topLevelArrDouble = for { JDouble(x) <- (parsedJson \ "testArrayDouble") } yield x
      assert(topLevelArrDouble.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      topLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))

      /* Test for second level object */
      val secondLevelString = (parsedJson \ "testObject" \ "innerString").extract[String]
      assert(secondLevelString.length == STRING_LENGTH, "parsed json String value should have a length of 10")

      val secondLevelInt = (parsedJson \ "testObject" \ "innerInt").extract[Int]
      assert((secondLevelInt >= INT_MIN && secondLevelInt <= INT_MAX), "parsed json Int value should be between " + INT_MIN + " and " + INT_MAX)

      val secondLevelLong = (parsedJson \ "testObject" \ "innerLong").extract[Long]
      assert((secondLevelLong >= LONG_MIN && secondLevelLong <= LONG_MAX), "parsed json Long value should be between " + LONG_MIN + " and " + LONG_MAX)

      val secondLevelDouble = (parsedJson \ "testObject" \ "innerDouble").extract[Double]
      assert((secondLevelDouble >= DOUBLE_MIN || secondLevelDouble <= DOUBLE_MAX), "parsed json Double value should be between " + DOUBLE_MIN + " and " + DOUBLE_MAX)

      val secondLevelBoolean = (parsedJson \ "testObject" \ "innerBoolean").extract[Boolean]
      assert((secondLevelBoolean == true || secondLevelBoolean == false), "parsed json Boolean value should be true or false")

      val secondLevelArrString = for { JString(x) <- (parsedJson \ "testObject" \ "innerArrayString") } yield x
      assert(secondLevelArrString.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      secondLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

      val secondLevelArrBoolean = for { JBool(x) <- (parsedJson \ "testObject" \ "innerArrayBoolean") } yield x
      assert(secondLevelArrBoolean.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      secondLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

      val secondLevelArrInt = for { JInt(x) <- (parsedJson \ "testObject" \ "innerArrayInt") } yield x
      assert(secondLevelArrInt.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      secondLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

      val secondLevelArrLong = for { JInt(x) <- (parsedJson \ "testObject" \ "innerArrayLong") } yield x
      assert(secondLevelArrLong.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      secondLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

      val secondLevelArrDouble = for { JDouble(x) <- (parsedJson \ "testObject" \ "innerArrayDouble") } yield x
      assert(secondLevelArrDouble.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      secondLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))
    }
  }

  describe("A nested Json Object containing String, Int, Long, Double, Boolean, null, Array and Object values") {
    it("should return a valid Json response") {
      val json = randomizeAndConvertToJson(complexNested)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)

      /* Test for first level object */
      val firstLevelString = (parsedJson \ "outTestString").extract[String]
      assert(firstLevelString.length == STRING_LENGTH, "parsed json String value should have a length of " + STRING_LENGTH)

      val firstLevelInt = (parsedJson \ "outTestInt").extract[Int]
      assert((firstLevelInt >= INT_MIN && firstLevelInt <= INT_MAX), "parsed json Int value should be between " + INT_MIN + " and " + INT_MAX)

      val firstLevelLong = (parsedJson \ "outTestLong").extract[Long]
      assert((firstLevelLong >= LONG_MIN && firstLevelLong <= LONG_MAX), "parsed json Long value should be between " + LONG_MIN + " and " + LONG_MAX)

      val firstLevelDouble = (parsedJson \ "outTestDouble").extract[Double]
      assert((firstLevelDouble >= DOUBLE_MIN || firstLevelDouble <= DOUBLE_MAX), "parsed json Double value should be between " + DOUBLE_MIN + " and " + DOUBLE_MAX)

      val firstLevelBoolean = (parsedJson \ "outTestBoolean").extract[Boolean]
      assert((firstLevelBoolean == true || firstLevelBoolean == false), "parsed json Boolean value should be true or false")

      val firstLevelArrString = for { JString(x) <- (parsedJson \ "outTestArrayString") } yield x
      assert(firstLevelArrString.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      firstLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

      val firstLevelArrBoolean = for { JBool(x) <- (parsedJson \ "outTestArrayBoolean") } yield x
      assert(firstLevelArrBoolean.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      firstLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

      val firstLevelArrInt = for { JInt(x) <- (parsedJson \ "outTestArrayInt") } yield x
      assert(firstLevelArrInt.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      firstLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

      val firstLevelArrLong = for { JInt(x) <- (parsedJson \ "outTestArrayLong") } yield x
      assert(firstLevelArrLong.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      firstLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

      val firstLevelArrDouble = for { JDouble(x) <- (parsedJson \ "outTestArrayDouble") } yield x
      assert(firstLevelArrDouble.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      firstLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))

      /* Test for second level object */
      val secondLevelString = (parsedJson \ "nestedTest1" \ "inTest1String").extract[String]
      assert(secondLevelString.length == STRING_LENGTH, "parsed json String value should have a length of " + STRING_LENGTH)

      val secondLevelInt = (parsedJson \ "nestedTest1" \ "inTest1Int").extract[Int]
      assert((secondLevelInt >= INT_MIN && secondLevelInt <= INT_MAX), "parsed json Int value should be between " + INT_MIN + " and " + INT_MAX)

      val secondLevelLong = (parsedJson \ "nestedTest1" \ "inTest1Long").extract[Long]
      assert((secondLevelLong >= LONG_MIN && secondLevelLong <= LONG_MAX), "parsed json Long value should be between " + LONG_MIN + " and " + LONG_MAX)

      val secondLevelDouble = (parsedJson \ "nestedTest1" \ "inTest1Double").extract[Double]
      assert((secondLevelDouble >= DOUBLE_MIN || secondLevelDouble <= DOUBLE_MAX), "parsed json Double value should be between " + DOUBLE_MIN + " and " + DOUBLE_MAX)

      val secondLevelBoolean = (parsedJson \ "nestedTest1" \ "inTest1Boolean").extract[Boolean]
      assert((secondLevelBoolean == true || secondLevelBoolean == false), "parsed json Boolean value should be true or false")

      val secondLevelArrString = for { JString(x) <- (parsedJson \ "nestedTest1" \ "inTest1ArrayString") } yield x
      assert(secondLevelArrString.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      secondLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

      val secondLevelArrBoolean = for { JBool(x) <- (parsedJson \ "nestedTest1" \ "inTest1ArrayBoolean") } yield x
      assert(secondLevelArrBoolean.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      secondLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

      val secondLevelArrInt = for { JInt(x) <- (parsedJson \ "nestedTest1" \ "inTest1ArrayInt") } yield x
      assert(secondLevelArrInt.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      secondLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

      val secondLevelArrLong = for { JInt(x) <- (parsedJson \ "nestedTest1" \ "inTest1ArrayLong") } yield x
      assert(secondLevelArrLong.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      secondLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

      val secondLevelArrDouble = for { JDouble(x) <- (parsedJson \ "nestedTest1" \ "inTest1ArrayDouble") } yield x
      assert(secondLevelArrDouble.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      secondLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))

      /* Test for third level object */
      val thirdLevelString = (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2String").extract[String]
      assert(thirdLevelString.length == STRING_LENGTH, "parsed json String value should have a length of " + STRING_LENGTH)

      val thirdLevelInt = (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2Int").extract[Int]
      assert((thirdLevelInt >= INT_MIN && thirdLevelInt <= INT_MAX), "parsed json Int value should be between " + INT_MIN + " and " + INT_MAX)

      val thirdLevelLong = (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2Long").extract[Long]
      assert((thirdLevelLong >= LONG_MIN && thirdLevelLong <= LONG_MAX), "parsed json Long value should be between " + LONG_MIN + " and " + LONG_MAX)

      val thirdLevelDouble = (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2Double").extract[Double]
      assert((thirdLevelDouble >= DOUBLE_MIN || thirdLevelDouble <= DOUBLE_MAX), "parsed json Double value should be between " + DOUBLE_MIN + " and " + DOUBLE_MAX)

      val thirdLevelBoolean = (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2Boolean").extract[Boolean]
      assert((thirdLevelBoolean == true || thirdLevelBoolean == false), "parsed json Boolean value should be true or false")

      val thirdLevelArrString = for { JString(x) <- (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2ArrayString") } yield x
      assert(thirdLevelArrString.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      thirdLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

      val thirdLevelArrBoolean = for { JBool(x) <- (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2ArrayBoolean") } yield x
      assert(thirdLevelArrBoolean.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      thirdLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

      val thirdLevelArrInt = for { JInt(x) <- (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2ArrayInt") } yield x
      assert(thirdLevelArrInt.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      thirdLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

      val thirdLevelArrLong = for { JInt(x) <- (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2ArrayLong") } yield x
      assert(thirdLevelArrLong.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      thirdLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

      val thirdLevelArrDouble = for { JDouble(x) <- (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2ArrayDouble") } yield x
      assert(thirdLevelArrDouble.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      thirdLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))

      /* Test for fourth level object */
      val fourthLevelString = (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3String").extract[String]
      assert(fourthLevelString.length == STRING_LENGTH, "parsed json String value should have a length of " + STRING_LENGTH)

      val fourthLevelInt = (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3Int").extract[Int]
      assert((fourthLevelInt >= INT_MIN && fourthLevelInt <= INT_MAX), "parsed json Int value should be between " + INT_MIN + " and " + INT_MAX)

      val fourthLevelLong = (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3Long").extract[Long]
      assert((fourthLevelLong >= LONG_MIN && fourthLevelLong <= LONG_MAX), "parsed json Long value should be between " + LONG_MIN + " and " + LONG_MAX)

      val fourthLevelDouble = (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3Double").extract[Double]
      assert((fourthLevelDouble >= DOUBLE_MIN || fourthLevelDouble <= DOUBLE_MAX), "parsed json Double value should be between " + DOUBLE_MIN + " and " + DOUBLE_MAX)

      val fourthLevelBoolean = (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3Boolean").extract[Boolean]
      assert((fourthLevelBoolean == true || fourthLevelBoolean == false), "parsed json Boolean value should be true or false")

      val fourthLevelArrString = for { JString(x) <- (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3ArrayString") } yield x
      assert(fourthLevelArrString.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      fourthLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

      val fourthLevelArrBoolean = for { JBool(x) <- (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3ArrayBoolean") } yield x
      assert(fourthLevelArrBoolean.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      fourthLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

      val fourthLevelArrInt = for { JInt(x) <- (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3ArrayInt") } yield x
      assert(fourthLevelArrInt.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      fourthLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

      val fourthLevelArrLong = for { JInt(x) <- (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3ArrayLong") } yield x
      assert(fourthLevelArrLong.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      fourthLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

      val fourthLevelArrDouble = for { JDouble(x) <- (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3ArrayDouble") } yield x
      assert(fourthLevelArrDouble.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      fourthLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))

      /* Test for fifth level object */
      val fifthLevelString = (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4String").extract[String]
      assert(fifthLevelString.length == STRING_LENGTH, "parsed json String value should have a length of " + STRING_LENGTH)

      val fifthLevelInt = (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4Int").extract[Int]
      assert((fifthLevelInt >= INT_MIN && fifthLevelInt <= INT_MAX), "parsed json Int value should be between " + INT_MIN + " and " + INT_MAX)

      val fifthLevelLong = (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4Long").extract[Long]
      assert((fifthLevelLong >= LONG_MIN && fifthLevelLong <= LONG_MAX), "parsed json Long value should be between " + LONG_MIN + " and " + LONG_MAX)

      val fifthLevelDouble = (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4Double").extract[Double]
      assert((fifthLevelDouble >= DOUBLE_MIN || fifthLevelDouble <= DOUBLE_MAX), "parsed json Double value should be between " + DOUBLE_MIN + " and " + DOUBLE_MAX)

      val fifthLevelBoolean = (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4Boolean").extract[Boolean]
      assert((fifthLevelBoolean == true || fifthLevelBoolean == false), "parsed json Boolean value should be true or false")

      val fifthLevelArrString = for { JString(x) <- (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4ArrayString") } yield x
      assert(fifthLevelArrString.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      fifthLevelArrString.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))

      val fifthLevelArrBoolean = for { JBool(x) <- (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4ArrayBoolean") } yield x
      assert(fifthLevelArrBoolean.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      fifthLevelArrBoolean.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))

      val fifthLevelArrInt = for { JInt(x) <- (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4ArrayInt") } yield x
      assert(fifthLevelArrInt.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      fifthLevelArrInt.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))

      val fifthLevelArrLong = for { JInt(x) <- (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4ArrayLong") } yield x
      assert(fifthLevelArrLong.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      fifthLevelArrLong.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))

      val fifthLevelArrDouble = for { JDouble(x) <- (parsedJson \ "nestedTest1" \ "inTest1NestedObject" \ "inTest2NestedObject" \ "inTest3NestedObject" \ "inTest4ArrayDouble") } yield x
      assert(fifthLevelArrDouble.length == ARRAY_LENGTH, "parsed json array should contain " + ARRAY_LENGTH + " elements")
      fifthLevelArrDouble.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Long"))
    }
  }

}
