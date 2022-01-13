package fr.alexdoru.sidebarmod.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class GuiIngameTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        try {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(basicClass);
            classReader.accept(classNode, 0);
            for (MethodNode methodNode : classNode.methods) {
                if (methodNode.name.equals(ASMLoadingPlugin.isObf ? "a" : "renderScoreboard") && methodNode.desc.equals(ASMLoadingPlugin.isObf ? "(Lauk;Lavr;)V" : "(Lnet/minecraft/scoreboard/ScoreObjective;Lnet/minecraft/client/gui/ScaledResolution;)V")) {
                    InsnList list = new InsnList();
                    list.add(new FieldInsnNode(GETSTATIC, "fr/alexdoru/sidebarmod/gui/CustomSidebar", "INSTANCE", "Lfr/alexdoru/sidebarmod/gui/CustomSidebar;"));
                    list.add(new VarInsnNode(ALOAD, 1));
                    list.add(new VarInsnNode(ALOAD, 2));
                    list.add(new MethodInsnNode(INVOKEVIRTUAL,
                            "fr/alexdoru/sidebarmod/gui/CustomSidebar",
                            "drawSidebar",
                            ASMLoadingPlugin.isObf ? "(Lauk;Lavr;)V" : "(Lnet/minecraft/scoreboard/ScoreObjective;Lnet/minecraft/client/gui/ScaledResolution;)V",
                            false));
                    list.add(new InsnNode(RETURN));
                    methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), list);
                    System.out.println("-------------TRANSFORMED GUI INGAME");
                    break;
                }
            }
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return basicClass;
    }
}
