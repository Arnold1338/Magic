package hellfirepvp.astralsorcery.common.world.marker;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.LockableLootTileEntity;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileCollectorCrystal;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Blocks;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.LootAS;
import net.minecraft.core.MutableBoundingBox;
import java.util.Random;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class MarkerManagerAS
{
    public static void handleMarker(final String marker, final BlockPos pos, final IWorld genWorld, final Random rand, final MutableBoundingBox box) {
        switch (marker) {
            case "brick_shrine_chest": {
                if (rand.nextBoolean()) {
                    makeChest(genWorld, pos, LootAS.SHRINE_CHEST, rand, box);
                    break;
                }
                genWorld.func_180501_a(pos, BlocksAS.MARBLE_BRICKS.defaultBlockState(), 2);
                break;
            }
            case "shrine_chest": {
                if (rand.nextBoolean()) {
                    makeChest(genWorld, pos, LootAS.SHRINE_CHEST, rand, box);
                    break;
                }
                genWorld.func_180501_a(pos, Blocks.field_150350_a.defaultBlockState(), 2);
                break;
            }
            case "random_top_block": {
                if (rand.nextFloat() < 0.7f) {
                    genWorld.func_180501_a(pos, genWorld.func_226691_t_(pos).func_242440_e().func_242502_e().func_204108_a(), 2);
                    break;
                }
                genWorld.func_180501_a(pos, Blocks.field_150350_a.defaultBlockState(), 2);
                break;
            }
            case "crystal": {
                makeCollectorCrystal(genWorld, pos, rand, box);
                break;
            }
        }
    }
    
    private static void makeCollectorCrystal(final IWorld world, final BlockPos pos, final Random rand, final MutableBoundingBox box) {
        if (box.func_175898_b((Vector3i)pos) && world.getBlockState(pos).getBlock() != BlocksAS.ROCK_COLLECTOR_CRYSTAL) {
            world.func_180501_a(pos, BlocksAS.ROCK_COLLECTOR_CRYSTAL.defaultBlockState(), 2);
            final TileCollectorCrystal tcc = MiscUtils.getTileAt((IBlockReader)world, pos, TileCollectorCrystal.class, true);
            if (tcc != null) {
                final IMajorConstellation cst = MiscUtils.getRandomEntry(ConstellationRegistry.getMajorConstellations(), rand);
                tcc.setAttributes(CrystalPropertiesAS.WORLDGEN_SHRINE_COLLECTOR_ATTRIBUTES);
                tcc.setAttunedConstellation(cst);
            }
        }
    }
    
    private static void makeChest(final IWorld world, final BlockPos pos, final ResourceLocation tableName, final Random rand, final MutableBoundingBox box) {
        if (box.func_175898_b((Vector3i)pos) && world.getBlockState(pos).getBlock() != Blocks.field_150486_ae) {
            final BlockState chest = StructurePiece.func_197528_a((IBlockReader)world, pos, Blocks.field_150486_ae.defaultBlockState());
            world.func_180501_a(pos, chest, 2);
            LockableLootTileEntity.func_195479_a((IBlockReader)world, rand, pos, tableName);
        }
    }
}
