package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.phys.shapes.Shapes;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import hellfirepvp.astralsorcery.common.container.factory.ContainerObservatoryProvider;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileObservatory;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.phys.BlockHitResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.BlockItemUseContext;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesMisc;
import net.minecraft.world.level.phys.AABB;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import hellfirepvp.astralsorcery.common.block.base.LargeBlock;
import net.minecraft.world.level.block.BaseEntityBlock;

public class BlockObservatory extends BaseEntityBlock implements LargeBlock, CustomItemBlock
{
    private static final AABB PLACEMENT_BOX;
    
    public BlockObservatory() {
        super(PropertiesMisc.defaultGoldMachinery().harvestTool(ToolType.PICKAXE).harvestLevel(1).func_226896_b_().func_200948_a(3.0f, 4.0f));
    }
    
    public AABB getBlockSpace() {
        return BlockObservatory.PLACEMENT_BOX;
    }
    
    @Nullable
    public BlockState func_196258_a(final BlockItemUseContext context) {
        return this.canPlaceAt(context) ? this.defaultBlockState() : null;
    }
    
    public InteractionResult func_225533_a_(final BlockState state, final World worldIn, final BlockPos pos, final Player player, final Hand handIn, final BlockHitResult hit) {
        if (!worldIn.func_201670_d()) {
            final TileObservatory observatory = MiscUtils.getTileAt((IBlockReader)worldIn, pos, TileObservatory.class, false);
            if (observatory != null && observatory.isUsable() && !player.func_225608_bj_()) {
                final Entity entity = observatory.findRideableObservatoryEntity();
                if (entity != null) {
                    if (player.getVehicle() != entity) {
                        player.func_184220_m(entity);
                    }
                    new ContainerObservatoryProvider(observatory).openFor((ServerPlayer)player);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
    
    public VoxelShape func_220071_b(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final CollisionContext context) {
        return VoxelShapes.func_197880_a();
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader worldIn) {
        return new TileObservatory();
    }
    
    static {
        PLACEMENT_BOX = new AABB(-1.0, 0.0, -1.0, 1.0, 3.0, 1.0);
    }
}
