package com.applicationsec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

public class TaintedGraph
{
  public static final HashMap<String, TaintedNode> taints = new HashMap<>();

  public static void add(TaintedNode taint){
    taints.put(taint.value, taint);
    System.out.println("IAST Detector: Adding a new tainted data to the pool: " + taint.value);
  }

  public static TaintedNode get(String value){
    return taints.get(value);
  }

  public static boolean has(String value){
    return taints.containsKey(value);
  }
}
