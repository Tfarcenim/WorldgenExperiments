package com.example.examplemod;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class ModFeatures {

    public static final Feature<LargeLakeFeature.Configuration> LARGE_LAKE = new LargeLakeFeature(LargeLakeFeature.Configuration.CODEC);


    private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String pKey, F pValue) {
        return Registry.register(Registry.FEATURE, pKey, pValue);
    }
}
