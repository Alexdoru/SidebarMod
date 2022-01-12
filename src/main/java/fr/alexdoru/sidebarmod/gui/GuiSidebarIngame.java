package fr.alexdoru.sidebarmod.gui;

import fr.alexdoru.sidebarmod.SidebarMod;
import fr.alexdoru.sidebarmod.gui.screen.GuiScreenSidebar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.client.GuiIngameForge;

public class GuiSidebarIngame extends GuiIngameForge {

    private final SidebarMod mod;

    public GuiSidebarIngame(SidebarMod mod, Minecraft mc) {
        super(mc);
        this.mod = mod;
    }

    protected void renderScoreboard(ScoreObjective objective, ScaledResolution res) {
        if (this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiScreenSidebar)
            return;
        this.mod.getSidebarGui().drawSidebar(objective, res);
    }
}