package com.example.examplemod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;

public class VeryLargeLakeFeature extends Feature<VeryLargeLakeFeature.Configuration> {

    private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

    public VeryLargeLakeFeature(Codec<Configuration> pCodec) {
        super(pCodec);
    }

    /**
     * Places the given feature at the given location.
     * During world generation, features are provided with a 3x3 region of chunks, centered on the chunk being generated,
     * that they can safely generate into.
     *
     * @param pContext A context object with a reference to the level and the position the feature is being placed at
     */
    public boolean place(FeaturePlaceContext<Configuration> pContext) {
        BlockPos origin = pContext.origin();
        WorldGenLevel worldgenlevel = pContext.level();
        RandomSource randomsource = pContext.random();
        Configuration configuration = pContext.config();
        //check if too low
        if (origin.getY() <= worldgenlevel.getMinBuildHeight() + configuration.depth) {
            return false;
        } else {

            //first make the shape
            boolean[] fill = new boolean[configuration.width * configuration.depth * configuration.width];

            double w = configuration.width/2d;
            double d = configuration.depth/2d;

            //an oblate spheroid looks like x^2/a^2 + y^2/b^2 + z^2/c^2 = 1
            //in this case it's 4 * x^2/width^2 +4 * y^2/depth^2 + 4 * z^2/width^2 = 1
            Vec3 center = new Vec3(w,d,w);
            for (int y = 0; y < configuration.depth;y++) {
                for (int z = 0; z < configuration.width;z++) {
                    for (int x = 0; x < configuration.width;x++) {
                        int index = x + configuration.width * z + configuration.width * configuration.width * y;
                        double x1 = x - center.x;
                        double y1 = y - center.y;
                        double z1 = z - center.z;

                        double xd = 2 * x1 / configuration.width;
                        double yd = 2 * y1 / configuration.depth;
                        double zd = 2 * z1 / configuration.width;
                        double dist = xd * xd + yd * yd + zd * zd;
                        if (dist < 1) {
                            fill[index] = true;
                        }
                    }
                }
            }

            //check for obstructions that would cause placement to completely fail (such as water or other fluids)

            for (int y = 0; y < configuration.depth;y++) {
                for (int z = 0; z < configuration.width;z++) {
                    for (int x = 0; x < configuration.width;x++) {
                        int index = x + configuration.width * z + configuration.width * configuration.width * y;
                        if (fill[index]) {
                            //offset so that we're centered on the lake, then offset again for the loop
                            BlockPos check = origin.offset(x-center.x,y-center.y,z-center.z);
                            Material material = worldgenlevel.getBlockState(check).getMaterial();
                            if (material.isLiquid()) {
                                return false;
                            }
                        }
                    }
                }
            }

            //now place the feature itself being careful not to replace the wrong blocks
            BlockState fluidState = configuration.fluid().getState(randomsource, origin);
            for (int y = 0; y < configuration.depth;y++) {
                for (int z = 0; z < configuration.width;z++) {
                    for (int x = 0; x < configuration.width;x++) {
                        int index = x + configuration.width * z + configuration.width * configuration.width * y;
                        //offset so that we're centered on the lake, then offset again for the loop
                        BlockPos check = origin.offset(x-center.x,y-center.y,z-center.z);
                        if (fill[index] && canReplaceBlock(worldgenlevel.getBlockState(check))) {
                            worldgenlevel.setBlock(check,fluidState,2);
                        }
                    }
                }
            }

            if (fluidState.getFluidState().is(FluidTags.WATER)) {
                for (int x = 0; x < 16; ++x) {
                    for (int z = 0; z < 16; ++z) {
                        int y = 4;
                        BlockPos offset = origin.offset(x-center.x,y-center.y,z-center.z);
                        if (worldgenlevel.getBiome(offset).value().shouldFreeze(worldgenlevel, offset, false) && this.canReplaceBlock(worldgenlevel.getBlockState(offset))) {
                            worldgenlevel.setBlock(offset, Blocks.ICE.defaultBlockState(), 2);
                        }
                    }
                }
            }

            System.out.println("Placed Very Large Lake at:" + origin);
            return true;
        }
    }

    private boolean canReplaceBlock(BlockState pState) {
        return !pState.is(BlockTags.FEATURES_CANNOT_REPLACE);
    }

    public record Configuration(BlockStateProvider fluid, BlockStateProvider barrier, int width, int depth) implements FeatureConfiguration {
        public static final Codec<Configuration> CODEC = RecordCodecBuilder.create((p_190962_) -> p_190962_.group(
                        BlockStateProvider.CODEC.fieldOf("fluid").forGetter(Configuration::fluid),
                        BlockStateProvider.CODEC.fieldOf("barrier").forGetter(Configuration::barrier),
                        Codec.intRange(1, 40).fieldOf("width").forGetter(Configuration::width),
                        Codec.intRange(1, 32).fieldOf("depth").forGetter(Configuration::depth)
                )
                .apply(p_190962_, Configuration::new));
    }

}
