package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.level.phys.shapes.Shapes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.pathfinding.PathType;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import hellfirepvp.observerlib.common.data.WorldCacheDomain;
import hellfirepvp.astralsorcery.common.lib.DataAS;
import hellfirepvp.astralsorcery.common.data.world.GatewayCache;
import net.minecraftforge.common.ForgeHooks;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.item.ItemAquamarine;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.phys.BlockHitResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileCelestialGateway;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.phys.HitResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.item.DyeColor;
import hellfirepvp.astralsorcery.common.util.ColorUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesGlass;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import hellfirepvp.observerlib.api.block.BlockStructureObserver;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.block.BaseEntityBlock;

public class BlockCelestialGateway extends BaseEntityBlock implements CustomItemBlock, BlockStructureObserver
{
    private static final VoxelShape SHAPE;
    
    public BlockCelestialGateway() {
        super(PropertiesGlass.coatedGlass().func_235838_a_(state -> 12).func_200948_a(-1.0f, 3600000.0f).harvestLevel(1).harvestTool(ToolType.PICKAXE));
    }
    
    @OnlyIn(Dist.CLIENT)
    public void func_190948_a(final ItemStack stack, @Nullable final IBlockReader worldIn, final List<Component> tooltip, final TooltipFlag flagIn) {
        super.func_190948_a(stack, worldIn, (List)tooltip, flagIn);
        final DyeColor color = getColor(stack);
        if (color != null) {
            tooltip.add((Component)ColorUtils.getTranslation(color).withStyle(ColorUtils.textFormattingForDye(color)));
        }
    }
    
    public ItemStack getPickBlock(final BlockState state, final HitResult target, final IBlockReader world, final BlockPos pos, final Player player) {
        final ItemStack stack = new ItemStack((ItemLike)BlocksAS.GATEWAY);
        final TileCelestialGateway gateway = MiscUtils.getTileAt(world, pos, TileCelestialGateway.class, true);
        if (gateway != null) {
            if (gateway.getSaveId()) {
                stack.setHoverName(gateway.getDisplayName());
            }
            gateway.getColor().ifPresent(color -> setColor(stack, color));
        }
        return stack;
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader world, final BlockPos pos, final CollisionContext context) {
        return BlockCelestialGateway.SHAPE;
    }
    
    public InteractionResult func_225533_a_(final BlockState state, final Level world, final BlockPos pos, final Player player, final Hand hand, final BlockHitResult hit) {
        final TileCelestialGateway gateway = MiscUtils.getTileAt((IBlockReader)world, pos, TileCelestialGateway.class, false);
        if (gateway != null && gateway.getOwner() != null && gateway.getOwner().isPlayer(player)) {
            if (gateway.isLocked()) {
                if (!world.level()) {
                    final ItemStack remaining = ItemUtils.dropItemToPlayer(player, new ItemStack((ItemLike)ItemsAS.AQUAMARINE));
                    if (!remaining.isEmpty()) {
                        ItemUtils.dropItemNaturally(world, player.getX(), player.getY(), player.getZ(), remaining);
                    }
                    gateway.unlock();
                }
                return InteractionResult.SUCCESS;
            }
            final ItemStack held = player.getItemInHand(hand);
            if (held.getItem() instanceof ItemAquamarine) {
                if (!world.level()) {
                    held.shrink(1);
                    gateway.lock();
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
    
    public void func_180633_a(final Level world, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
        super.func_180633_a(world, pos, state, placer, stack);
        final TileCelestialGateway gateway = MiscUtils.getTileAt((IBlockReader)world, pos, TileCelestialGateway.class, true);
        if (gateway != null) {
            if (stack.func_82837_s()) {
                gateway.setDisplayText(stack.func_200301_q());
            }
            final DyeColor color = getColor(stack);
            if (color != null) {
                gateway.setColor(color);
            }
        }
    }
    
    public float func_180647_a(final BlockState state, final Player player, final IBlockReader world, final BlockPos pos) {
        final TileCelestialGateway gateway = MiscUtils.getTileAt(world, pos, TileCelestialGateway.class, true);
        if (gateway != null && (!gateway.isLocked() || (gateway.getOwner() != null && gateway.getOwner().isPlayer(player)))) {
            final int i = ForgeHooks.canHarvestBlock(state, player, world, pos) ? 30 : 100;
            return player.getDigSpeed(state, pos) / 2.5f / i;
        }
        return super.func_180647_a(state, player, world, pos);
    }
    
    public void func_196243_a(final BlockState state, final Level world, final BlockPos pos, final BlockState newState, final boolean moving) {
        if (state != newState && !world.level()) {
            ((GatewayCache)DataAS.DOMAIN_AS.getData(world, (WorldCacheDomain.SaveKey)DataAS.KEY_GATEWAY_CACHE)).removePosition(world, pos);
            final TileCelestialGateway gateway = MiscUtils.getTileAt((IBlockReader)world, pos, TileCelestialGateway.class, true);
            if (gateway != null && gateway.isLocked()) {
                ItemUtils.dropItemNaturally(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack((ItemLike)ItemsAS.AQUAMARINE));
            }
        }
        super.func_196243_a(state, world, pos, newState, moving);
    }
    
    public BlockState func_196271_a(final BlockState state, final Direction placedAgainst, final BlockState facingState, final IWorld world, final BlockPos pos, final BlockPos facingPos) {
        if (!this.func_196260_a(state, (IWorldReader)world, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }
    
    public boolean func_196260_a(final BlockState state, final IWorldReader world, final BlockPos pos) {
        final TileCelestialGateway gateway = MiscUtils.getTileAt((IBlockReader)world, pos, TileCelestialGateway.class, true);
        return (gateway != null && gateway.isLocked()) || func_220064_c((IBlockReader)world, pos.renderItem());
    }
    
    @Nullable
    public static DyeColor getColor(final ItemStack stack) {
        if (!(stack.getItem() instanceof BlockItem) || !(((BlockItem)stack.getItem()).func_179223_d() instanceof BlockCelestialGateway)) {
            return null;
        }
        final CompoundTag tag = NBTHelper.getPersistentData(stack);
        if (!tag.contains("color")) {
            return null;
        }
        return NBTHelper.readEnum(tag, "color", DyeColor.class);
    }
    
    public static void setColor(final ItemStack stack, @Nullable final DyeColor color) {
        if (!(stack.getItem() instanceof BlockItem) || !(((BlockItem)stack.getItem()).func_179223_d() instanceof BlockCelestialGateway)) {
            return;
        }
        final CompoundTag tag = NBTHelper.getPersistentData(stack);
        if (color == null) {
            tag.func_82580_o("color");
        }
        else {
            NBTHelper.writeEnum(tag, "color", color);
        }
    }
    
    public boolean func_196266_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }
    
    public RenderShape func_149645_b(final BlockState state) {
        return RenderShape.MODEL;
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader world) {
        return new TileCelestialGateway();
    }
    
    static {
        SHAPE = VoxelShapes.func_197873_a(0.0625, 0.0, 0.0625, 0.9375, 0.0625, 0.9375);
    }
}
