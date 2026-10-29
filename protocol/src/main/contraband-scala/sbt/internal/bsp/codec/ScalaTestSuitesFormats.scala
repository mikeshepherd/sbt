/**
 * This code is generated using [[https://www.scala-sbt.org/contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.internal.bsp.codec
import _root_.sjsonnew.{ Unbuilder, Builder, JsonFormat, deserializationError }
trait ScalaTestSuitesFormats { self: sbt.internal.bsp.codec.ScalaTestSuiteSelectionFormats & sjsonnew.BasicJsonProtocol =>
given ScalaTestSuitesFormat: JsonFormat[sbt.internal.bsp.ScalaTestSuites] = new JsonFormat[sbt.internal.bsp.ScalaTestSuites] {
  override def read[J](__jsOpt: Option[J], unbuilder: Unbuilder[J]): sbt.internal.bsp.ScalaTestSuites = {
    __jsOpt match {
      case Some(__js) =>
      unbuilder.beginObject(__js)
      val suites = unbuilder.readField[Vector[sbt.internal.bsp.ScalaTestSuiteSelection]]("suites")
      val jvmOptions = unbuilder.readField[Vector[String]]("jvmOptions")
      val environmentVariables = unbuilder.readField[Vector[String]]("environmentVariables")
      unbuilder.endObject()
      sbt.internal.bsp.ScalaTestSuites(suites, jvmOptions, environmentVariables)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: sbt.internal.bsp.ScalaTestSuites, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("suites", obj.suites)
    builder.addField("jvmOptions", obj.jvmOptions)
    builder.addField("environmentVariables", obj.environmentVariables)
    builder.endObject()
  }
}
}
