package com.example.examplemod;

import com.example.examplemod.datagen.ModDatagen;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod {

    public static final String MODID = "examplemod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public ExampleMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
       // FMLJavaModLoadingContext.get().getModEventBus().addListener(ModDatagen::gather);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::register);
    }

    private void register(RegisterEvent event) {
        event.register(Registry.FEATURE_REGISTRY,new ResourceLocation(MODID,"large_lake"),() -> ModFeatures.LARGE_LAKE);
        event.register(Registry.FEATURE_REGISTRY,new ResourceLocation(MODID,"very_large_lake"),() -> ModFeatures.VERY_LARGE_LAKE);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }
}
