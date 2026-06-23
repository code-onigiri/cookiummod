package com.code_onigiri.cookium.block_entity.biggerchest;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class BiggerChestScreen extends AbstractContainerScreen<BiggerChestMenu> implements MenuAccess<BiggerChestMenu> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath("cookium", "textures/gui/bigger_chest.png");

    public BiggerChestScreen(BiggerChestMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 222);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 206;
    }

    @Override
    public BiggerChestMenu getMenu() {
        return this.menu;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 176, 222);
    }
}
