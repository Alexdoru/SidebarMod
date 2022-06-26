package fr.alexdoru.sidebarmod.gui.config;

import fr.alexdoru.sidebarmod.SidebarMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.client.config.GuiSlider;

public class ConfigGuiScreen extends ConfigGuiScreenWithSidebar {

    private GuiButton buttonToggle;
    private GuiButton buttonNumbers;
    private GuiButton buttonShadow;
    private GuiSlider sliderScale;

    public ConfigGuiScreen(SidebarMod mod) {
        super(mod);
    }

    @Override
    public void initGui() {
        this.buttonList.add(buttonToggle = new GuiButton(0, getCenter() - 75, getRowPos(1), 150, 20, "Sidebar: " + getSuffix(this.sidebar.enabled)));
        this.buttonList.add(buttonNumbers = new GuiButton(1, getCenter() - 75, getRowPos(2), 150, 20, "Red Numbers: " + getSuffix(this.sidebar.redNumbers)));
        this.buttonList.add(buttonShadow = new GuiButton(2, getCenter() - 75, getRowPos(3), 150, 20, "Text Shadow: " + getSuffix(this.sidebar.shadow)));
        this.buttonList.add(new GuiButton(3, getCenter() - 75, getRowPos(4), 150, 20, "Change Background"));
        this.buttonList.add(this.sliderScale = new GuiSlider(4, getCenter() - 75, getRowPos(5), 150, 20, "Scale: ", "%", 50.0D, 200.0D, Math.round(this.sidebar.scale * 100.0F), false, true));
        this.buttonList.add(new GuiButton(5, getCenter() - 75, getRowPos(6), 150, 20, "Reset Settings"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.sliderScale.dragging) {
            this.sidebar.scale = this.sliderScale.getValueInt() / 100.0F;
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                this.sidebar.enabled = !this.sidebar.enabled;
                button.displayString = "Sidebar: " + getSuffix(this.sidebar.enabled);
                break;
            case 1:
                this.sidebar.redNumbers = !this.sidebar.redNumbers;
                button.displayString = "Red Numbers: " + getSuffix(this.sidebar.redNumbers);
                break;
            case 2:
                this.sidebar.shadow = !this.sidebar.shadow;
                button.displayString = "Text Shadow: " + getSuffix(this.sidebar.shadow);
                break;
            case 3:
                this.mc.displayGuiScreen(new ConfigGuiScreenColors(this, this.mod));
                break;
            case 5:
                this.sidebar.enabled = true;
                this.sidebar.redNumbers = true;
                this.sidebar.shadow = false;
                this.sidebar.color = 0;
                this.sidebar.alpha = 50;
                this.sidebar.chromaEnabled = false;
                this.sidebar.chromaSpeed = 2;
                this.sidebar.scale = 1.0F;
                this.sidebar.offsetY = this.sidebar.offsetX = 0;

                this.buttonToggle.displayString = "Sidebar: " + getSuffix(true);
                this.buttonNumbers.displayString = "Red Numbers: " + getSuffix(true);
                this.buttonShadow.displayString = "Text Shadow: " + getSuffix(false);
                this.sliderScale.setValue(100.0D);
                this.sliderScale.updateSlider();
                break;
        }
    }

}