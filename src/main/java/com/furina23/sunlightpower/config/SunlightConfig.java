package com.furina23.sunlightpower.config;

import com.furina23.sunlightpower.SunlightPower;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/** 阳光动力的服务端配置。数值统一使用 Fabric Energy API 单位。 */
public final class SunlightConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("sunlight_power.json");

    public long generationPerTick = 8;
    public long capacity = 8000;
    public long maxOutputPerSide = 16;
    public double rainMultiplier = 0.25;
    public double thunderMultiplier = 0.1;
    public long chargingSlotRate = 16;

    public void validate() {
        generationPerTick = Math.max(0, generationPerTick);
        capacity = Math.max(1, capacity);
        maxOutputPerSide = Math.max(0, maxOutputPerSide);
        rainMultiplier = clampMultiplier(rainMultiplier);
        thunderMultiplier = clampMultiplier(thunderMultiplier);
        chargingSlotRate = Math.max(0, chargingSlotRate);
    }

    private static double clampMultiplier(double value) {
        return Double.isFinite(value) ? Math.max(0, Math.min(1, value)) : 0;
    }

    public static SunlightConfig load() {
        SunlightConfig config = null;
        if (Files.isRegularFile(PATH)) {
            try (Reader reader = Files.newBufferedReader(PATH)) {
                config = GSON.fromJson(reader, SunlightConfig.class);
            } catch (Exception exception) {
                SunlightPower.LOGGER.warn("无法读取配置文件 {}，将使用默认配置", PATH, exception);
            }
        }
        if (config == null) {
            config = new SunlightConfig();
        }
        config.validate();
        try {
            Files.createDirectories(PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(PATH)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException exception) {
            SunlightPower.LOGGER.warn("无法写入配置文件 {}", PATH, exception);
        }
        return config;
    }
}
