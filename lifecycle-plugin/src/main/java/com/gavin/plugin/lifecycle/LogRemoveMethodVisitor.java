package com.gavin.plugin.lifecycle;


import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

import java.util.Arrays;

/**
 * @author gavin
 * @date 2019/2/19
 */
public class LogRemoveMethodVisitor extends MethodVisitor {

    public LogRemoveMethodVisitor(MethodVisitor mv) {
        super(Opcodes.ASM4, mv);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        //方法执行前插入
//        mv.visitLdcInsn("TAG");
//        mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
//        mv.visitInsn(Opcodes.DUP);
//        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
//        mv.visitLdcInsn("-------> onDestroy : ");
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
//        mv.visitVarInsn(Opcodes.ALOAD, 0);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getSimpleName", "()Ljava/lang/String;", false);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
//        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
//        mv.visitInsn(Opcodes.POP);
    }

    //184
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        System.out.println("visitMethodInsn opcode = " + opcode +" " + (opcode == Opcodes.INVOKESTATIC ? "INVOKESTATIC" : "") + ", owner = " + owner + ", name = " + name + ", desc = " + desc + ", itf = " + itf);
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    @Override
    public void visitInsn(int opcode) {
        System.out.println("visitInsn opcode: " + Integer.toHexString(opcode) + " = " + opcode);
        super.visitInsn(opcode);
    }

    public LogRemoveMethodVisitor(int api) {
        super(api);
    }

    public LogRemoveMethodVisitor(int api, MethodVisitor mv) {
        super(api, mv);
    }

    @Override
    public void visitParameter(String name, int access) {
        System.out.println("name = " + name + ", access = " + access);
        super.visitParameter(name, access);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        System.out.println();
        return super.visitAnnotationDefault();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        System.out.println("desc = " + desc + ", visible = " + visible);
        return super.visitAnnotation(desc, visible);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
        System.out.println("typeRef = " + typeRef + ", typePath = " + typePath + ", desc = " + desc + ", visible = " + visible);
        return super.visitTypeAnnotation(typeRef, typePath, desc, visible);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        System.out.println("parameter = " + parameter + ", desc = " + desc + ", visible = " + visible);
        return super.visitParameterAnnotation(parameter, desc, visible);
    }

    @Override
    public void visitAttribute(Attribute attr) {
        System.out.println("attr = " + attr);
        super.visitAttribute(attr);
    }

    @Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        System.out.println("type = " + type + ", nLocal = " + nLocal + ", local = " + Arrays.deepToString(local) + ", nStack = " + nStack + ", stack = " + Arrays.deepToString(stack));
        super.visitFrame(type, nLocal, local, nStack, stack);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        System.out.println("opcode = " + opcode + ", operand = " + operand);
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        System.out.println("opcode = " + opcode + ", var = " + var);
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        System.out.println("opcode = " + opcode + ", type = " + type);
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        System.out.println("opcode = " + opcode + ", owner = " + owner + ", name = " + name + ", desc = " + desc);
        super.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        System.out.println("opcode = " + opcode + ", owner = " + owner + ", name = " + name + ", desc = " + desc);
        super.visitMethodInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
        System.out.println("name = " + name + ", desc = " + desc + ", bsm = " + bsm + ", bsmArgs = " + Arrays.deepToString(bsmArgs));
        super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        System.out.println("opcode = " + opcode + ", label = " + label);
        super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitLabel(Label label) {
        System.out.println("label = " + label);
        super.visitLabel(label);
    }

    @Override
    public void visitLdcInsn(Object cst) {
        System.out.println("cst = " + cst);
        super.visitLdcInsn(cst);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        System.out.println("var = " + var + ", increment = " + increment);
        super.visitIincInsn(var, increment);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        System.out.println("min = " + min + ", max = " + max + ", dflt = " + dflt + ", labels = " + Arrays.deepToString(labels));
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        System.out.println("dflt = " + dflt + ", keys = " + Arrays.toString(keys) + ", labels = " + Arrays.deepToString(labels));
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        System.out.println("desc = " + desc + ", dims = " + dims);
        super.visitMultiANewArrayInsn(desc, dims);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
        System.out.println("typeRef = " + typeRef + ", typePath = " + typePath + ", desc = " + desc + ", visible = " + visible);
        return super.visitInsnAnnotation(typeRef, typePath, desc, visible);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        System.out.println("start = " + start + ", end = " + end + ", handler = " + handler + ", type = " + type);
        super.visitTryCatchBlock(start, end, handler, type);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
        System.out.println("typeRef = " + typeRef + ", typePath = " + typePath + ", desc = " + desc + ", visible = " + visible);
        return super.visitTryCatchAnnotation(typeRef, typePath, desc, visible);
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        System.out.println("name = " + name + ", desc = " + desc + ", signature = " + signature + ", start = " + start + ", end = " + end + ", index = " + index);
        super.visitLocalVariable(name, desc, signature, start, end, index);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String desc, boolean visible) {
        System.out.println("typeRef = " + typeRef + ", typePath = " + typePath + ", start = " + Arrays.deepToString(start) + ", end = " + Arrays.deepToString(end) + ", index = " + Arrays.toString(index) + ", desc = " + desc + ", visible = " + visible);
        return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        System.out.println("line = " + line + ", start = " + start);
        super.visitLineNumber(line, start);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        System.out.println("maxStack = " + maxStack + ", maxLocals = " + maxLocals);
        super.visitMaxs(maxStack, maxLocals);
    }

    @Override
    public void visitEnd() {
        System.out.println();
        super.visitEnd();
    }
}
