package com.example.examplemod.datagen.tags;

import com.example.examplemod.ExampleMod;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class FeatureTagsProvider extends TagsProvider<PlacedFeature> {
    public FeatureTagsProvider(DataGenerator pGenerator, Registry<PlacedFeature> pRegistry, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, pRegistry, ExampleMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
    }
}
