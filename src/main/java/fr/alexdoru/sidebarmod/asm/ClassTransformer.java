package fr.alexdoru.sidebarmod.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ClassTransformer implements IClassTransformer, Opcodes {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if ("net.minecraftforge.client.GuiIngameForge".equals(transformedName)) {
            return transformGuiIngameForge(basicClass);
        }
        return basicClass;
    }

    private static byte[] transformGuiIngameForge(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals(ASMLoadingPlugin.isObf ? "a" : "renderGameOverlay") && methodNode.desc.equals("(F)V")) {
                for (AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
                    if (isRenderScoreboardNode(insnNode)) {
                        AbstractInsnNode previous = insnNode.getPrevious();
                        for (int i = 0; i < 3; i++) {
                            previous = previous.getPrevious();
                        }
                        if (isAload0(previous)) {
                            methodNode.instructions.insertBefore(previous, new FieldInsnNode(
                                    GETSTATIC,
                                    "fr/alexdoru/sidebarmod/gui/CustomSidebar",
                                    "INSTANCE",
                                    "Lfr/alexdoru/sidebarmod/gui/CustomSidebar;"));
                            methodNode.instructions.insertBefore(insnNode, new MethodInsnNode(
                                    INVOKEVIRTUAL,
                                    "fr/alexdoru/sidebarmod/gui/CustomSidebar",
                                    "drawSidebar",
                                    ASMLoadingPlugin.isObf ? "(Lauk;Lavr;)V" : "(Lnet/minecraft/scoreboard/ScoreObjective;Lnet/minecraft/client/gui/ScaledResolution;)V",
                                    false));
                            methodNode.instructions.remove(previous);
                            methodNode.instructions.remove(insnNode);
                        }
                    }
                }
            }
        }
        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    private static boolean isAload0(AbstractInsnNode insnNode) {
        return insnNode instanceof VarInsnNode && insnNode.getOpcode() == ALOAD && ((VarInsnNode) insnNode).var == 0;
    }

    private static boolean isRenderScoreboardNode(AbstractInsnNode insnNode) {
        return insnNode instanceof MethodInsnNode && insnNode.getOpcode() == INVOKEVIRTUAL
                && ((MethodInsnNode) insnNode).name.equals(ASMLoadingPlugin.isObf ? "a" : "renderScoreboard")
                && ((MethodInsnNode) insnNode).owner.equals("net/minecraftforge/client/GuiIngameForge")
                && ((MethodInsnNode) insnNode).desc.equals(ASMLoadingPlugin.isObf ? "(Lauk;Lavr;)V" : "(Lnet/minecraft/scoreboard/ScoreObjective;Lnet/minecraft/client/gui/ScaledResolution;)V");
    }

}
