package fr.alexdoru.sidebarmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomSidebar extends Gui {

    public static CustomSidebar INSTANCE;

    private final FontRenderer fontRendererObj = (Minecraft.getMinecraft()).fontRendererObj;
    private int sidebarX;
    private int sidebarY;
    private int sidebarWidth;
    private int sidebarHeight;
    public boolean enabled;
    public boolean redNumbers;
    public boolean shadow;
    public float scale;
    public int offsetX;
    public int offsetY;
    public int color;
    public int alpha;
    public boolean chromaEnabled;
    public int chromaSpeed;

    public CustomSidebar() {
        INSTANCE = this;
    }

    public boolean isMouseOverSidebar(int mouseX, int mouseY) {
        float relativeScale = this.scale - 1F;
        float minX = this.sidebarX - this.sidebarWidth * relativeScale;
        float maxX = minX + this.sidebarWidth * this.scale;
        float maxY = this.sidebarY + (this.sidebarHeight / 2f) * relativeScale;
        float minY = maxY - this.sidebarHeight * this.scale;
        return (mouseX > minX && mouseX < maxX && mouseY > minY - this.fontRendererObj.FONT_HEIGHT * this.scale && mouseY < maxY);
    }

    /**
     * Method call inject via ASM
     */
    public void drawSidebar(ScoreObjective scoreObjective, ScaledResolution scaledResolution) {
        if (!this.enabled) {
            return;
        }
        Scoreboard scoreboard = scoreObjective.getScoreboard();
        List<Score> scoreList = new ArrayList<>();
        int width = fontRendererObj.getStringWidth(scoreObjective.getDisplayName());
        for (Score score : scoreboard.getSortedScores(scoreObjective)) {
            String name = score.getPlayerName();
            if (scoreList.size() < 15 && name != null && !name.startsWith("#")) {
                ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(name);
                String str = ScorePlayerTeam.formatPlayerName(scorePlayerTeam, name) + (this.redNumbers ? (": " + EnumChatFormatting.RED + score.getScorePoints()) : "");
                width = Math.max(width, fontRendererObj.getStringWidth(str));
                scoreList.add(score);
            }
        }
        this.sidebarWidth = width;
        this.sidebarHeight = scoreList.size() * fontRendererObj.FONT_HEIGHT;
        this.sidebarX = scaledResolution.getScaledWidth() - this.sidebarWidth - 3 + this.offsetX;
        this.sidebarY = (scaledResolution.getScaledHeight() + this.sidebarHeight) / 2 + this.offsetY;
        int scalePointX = this.sidebarX + this.sidebarWidth;
        int scalePointY = this.sidebarY - this.sidebarHeight / 2;
        float relativeScale = this.scale - 1.0F;
        GL11.glTranslatef(-scalePointX * relativeScale, -scalePointY * relativeScale, 0.0F);
        GL11.glScalef(this.scale, this.scale, 1.0F);
        int index = 0;
        int color = getColor(false);
        int scoreX = this.sidebarX + this.sidebarWidth + 1;
        for (Score score : scoreList) {
            index++;
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(team, score.getPlayerName());
            String s2 = EnumChatFormatting.RED + "" + score.getScorePoints();
            int scoreY = this.sidebarY - index * fontRendererObj.FONT_HEIGHT;
            drawRect(this.sidebarX - 2, scoreY, scoreX, scoreY + fontRendererObj.FONT_HEIGHT, color);
            drawString(s1, this.sidebarX, scoreY);
            if (this.redNumbers) {
                drawString(s2, scoreX - fontRendererObj.getStringWidth(s2), scoreY);
            }
            if (index == scoreList.size()) {
                String s3 = scoreObjective.getDisplayName();
                drawRect(this.sidebarX - 2, scoreY - fontRendererObj.FONT_HEIGHT - 1, scoreX, scoreY - 1, getColor(true));
                drawRect(this.sidebarX - 2, scoreY - 1, scoreX, scoreY, color);
                drawString(s3, this.sidebarX + (this.sidebarWidth - fontRendererObj.getStringWidth(s3)) / 2, scoreY - fontRendererObj.FONT_HEIGHT);
            }
        }
        GL11.glScalef(1.0F / this.scale, 1.0F / this.scale, 1.0F);
        GL11.glTranslatef(scalePointX * relativeScale, scalePointY * relativeScale, 0.0F);
    }

    private int getColor(boolean darker) {
        int rgb = this.color;
        if (this.chromaEnabled) {
            long time = 10000L / this.chromaSpeed;
            rgb = Color.HSBtoRGB((float) (System.currentTimeMillis() % time) / (float) time, 0.8F, 0.8F);
        }
        return darker ? getColorWithAlpha(rgb, Math.min(255, this.alpha + 10)) : getColorWithAlpha(rgb, this.alpha);
    }

    private int getColorWithAlpha(int rgb, int a) {
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        return a << 24 | r << 16 | g << 8 | b;
    }

    private void drawString(String str, int x, int y) {
        if (this.shadow) {
            this.fontRendererObj.drawStringWithShadow(str, x, y, -1);
        } else {
            this.fontRendererObj.drawString(str, x, y, -1);
        }
    }
}