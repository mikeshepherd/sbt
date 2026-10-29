/**
 * This code is generated using [[https://www.scala-sbt.org/contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.internal.bsp
final class ScalaTestSuiteSelection private (
  val className: String,
  val tests: Vector[String]) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = this.eq(o.asInstanceOf[AnyRef]) || (o match {
    case x: ScalaTestSuiteSelection => (this.className == x.className) && (this.tests == x.tests)
    case _ => false
  })
  override def hashCode: Int = {
    37 * (37 * (37 * (17 + "sbt.internal.bsp.ScalaTestSuiteSelection".##) + className.##) + tests.##)
  }
  override def toString: String = {
    "ScalaTestSuiteSelection(" + className + ", " + tests + ")"
  }
  private def copy(className: String = className, tests: Vector[String] = tests): ScalaTestSuiteSelection = {
    new ScalaTestSuiteSelection(className, tests)
  }
  def withClassName(className: String): ScalaTestSuiteSelection = {
    copy(className = className)
  }
  def withTests(tests: Vector[String]): ScalaTestSuiteSelection = {
    copy(tests = tests)
  }
}
object ScalaTestSuiteSelection {
  
  def apply(className: String, tests: Vector[String]): ScalaTestSuiteSelection = new ScalaTestSuiteSelection(className, tests)
}
