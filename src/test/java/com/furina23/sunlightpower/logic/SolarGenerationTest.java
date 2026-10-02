package com.furina23.sunlightpower.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolarGenerationTest {
    @Test
    void nightAndBlockedSkyProduceNoEnergy() {
        assertEquals(0, SolarGeneration.calculate(18000, true, false, false, 8, 0.5, 0.5));
        assertEquals(0, SolarGeneration.calculate(6000, false, false, false, 8, 0.5, 0.5));
    }

    @Test
    void noonUsesBaseGeneration() {
        assertEquals(8, SolarGeneration.calculate(6000, true, false, false, 8, 0.5, 0.5));
    }

    @Test
    void weatherMultipliersApplyAtNoon() {
        assertEquals(4, SolarGeneration.calculate(6000, true, true, false, 8, 0.5, 0.5));
        assertEquals(4, SolarGeneration.calculate(6000, true, true, true, 8, 0.5, 0.5));
    }

    @Test
    void timeWrapsAcrossDays() {
        assertEquals(SolarGeneration.calculate(6000, true, false, false, 8, 0.5, 0.5),
                SolarGeneration.calculate(30000, true, false, false, 8, 0.5, 0.5));
    }
}
