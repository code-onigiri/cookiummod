package com.code_onigiri.cookium.block_entity;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class MyBlockScreen extends AbstractContainerScreen<MyBlockMenu> implements MenuAccess<MyBlockMenu> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath("cookium", "textures/gui/my_block.png");

    public MyBlockScreen(MyBlockMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public MyBlockMenu getMenu() {
        return this.menu;
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractContents(graphics, mouseX, mouseY, partialTick);

        // カスタム背景描画
        int x = this.leftPos;
        int y = this.topPos;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0,
                this.imageWidth, this.imageHeight, 256, 256);
    }
}