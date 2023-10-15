package com.applicationsec;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class IASTClassTransformer implements ClassFileTransformer {

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            if (className.equals("com/applicationsec/Controller")) {
                ClassReader reader = new ClassReader(classfileBuffer);
                ClassWriter writer = new ClassWriter(new ClassReader(classfileBuffer), ClassWriter.COMPUTE_MAXS);
                reader.accept(new IASTClassVisitor(writer, className), ClassReader.EXPAND_FRAMES);
                byte[] enhanced = writer.toByteArray();
                File file = new File("NewController.class");
                FileOutputStream outputStream;
                try {
                    outputStream = new FileOutputStream(file);
                    outputStream.write(enhanced);
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return enhanced;
        } else if (className.equals("org/springframework/jdbc/core/JdbcTemplate") ){
              ClassReader reader = new ClassReader(classfileBuffer);
              ClassWriter writer = new ClassWriter(new ClassReader(classfileBuffer), ClassWriter.COMPUTE_MAXS);
              reader.accept(new IASTSinkClassVisitor(writer), ClassReader.EXPAND_FRAMES);
              byte[] enhanced = writer.toByteArray();
              File file = new File("NewJdbcTemplate.class");
              FileOutputStream outputStream;
              try {
                outputStream = new FileOutputStream(file);
                outputStream.write(enhanced);
                outputStream.close();
              } catch (IOException e) {
                e.printStackTrace();
              }
              return enhanced;
            }
            else {
              return null;
          }
        }
}
