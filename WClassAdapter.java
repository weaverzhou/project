package weaver.aop;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class WClassAdapter extends ClassAdapter {
    private String[] methods;
    private String className;
    private String targetClassName;

    public WClassAdapter(ClassVisitor cv, String className, String targetClassName, String[] methods) {
        super(cv);
        this.methods = methods;
        this.className = className;
        this.targetClassName = targetClassName;
    }

    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        // System.out.println("desc=" + desc + ";sign=" + signature);
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (mv == null || (access & (Opcodes.ACC_ABSTRACT | Opcodes.ACC_NATIVE)) > 0) {
            return mv;
        } else {
            for (int i = 2; i < methods.length; i++) {
                String[] ma = methods[i].split("=");
               
                if ((name + desc).equals(ma[0])) {
                    return new WAdviceAdapter(mv, access, this.className, this.targetClassName, name, ma[1], desc, signature, exceptions);
                }
            }
        }
        return mv;
    }
}
