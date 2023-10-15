package com.applicationsec;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Arrays;
import java.util.jar.JarFile;

public class IASTAgent {

    public static void premain(String args, Instrumentation instrumentation) throws IOException, UnmodifiableClassException {
       initialize(instrumentation);
    }

    public static void initialize(Instrumentation inst) throws IOException, UnmodifiableClassException {
        var dir = System.getProperty("user.dir");
        JarFile file = new JarFile(dir + "/iast-agent/target/iast-agent-1.0-SNAPSHOT.jar");
        inst.appendToBootstrapClassLoaderSearch(file);
        Class clazz = null;
        Class[] classes =  inst.getAllLoadedClasses();
        for (Class cls : classes) {
            if(cls.getName().contains("StringConcatFactory")){
                clazz = cls;
            }
        }
        inst.addTransformer(new IASTClassTransformer(), true);
        inst.retransformClasses(clazz);
    }

}
