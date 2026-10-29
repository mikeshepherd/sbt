/**
 * This code is generated using [[https://www.scala-sbt.org/contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.internal.bsp
/** Scala test suite selection for buildTarget/test with dataKind "scala-test-suites-selection". */
final class ScalaTestSuites private (
  val suites: Vector[sbt.internal.bsp.ScalaTestSuiteSelection],
  val jvmOptions: Vector[String],
  val environmentVariables: Vector[String]) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = this.eq(o.asInstanceOf[AnyRef]) || (o match {
    case x: ScalaTestSuites => (this.suites == x.suites) && (this.jvmOptions == x.jvmOptions) && (this.environmentVariables == x.environmentVariables)
    case _ => false
  })
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (17 + "sbt.internal.bsp.ScalaTestSuites".##) + suites.##) + jvmOptions.##) + environmentVariables.##)
  }
  override def toString: String = {
    "ScalaTestSuites(" + suites + ", " + jvmOptions + ", " + environmentVariables + ")"
  }
  private def copy(suites: Vector[sbt.internal.bsp.ScalaTestSuiteSelection] = suites, jvmOptions: Vector[String] = jvmOptions, environmentVariables: Vector[String] = environmentVariables): ScalaTestSuites = {
    new ScalaTestSuites(suites, jvmOptions, environmentVariables)
  }
  def withSuites(suites: Vector[sbt.internal.bsp.ScalaTestSuiteSelection]): ScalaTestSuites = {
    copy(suites = suites)
  }
  def withJvmOptions(jvmOptions: Vector[String]): ScalaTestSuites = {
    copy(jvmOptions = jvmOptions)
  }
  def withEnvironmentVariables(environmentVariables: Vector[String]): ScalaTestSuites = {
    copy(environmentVariables = environmentVariables)
  }
}
object ScalaTestSuites {
  
  def apply(suites: Vector[sbt.internal.bsp.ScalaTestSuiteSelection], jvmOptions: Vector[String], environmentVariables: Vector[String]): ScalaTestSuites = new ScalaTestSuites(suites, jvmOptions, environmentVariables)
}
