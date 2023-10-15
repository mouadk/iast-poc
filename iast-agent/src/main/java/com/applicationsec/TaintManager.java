package com.applicationsec;

public class TaintManager {

  public static void onNewPropagation(String finalValue, String value, String concatenatedWith, String propagator, String className){
    if(TaintedGraph.has(concatenatedWith)){
      System.out.println("IAST Detector: Propagation of Tainted value: " + concatenatedWith);
      TaintedGraph.add(new TaintedNode(finalValue,propagator, className, TaintedGraph.get(concatenatedWith) ));
    }
    if(TaintedGraph.has(value)){
      System.out.println("IAST Detector: Propagation of Tainted value: " + value);
      TaintedGraph.add(new TaintedNode(finalValue,propagator, className, TaintedGraph.get(value)));
    }
  }

  public static void onSink(String value){
    if(TaintedGraph.has(value)){
      System.out.println("-------------IAST Detector - SQL Injection: Tainted data reached Sensitive Sink-------------");
      var tained =  TaintedGraph.get(value);
      while (tained != null){
        if(tained.isSource()){
          System.out.println("Source: "+ tained.className+ "#"+tained.methodName);

        }else {
          System.out.println("Propagator: "+ tained.className+ "#"+tained.methodName);
        }
        tained = tained.parent;
      }

    }
  }

}
