package hellfirepvp.astralsorcery.common.item.wand;

import net.minecraft.world.level.block.state.Property;
import hellfirepvp.astralsorcery.common.block.tile.BlockFlareLight;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import hellfirepvp.astralsorcery.common.util.block.BlockUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.CollisionContext;
import hellfirepvp.astralsorcery.common.tile.TileIlluminator;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import net.minecraft.world.level.phys.shapes.Shapes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvent;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import net.minecraft.sounds.SoundSource;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileTranslucentBlock;
import hellfirepvp.astralsorcery.common.block.tile.BlockTranslucentBlock;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.item.ItemUseContext;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.item.DyeColor;
import hellfirepvp.astralsorcery.common.util.ColorUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.item.base.AlignmentChargeConsumer;
import hellfirepvp.astralsorcery.common.item.base.client.ItemDynamicColor;
import net.minecraft.world.item.Item;

public class ItemIlluminationWand extends Item implements ItemDynamicColor, AlignmentChargeConsumer
{
    private static final float COST_PER_ILLUMINATION = 650.0f;
    private static final float COST_PER_FLARE = 300.0f;
    
    public ItemIlluminationWand() {
        super(new Item.Properties().func_200917_a(1).hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
    
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final Level worldIn, final List<Component> tooltip, final TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, (List)tooltip, flagIn);
        final DyeColor color = getConfiguredColor(stack);
        tooltip.add((Component)ColorUtils.getTranslation(color).toString()ColorUtils.textFormattingForDye(color)));
    }
    
    public float getAlignmentChargeCost(final Player player, final ItemStack stack) {
        if (player.isCrouching()) {
            return 650.0f;
        }
        return 300.0f;
    }
    
    public InteractionResult func_195939_a(final ItemUseContext context) {
        final Level world = context.func_195991_k();
        final Direction dir = context.func_196000_l();
        final BlockPos pos = context.func_195995_a();
        final Player player = context.func_195999_j();
        final ItemStack stack = context.func_195996_i();
        if (world.level() || player == null || stack.isEmpty() || !(stack.getItem() instanceof ItemIlluminationWand)) {
            return InteractionResult.SUCCESS;
        }
        final BlockState state = world.getBlockState(pos);
        if (player.isCrouching()) {
            if (state.getBlock() instanceof BlockTranslucentBlock) {
                final TileTranslucentBlock tb = MiscUtils.getTileAt((IBlockReader)world, pos, TileTranslucentBlock.class, true);
                if (tb != null && (tb.getPlayerUUID() == null || tb.getPlayerUUID().equals(player.getUUID())) && tb.revert()) {
                    SoundHelper.playSoundAround(SoundsAS.ILLUMINATION_WAND_UNHIGHLIGHT, SoundSource.BLOCKS, world, (Vector3i)pos, 0.6f, 0.9f + ItemIlluminationWand.count.nextFloat() * 0.2f);
                }
            }
            else {
                final BlockEntity tile = MiscUtils.getTileAt((IBlockReader)world, pos, BlockEntity.class, true);
                if (tile == null && !state.hasTileEntity() && player.func_175151_a(pos, dir, stack) && VoxelShapes.func_197868_b().equals(world.getBlockState(pos).func_196954_c((IBlockReader)world, pos)) && AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, 650.0f, false) && world.func_180501_a(pos, BlocksAS.TRANSLUCENT_BLOCK.defaultBlockState(), 11)) {
                    SoundHelper.playSoundAround(SoundsAS.ILLUMINATION_WAND_HIGHLIGHT, SoundSource.BLOCKS, world, (Vector3i)pos, 0.6f, 0.9f + ItemIlluminationWand.count.nextFloat() * 0.2f);
                    final TileTranslucentBlock tb2 = MiscUtils.getTileAt((IBlockReader)world, pos, TileTranslucentBlock.class, true);
                    if (tb2 != null) {
                        tb2.setFakedState(state);
                        tb2.setOverlayColor(ColorUtils.flareColorFromDye(getConfiguredColor(stack)));
                        tb2.setPlayerUUID(player.getUUID());
                    }
                    else {
                        world.func_180501_a(pos, state, 11);
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        final TileIlluminator illum = MiscUtils.getTileAt((IBlockReader)world, pos, TileIlluminator.class, true);
        if (illum != null) {
            illum.onWandUsed(stack);
            SoundHelper.playSoundAround(SoundsAS.ILLUMINATION_WAND_LIGHT, SoundSource.BLOCKS, world, (Vector3i)pos, 0.6f, 1.0f);
            return InteractionResult.SUCCESS;
        }
        final CollisionContext selContext = CollisionContext.func_216374_a((Entity)player);
        BlockPos placePos = pos;
        final BlockState placeState = getPlacingState(stack);
        if (!BlockUtils.isReplaceable(world, pos)) {
            placePos = placePos.func_177972_a(dir);
        }
        if (!BlockUtils.isReplaceable(world, placePos)) {
            return InteractionResult.SUCCESS;
        }
        if (player.func_175151_a(placePos, dir, stack)) {
            if (world.getBlockState(placePos).equals(placeState)) {
                if (world.func_180501_a(placePos, Blocks.AIR.defaultBlockState(), 11)) {
                    SoundHelper.playSoundAround(SoundsAS.ILLUMINATION_WAND_LIGHT, SoundSource.BLOCKS, world, (Vector3i)pos, 0.6f, 1.0f);
                }
            }
            else if (placeState.func_196955_c((IWorldReader)world, placePos) && world.func_226663_a_(placeState, placePos, selContext) && AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, 300.0f, false) && world.func_180501_a(placePos, placeState, 11)) {
                SoundHelper.playSoundAround(SoundsAS.ILLUMINATION_WAND_LIGHT, SoundSource.BLOCKS, world, (Vector3i)pos, 0.6f, 1.0f);
            }
        }
        return InteractionResult.SUCCESS;
    }
    
    public int getColor(final ItemStack stack, final int tintIndex) {
        if (tintIndex != 1) {
            return -1;
        }
        final DyeColor color = getConfiguredColor(stack);
        return ColorUtils.flareColorFromDye(color).getRGB() | 0xFF000000;
    }
    
    public static void setConfiguredColor(final ItemStack stack, final DyeColor color) {
        NBTHelper.getPersistentData(stack).putInt("color", (color != null) ? color.func_196059_a() : DyeColor.YELLOW.func_196059_a());
    }
    
    @Nonnull
    public static DyeColor getConfiguredColor(final ItemStack stack) {
        final CompoundTag tag = NBTHelper.getPersistentData(stack);
        if (tag.contains("color")) {
            return DyeColor.func_196056_a(tag.getInt("color"));
        }
        return DyeColor.YELLOW;
    }
    
    @Nonnull
    public static BlockState getPlacingState(final ItemStack wand) {
        return (BlockState)BlocksAS.FLARE_LIGHT.defaultBlockState().setValue((Property)BlockFlareLight.COLOR, (Comparable)getConfiguredColor(wand));
    }
}
