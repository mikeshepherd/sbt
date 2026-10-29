package tests

import org.scalatest.FreeSpec

class SelectionTest extends FreeSpec {
  "selected test" in {
    assert(true)
  }

  "unselected test" in {
    fail("This test should not run when selecting a single test")
  }
}
