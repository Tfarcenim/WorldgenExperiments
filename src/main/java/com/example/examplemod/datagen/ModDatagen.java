package com.example.examplemod.datagen;

import com.example.examplemod.datagen.tags.FeatureTagsProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

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
