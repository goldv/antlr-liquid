package com.goldv.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by goldv on 27/04/2015.
 */
public class Context {

  private final Map<String, Object> scope;

  private final Stack<ChildContext> childContexts = new Stack<>();

  public Context(){
    this.scope = new HashMap<>();
  }

  public Context(Map<String, Object> scope){
    this.scope = scope;
  }

  public ChildContext deleteChild(){
    return childContexts.pop();
  }

  public void add(String field, Object value){
    scope.put(field, value);
  }

  public Map<String, Object> getScope(){ return scope; }

  public ChildContext createChild(){
    ChildContext cc = new ChildContext(this);
    childContexts.push(cc);
    return cc;
  }

  public Double getAsNumber(String field){
    Object value = getValue(field);
    if(value != null){
      if(value instanceof Integer) return ((Integer) value).doubleValue();
      else if(value instanceof Long) return ((Long) value).doubleValue();
      else if(value instanceof Double) return ((Double) value);
    }

    return null;
  }

  public String getAsString(String field){
    Object value = getValue(field);
    if(value != null){
      return value.toString();
    }

    return null;
  }

  public Collection<Object> getAsCollection(String field){
    Object value = getValue(field);
    if(value != null && value instanceof Collection){
      return (Collection)value;
    }

    return null;
  }

  public Boolean getAsBoolean(String field){
    Object value = getValue(field);
    if(value != null && value instanceof Boolean){
      return (Boolean)value;
    }

    return null;
  }

  private Object getValue(String field){
    // first check the child scopes
    Object childValue = getValueFromChildContext(field);
    if(childValue != null) return childValue;
    else return getValue(field, scope);
  }

  private Object getValueFromChildContext(String field){

    for( ChildContext child : childContexts){
      Object value = getValue(field, child.getScope());
      if(value != null) return value;
    }

    return null;
  }

  private Object getValue(String field, Map<String, Object> searchScope){

    int delimIdx = field.indexOf(".");
    if(delimIdx < 0){
      return searchScope.get(field);
    } else {
      String nextField = field.substring(0, delimIdx);

      Object next = searchScope.get(nextField);
      if(next != null && next instanceof Map){
        return getValue(field.substring(delimIdx + 1), ((Map) next));
      } else {
        return null;
      }
    }
  }
}
