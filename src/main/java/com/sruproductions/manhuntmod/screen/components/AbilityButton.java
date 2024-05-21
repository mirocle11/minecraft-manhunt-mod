package com.sruproductions.manhuntmod.screen.components;

import com.sruproductions.manhuntmod.ManhuntMod;
import com.sruproductions.manhuntmod.data.QuestProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class AbilityButton {
    private final int x, y, width, height;
    private final QuestProgress.Ability ability;
    private final ResourceLocation texture;

    public AbilityButton(int x, int y, int width, int height, QuestProgress.Ability ability) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.ability = ability;
        this.texture = new ResourceLocation(ManhuntMod.MOD_ID,
                "textures/gui/" + ability.getName().toLowerCase().replace(" ", "_") + ".png");
    }

    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float pPartialTick) {
        Minecraft.getInstance().getTextureManager().bindForSetup(texture);
        pGuiGraphics.blit(texture, x, y, 0, 0, width, height, width, height);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    public QuestProgress.Ability getAbility() {
        return ability;
    }
}
