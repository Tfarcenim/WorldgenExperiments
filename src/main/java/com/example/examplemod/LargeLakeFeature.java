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

public class LargeLakeFeature extends Feature<LargeLakeFeature.Configuration> {
    private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

    public LargeLakeFeature(Codec<Configuration> pCodec) {
        super(pCodec);
    }

    /**
     * Places the given feature at the given location.
     * During world generation, features are provided with a 3x3 region of chunks, centered on the chunk being generated,
     * that they can safely generate into.
     * @param pContext A context object with a reference to the level and the position the feature is being placed at
     */
    public boolean place(FeaturePlaceContext<Configuration> pContext) {
        BlockPos blockpos = pContext.origin();
        WorldGenLevel worldgenlevel = pContext.level();
        RandomSource randomsource = pContext.random();
        Configuration configuration = pContext.config();
        //check if too low
        if (blockpos.getY() <= worldgenlevel.getMinBuildHeight() + 4) {
            return false;
        } else {
            blockpos = blockpos.below(4);
            boolean[] booleans = new boolean[2048];
            int i = randomsource.nextInt(4) + 4;

            for(int j = 0; j < i; ++j) {
                double d0 = randomsource.nextDouble() * 6.0D + 3.0D;
                double d1 = randomsource.nextDouble() * 4.0D + 2.0D;
                double d2 = randomsource.nextDouble() * 6.0D + 3.0D;
                double dx = randomsource.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
                double dy = randomsource.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
                double dz = randomsource.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

                for(int x = 1; x < 15; ++x) {
                    for(int z = 1; z < 15; ++z) {
                        for(int y = 1; y < 7; ++y) {
                            double d6 = ((double)x - dx) / (d0 / 2.0D);
                            double d7 = ((double)y - dy) / (d1 / 2.0D);
                            double d8 = ((double)z - dz) / (d2 / 2.0D);
                            double distSquare = d6 * d6 + d7 * d7 + d8 * d8;
                            if (distSquare < 1.0D) {
                                int index = (x * 16 + z) * 8 + y;
                                booleans[index] = true;
                            }
                        }
                    }
                }
            }

            BlockState blockstate1 = configuration.fluid().getState(randomsource, blockpos);

            for(int x = 0; x < 16; ++x) {
                for(int z = 0; z < 16; ++z) {
                    for(int y = 0; y < 8; ++y) {
                        boolean flag = !booleans[(x * 16 + z) * 8 + y] && (x < 15 && booleans[((x + 1) * 16 + z) * 8 + y] || x > 0 && booleans[((x - 1) * 16 + z) * 8 + y] || z < 15 && booleans[(x * 16 + z + 1) * 8 + y] || z > 0 && booleans[(x * 16 + (z - 1)) * 8 + y] || y < 7 && booleans[(x * 16 + z) * 8 + y + 1] || y > 0 && booleans[(x * 16 + z) * 8 + (y - 1)]);
                        if (flag) {
                            Material material = worldgenlevel.getBlockState(blockpos.offset(x, y, z)).getMaterial();
                            if (y >= 4 && material.isLiquid()) {
                                return false;
                            }

                            if (y < 4 && !material.isSolid() && worldgenlevel.getBlockState(blockpos.offset(x, y, z)) != blockstate1) {
                                return false;
                            }
                        }
                    }
                }
            }

            for(int x = 0; x < 16; ++x) {
                for(int z = 0; z < 16; ++z) {
                    for(int y = 0; y < 8; ++y) {
                        if (booleans[(x * 16 + z) * 8 + y]) {
                            BlockPos offset = blockpos.offset(x, y, z);
                            if (this.canReplaceBlock(worldgenlevel.getBlockState(offset))) {
                                boolean flag1 = y >= 4;
                                worldgenlevel.setBlock(offset, flag1 ? AIR : blockstate1, 2);
                                if (flag1) {
                                    worldgenlevel.scheduleTick(offset, AIR.getBlock(), 0);
                                    this.markAboveForPostProcessing(worldgenlevel, offset);
                                }
                            }
                        }
                    }
                }
            }

            BlockState blockstate2 = configuration.barrier().getState(randomsource, blockpos);
            if (!blockstate2.isAir()) {
                for(int x = 0; x < 16; ++x) {
                    for(int z = 0; z < 16; ++z) {
                        for(int y = 0; y < 8; ++y) {
                            boolean flag2 = !booleans[(x * 16 + z) * 8 + y] && (x < 15 && booleans[((x + 1) * 16 + z) * 8 + y] || x > 0 && booleans[((x - 1) * 16 + z) * 8 + y] || z < 15 && booleans[(x * 16 + z + 1) * 8 + y] || z > 0 && booleans[(x * 16 + (z - 1)) * 8 + y] || y < 7 && booleans[(x * 16 + z) * 8 + y + 1] || y > 0 && booleans[(x * 16 + z) * 8 + (y - 1)]);
                            if (flag2 && (y < 4 || randomsource.nextInt(2) != 0)) {
                                BlockState blockstate = worldgenlevel.getBlockState(blockpos.offset(x, y, z));
                                if (blockstate.getMaterial().isSolid() && !blockstate.is(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)) {
                                    BlockPos offset = blockpos.offset(x, y, z);
                                    worldgenlevel.setBlock(offset, blockstate2, 2);
                                    this.markAboveForPostProcessing(worldgenlevel, offset);
                                }
                            }
                        }
                    }
                }
            }

            if (blockstate1.getFluidState().is(FluidTags.WATER)) {
                for(int x = 0; x < 16; ++x) {
                    for(int z = 0; z < 16; ++z) {
                        int y = 4;
                        BlockPos offset = blockpos.offset(x, y, z);
                        if (worldgenlevel.getBiome(offset).value().shouldFreeze(worldgenlevel, offset, false) && this.canReplaceBlock(worldgenlevel.getBlockState(offset))) {
                            worldgenlevel.setBlock(offset, Blocks.ICE.defaultBlockState(), 2);
                        }
                    }
                }
            }
            System.out.println("Placed Lake at:"+blockpos);
            return true;
        }
    }

    private boolean canReplaceBlock(BlockState pState) {
        return !pState.is(BlockTags.FEATURES_CANNOT_REPLACE);
    }

    public record Configuration(BlockStateProvider fluid, BlockStateProvider barrier) implements FeatureConfiguration {
        public static final Codec<Configuration> CODEC = RecordCodecBuilder.create((p_190962_) -> p_190962_.group(BlockStateProvider.CODEC.fieldOf("fluid")
                .forGetter(Configuration::fluid), BlockStateProvider.CODEC.fieldOf("barrier")
                .forGetter(Configuration::barrier))
                .apply(p_190962_, Configuration::new));
    }
}
