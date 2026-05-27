package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.pathfinding.PathType;
import hellfirepvp.astralsorcery.common.GuiType;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.item.ItemParchment;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileRefractionTable;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.Random;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.world.item.BlockItemUseContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesWood;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.base.LargeBlock;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.block.BaseEntityBlock;

public class BlockRefractionTable extends BaseEntityBlock implements CustomItemBlock, LargeBlock
{
    private static final VoxelShape REFRACTION_TABLE;
    private static final AABB PLACEMENT_BOX;
    
    public BlockRefractionTable() {
        super(PropertiesWood.defaultInfusedWood().func_226896_b_());
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final CollisionContext context) {
        return BlockRefractionTable.REFRACTION_TABLE;
    }
    
    public AABB getBlockSpace() {
        return BlockRefractionTable.PLACEMENT_BOX;
    }
    
    @Nullable
    public BlockState func_196258_a(final BlockItemUseContext context) {
        return this.canPlaceAt(context) ? this.defaultBlockState() : null;
    }
    
    @OnlyIn(Dist.CLIENT)
    public void func_180655_c(final BlockState stateIn, final Level worldIn, final BlockPos pos, final Random rand) {
        for (int i = 0; i < rand.nextInt(3); ++i) {
            final Vector3 offset = new Vector3(-0.3125, 1.505, -0.1875);
            final int random = rand.nextInt(ColorsAS.REFRACTION_TABLE_COLORS.length);
            if (random >= ColorsAS.REFRACTION_TABLE_COLORS.length / 2) {
                offset.addX(1.5);
            }
            offset.addZ(random % (ColorsAS.REFRACTION_TABLE_COLORS.length / 2) * 0.25);
            offset.add(rand.nextFloat() * 0.1, 0.0, rand.nextFloat() * 0.1).add((Vec3i)pos);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(offset).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.15f + rand.nextFloat() * 0.1f).color(VFXColorFunction.constant(ColorsAS.REFRACTION_TABLE_COLORS[random])).setMaxAge(35 + rand.nextInt(30));
        }
    }
    
    public InteractionResult func_225533_a_(final BlockState state, final Level world, final BlockPos pos, final Player player, final Hand hand, final BlockHitResult hit) {
        final ItemStack held = player.getItemInHand(hand);
        if (!world.level()) {
            final TileRefractionTable tft = MiscUtils.getTileAt((IBlockReader)world, pos, TileRefractionTable.class, true);
            if (tft != null) {
                if (player.isCrouching()) {
                    if (!tft.getInputStack().isEmpty()) {
                        final ItemStack remaining = ItemUtils.dropItemToPlayer(player, tft.setInputStack(ItemStack.EMPTY));
                        if (!remaining.isEmpty()) {
                            ItemUtils.dropItemNaturally(world, player.getX(), player.getY(), player.getZ(), remaining);
                        }
                        return InteractionResult.SUCCESS;
                    }
                    if (!tft.getGlassStack().isEmpty()) {
                        final ItemStack remaining = ItemUtils.dropItemToPlayer(player, tft.setGlassStack(ItemStack.EMPTY));
                        if (!remaining.isEmpty()) {
                            ItemUtils.dropItemNaturally(world, player.getX(), player.getY(), player.getZ(), remaining);
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
                else if (!held.isEmpty()) {
                    if (held.getItem() instanceof ItemParchment && tft.getParchmentCount() < 64) {
                        final int leftover = tft.addParchment(held.getCount());
                        if (leftover < tft.getParchmentCount() && !player.getVehicle()) {
                            held.setCount(leftover);
                            if (held.isEmpty()) {
                                player.func_184611_a(hand, ItemStack.EMPTY);
                            }
                            else {
                                player.func_184611_a(hand, held);
                            }
                        }
                    }
                    else {
                        if (TileRefractionTable.isValidGlassStack(held) && tft.getGlassStack().isEmpty()) {
                            final ItemStack previous = tft.setGlassStack(ItemUtils.copyStackWithSize(held, 1));
                            if (!previous.isEmpty()) {
                                ItemUtils.dropItemNaturally(world, pos.getX() + 0.5, pos.getY() + 1.8, pos.getZ() + 0.5, previous);
                            }
                            if (!player.getVehicle()) {
                                held.shrink(1);
                                if (held.isEmpty()) {
                                    player.func_184611_a(hand, ItemStack.EMPTY);
                                }
                                else {
                                    player.func_184611_a(hand, held);
                                }
                            }
                            return InteractionResult.PASS;
                        }
                        if (tft.getInputStack().isEmpty()) {
                            final ItemStack previous = tft.setInputStack(ItemUtils.copyStackWithSize(held, 1));
                            if (!previous.isEmpty()) {
                                ItemUtils.dropItemNaturally(world, pos.getX() + 0.5, pos.getY() + 1.8, pos.getZ() + 0.5, previous);
                            }
                            if (!player.getVehicle()) {
                                held.shrink(1);
                                if (held.isEmpty()) {
                                    player.func_184611_a(hand, ItemStack.EMPTY);
                                }
                                else {
                                    player.func_184611_a(hand, held);
                                }
                            }
                        }
                        else {
                            AstralSorcery.getProxy().openGui(player, GuiType.REFRACTION_TABLE, pos);
                        }
                    }
                }
                else {
                    AstralSorcery.getProxy().openGui(player, GuiType.REFRACTION_TABLE, pos);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
    
    public void func_196243_a(final BlockState state, final Level world, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        final TileRefractionTable te = MiscUtils.getTileAt((IBlockReader)world, pos, TileRefractionTable.class, true);
        if (te != null && !world.isClientSide) {
            te.dropContents();
        }
        super.func_196243_a(state, world, pos, newState, isMoving);
    }
    
    public boolean func_196266_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader worldIn) {
        return new TileRefractionTable();
    }
    
    static {
        REFRACTION_TABLE = Block.of(-6.0, 0.0, -4.0, 22.0, 24.0, 20.0);
        PLACEMENT_BOX = new AABB(-1.0, 0.0, -1.0, 1.0, 1.0, 1.0);
    }
}
