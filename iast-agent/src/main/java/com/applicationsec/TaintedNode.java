package com.applicationsec;

public class TaintedNode {

  String value;
  String methodName;
  String className;
  TaintedNode parent;

  public TaintedNode(String value, String methodName, String className, TaintedNode parent){
    this.value = value;
    this.methodName = methodName;
    this.parent = parent;
    this.className = className;
  }

  public boolean isSource() {
    return parent == null;
  }

}
