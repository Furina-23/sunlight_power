package com.furina23.sunlightpower.client;

import com.furina23.sunlightpower.menu.SolarPanelMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public final class SolarPanelScreen extends AbstractContainerScreen<SolarPanelMenu> {
    public SolarPanelScreen(SolarPanelMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        int left = leftPos;
        int top = topPos;
        graphics.fill(left, top, left + imageWidth, top + imageHeight, 0xff20242b);
        graphics.fill(left + 5, top + 5, left + 171, top + 31, 0xff303640);
        graphics.fill(left + 15, top + 39, left + 161, top + 51, 0xff111318);
        graphics.fill(left + 15, top + 39, left + 15 + Math.min(146, menu.generation() * 146
                / Math.max(1, menu.generation())), top + 51, 0xffe6b52e);
        graphics.fill(left + 15, top + 55, left + 161, top + 67, 0xff111318);
        graphics.fill(left + 15, top + 55, left + 15 + Math.min(146, menu.energy() * 146
                / Math.max(1, menu.capacity())), top + 67, 0xffb73535);
        graphics.fill(left + 33, top + 16, left + 58, top + 42, 0xff14171c);
        graphics.fill(left + 34, top + 17, left + 57, top + 41, 0xff353c47);
        graphics.fill(left + 69, top + 16, left + 159, top + 42, 0xff14171c);
        graphics.fill(left + 70, top + 17, left + 158, top + 41, 0xff353c47);
        graphics.fill(left + 5, top + 72, left + 171, top + 78, 0xff303640);
        graphics.fill(left + 5, top + 79, left + 171, top + 166, 0xff171a20);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, "阳光动力 I", 8, 11, 0xfff4f0df, false);
        graphics.drawString(font, "⚡", 39, 24, 0xffffd34e, false);
        graphics.drawString(font, menu.generation() + " FE/tick", 74, 21, 0xffffd34e, false);
        graphics.drawString(font, menu.energy() + " / " + menu.capacity() + " FE", 74, 31, 0xffff6a6a, false);
        graphics.drawString(font, "物品栏", 8, 70, 0xffd9d9d9, false);
        graphics.drawString(font, "发电", 18, 41, 0xffffd34e, false);
        graphics.drawString(font, "储能", 18, 57, 0xffff6a6a, false);
        graphics.drawString(font, "输出 " + menu.output() + " FE/tick", 72, 79, 0xffa9b1be, false);
    }
}
