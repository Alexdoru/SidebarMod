package fr.alexdoru.sidebarmod.gui;

import fr.alexdoru.sidebarmod.SidebarMod;
import fr.alexdoru.sidebarmod.gui.config.ConfigGuiScreenWithSidebar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.client.GuiIngameForge;

public class GuiSidebarIngame extends GuiIngameForge { // TODO delete class and call via ASM

    private final SidebarMod mod;

    public GuiSidebarIngame(SidebarMod mod, Minecraft mc) {
        super(mc);
        this.mod = mod;
    }

    @Override
    protected void renderScoreboard(ScoreObjective scoreObjective, ScaledResolution scaledResolution) {
        if (this.mc.currentScreen != null && this.mc.currentScreen instanceof ConfigGuiScreenWithSidebar)
            return;
        this.mod.getSidebarGui().drawSidebar(scoreObjective, scaledResolution);
    }
}