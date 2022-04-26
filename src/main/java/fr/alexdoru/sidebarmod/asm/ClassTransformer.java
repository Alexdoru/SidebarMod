package fr.alexdoru.sidebarmod.asm;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) {
            return null;
        }
        if (transformedName.equals("net.minecraftforge.client.GuiIngameForge")) {
            return new GuiIngameForgeTransformer().transform(name, transformedName, basicClass);
        }
        return basicClass;
    }
}
