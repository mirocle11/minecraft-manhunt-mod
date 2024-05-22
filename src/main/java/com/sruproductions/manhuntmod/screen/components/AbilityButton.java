package com.sruproductions.manhuntmod.screen.components;

import com.sruproductions.manhuntmod.data.QuestProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class AbilityButton extends Button {
    private final List<QuestProgress.Ability> abilities;
    private ResourceLocation texture;

    public AbilityButton(int x, int y, int width, int height, List<QuestProgress.Ability> abilities) {
        super(Button.builder(Component.literal(abilities.get(0).getName()), button -> {})
                .bounds(x, y, width, height));
        this.abilities = abilities;
    }

    public List<QuestProgress.Ability> getAbilities() {
        return abilities;
    }

    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (texture != null) {
            Minecraft.getInstance().getTextureManager().bindForSetup(texture);
            pGuiGraphics.blit(texture, this.getX(), this.getY(), 0, 0, this.width, this.height);
        }

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean isHovered() {
        return super.isHovered();
    }
}
