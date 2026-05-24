package hellfirepvp.astralsorcery.common.block.foliage;

import net.minecraftforge.common.PlantType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import javax.annotation.Nonnull;
import net.minecraft.world.level.effect.MobEffects;
import net.minecraft.world.level.effect.MobEffect;
import net.minecraft.world.level.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesMisc;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import hellfirepvp.astralsorcery.common.block.base.template.BlockFlowerTemplate;

public class BlockGlowFlower extends BlockFlowerTemplate implements IPlantable
{
    private final VoxelShape shape;
    
    public BlockGlowFlower() {
        super(PropertiesMisc.defaultTickingPlant().func_235838_a_(state -> 5));
        this.shape = this.createShape();
    }
    
    private VoxelShape createShape() {
        return Block.func_208617_a(1.5, 0.0, 1.5, 14.5, 13.0, 14.5);
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader world, final BlockPos pos, final CollisionContext ctx) {
        final Vec3 offset = state.func_191059_e(world, pos);
        return this.shape.func_197751_a(offset.field_72450_a, offset.field_72448_b, offset.field_72449_c);
    }
    
    @Nonnull
    @Override
    public Effect func_220094_d() {
        return Effects.field_188425_z;
    }
    
    @Override
    public int func_220095_e() {
        return 40;
    }
    
    public int getExpDrop(final BlockState state, final IWorldReader world, final BlockPos pos, final int fortune, final int silktouch) {
        if (silktouch == 0) {
            return 0;
        }
        if (fortune > 0) {
            return fortune * Mth.func_76136_a(this.RANDOM, 2, 5);
        }
        return Mth.func_76136_a(this.RANDOM, 1, 2);
    }
    
    public PlantType getPlantType(final IBlockReader world, final BlockPos pos) {
        return PlantType.CAVE;
    }
}
