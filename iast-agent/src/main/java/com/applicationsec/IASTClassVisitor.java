package com.applicationsec;


import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.ArrayList;
import java.util.List;

public class IASTClassVisitor extends ClassVisitor {

    protected String className;

    public IASTClassVisitor(ClassVisitor visitor, String className){
        super(Opcodes.ASM7, visitor);
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(int methodAccess, String methodName, String methodDesc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(methodAccess, methodName, methodDesc, signature, exceptions);
        return new IASTAdviceAdapter(Opcodes.ASM7, methodVisitor, methodAccess, methodName, methodDesc,className);
    }

    public static class IASTAdviceAdapter extends AdviceAdapter {
        private final String methodName;

        private final String className;
        private static final List<String> TAINT_ANNOTATIONS = List.of("Lorg/springframework/web/bind/annotation/RequestParam;");
        private final ArrayList<Integer> taintedParams = new ArrayList<>();

        protected IASTAdviceAdapter(int api, MethodVisitor methodVisitor, int methodAccess, String methodName, String methodDesc, String className) {
            super(api, methodVisitor, methodAccess, methodName, methodDesc);
            this.methodName = methodName;
            this.className = className;
        }

        @Override
        public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
            if(name.contains("makeConcat")){
                dup2();
                mv.visitVarInsn(ASTORE,  8);
                mv.visitVarInsn(ASTORE,  9);
            }
            super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
            dup();
            mv.visitVarInsn(ALOAD, 8);
            mv.visitVarInsn(ALOAD,  9);
            mv.visitLdcInsn(methodName);
            mv.visitLdcInsn(className);
            mv.visitMethodInsn(INVOKESTATIC, "com/applicationsec/TaintManager", "onNewPropagation", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false);
        }



        @Override
        protected void onMethodEnter() {
            if (!taintedParams.isEmpty()) {
                for (Integer taintedParam : taintedParams) {
                    mv.visitTypeInsn(NEW, "com/applicationsec/TaintedNode");
                    mv.visitInsn(DUP);
                    mv.visitVarInsn(ALOAD, taintedParam);
                    mv.visitLdcInsn(methodName);
                    mv.visitLdcInsn(className);
                    mv.visitInsn(ACONST_NULL);
                    mv.visitMethodInsn(INVOKESPECIAL, "com/applicationsec/TaintedNode", "<init>",
                            "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/applicationsec/TaintedNode;)V", false);
                    mv.visitVarInsn(ASTORE, 5);
                    mv.visitVarInsn(ALOAD, 5);
                    mv.visitMethodInsn(INVOKESTATIC, "com/applicationsec/TaintedGraph", "add", "(Lcom/applicationsec/TaintedNode;)V", false);
                }
            }

        }


        @Override
        public AnnotationVisitor visitParameterAnnotation(
                final int parameter, final String descriptor, final boolean visible) {
            if(TAINT_ANNOTATIONS.contains(descriptor)){
                taintedParams.add(parameter+1);
            }
            return super.visitParameterAnnotation(parameter, descriptor, visible);
        }

    }

}
