package hellfirepvp.astralsorcery.common.perk.type;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.util.block.BlockUtils;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.util.block.BlockPredicate;
import net.minecraft.world.level.phys.BlockHitResult;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.Mth;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.phys.HitResult;
import net.minecraft.world.level.ClipContext;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import hellfirepvp.astralsorcery.common.event.EventFlags;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.BlockEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;

public class AttributeTypeMiningSize extends PerkAttributeType
{
    public static final Config CONFIG;
    
    public AttributeTypeMiningSize() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_MINING_SIZE);
    }
    
    @Override
    protected void attachListeners(final IEventBus eventBus) {
        super.attachListeners(eventBus);
        eventBus.addListener((Consumer)this::onBreak);
    }
    
    private void onBreak(final BlockEvent.BreakEvent event) {
        final IWorld world = event.getWorld();
        final Player player = event.getPlayer();
        if (!(world instanceof World) || world.func_201670_d()) {
            return;
        }
        if (player instanceof ServerPlayer) {
            final PlayerProgress prog = ResearchHelper.getProgress(player, LogicalSide.SERVER);
            if (!prog.doPerkAbilities() || MiscUtils.isPlayerFakeMP((ServerPlayer)player)) {
                return;
            }
            EventFlags.MINING_SIZE_BREAK.executeWithFlag(() -> {
                final float size = PerkAttributeHelper.getOrCreateMap(player, LogicalSide.SERVER).modifyValue(player, prog, PerkAttributeTypesAS.ATTR_TYPE_MINING_SIZE, 0.0f);
                final float size2 = AttributeEvent.postProcessModded(player, PerkAttributeTypesAS.ATTR_TYPE_MINING_SIZE, size);
                if (size2 >= 1.0f) {
                    final BlockHitResult brtr = MiscUtils.rayTraceLookBlock(player, ClipContext.BlockMode.OUTLINE, ClipContext.FluidMode.NONE);
                    if (brtr != null && brtr.func_216346_c() == HitResult.Type.BLOCK) {
                        final int levelBroken = event.getState().getHarvestLevel();
                        final float hardnessBroken = event.getState().func_185887_b((IBlockReader)world, event.getPos());
                        final BlockPredicate miningTest = (worldIn, posIn, stateIn) -> stateIn.getHarvestLevel() <= levelBroken && stateIn.func_185887_b((IBlockReader)worldIn, posIn) <= hardnessBroken;
                        final Direction dir = brtr.func_216354_b();
                        if (dir.func_176740_k() == Direction.Axis.Y) {
                            this.breakBlocksPlaneHorizontal((ServerPlayer)player, dir, (World)world, event.getPos(), miningTest, Mth.func_76141_d(size2));
                        }
                        else {
                            this.breakBlocksPlaneVertical((ServerPlayer)player, dir, (World)world, event.getPos(), miningTest, Mth.func_76141_d(size2));
                        }
                    }
                }
            });
        }
    }
    
    private void breakBlocksPlaneVertical(final ServerPlayer player, final Direction sideBroken, final World world, final BlockPos at, final BlockPredicate miningTest, final int size) {
        if (size <= 0) {
            return;
        }
        for (int xx = -size; xx <= size; ++xx) {
            if (sideBroken.func_176730_m().getX() == 0 || xx == 0) {
                for (int yy = -1; yy <= size * 2 - 1; ++yy) {
                    if (sideBroken.func_176730_m().getY() == 0 || yy == 0) {
                        for (int zz = -size; zz <= size; ++zz) {
                            if (sideBroken.func_176730_m().getZ() == 0 || zz == 0) {
                                if (xx != 0 || yy != 0 || zz != 0) {
                                    final BlockPos other = at.offset(xx, yy, zz);
                                    final BlockState otherState = world.getBlockState(other);
                                    if (otherState.func_185887_b((IBlockReader)world, other) != -1.0f && (player.func_184812_l_() || miningTest.test(world, other, otherState)) && AlignmentChargeHandler.INSTANCE.drainCharge((Player)player, LogicalSide.SERVER, (float)(int)AttributeTypeMiningSize.CONFIG.chargeCostPerBreak.get(), true)) {
                                        final BlockState state = world.getBlockState(other);
                                        if (!BlockUtils.isFluidBlock(state) && (player.func_184812_l_() || otherState.canHarvestBlock((IBlockReader)world, other, (Player)player)) && player.field_71134_c.func_180237_b(other) && AttributeTypeMiningSize.rand.nextInt(3) == 0) {
                                            AlignmentChargeHandler.INSTANCE.drainCharge((Player)player, LogicalSide.SERVER, (float)(int)AttributeTypeMiningSize.CONFIG.chargeCostPerBreak.get(), false);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void breakBlocksPlaneHorizontal(final ServerPlayer player, final Direction sideBroken, final World world, final BlockPos at, final BlockPredicate miningTest, final int size) {
        if (size <= 0) {
            return;
        }
        for (int xx = -size; xx <= size; ++xx) {
            if (sideBroken.func_176730_m().getX() == 0 || xx == 0) {
                for (int zz = -size; zz <= size; ++zz) {
                    if (sideBroken.func_176730_m().getZ() == 0 || zz == 0) {
                        if (xx != 0 || zz != 0) {
                            final BlockPos other = at.offset(xx, 0, zz);
                            final BlockState otherState = world.getBlockState(other);
                            if (otherState.func_185887_b((IBlockReader)world, other) != -1.0f && (player.func_184812_l_() || miningTest.test(world, other, otherState)) && AlignmentChargeHandler.INSTANCE.drainCharge((Player)player, LogicalSide.SERVER, (float)(int)AttributeTypeMiningSize.CONFIG.chargeCostPerBreak.get(), true)) {
                                final BlockState state = world.getBlockState(other);
                                if (!BlockUtils.isFluidBlock(state) && (player.func_184812_l_() || otherState.canHarvestBlock((IBlockReader)world, other, (Player)player)) && player.field_71134_c.func_180237_b(other) && AttributeTypeMiningSize.rand.nextInt(3) == 0) {
                                    AlignmentChargeHandler.INSTANCE.drainCharge((Player)player, LogicalSide.SERVER, (float)(int)AttributeTypeMiningSize.CONFIG.chargeCostPerBreak.get(), false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    static {
        CONFIG = new Config("type." + PerkAttributeTypesAS.KEY_ATTR_TYPE_MINING_SIZE.func_110623_a());
    }
    
    private static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.IntValue chargeCostPerBreak;
        
        private Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.chargeCostPerBreak = cfgBuilder.comment("Defines the amount of starlight charge consumed per additional block break through this attribute.").translation(this.translationKey("chargeCostPerBreak")).defineInRange("chargeCostPerBreak", 2, 1, 500);
        }
    }
}
