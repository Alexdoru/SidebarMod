package fr.alexdoru.sidebarmod.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class GuiIngameForgeTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        try {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(basicClass);
            classReader.accept(classNode, 0);
            for (MethodNode methodNode : classNode.methods) {
                if (methodNode.name.equals(ASMLoadingPlugin.isObf ? "a" : "renderGameOverlay") && methodNode.desc.equals("(F)V")) {
                    for (AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
                        if (insnNode instanceof MethodInsnNode && insnNode.getOpcode() == INVOKEVIRTUAL
                                && ((MethodInsnNode) insnNode).name.equals(ASMLoadingPlugin.isObf ? "a" : "renderScoreboard")
                                && ((MethodInsnNode) insnNode).owner.equals("net/minecraftforge/client/GuiIngameForge")
                                && ((MethodInsnNode) insnNode).desc.equals(ASMLoadingPlugin.isObf ? "(Lauk;Lavr;)V" : "(Lnet/minecraft/scoreboard/ScoreObjective;Lnet/minecraft/client/gui/ScaledResolution;)V")) {
                            AbstractInsnNode previous = insnNode.getPrevious();
                            for (int i = 0; i < 3; i++) {
                                previous = previous.getPrevious();
                            }
                            if (previous instanceof VarInsnNode && previous.getOpcode() == ALOAD) {
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
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return basicClass;
    }

}
