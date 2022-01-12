package fr.alexdoru.sidebarmod.gui.config;

import fr.alexdoru.sidebarmod.SidebarMod;
import fr.alexdoru.sidebarmod.gui.GuiSidebar;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;

public class ConfigGuiScreenWithSidebar extends GuiScreen {

    protected final SidebarMod mod;
    protected final GuiSidebar sidebar;
    private boolean dragging;
    private int lastX;
    private int lastY;

    public ConfigGuiScreenWithSidebar(SidebarMod mod) {
        this.mod = mod;
        this.sidebar = mod.getSidebarGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.mc.thePlayer != null) {
            ScoreObjective scoreObjective = this.mc.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1);
            if (scoreObjective != null) {
                this.sidebar.drawSidebar(scoreObjective, new ScaledResolution(this.mc));
            }
        }
        if (this.dragging) {
            this.sidebar.offsetX += mouseX - this.lastX;
            this.sidebar.offsetY += mouseY - this.lastY;
        }
        this.lastX = mouseX;
        this.lastY = mouseY;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.sidebar.contains(mouseX, mouseY)) {
            this.dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.dragging = false;
    }

    @Override
    public void onGuiClosed() {
        this.mod.saveConfig();
    }

    public String getSuffix(boolean enabled) {
        return enabled ? (EnumChatFormatting.GREEN + "Enabled") : (EnumChatFormatting.RED + "Disabled");
    }

    public int getRowPos(int rowNumber) {
        return this.height / 4 + 24 * rowNumber - 40;
    }

    public int getCenter() {
        return this.width / 2;
    }

}