package fr.alexdoru.sidebarmod;

import fr.alexdoru.sidebarmod.gui.screen.GuiScreenSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CommandSidebar extends CommandBase {

    private final SidebarMod mod;

    public CommandSidebar(SidebarMod mod) {
        this.mod = mod;
    }

    public String getCommandName() {
        return "sidebarmod";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "/sidebarmod";
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    public void processCommand(ICommandSender sender, String[] args) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(new GuiScreenSettings(this.mod));
    }
}