package com.furina23.sunlightpower.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolarGenerationTest {
    @Test
    void overworldNightAndBlockedSkyProduceNoEnergy() {
        assertEquals(0, calculate(SolarGeneration.Dimension.OVERWORLD, 18000, true, false, false));
        assertEquals(0, calculate(SolarGeneration.Dimension.OVERWORLD, 6000, false, false, false));
    }

    @Test
    void overworldNoonUsesBaseGeneration() {
        assertEquals(8, calculate(SolarGeneration.Dimension.OVERWORLD, 6000, true, false, false));
    }

    @Test
    void overworldWeatherMultipliersApplyAtNoon() {
        assertEquals(4, SolarGeneration.calculate(SolarGeneration.Dimension.OVERWORLD,
                6000, true, true, false, 8, 0.5, 0.25));
        assertEquals(2, SolarGeneration.calculate(SolarGeneration.Dimension.OVERWORLD,
                6000, true, true, true, 8, 0.5, 0.25));
    }

    @Test
    void netherGeneratesDayAndNightAtFixedRateWithoutWeather() {
        assertEquals(6, calculate(SolarGeneration.Dimension.NETHER, 6000, true, false, false));
        assertEquals(6, calculate(SolarGeneration.Dimension.NETHER, 18000, true, true, true));
    }

    @Test
    void endGeneratesDayAndNightAtFixedRateWithoutWeather() {
        assertEquals(6, calculate(SolarGeneration.Dimension.END, 6000, true, false, false));
        assertEquals(6, calculate(SolarGeneration.Dimension.END, 18000, true, true, true));
    }

    @Test
    void allDimensionsStopWhenSkyIsBlocked() {
        for (SolarGeneration.Dimension dimension : SolarGeneration.Dimension.values()) {
            assertEquals(0, calculate(dimension, 6000, false, false, false));
        }
    }

    @Test
    void nonPositiveBaseGenerationProducesNoEnergy() {
        assertEquals(0, calculate(SolarGeneration.Dimension.OVERWORLD, 6000, true, false, false, 0));
        assertEquals(0, calculate(SolarGeneration.Dimension.NETHER, 6000, true, false, false, -1));
    }

    @Test
    void timeWrapsAcrossDays() {
        assertEquals(calculate(SolarGeneration.Dimension.OVERWORLD, 6000, true, false, false),
                calculate(SolarGeneration.Dimension.OVERWORLD, 30000, true, false, false));
    }

    private static long calculate(SolarGeneration.Dimension dimension, long dayTime, boolean skyVisible,
                                  boolean raining, boolean thundering) {
        return calculate(dimension, dayTime, skyVisible, raining, thundering, 8);
    }

    private static long calculate(SolarGeneration.Dimension dimension, long dayTime, boolean skyVisible,
                                  boolean raining, boolean thundering, long baseGeneration) {
        return SolarGeneration.calculate(dimension, dayTime, skyVisible, raining, thundering,
                baseGeneration, 0.5, 0.25);
    }
}
