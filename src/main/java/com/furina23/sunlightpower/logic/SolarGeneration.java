package com.furina23.sunlightpower.logic;

/** 无世界对象的发电计算，便于单元测试。 */
public final class SolarGeneration {
    private SolarGeneration() {
    }

    public static long calculate(long dayTime, boolean skyVisible, boolean raining, boolean thundering,
                                 long baseGeneration, double rainMultiplier, double thunderMultiplier) {
        if (!skyVisible || baseGeneration <= 0) {
            return 0;
        }
        long time = Math.floorMod(dayTime, 24000L);
        if (time >= 12000L) {
            return 0;
        }
        double daylight = Math.sin(time * Math.PI / 12000.0);
        double weather = thundering ? thunderMultiplier : raining ? rainMultiplier : 1.0;
        return Math.max(0, (long) Math.floor(baseGeneration * daylight * Math.max(0, Math.min(1, weather))));
    }
}
