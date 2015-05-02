package com.goldv.context

import org.specs2.mutable.Specification

/**
 * Created by vince on 02/05/15.
 */
class ContextSpec extends Specification{


  "Context" should{
    "resolve basic value" in{
      val c = Context()
      c.put("test", "value")
      c.getString("test") mustEqual Some("value")
    }

    "resolve nested value" in{
      val c = Context()
      val m = Map("something" -> "value")
      c.put("test", m)
      c.getString("test.something") mustEqual Some("value")
    }

    "resolve a number" in{
      val c = Context()
      c.put("test", 2.0)
      c.getDouble("test") mustEqual Some(2.0)
    }

    "resolve a collection" in{
      val l = List("one", "two")
      val c = Context()
      c.put("test", l)

      c.getList("test") mustEqual Some(l)
    }
    "resolve child context" in{
      val c = Context()
      c.put("test", "value")
      val cc = c.createChild
      cc.put("test", "child-value")


      c.getString("test") mustEqual Some("child-value")
      c.deleteChild
      c.getString("test") mustEqual Some("value")
    }
  }
}
