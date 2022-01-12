package fr.alexdoru.sidebarmod.gui.screen;

import fr.alexdoru.sidebarmod.SidebarMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;

public class GuiScreenBackground extends GuiScreenSidebar {

    private final GuiScreen parent;
    private GuiButton buttonDone;
    private GuiSlider sliderRed;
    private GuiSlider sliderGreen;
    private GuiSlider sliderBlue;
    private GuiSlider sliderAlpha;
    private GuiSlider sliderChromaSpeed;

    public GuiScreenBackground(GuiScreen parent, SidebarMod mod) {
        super(mod);
        this.parent = parent;
    }

    public void initGui() {
        this.buttonList.add(new GuiButton(0, getCenter() - 75, getRowPos(1), 150, 20, "Chroma: " + getSuffix(this.sidebar.chromaEnabled)));
        this.buttonList.add(this.sliderRed = new GuiSlider(1, getCenter() - 75, getRowPos(2), 150, 20, "Red: ", "", 0.0D, 255.0D, (this.sidebar.color >> 16 & 0xFF), false, true));
        this.buttonList.add(this.sliderGreen = new GuiSlider(2, getCenter() - 75, getRowPos(3), 150, 20, "Green: ", "", 0.0D, 255.0D, (this.sidebar.color >> 8 & 0xFF), false, true));
        this.buttonList.add(this.sliderBlue = new GuiSlider(3, getCenter() - 75, getRowPos(4), 150, 20, "Blue: ", "", 0.0D, 255.0D, (this.sidebar.color & 0xFF), false, true));
        this.buttonList.add(this.sliderAlpha = new GuiSlider(4, getCenter() - 75, getRowPos(5), 150, 20, "Alpha: ", "", 0.0D, 255.0D, this.sidebar.alpha, false, true));
        this.buttonList.add(this.sliderChromaSpeed = new GuiSlider(3, getCenter() - 75, getRowPos(3), 150, 20, "Chroma Speed: ", "", 1.0D, 10.0D, this.sidebar.chromaSpeed, false, true));
        this.buttonList.add(this.buttonDone = new GuiButton(6, getCenter() - 75, getRowPos(6), 150, 20, "Done"));
        setSlidersVisibility();
    }

    public int getRowPos(int rowNumber) {
        return this.height / 4 + 24 * rowNumber - 40;
    }

    public int getCenter() {
        return this.width / 2;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        updateSettings();
    }

    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                this.sidebar.chromaEnabled = !this.sidebar.chromaEnabled;
                button.displayString = "Chroma: " + getSuffix(this.sidebar.chromaEnabled);
                setSlidersVisibility();
                break;
            case 6:
                this.mc.displayGuiScreen(this.parent);
                break;
        }
    }

    private void setSlidersVisibility() {
        boolean mode = this.sidebar.chromaEnabled;
        this.sliderRed.visible = !mode;
        this.sliderGreen.visible = !mode;
        this.sliderBlue.visible = !mode;
        this.sliderChromaSpeed.visible = mode;
        this.sliderAlpha.yPosition = mode ? getRowPos(2) : getRowPos(5);
        this.buttonDone.yPosition = mode ? getRowPos(4) : getRowPos(6);
    }

    private void updateSettings() {
        this.sidebar.color = this.sliderRed.getValueInt() << 16 | this.sliderGreen.getValueInt() << 8 | this.sliderBlue.getValueInt();
        this.sidebar.alpha = this.sliderAlpha.getValueInt();
        this.sidebar.chromaSpeed = this.sliderChromaSpeed.getValueInt();
    }

    private String getSuffix(boolean enabled) {
        return enabled ? (EnumChatFormatting.GREEN + "Enabled") : (EnumChatFormatting.RED + "Disabled");
    }
}