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

import com.github.damianmcdonald.jsonrandomizer.TestValues._
import net.liftweb.json._
import org.scalatest.FunSpec

class JsonRandomizerBasicTypesTest extends FunSpec with JsonRandomizer with JsonData {

  implicit val formats = net.liftweb.json.DefaultFormats

  describe("A simple Json String object") {
    it("should return a valid Json response") {
      val json = randomizeAndConvertToJson(simpleString)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = (parsedJson \ "testString").extract[String]
      assert(value.length == STRING_LENGTH, "parsed json value should have a length of " + STRING_LENGTH)
      intercept[NumberFormatException] {
        // expected this exception as String can not be parsed to Int
        value.toInt
      }
    }
  }

  describe("A simple Json Int object") {
    it("should return a valid Json response") {
      val json = randomizeAndConvertToJson(simpleInt)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = (parsedJson \ "testInt").extract[Int]
      assert((value >= INT_MIN && value <= INT_MAX), "parsed json value should be between " + INT_MIN + " and " + INT_MAX)
    }
  }

  describe("A simple Json Long object") {
    it("should return a valid Json response") {
      val json = randomizeAndConvertToJson(simpleLong)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = (parsedJson \ "testLong").extract[Long]
      assert((value >= LONG_MIN && value <= LONG_MAX), "parsed json value should be between " + LONG_MIN + " and " + LONG_MAX)
    }
  }

  describe("A simple Json Double object") {
    it("should return a valid Json response") {
      val json = randomizeAndConvertToJson(simpleDouble)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = (parsedJson \ "testDouble").extract[Double]
      assert((value >= DOUBLE_MIN || value <= DOUBLE_MAX), "parsed json value should be between " + DOUBLE_MIN + " and " + DOUBLE_MAX)
      val pattern = "^\\d+(\\.\\d{0,4})?$".r
      pattern findFirstIn value.toString match {
        case Some(s) => // Matched
        case None => throw new RuntimeException("returned double: " + value + " does not meet expected pattern ^\\d+(\\.\\d{0,4})?$")
      }
    }
  }

  describe("A simple Json Boolean object") {
    it("should return a valid Json response") {
      val json = randomizeAndConvertToJson(simpleBoolean)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = (parsedJson \ "testBoolean").extract[Boolean]
      assert((value == true || value == false), "parsed json value should be true or false")
    }
  }

  describe("A simple Json Null object") {
    it("should return a valid Json response") {
      val json = randomizeAndConvertToJson(simpleNull)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
    }
  }

  describe("A simple Json Array[JsonString] object") {
    it("should return a valid Json response") {
      val json = randomizeAndConvertToJson(simpleArrayString)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = for { JString(x) <- (parsedJson \ "testArray") } yield x
      assert(value.length == ARRAY_LENGTH, "parsed json value should contain " + ARRAY_LENGTH + " elements")
      value.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))
    }
  }

  describe("A simple Json Array[JsonBoolean] object") {
    it("should return a valid Json response") {
      val m = simpleArrayBoolean
      val json = randomizeAndConvertToJson(m)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = for { JBool(x) <- (parsedJson \ "testArray") } yield x
      assert(value.length == ARRAY_LENGTH, "parsed json value should contain " + ARRAY_LENGTH + " elements")
      value.foreach(e => assert(e.isInstanceOf[Boolean], "json array elements should be of type Boolean"))
    }
  }

  describe("A simple Json Array[JsonInt] object") {
    it("should return a valid Json response") {
      val json = randomizeAndConvertToJson(simpleArrayInt)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = for { JInt(x) <- (parsedJson \ "testArray") } yield x
      assert(value.length == ARRAY_LENGTH, "parsed json value should contain " + ARRAY_LENGTH + " elements")
      value.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Int"))
    }
  }

  describe("A simple Json Array[JsonLong] object") {
    it("should return a valid Json response") {
      val json = randomizeAndConvertToJson(simpleArrayLong)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = for { JInt(x) <- (parsedJson \ "testArray") } yield x
      assert(value.length == ARRAY_LENGTH, "parsed json value should contain " + ARRAY_LENGTH + " elements")
      value.foreach(e => assert(e.isInstanceOf[BigInt], "json array elements should be of type Long"))
    }
  }

  describe("A simple Json Array[JsonDouble] object") {
    it("should return a valid Json response") {
      val json = randomizeAndConvertToJson(simpleArrayDouble)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = for { JDouble(x) <- (parsedJson \ "testArray") } yield x
      assert(value.length == ARRAY_LENGTH, "parsed json value should contain " + ARRAY_LENGTH + " elements")
      value.foreach(e => assert(e.isInstanceOf[Double], "json array elements should be of type Double"))
    }
  }

  describe("A simple Json Array[JsonNull] object") {
    it("should return a valid Json response") {
      val json = randomizeAndConvertToJson(simpleArrayNull)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = (for { JArray(x) <- (parsedJson \ "testArray") } yield x).head
      assert(value.length == ARRAY_LENGTH, "parsed json value should contain " + ARRAY_LENGTH + " elements")
    }
  }

  describe("A simple Json Array[JsonObject] object") {
    it("should return a valid Json response") {
      val json = randomizeAndConvertToJson(simpleArrayJsonObject)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)
      val value = (for { JArray(x) <- (parsedJson \ "testArray") } yield x).head
      assert(value.length == ARRAY_LENGTH, "parsed json value should contain " + ARRAY_LENGTH + " elements")
    }
  }

  describe("A simple Json Object object") {
    it("should return a valid Json response") {
      val json = randomizeAndConvertToJson(simpleJsonObject)
      assert(json.isInstanceOf[String], "json should be of type String")
      // parsing the result is a test in itself as a ParseExecption will be thrown if the json format is invalid
      val parsedJson = parse(json)

      val valueStr = (parsedJson \\ "stringVal").extract[String]
      assert(valueStr.length == STRING_LENGTH, "parsed json value should have a length of " + STRING_LENGTH)
      intercept[NumberFormatException] {
        // expected this exception as String can not be parsed to Int
        valueStr.toInt
      }

      val valueInt = (parsedJson \\ "intVal").extract[Int]
      assert((valueInt >= INT_MIN && valueInt <= INT_MAX), "parsed json value should be between " + INT_MIN + " and " + INT_MAX)

      val valueLong = (parsedJson \\ "longVal").extract[Long]
      assert((valueLong >= LONG_MIN && valueLong <= LONG_MAX), "parsed json value should be between " + LONG_MIN + " and " + LONG_MAX)

      val valueDouble = (parsedJson \\ "doubleVal").extract[Double]
      assert((valueDouble >= DOUBLE_MIN || valueDouble <= DOUBLE_MAX), "parsed json value should be between " + DOUBLE_MIN + " and " + DOUBLE_MAX)
      val pattern = "^\\d+(\\.\\d{0,4})?$".r
      pattern findFirstIn valueDouble.toString match {
        case Some(s) => // Matched
        case None => throw new RuntimeException("returned double: " + valueDouble + " does not meet expected pattern ^\\d+(\\.\\d{0,4})?$")
      }

      val valueBoolean = (parsedJson \\ "booleanVal").extract[Boolean]
      assert((valueBoolean == true || valueBoolean == false), "parsed json value should be true or false")

      val valueArr = for { JString(x) <- (parsedJson \\ "arrayVal") } yield x
      assert(valueArr.length == ARRAY_LENGTH, "parsed json value should contain " + ARRAY_LENGTH + " elements")
      valueArr.foreach(e => assert(e.isInstanceOf[String], "json array elements should be of type String"))
    }
  }

}
