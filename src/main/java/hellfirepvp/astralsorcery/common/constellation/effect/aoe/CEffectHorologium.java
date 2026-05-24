package hellfirepvp.astralsorcery.common.constellation.effect.aoe;

import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;
import net.minecraft.world.level.LevelReader;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.time.TimeStopZone;
import hellfirepvp.astralsorcery.common.util.time.TimeStopController;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightning;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import java.util.function.Supplier;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.block.iterator.BlockRandomProximityPositionGenerator;
import hellfirepvp.astralsorcery.common.util.block.iterator.BlockPositionGenerator;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.data.config.registry.TileAccelerationBlacklistRegistry;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import hellfirepvp.astralsorcery.common.event.PlayerAffectionFlags;
import hellfirepvp.astralsorcery.common.constellation.effect.base.ListEntries;
import hellfirepvp.astralsorcery.common.constellation.effect.base.CEffectAbstractList;

public class CEffectHorologium extends CEffectAbstractList<ListEntries.PosEntry>
{
    public static PlayerAffectionFlags.AffectionFlag FLAG;
    public static HorologiumConfig CONFIG;
    
    public CEffectHorologium(@Nonnull final ILocatable origin) {
        super(origin, ConstellationsAS.horologium, (int)CEffectHorologium.CONFIG.maxAmount.get(), (world, pos, state) -> TileAccelerationBlacklistRegistry.INSTANCE.canBeInfluenced(MiscUtils.getTileAt((IBlockReader)world, pos, BlockEntity.class, false)));
    }
    
    @Nonnull
    @Override
    protected BlockPositionGenerator createPositionStrategy() {
        return new BlockRandomProximityPositionGenerator();
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
        for (int i = 0; i < 2; ++i) {
            final Color c = MiscUtils.eitherOf(CEffectHorologium.rand, (Supplier<Color>[])new Supplier[] { () -> Color.WHITE, () -> ColorsAS.CONSTELLATION_HOROLOGIUM });
            final Vector3 at = Vector3.random().normalize().multiply(CEffectHorologium.rand.nextFloat() * prop.getSize()).add((Vector3i)pos).add(0.5, 0.5, 0.5);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.constant(c)).setScaleMultiplier(0.3f + CEffectHorologium.rand.nextFloat() * 0.5f).setMaxAge(40 + CEffectHorologium.rand.nextInt(20));
        }
        if (CEffectHorologium.rand.nextInt(16) == 0) {
            final Vector3 rand1 = Vector3.random().normalize().multiply(CEffectHorologium.rand.nextFloat() * prop.getSize()).add((Vector3i)pos).add(0.5, 0.5, 0.5);
            final Vector3 rand2 = Vector3.random().normalize().multiply(CEffectHorologium.rand.nextFloat() * prop.getSize()).add((Vector3i)pos).add(0.5, 0.5, 0.5);
            EffectHelper.of(EffectTemplatesAS.LIGHTNING).spawn(rand1).makeDefault(rand2).color(VFXColorFunction.WHITE);
        }
    }
    
    @Override
    public boolean playEffect(final World world, final BlockPos pos, final ConstellationEffectProperties properties, @Nullable final IMinorConstellation trait) {
        boolean changed = false;
        if (properties.isCorrupted()) {
            TimeStopZone zone = TimeStopController.tryGetZoneAt(world, pos);
            if (zone == null) {
                zone = TimeStopController.freezeWorldAt(TimeStopZone.EntityTargetController.noPlayers(), world, pos, (float)properties.getSize(), 100);
            }
            zone.setTicksToLive(100);
            return true;
        }
        final ListEntries.PosEntry entry = this.getRandomElementChanced();
        if (entry != null && MiscUtils.executeWithChunk((IWorldReader)world, entry.getPos(), () -> {
            final BlockEntity tile = MiscUtils.getTileAt((IBlockReader)world, entry.getPos(), BlockEntity.class, true);
            if (tile != null && this.isValid(world, entry)) {
                this.sendConstellationPing(world, new Vector3((Vector3i)entry.getPos()).add(Vector3.positiveRandom()));
                this.sendConstellationPing(world, new Vector3((Vector3i)entry.getPos()).add(Vector3.positiveRandom()));
                try {
                    final long startNs = System.nanoTime();
                    int times = 4 + CEffectHorologium.rand.nextInt(2);
                    while (times > 0) {
                        ((ITickableTileEntity)tile).func_73660_a();
                        if (System.nanoTime() - startNs >= 80000L) {
                            break;
                        }
                        else {
                            --times;
                        }
                    }
                }
                catch (final Exception exc) {
                    TileAccelerationBlacklistRegistry.INSTANCE.addErrored(tile);
                    this.removeElement(entry);
                    AstralSorcery.log.warn("Couldn't accelerate BlockEntity " + tile.getClass().getName() + ".");
                    AstralSorcery.log.warn("Temporarily blacklisting that class. Consider adding that to the blacklist if it persists?");
                    exc.printStackTrace();
                }
                return false;
            }
            else {
                this.removeElement(entry);
                return true;
            }
        }, false)) {
            changed = true;
        }
        if (this.findNewPosition(world, pos, properties).ifRight(attemptedPos -> this.sendConstellationPing(world, new Vector3((Vector3i)attemptedPos).add(0.5, 0.5, 0.5))).left().isPresent()) {
            changed = true;
        }
        return changed;
    }
    
    @Override
    public Config getConfig() {
        return CEffectHorologium.CONFIG;
    }
    
    @Override
    public PlayerAffectionFlags.AffectionFlag getPlayerAffectionFlag() {
        return CEffectHorologium.FLAG;
    }
    
    static {
        CEffectHorologium.FLAG = ConstellationEffect.makeAffectionFlag("horologium");
        CEffectHorologium.CONFIG = new HorologiumConfig();
    }
    
    private static class HorologiumConfig extends CountConfig
    {
        public HorologiumConfig() {
            super("horologium", 6.0, 3.0, 32);
        }
    }
}
