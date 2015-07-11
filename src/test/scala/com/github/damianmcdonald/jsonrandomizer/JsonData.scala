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

object TestValues {
  val STRING_LENGTH = 10
  val INT_MIN = 5
  val INT_MAX = 20
  val LONG_MIN = 542525
  val LONG_MAX = 635235323543223L
  val DOUBLE_MIN = 523
  val DOUBLE_MAX = 78906
  val DOUBLE_DP = 4
  val ARRAY_LENGTH = 15
  val ONE = 1
  val ZERO = 0
  val TIMEOUT = 3000L
  val REQUESTS = 1000
  val OBJECT_ID_REGEX = "^[a-f\\d]{24}$"
  val API_ROUTE = "http://localhost:8085/v1/trade"
}

trait JsonData {

  import com.github.damianmcdonald.jsonrandomizer.TestValues._

  /**
   * Start of test data: JsonRandomizerBasicTypesTest
   */

  // simple String test
  val simpleString = Map("testString" -> JsonString(STRING_LENGTH))

  // simple Int test
  val simpleInt = Map("testInt" -> JsonInt(INT_MIN, INT_MAX))

  // simple Long test
  val simpleLong = Map("testLong" -> JsonLong(LONG_MIN, LONG_MAX))

  // simple Double test
  val simpleDouble = Map("testDouble" -> JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP))

  // simple Boolean test
  val simpleBoolean = Map("testBoolean" -> JsonBoolean())

  // simple Null test
  val simpleNull = Map("testNull" -> JsonNull())

  // simple Array[String] test
  val simpleArrayString = Map("testArray" -> JsonArray(JsonString(STRING_LENGTH), ARRAY_LENGTH))

  // simple Array[Boolean] test
  val simpleArrayBoolean = Map("testArray" -> JsonArray(JsonBoolean(), ARRAY_LENGTH))

  // simple Array[Int] test
  val simpleArrayInt = Map("testArray" -> JsonArray(JsonInt(INT_MIN, INT_MAX), ARRAY_LENGTH))

  // simple Array[Long] test
  val simpleArrayLong = Map("testArray" -> JsonArray(JsonLong(LONG_MIN, LONG_MAX), ARRAY_LENGTH))

  // simple Array[Double] test
  val simpleArrayDouble = Map("testArray" -> JsonArray(JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP), ARRAY_LENGTH))

  // simple Array[null] test
  val simpleArrayNull = Map("testArray" -> JsonArray(JsonNull(), ARRAY_LENGTH))

  // simple Array[JsonObject] test
  val simpleArrayJsonObject = {
    val jsObject = JsonObject(Map(
      "stringVal" -> JsonString(STRING_LENGTH),
      "intVal" -> JsonInt(INT_MIN, INT_MAX),
      "longVal" -> JsonLong(LONG_MIN, LONG_MAX),
      "doubleVal" -> JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP),
      "booleanVal" -> JsonBoolean(),
      "nullVal" -> JsonNull(),
      "arrayVal" -> JsonArray(JsonString(STRING_LENGTH), ARRAY_LENGTH)
    ))
    Map("testArray" -> JsonArray(jsObject, 15))
  }

  // simple JsonObject test
  val simpleJsonObject = {
    val jsObject = JsonObject(Map(
      "stringVal" -> JsonString(STRING_LENGTH),
      "intVal" -> JsonInt(INT_MIN, INT_MAX),
      "longVal" -> JsonLong(LONG_MIN, LONG_MAX),
      "doubleVal" -> JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP),
      "booleanVal" -> JsonBoolean(),
      "nullVal" -> JsonNull(),
      "arrayVal" -> JsonArray(JsonString(STRING_LENGTH), ARRAY_LENGTH)
    ))
    Map("testObject" -> jsObject)
  }

  /**
   * End of test data: JsonRandomizerBasicTypesTest
   */

  /*******************************************************/

  /**
   * Start of test data: JsonRandomizerComplexTypesTest
   */

  // complex JsonObject test
  val complexJsonObject = {
    val jsObject = JsonObject(Map(
      "stringVal" -> JsonString(STRING_LENGTH),
      "intVal" -> JsonInt(INT_MIN, INT_MAX),
      "longVal" -> JsonLong(LONG_MIN, LONG_MAX),
      "doubleVal" -> JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP),
      "booleanVal" -> JsonBoolean(),
      "nullVal" -> JsonNull(),
      "arrayVal" -> JsonArray(JsonString(STRING_LENGTH), ARRAY_LENGTH)
    ))
    Map(
      "testString" -> JsonString(STRING_LENGTH),
      "testInt" -> JsonInt(INT_MIN, INT_MAX),
      "testLong" -> JsonLong(LONG_MIN, LONG_MAX),
      "testDouble" -> JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP),
      "testBoolean" -> JsonBoolean(),
      "testNull" -> JsonNull(),
      "testArrayString" -> JsonArray(JsonString(STRING_LENGTH), ARRAY_LENGTH),
      "testArrayBoolean" -> JsonArray(JsonBoolean(), ARRAY_LENGTH),
      "testArrayInt" -> JsonArray(JsonInt(INT_MIN, INT_MAX), ARRAY_LENGTH),
      "testArrayLong" -> JsonArray(JsonLong(LONG_MIN, LONG_MAX), ARRAY_LENGTH),
      "testArrayDouble" -> JsonArray(JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP), ARRAY_LENGTH),
      "testArrayNull" -> JsonArray(JsonNull(), ARRAY_LENGTH),
      "testArrayObject" -> JsonArray(jsObject, ARRAY_LENGTH)
    )
  }

  // complex JsonObject with Array[JsonObject] test
  val complexJsonObjectWithArray = {
    val jsObject = JsonObject(Map(
      "innerString" -> JsonString(STRING_LENGTH),
      "innerInt" -> JsonInt(INT_MIN, INT_MAX),
      "innerLong" -> JsonLong(LONG_MIN, LONG_MAX),
      "innerDouble" -> JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP),
      "innerBoolean" -> JsonBoolean(),
      "innerNull" -> JsonNull(),
      "innerArrayString" -> JsonArray(JsonString(STRING_LENGTH), ARRAY_LENGTH),
      "innerArrayBoolean" -> JsonArray(JsonBoolean(), ARRAY_LENGTH),
      "innerArrayInt" -> JsonArray(JsonInt(INT_MIN, INT_MAX), ARRAY_LENGTH),
      "innerArrayLong" -> JsonArray(JsonLong(LONG_MIN, LONG_MAX), ARRAY_LENGTH),
      "innerArrayDouble" -> JsonArray(JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP), ARRAY_LENGTH),
      "innerArrayNull" -> JsonArray(JsonNull(), ARRAY_LENGTH)
    ))
    Map(
      "testString" -> JsonString(STRING_LENGTH),
      "testInt" -> JsonInt(INT_MIN, INT_MAX),
      "testLong" -> JsonLong(LONG_MIN, LONG_MAX),
      "testDouble" -> JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP),
      "testBoolean" -> JsonBoolean(),
      "testNull" -> JsonNull(),
      "testArrayString" -> JsonArray(JsonString(STRING_LENGTH), ARRAY_LENGTH),
      "testArrayBoolean" -> JsonArray(JsonBoolean(), ARRAY_LENGTH),
      "testArrayInt" -> JsonArray(JsonInt(INT_MIN, INT_MAX), ARRAY_LENGTH),
      "testArrayLong" -> JsonArray(JsonLong(LONG_MIN, LONG_MAX), ARRAY_LENGTH),
      "testArrayDouble" -> JsonArray(JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP), ARRAY_LENGTH),
      "testArrayNull" -> JsonArray(JsonNull(), ARRAY_LENGTH),
      "testArrayObject" -> JsonArray(jsObject, ARRAY_LENGTH),
      "testObject" -> jsObject
    )
  }

  // complex nested object test
  val complexNested = {
    val innerMap4 = Map(
      "inTest4String" -> JsonString(STRING_LENGTH),
      "inTest4Int" -> JsonInt(INT_MIN, INT_MAX),
      "inTest4Long" -> JsonLong(LONG_MIN, LONG_MAX),
      "inTest4Double" -> JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP),
      "inTest4Boolean" -> JsonBoolean(),
      "inTest4Null" -> JsonNull(),
      "inTest4ArrayString" -> JsonArray(JsonString(STRING_LENGTH), ARRAY_LENGTH),
      "inTest4ArrayBoolean" -> JsonArray(JsonBoolean(), ARRAY_LENGTH),
      "inTest4ArrayInt" -> JsonArray(JsonInt(INT_MIN, INT_MAX), ARRAY_LENGTH),
      "inTest4ArrayLong" -> JsonArray(JsonLong(LONG_MIN, LONG_MAX), ARRAY_LENGTH),
      "inTest4ArrayDouble" -> JsonArray(JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP), ARRAY_LENGTH),
      "inTest4ArrayNull" -> JsonArray(JsonNull(), ARRAY_LENGTH)
    )
    val innerMap3 = Map(
      "inTest3String" -> JsonString(STRING_LENGTH),
      "inTest3Int" -> JsonInt(INT_MIN, INT_MAX),
      "inTest3Long" -> JsonLong(LONG_MIN, LONG_MAX),
      "inTest3Double" -> JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP),
      "inTest3Boolean" -> JsonBoolean(),
      "inTest3Null" -> JsonNull(),
      "inTest3ArrayString" -> JsonArray(JsonString(STRING_LENGTH), ARRAY_LENGTH),
      "inTest3ArrayBoolean" -> JsonArray(JsonBoolean(), ARRAY_LENGTH),
      "inTest3ArrayInt" -> JsonArray(JsonInt(INT_MIN, INT_MAX), ARRAY_LENGTH),
      "inTest3ArrayLong" -> JsonArray(JsonLong(LONG_MIN, LONG_MAX), ARRAY_LENGTH),
      "inTest3ArrayDouble" -> JsonArray(JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP), ARRAY_LENGTH),
      "inTest3ArrayNull" -> JsonArray(JsonNull(), ARRAY_LENGTH),
      "inTest3NestedObject" -> JsonObject(innerMap4)
    )
    val innerMap2 = Map(
      "inTest2String" -> JsonString(STRING_LENGTH),
      "inTest2Int" -> JsonInt(INT_MIN, INT_MAX),
      "inTest2Long" -> JsonLong(LONG_MIN, LONG_MAX),
      "inTest2Double" -> JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP),
      "inTest2Boolean" -> JsonBoolean(),
      "inTest2Null" -> JsonNull(),
      "inTest2ArrayString" -> JsonArray(JsonString(STRING_LENGTH), ARRAY_LENGTH),
      "inTest2ArrayBoolean" -> JsonArray(JsonBoolean(), ARRAY_LENGTH),
      "inTest2ArrayInt" -> JsonArray(JsonInt(INT_MIN, INT_MAX), ARRAY_LENGTH),
      "inTest2ArrayLong" -> JsonArray(JsonLong(LONG_MIN, LONG_MAX), ARRAY_LENGTH),
      "inTest2ArrayDouble" -> JsonArray(JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP), ARRAY_LENGTH),
      "inTest2ArrayNull" -> JsonArray(JsonNull(), ARRAY_LENGTH),
      "inTest2NestedObject" -> JsonObject(innerMap3)
    )
    val innerMap1 = Map(
      "inTest1String" -> JsonString(STRING_LENGTH),
      "inTest1Int" -> JsonInt(INT_MIN, INT_MAX),
      "inTest1Long" -> JsonLong(LONG_MIN, LONG_MAX),
      "inTest1Double" -> JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP),
      "inTest1Boolean" -> JsonBoolean(),
      "inTest1Null" -> JsonNull(),
      "inTest1ArrayString" -> JsonArray(JsonString(STRING_LENGTH), ARRAY_LENGTH),
      "inTest1ArrayBoolean" -> JsonArray(JsonBoolean(), ARRAY_LENGTH),
      "inTest1ArrayInt" -> JsonArray(JsonInt(INT_MIN, INT_MAX), ARRAY_LENGTH),
      "inTest1ArrayLong" -> JsonArray(JsonLong(LONG_MIN, LONG_MAX), ARRAY_LENGTH),
      "inTest1ArrayDouble" -> JsonArray(JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP), ARRAY_LENGTH),
      "inTest1ArrayNull" -> JsonArray(JsonNull(), ARRAY_LENGTH),
      "inTest1NestedObject" -> JsonObject(innerMap2)
    )
    val outerMap = Map(
      "outTestString" -> JsonString(STRING_LENGTH),
      "outTestInt" -> JsonInt(INT_MIN, INT_MAX),
      "outTestLong" -> JsonLong(LONG_MIN, LONG_MAX),
      "outTestDouble" -> JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP),
      "outTestBoolean" -> JsonBoolean(),
      "outTestNull" -> JsonNull(),
      "outTestArrayString" -> JsonArray(JsonString(STRING_LENGTH), ARRAY_LENGTH),
      "outTestArrayBoolean" -> JsonArray(JsonBoolean(), ARRAY_LENGTH),
      "outTestArrayInt" -> JsonArray(JsonInt(INT_MIN, INT_MAX), ARRAY_LENGTH),
      "outTestArrayLong" -> JsonArray(JsonLong(LONG_MIN, LONG_MAX), ARRAY_LENGTH),
      "outTestArrayDouble" -> JsonArray(JsonDouble(DOUBLE_MIN, DOUBLE_MAX, DOUBLE_DP), ARRAY_LENGTH),
      "outTestArrayNull" -> JsonArray(JsonNull(), ARRAY_LENGTH),
      "nestedTest1" -> JsonObject(innerMap1)
    )
    outerMap
  }

  /**
   * End of test data: JsonRandomizerComplexTypesTest
   */
}
