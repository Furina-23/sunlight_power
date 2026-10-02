package com.furina23.sunlightpower.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import com.furina23.sunlightpower.SunlightPower;

public class SunlightPowerClient implements ClientModInitializer {
	@Override
    public void onInitializeClient() {
        MenuScreens.register(SunlightPower.SOLAR_PANEL_MENU, SolarPanelScreen::new);
    }
}
