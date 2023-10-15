package com.applicationsec;


import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class IASTSinkClassVisitor extends ClassVisitor {

    public IASTSinkClassVisitor(ClassVisitor visitor){
        super(Opcodes.ASM7, visitor);
    }

    @Override
    public MethodVisitor visitMethod(int methodAccess, String methodName, String methodDesc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(methodAccess, methodName, methodDesc, signature, exceptions);
        if(methodName.equals("execute") && methodDesc.contains("(Ljava/lang/String;)V")){
            return new IASTAdviceAdapter(Opcodes.ASM7, methodVisitor, methodAccess, methodName, methodDesc);
        }
        return methodVisitor;
    }

    public static class IASTAdviceAdapter extends AdviceAdapter {
        private final String methodName;

        protected IASTAdviceAdapter(int api, MethodVisitor methodVisitor, int methodAccess, String methodName, String methodDesc) {
            super(api, methodVisitor, methodAccess, methodName, methodDesc);
            this.methodName = methodName;
        }



        @Override
        protected void onMethodEnter() {
           if(methodName.equals("execute") && methodDesc.contains("(Ljava/lang/String;)V")){
               mv.visitVarInsn(ALOAD, 1);
               mv.visitMethodInsn(INVOKESTATIC, "com/applicationsec/TaintManager", "onSink", "(Ljava/lang/String;)V", false);
           }
        }

        @Override
        public void visitMaxs(final int maxStack, final int maxLocals) {
            if (methodName.equals("execute") && methodDesc.contains("(Ljava/lang/String;)V")) {
                mv.visitMaxs(6, 8);
            }
        }

    }

}
