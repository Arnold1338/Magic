package hellfirepvp.astralsorcery.common.constellation.effect.aoe;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfiguredBlockStateList;
import hellfirepvp.astralsorcery.common.util.block.BlockStateList;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.data.config.registry.OreBlockRarityRegistry;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import java.util.function.Supplier;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.block.iterator.BlockRandomPositionGenerator;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import hellfirepvp.astralsorcery.common.util.block.iterator.BlockLayerPositionGenerator;
import hellfirepvp.astralsorcery.common.util.block.iterator.BlockPositionGenerator;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import hellfirepvp.astralsorcery.common.event.PlayerAffectionFlags;
import hellfirepvp.astralsorcery.common.constellation.effect.base.ListEntries;
import hellfirepvp.astralsorcery.common.constellation.effect.base.CEffectAbstractList;

public class CEffectMineralis extends CEffectAbstractList<ListEntries.PosEntry>
{
    public static PlayerAffectionFlags.AffectionFlag FLAG;
    public static MineralisConfig CONFIG;
    
    public CEffectMineralis(@Nonnull final ILocatable origin) {
        super(origin, ConstellationsAS.mineralis, (int)CEffectMineralis.CONFIG.maxAmount.get(), (world, pos, state) -> true);
        this.excludeRitualColumn();
        this.selectSphericalPositions();
    }
    
    @Nonnull
    @Override
    protected BlockPositionGenerator createPositionStrategy() {
        return new BlockLayerPositionGenerator();
    }
    
    @Nonnull
    @Override
    protected BlockPositionGenerator selectPositionStrategy(final BlockPositionGenerator defaultGenerator, final ConstellationEffectProperties properties) {
        if (!properties.isCorrupted()) {
            return new BlockRandomPositionGenerator();
        }
        return defaultGenerator;
    }
    
    @Nullable
    @Override
    public ListEntries.PosEntry recreateElement(final CompoundTag tag, final BlockPos pos) {
        return new ListEntries.PosEntry(pos);
    }
    
    @Nullable
    @Override
    public ListEntries.PosEntry createElement(final World world, final BlockPos pos) {
        return new ListEntries.PosEntry(pos);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void playClientEffect(final World world, final BlockPos pos, final TileRitualPedestal pedestal, final float alphaMultiplier, final boolean extended) {
        final ConstellationEffectProperties prop = this.createProperties(pedestal.getMirrorCount());
        if (CEffectMineralis.rand.nextFloat() < 0.6f) {
            final Color c = MiscUtils.eitherOf(CEffectMineralis.rand, (Supplier<Color>[])new Supplier[] { () -> ColorsAS.CONSTELLATION_MINERALIS, () -> ColorsAS.CONSTELLATION_MINERALIS.brighter() });
            final Vector3 at = Vector3.random().normalize().multiply(CEffectMineralis.rand.nextFloat() * prop.getSize()).add((Vector3i)pos).add(0.5, 0.5, 0.5);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).alpha(VFXAlphaFunction.FADE_OUT).setMotion(Vector3.random().multiply(0.05f)).color(VFXColorFunction.constant(c)).setScaleMultiplier(0.5f + CEffectMineralis.rand.nextFloat() * 0.25f).setMaxAge(50 + CEffectMineralis.rand.nextInt(40));
        }
    }
    
    @Override
    public boolean playEffect(final World world, final BlockPos pos, final ConstellationEffectProperties properties, @Nullable final IMinorConstellation trait) {
        return this.peekNewPosition(world, pos, properties).mapLeft(entry -> {
            final BlockPos at = entry.getPos();
            final BlockState atState = world.getBlockState(at);
            if (properties.isCorrupted()) {
                final boolean generateOre = CEffectMineralis.rand.nextInt(25) == 0;
                if (atState.isAir((IBlockReader)world, at) || (generateOre && atState.getBlock() == Blocks.field_150348_b)) {
                    if (generateOre) {
                        final Block ore = OreBlockRarityRegistry.MINERALIS_RITUAL.getRandomBlock(CEffectMineralis.rand);
                        if (ore != null) {
                            return world.func_175656_a(at, ore.defaultBlockState());
                        }
                        else {
                            return world.func_175656_a(at, Blocks.field_150348_b.defaultBlockState());
                        }
                    }
                    else {
                        return world.func_175656_a(at, Blocks.field_150348_b.defaultBlockState());
                    }
                }
            }
            else if (CEffectMineralis.CONFIG.replaceableStates.test(atState)) {
                final Block ore2 = OreBlockRarityRegistry.MINERALIS_RITUAL.getRandomBlock(CEffectMineralis.rand);
                if (ore2 != null) {
                    return world.func_175656_a(at, ore2.defaultBlockState());
                }
                else {
                    this.sendConstellationPing(world, new Vector3((Vector3i)at).add(0.5, 0.5, 0.5));
                }
            }
            else {
                this.sendConstellationPing(world, new Vector3((Vector3i)at).add(0.5, 0.5, 0.5));
            }
            return false;
        }).ifRight(attemptedBreak -> this.sendConstellationPing(world, new Vector3((Vector3i)attemptedBreak).add(0.5, 0.5, 0.5))).left().orElse(false);
    }
    
    @Override
    public Config getConfig() {
        return CEffectMineralis.CONFIG;
    }
    
    @Override
    public PlayerAffectionFlags.AffectionFlag getPlayerAffectionFlag() {
        return CEffectMineralis.FLAG;
    }
    
    static {
        CEffectMineralis.FLAG = ConstellationEffect.makeAffectionFlag("mineralis");
        CEffectMineralis.CONFIG = new MineralisConfig(new BlockStateList().add(Blocks.field_150348_b));
    }
    
    private static class MineralisConfig extends CountConfig
    {
        private final BlockStateList defaultReplaceableStates;
        private ConfiguredBlockStateList replaceableStates;
        
        public MineralisConfig(final BlockStateList defaultReplaceableStates) {
            super("mineralis", 5.0, 2.0, 1);
            this.defaultReplaceableStates = defaultReplaceableStates;
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            this.replaceableStates = this.defaultReplaceableStates.getAsConfig(cfgBuilder, "replaceableStates", this.translationKey("replaceableStates"), "Defines the blockstates that may be replaced by generated ore from the ritual.");
        }
    }
}
