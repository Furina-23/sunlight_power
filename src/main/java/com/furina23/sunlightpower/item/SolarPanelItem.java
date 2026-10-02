package com.furina23.sunlightpower.item;

import com.furina23.sunlightpower.SunlightPower;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public final class SolarPanelItem extends BlockItem {
    public SolarPanelItem(net.minecraft.world.level.block.Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("阳光动力 I").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("发电效率: " + SunlightPower.CONFIG.generationPerTick + " FE/tick")
                .withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("最大储能: " + SunlightPower.CONFIG.capacity + " FE")
                .withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("输出上限: " + SunlightPower.CONFIG.maxOutputPerSide + " FE/tick")
                .withStyle(ChatFormatting.GRAY));
    }
}
