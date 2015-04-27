package com.goldv.context;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by goldv on 27/04/2015.
 */
public class ContextTest {

  @Test
  public void testBasicFieldRetrieval(){
    Context c = new Context();
    c.add("test", "value");
    assertEquals(c.getAsString("test"), "value");
  }

  @Test
  public void testDelimitedFieldRetrieval(){
    Context c = new Context();
    Map<String, Object> m = new HashMap<>();
    m.put("something", "value");
    c.add("test", m);
    assertEquals(c.getAsString("test.something"), "value");
  }

  @Test
  public void testBasicNumberRetrieval(){
    Context c = new Context();
    c.add("test", 1);
    assertEquals(c.getAsNumber("test"), new Double(1.0) );
  }

  @Test
  public void testBasicCollectionRetrieval(){
    List<String> l = new LinkedList<>();
    l.add("one");
    l.add("two");

    Context c = new Context();
    c.add("test", l);
    assertEquals(c.getAsCollection("test"),l );
  }

  @Test
  public void testDelimitedNumberFieldRetrieval(){
    Context c = new Context();
    Map<String, Object> m = new HashMap<>();
    m.put("something", 3);
    c.add("test", m);
    assertEquals(c.getAsNumber("test.something"), new Double(3));
  }

  @Test
  public void testChildContext(){
    Context c = new Context();
    c.add("test", "value");
    ChildContext cc = c.createChild();
    cc.add("test", "child-value");
    assertEquals(c.getAsString("test"), "child-value");

    cc.delete();
    assertEquals(c.getAsString("test"), "value");
  }
}
