package com.furina23.sunlightpower.client;

import com.furina23.sunlightpower.SunlightPower;
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
        graphics.fill(left, top, left + imageWidth, top + imageHeight, 0xff171a20);
        graphics.fill(left + 5, top + 5, left + 171, top + 31, 0xff303640);
        graphics.fill(left + 143, top + 8, left + 161, top + 26, 0xff353c47);
        graphics.fill(left + 144, top + 9, left + 160, top + 25, 0xff9ca2a2);
        graphics.fill(left + 122, top + 40, left + 138, top + 88, 0xff111318);
        int generationHeight = (int) Math.min(46L, menu.generation() * 46L
                / Math.max(1L, SunlightPower.CONFIG.generationPerTick));
        graphics.fill(left + 123, top + 87 - generationHeight, left + 137, top + 87, 0xffe6b52e);
        graphics.fill(left + 144, top + 40, left + 160, top + 88, 0xff111318);
        int energyHeight = Math.min(46, menu.energy() * 46 / Math.max(1, menu.capacity()));
        graphics.fill(left + 145, top + 87 - energyHeight, left + 159, top + 87, 0xffb73535);
        for (int i = 0; i < 5; i++) {
            int slotLeft = left + 8 + i * 18;
            graphics.fill(slotLeft, top + 56, slotLeft + 18, top + 74, 0xff3b3f43);
            graphics.fill(slotLeft + 1, top + 57, slotLeft + 17, top + 73, 0xff969696);
        }
        graphics.fill(left + 5, top + 79, left + 171, top + 85, 0xff303640);
        graphics.fill(left + 5, top + 88, left + 171, top + imageHeight, 0xff171a20);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, "阳光动力 I", 8, 11, 0xfff4f0df, false);
        graphics.drawString(font, "⚡", 148, 13, 0xff555555, false);
        graphics.drawString(font, "发电: " + menu.generation() + " FE/tick", 8, 24, 0xffffd34e, false);
        graphics.drawString(font, "储能: " + menu.energy() + " / " + menu.capacity() + " FE", 8, 40, 0xffff6a6a, false);
        graphics.drawString(font, "效率: " + (menu.generation() * 100 / Math.max(1, SunlightPower.CONFIG.generationPerTick)) + "%", 8, 56, 0xffd9d9d9, false);
        graphics.drawString(font, "输出上限: " + menu.output() + " FE/tick", 8, 72, 0xffa9b1be, false);
        graphics.drawString(font, "物品栏", 8, 83, 0xffd9d9d9, false);
    }
}
