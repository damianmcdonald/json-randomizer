package jsonrandomizer

trait JsonData {

  val innerMap4 = Map(
    "inTest4String" -> JsonString(10),
    "inTest4Int" -> JsonInt(5, 20),
    "inTest4Long" -> JsonLong(542525, 635235323543223L),
    "inTest4Double" -> JsonDouble(523, 78906, 4),
    "inTest4Boolean" -> JsonBoolean(),
    "inTest4Null" -> JsonNull(),
    "inTest4ArrayString" -> JsonArray(JsonString(10), 15),
    "inTest4ArrayBoolean" -> JsonArray(JsonBoolean(), 15),
    "inTest4ArrayInt" -> JsonArray(JsonInt(1, 50), 15),
    "inTest4ArrayLong" -> JsonArray(JsonLong(73646, 36436436436436L), 15),
    "inTest4ArrayDouble" -> JsonArray(JsonDouble(443214, 654636342, 4), 15),
    "inTest4ArrayNull" -> JsonArray(JsonNull(), 15)
  )
  val innerMap3 = Map(
    "inTest3String" -> JsonString(10),
    "inTest3Int" -> JsonInt(5, 20),
    "inTest3Long" -> JsonLong(542525, 635235323543223L),
    "inTest3Double" -> JsonDouble(523, 78906, 4),
    "inTest3Boolean" -> JsonBoolean(),
    "inTest3Null" -> JsonNull(),
    "inTest3ArrayString" -> JsonArray(JsonString(10), 15),
    "inTest3ArrayBoolean" -> JsonArray(JsonBoolean(), 15),
    "inTest3ArrayInt" -> JsonArray(JsonInt(1, 50), 15),
    "inTest3ArrayLong" -> JsonArray(JsonLong(73646, 36436436436436L), 15),
    "inTest3ArrayDouble" -> JsonArray(JsonDouble(443214, 654636342, 4), 15),
    "inTest3ArrayNull" -> JsonArray(JsonNull(), 15),
    "inTest3NestedObject" -> JsonObject(innerMap4)
  )
  val innerMap2 = Map(
    "inTest2String" -> JsonString(10),
    "inTest2Int" -> JsonInt(5, 20),
    "inTest2Long" -> JsonLong(542525, 635235323543223L),
    "inTest2Double" -> JsonDouble(523, 78906, 4),
    "inTest2Boolean" -> JsonBoolean(),
    "inTest2Null" -> JsonNull(),
    "inTest2ArrayString" -> JsonArray(JsonString(10), 15),
    "inTest2ArrayBoolean" -> JsonArray(JsonBoolean(), 15),
    "inTest2ArrayInt" -> JsonArray(JsonInt(1, 50), 15),
    "inTest2ArrayLong" -> JsonArray(JsonLong(73646, 36436436436436L), 15),
    "inTest2ArrayDouble" -> JsonArray(JsonDouble(443214, 654636342, 4), 15),
    "inTest2ArrayNull" -> JsonArray(JsonNull(), 15),
    "inTest2NestedObject" -> JsonObject(innerMap3)
  )
  val innerMap1 = Map(
    "inTest1String" -> JsonString(10),
    "inTest1Int" -> JsonInt(5, 20),
    "inTest1Long" -> JsonLong(542525, 635235323543223L),
    "inTest1Double" -> JsonDouble(523, 78906, 4),
    "inTest1Boolean" -> JsonBoolean(),
    "inTest1Null" -> JsonNull(),
    "inTest1ArrayString" -> JsonArray(JsonString(10), 15),
    "inTest1ArrayBoolean" -> JsonArray(JsonBoolean(), 15),
    "inTest1ArrayInt" -> JsonArray(JsonInt(1, 50), 15),
    "inTest1ArrayLong" -> JsonArray(JsonLong(73646, 36436436436436L), 15),
    "inTest1ArrayDouble" -> JsonArray(JsonDouble(443214, 654636342, 4), 15),
    "inTest1ArrayNull" -> JsonArray(JsonNull(), 15),
    "inTest1NestedObject" -> JsonObject(innerMap2)
  )
  val outerMap = Map(
    "outTestString" -> JsonString(10),
    "outTestInt" -> JsonInt(5, 20),
    "outTestLong" -> JsonLong(542525, 635235323543223L),
    "outTestDouble" -> JsonDouble(523, 78906, 4),
    "outTestBoolean" -> JsonBoolean(),
    "outTestNull" -> JsonNull(),
    "outTestArrayString" -> JsonArray(JsonString(10), 15),
    "outTestArrayBoolean" -> JsonArray(JsonBoolean(), 15),
    "outTestArrayInt" -> JsonArray(JsonInt(1, 50), 15),
    "outTestArrayLong" -> JsonArray(JsonLong(73646, 36436436436436L), 15),
    "outTestArrayDouble" -> JsonArray(JsonDouble(443214, 654636342, 4), 15),
    "outTestArrayNull" -> JsonArray(JsonNull(), 15),
    "nestedTest1" -> JsonObject(innerMap1)
  )
  val topMap = Map(
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
    "testObject" -> JsonObject(outerMap)
  )
  val finalMap = Map(
    "innerString" -> JsonString(10),
    "innerInt" -> JsonInt(5, 20),
    "innerLong" -> JsonLong(542525, 635235323543223L),
    "innerDouble" -> JsonDouble(523, 78906, 4),
    "innerBoolean" -> JsonBoolean(),
    "innerNull" -> JsonNull(),
    "innerArrayString" -> JsonArray(JsonString(10), 15),
    "innerArrayBoolean" -> JsonArray(JsonBoolean(), 15),
    "innerArrayInt" -> JsonArray(JsonInt(1, 50), 15),
    "innerArrayLong" -> JsonArray(JsonLong(73646, 36436436436436L), 15),
    "innerArrayDouble" -> JsonArray(JsonDouble(443214, 654636342, 4), 15),
    "innerArrayNull" -> JsonArray(JsonNull(), 15)
  )

}