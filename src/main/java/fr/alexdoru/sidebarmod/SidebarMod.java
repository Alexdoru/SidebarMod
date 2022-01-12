package fr.alexdoru.sidebarmod;

import fr.alexdoru.sidebarmod.gui.GuiSidebar;
import fr.alexdoru.sidebarmod.gui.GuiSidebarIngame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;

@Mod(modid = "sidebarmod", name = "Sidebar Mod", version = "2.0", acceptedMinecraftVersions = "[1.8.9]")
public class SidebarMod {
  private Minecraft mc = Minecraft.getMinecraft();
  
  private File saveFile;
  
  private GuiSidebar guiSidebar;
  
  private GuiSidebarIngame ingame;
  
  @EventHandler
  public void init(FMLInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(this);
    ClientCommandHandler.instance.registerCommand((ICommand)new CommandSidebar(this));
    this.saveFile = new File(this.mc.mcDataDir, "config/SidebarMod.config");
    this.guiSidebar = new GuiSidebar();
    this.ingame = new GuiSidebarIngame(this, this.mc);
    loadConfig();
  }
  
  @SubscribeEvent
  public void onClientTick(TickEvent.ClientTickEvent event) {
    if (!(this.mc.ingameGUI instanceof GuiSidebarIngame))
      this.mc.ingameGUI = (GuiIngame)this.ingame; 
  }
  
  public GuiSidebar getSidebarGui() {
    return this.guiSidebar;
  }
  
  public void saveConfig() {
    Configuration config = new Configuration(this.saveFile);
    updateConfig(config, false);
    config.save();
  }
  
  public void loadConfig() {
    Configuration config = new Configuration(this.saveFile);
    config.load();
    updateConfig(config, true);
  }
  
  private void updateConfig(Configuration config, boolean load) {
    Property prop = config.get("General", "enabled", true);
    if (load) {
      this.guiSidebar.enabled = prop.getBoolean();
    } else {
      prop.setValue(this.guiSidebar.enabled);
    } 
    prop = config.get("General", "offsetX", 0);
    if (load) {
      this.guiSidebar.offsetX = prop.getInt();
    } else {
      prop.setValue(this.guiSidebar.offsetX);
    } 
    prop = config.get("General", "offsetY", 0);
    if (load) {
      this.guiSidebar.offsetY = prop.getInt();
    } else {
      prop.setValue(this.guiSidebar.offsetY);
    } 
    prop = config.get("General", "scale", 1.0D);
    if (load) {
      this.guiSidebar.scale = (float)prop.getDouble();
    } else {
      prop.setValue(this.guiSidebar.scale);
    } 
    prop = config.get("General", "rednumbers", true);
    if (load) {
      this.guiSidebar.redNumbers = prop.getBoolean();
    } else {
      prop.setValue(this.guiSidebar.redNumbers);
    } 
    prop = config.get("General", "shadow", false);
    if (load) {
      this.guiSidebar.shadow = prop.getBoolean();
    } else {
      prop.setValue(this.guiSidebar.shadow);
    } 
    prop = config.get("Color", "rgb", 0);
    if (load) {
      this.guiSidebar.color = prop.getInt();
    } else {
      prop.setValue(this.guiSidebar.color);
    } 
    prop = config.get("Color", "alpha", 50);
    if (load) {
      this.guiSidebar.alpha = prop.getInt();
    } else {
      prop.setValue(this.guiSidebar.alpha);
    } 
    prop = config.get("Chroma", "enabled", false);
    if (load) {
      this.guiSidebar.chromaEnabled = prop.getBoolean();
    } else {
      prop.setValue(this.guiSidebar.chromaEnabled);
    } 
    prop = config.get("Chroma", "speed", 2);
    if (load) {
      this.guiSidebar.chromaSpeed = prop.getInt();
    } else {
      prop.setValue(this.guiSidebar.chromaSpeed);
    } 
  }
}


/* Location:              D:\Minecraft\Mods\1.8.9 mods\[1.8.9] Sidebar Mod Revamp-deobf.jar!\revamp\sidebarmod\SidebarMod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */