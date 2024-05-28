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
    private boolean isLocked;

    public AbilityButton(int x, int y, int width, int height, List<QuestProgress.Ability> abilities, boolean isLocked) {
        super(Button.builder(Component.nullToEmpty(null), button -> {})
                .bounds(x, y, width, height));
        this.abilities = abilities;
        this.isLocked = isLocked;
    }

    public List<QuestProgress.Ability> getAbilities() {
        return abilities;
    }

    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        if (texture != null) {
            Minecraft.getInstance().getTextureManager().bindForSetup(texture);
            pGuiGraphics.blit(texture, this.getX(), this.getY(), 0, 0, this.width,
                    this.height, this.width, this.height);
        }

        if (isLocked) {
            // Render a shady overlay to indicate the button is locked
            pGuiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width,
                    this.getY() + this.height, 0xAA000000);
        }

        // Draw border when hovered
        if (this.isHovered() && !isLocked) {
            drawBorder(pGuiGraphics);
        }
    }

    private void drawBorder(GuiGraphics pGuiGraphics) {
        int borderColor = 0xFFFFFFFF; // White color
        int borderWidth = 2;

        // Top border
        pGuiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + borderWidth,
                borderColor);
        // Bottom border
        pGuiGraphics.fill(this.getX(), this.getY() + this.height - borderWidth, this.getX() + this.width,
                this.getY() + this.height, borderColor);
        // Left border
        pGuiGraphics.fill(this.getX(), this.getY(), this.getX() + borderWidth, this.getY() + this.height,
                borderColor);
        // Right border
        pGuiGraphics.fill(this.getX() + this.width - borderWidth, this.getY(), this.getX() + this.width,
                this.getY() + this.height, borderColor);
    }

    @Override
    public boolean isHovered() {
        return super.isHovered();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return !isLocked && super.mouseClicked(mouseX, mouseY, button);
    }
}
