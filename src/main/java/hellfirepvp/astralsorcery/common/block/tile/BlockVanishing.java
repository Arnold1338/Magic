package hellfirepvp.astralsorcery.common.block.tile;

import hellfirepvp.astralsorcery.common.tile.TileVanishing;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.phys.shapes.Shapes;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import javax.annotation.Nullable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.BaseEntityBlock;

public class BlockVanishing extends BaseEntityBlock
{
    public BlockVanishing() {
        super(AbstractBlock.Properties.func_200949_a(Material.field_175972_I, MaterialColor.field_151660_b).func_200948_a(-1.0f, 3600000.0f).func_200947_a(SoundType.field_185852_e));
    }
    
    @OnlyIn(Dist.CLIENT)
    public void func_180655_c(final BlockState state, final Level world, final BlockPos pos, final Random random) {
    }
    
    public boolean canEntityDestroy(final BlockState state, final IBlockReader world, final BlockPos pos, final Entity entity) {
        return false;
    }
    
    public boolean canCreatureSpawn(final BlockState state, final IBlockReader world, final BlockPos pos, final SpawnPlacements.PlacementType type, @Nullable final EntityType<?> entityType) {
        return false;
    }
    
    public VoxelShape func_220053_a(final BlockState p_220053_1_, final IBlockReader p_220053_2_, final BlockPos p_220053_3_, final CollisionContext p_220053_4_) {
        return VoxelShapes.func_197880_a();
    }
    
    public VoxelShape func_220071_b(final BlockState state, final IBlockReader world, final BlockPos pos, final CollisionContext ctx) {
        if (ctx.getEntity() instanceof Player) {
            return VoxelShapes.func_197868_b();
        }
        return VoxelShapes.func_197880_a();
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader worldIn) {
        return new TileVanishing();
    }
}
