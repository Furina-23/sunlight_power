package com.furina23.sunlightpower.integration;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourceIntegrationTest {
    private static final Path ROOT = Path.of("src/main/resources");

    @Test
    void modResourcesContainACompletePanelDefinition() {
        List<String> required = List.of(
                "fabric.mod.json",
                "pack.mcmeta",
                "assets/sunlight_power/blockstates/solar_panel.json",
                "assets/sunlight_power/models/block/solar_panel.json",
                "assets/sunlight_power/models/item/solar_panel.json",
                "assets/sunlight_power/textures/block/solar_panel_slab_top.png",
                "assets/sunlight_power/textures/block/solar_panel_slab_side.png",
                "assets/sunlight_power/textures/block/solar_panel_slab_bottom.png",
                "data/sunlight_power/recipes/solar_panel.json",
                "data/sunlight_power/loot_tables/blocks/solar_panel.json");
        for (String path : required) {
            assertTrue(Files.isRegularFile(ROOT.resolve(path)), "缺少资源: " + path);
        }
    }

    @Test
    void sourceDoesNotReferenceForgeEnergyApi() throws Exception {
        try (var paths = Files.walk(Path.of("src/main/java"))) {
            paths.filter(path -> path.toString().endsWith(".java")).forEach(path -> {
                try {
                    String source = Files.readString(path);
                    assertTrue(!source.contains("net.minecraftforge") && !source.contains("net.neoforged"),
                            "发现 Forge/NeoForge 引用: " + path);
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            });
        }
    }
}
