package fr.alexdoru.sidebarmod;

import fr.alexdoru.sidebarmod.command.CommandSidebar;
import fr.alexdoru.sidebarmod.gui.CustomSidebar;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

@Mod(modid = "sidebarmod", name = "Sidebar Mod", version = "2.1", acceptedMinecraftVersions = "[1.8.9]")
public class SidebarMod {

    private File configFile;
    private Configuration config;
    private CustomSidebar customSidebar;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.configFile = event.getSuggestedConfigurationFile();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new CommandSidebar(this));
        this.customSidebar = new CustomSidebar();
        config = new Configuration(this.configFile);
        loadConfig();
    }

    public CustomSidebar getSidebarGui() {
        return this.customSidebar;
    }

    public void saveConfig() {
        updateConfig(config, false);
        config.save();
    }

    private void loadConfig() {
        config.load();
        updateConfig(config, true);
    }

    private void updateConfig(Configuration config, boolean load) {
        Property prop = config.get("General", "enabled", true);
        if (load) {
            this.customSidebar.enabled = prop.getBoolean();
        } else {
            prop.setValue(this.customSidebar.enabled);
        }
        prop = config.get("General", "offsetX", 0);
        if (load) {
            this.customSidebar.offsetX = prop.getInt();
        } else {
            prop.setValue(this.customSidebar.offsetX);
        }
        prop = config.get("General", "offsetY", 0);
        if (load) {
            this.customSidebar.offsetY = prop.getInt();
        } else {
            prop.setValue(this.customSidebar.offsetY);
        }
        prop = config.get("General", "scale", 1.0D);
        if (load) {
            this.customSidebar.scale = (float) prop.getDouble();
        } else {
            prop.setValue(this.customSidebar.scale);
        }
        prop = config.get("General", "rednumbers", true);
        if (load) {
            this.customSidebar.redNumbers = prop.getBoolean();
        } else {
            prop.setValue(this.customSidebar.redNumbers);
        }
        prop = config.get("General", "shadow", false);
        if (load) {
            this.customSidebar.shadow = prop.getBoolean();
        } else {
            prop.setValue(this.customSidebar.shadow);
        }
        prop = config.get("Color", "rgb", 0);
        if (load) {
            this.customSidebar.color = prop.getInt();
        } else {
            prop.setValue(this.customSidebar.color);
        }
        prop = config.get("Color", "alpha", 50);
        if (load) {
            this.customSidebar.alpha = prop.getInt();
        } else {
            prop.setValue(this.customSidebar.alpha);
        }
        prop = config.get("Chroma", "enabled", false);
        if (load) {
            this.customSidebar.chromaEnabled = prop.getBoolean();
        } else {
            prop.setValue(this.customSidebar.chromaEnabled);
        }
        prop = config.get("Chroma", "speed", 2);
        if (load) {
            this.customSidebar.chromaSpeed = prop.getInt();
        } else {
            prop.setValue(this.customSidebar.chromaSpeed);
        }
    }
}