package fr.alexdoru.sidebarmod.gui.screen;

import fr.alexdoru.sidebarmod.SidebarMod;
import fr.alexdoru.sidebarmod.gui.GuiSidebar;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;

import java.io.IOException;

public class GuiScreenSidebar extends GuiScreen {
  protected SidebarMod mod;
  
  protected GuiSidebar sidebar;
  
  private boolean dragging;
  
  private int lastX;
  
  private int lastY;
  
  public GuiScreenSidebar(SidebarMod mod) {
    this.mod = mod;
    this.sidebar = mod.getSidebarGui();
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    if (this.mc.thePlayer != null) {
      ScoreObjective scoreObjective = this.mc.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1);
      if (scoreObjective != null)
        this.sidebar.drawSidebar(scoreObjective, new ScaledResolution(this.mc)); 
    } 
    if (this.dragging) {
      this.sidebar.offsetX += mouseX - this.lastX;
      this.sidebar.offsetY += mouseY - this.lastY;
    } 
    this.lastX = mouseX;
    this.lastY = mouseY;
  }
  
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    if (this.sidebar.contains(mouseX, mouseY))
      this.dragging = true; 
  }
  
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    super.mouseReleased(mouseX, mouseY, state);
    this.dragging = false;
  }
  
  public void onGuiClosed() {
    this.mod.saveConfig();
  }
}


/* Location:              D:\Minecraft\Mods\1.8.9 mods\[1.8.9] Sidebar Mod Revamp-deobf.jar!\revamp\sidebarmod\gui\screen\GuiScreenSidebar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */