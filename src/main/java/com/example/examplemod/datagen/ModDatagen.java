package com.example.examplemod.datagen;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.ModPlacedFeatures;
import com.example.examplemod.ModTags;
import com.example.examplemod.datagen.tags.FeatureTagsProvider;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class ModDatagen {

    public static void gather(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        boolean server = event.includeServer();


        final RegistryAccess registryAccess = RegistryAccess.builtinCopy();
       generator.addProvider(server,new FeatureTagsProvider(generator,registryAccess.registryOrThrow(Registry.PLACED_FEATURE_REGISTRY),helper));

        biomeModifier(generator,helper,server);


    }


    protected static void biomeModifier(DataGenerator generator, ExistingFileHelper helper, boolean server) {

    }

}
