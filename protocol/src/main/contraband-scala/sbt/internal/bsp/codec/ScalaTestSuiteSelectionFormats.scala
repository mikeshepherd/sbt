/**
 * This code is generated using [[https://www.scala-sbt.org/contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.internal.bsp.codec
import _root_.sjsonnew.{ Unbuilder, Builder, JsonFormat, deserializationError }
trait ScalaTestSuiteSelectionFormats { self: sjsonnew.BasicJsonProtocol =>
given ScalaTestSuiteSelectionFormat: JsonFormat[sbt.internal.bsp.ScalaTestSuiteSelection] = new JsonFormat[sbt.internal.bsp.ScalaTestSuiteSelection] {
  override def read[J](__jsOpt: Option[J], unbuilder: Unbuilder[J]): sbt.internal.bsp.ScalaTestSuiteSelection = {
    __jsOpt match {
      case Some(__js) =>
      unbuilder.beginObject(__js)
      val className = unbuilder.readField[String]("className")
      val tests = unbuilder.readField[Vector[String]]("tests")
      unbuilder.endObject()
      sbt.internal.bsp.ScalaTestSuiteSelection(className, tests)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: sbt.internal.bsp.ScalaTestSuiteSelection, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("className", obj.className)
    builder.addField("tests", obj.tests)
    builder.endObject()
  }
}
}
