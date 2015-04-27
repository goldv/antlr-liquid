package com.goldv.context;

/**
 * Created by goldv on 27/04/2015.
 */
public class ChildContext extends Context{

  private final Context parent;

  public ChildContext(Context parent){
    this.parent = parent;
  }

  public void delete(){
    this.parent.deleteChild();
  }

}
