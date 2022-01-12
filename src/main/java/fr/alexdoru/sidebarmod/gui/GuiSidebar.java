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

public class GuiSidebar extends Gui {

    private final FontRenderer fr = (Minecraft.getMinecraft()).fontRendererObj;
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

    public boolean contains(int mouseX, int mouseY) {
        float mscale = this.scale - 1.0F;
        float minX = this.sidebarX - this.sidebarWidth * mscale;
        float maxX = minX + this.sidebarWidth * this.scale;
        float maxY = (this.sidebarY + ((float) this.sidebarHeight / 2)) * mscale;
        float minY = maxY - this.sidebarHeight * this.scale;
        return (mouseX > minX && mouseX < maxX && mouseY > minY - this.fr.FONT_HEIGHT * this.scale && mouseY < maxY);
    }

    public void drawSidebar(ScoreObjective scoreObjective, ScaledResolution scaledResolution) {
        if (!this.enabled) {
            return;
        }
        FontRenderer fr = (Minecraft.getMinecraft()).fontRendererObj;
        Scoreboard scoreboard = scoreObjective.getScoreboard();
        List<Score> scores = new ArrayList<>();
        this.sidebarWidth = fr.getStringWidth(scoreObjective.getDisplayName());
        for (Score score : scoreboard.getSortedScores(scoreObjective)) {
            String name = score.getPlayerName();
            if (scores.size() < 15 && name != null && !name.startsWith("#")) {
                ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(name);
                String s2 = this.redNumbers ? (": " + EnumChatFormatting.RED + score.getScorePoints()) : "";
                String str = ScorePlayerTeam.formatPlayerName(scorePlayerTeam, name) + s2;
                this.sidebarWidth = Math.max(this.sidebarWidth, fr.getStringWidth(str));
                scores.add(score);
            }
        }
        this.sidebarHeight = scores.size() * fr.FONT_HEIGHT;
        this.sidebarX = scaledResolution.getScaledWidth() - this.sidebarWidth - 3 + this.offsetX;
        this.sidebarY = scaledResolution.getScaledHeight() / 2 + this.sidebarHeight / 3 + this.offsetY;
        int scalePointX = this.sidebarX + this.sidebarWidth;
        int scalePointY = this.sidebarY - this.sidebarHeight / 2;
        float mscale = this.scale - 1.0F;
        GL11.glTranslatef(-scalePointX * mscale, -scalePointY * mscale, 0.0F);
        GL11.glScalef(this.scale, this.scale, 1.0F);
        int index = 0;
        for (Score score : scores) {
            index++;
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(team, score.getPlayerName());
            String s2 = EnumChatFormatting.RED + "" + score.getScorePoints();
            if (!this.redNumbers)
                s2 = "";
            int scoreX = this.sidebarX + this.sidebarWidth + 1;
            int scoreY = this.sidebarY - index * fr.FONT_HEIGHT;
            drawRect(this.sidebarX - 2, scoreY, scoreX, scoreY + fr.FONT_HEIGHT, getColor(false));
            drawString(s1, this.sidebarX, scoreY);
            drawString(s2, scoreX - fr.getStringWidth(s2), scoreY);
            if (index == scores.size()) {
                String s3 = scoreObjective.getDisplayName();
                drawRect(this.sidebarX - 2, scoreY - fr.FONT_HEIGHT - 1, scoreX, scoreY - 1, getColor(true));
                drawRect(this.sidebarX - 2, scoreY - 1, scoreX, scoreY, getColor(false));
                drawString(s3, this.sidebarX + (this.sidebarWidth - fr.getStringWidth(s3)) / 2, scoreY - fr.FONT_HEIGHT);
            }
        }
        GL11.glScalef(1.0F / this.scale, 1.0F / this.scale, 1.0F);
        GL11.glTranslatef(scalePointX * mscale, scalePointY * mscale, 0.0F);
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
            this.fr.drawStringWithShadow(str, x, y, -1);
        } else {
            this.fr.drawString(str, x, y, -1);
        }
    }
}