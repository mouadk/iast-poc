package com.applicationsec;

import java.util.ArrayList;

public class TaintedStorage {
    private static final ArrayList<String> taints = new ArrayList<>();

    public static void add(String taint){
        taints.add(taint);
    }
}
