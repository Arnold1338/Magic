package hellfirepvp.astralsorcery.common.constellation.effect;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.network.PacketChannel;

import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.event.PlayerAffectionFlags;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.tile.TileRitualLink;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.world.phys.AABB;
import java.util.Random;

public abstract class ConstellationEffect
{
    protected static final Random rand;
    protected static final AABB BOX;
    private final IWeakConstellation cst;
    private final ILocatable pos;
    private boolean needsChunkToBeLoaded;
    
    protected ConstellationEffect(@Nonnull final ILocatable origin, @Nonnull final IWeakConstellation cst) {
        this.needsChunkToBeLoaded = false;
        this.cst = cst;
        this.pos = origin;
    }
    
    protected void setChunkNeedsToBeLoaded() {
        this.needsChunkToBeLoaded = true;
    }
    
    public boolean needsChunkToBeLoaded() {
        return this.needsChunkToBeLoaded;
    }
    
    @OnlyIn(Dist.CLIENT)
    public abstract void playClientEffect(final Level p0, final BlockPos p1, final TileRitualPedestal p2, final float p3, final boolean p4);
    
    public abstract boolean playEffect(final Level p0, final BlockPos p1, final ConstellationEffectProperties p2, @Nullable final IMinorConstellation p3);
    
    @Nullable
    public TileRitualPedestal getPedestal(final Level world, BlockPos pos) {
        final BlockEntity te = MiscUtils.getTileAt((IBlockReader)world, pos, BlockEntity.class, false);
        if (te instanceof TileRitualLink) {
            final TileRitualLink link = (TileRitualLink)te;
            pos = link.getLinkedTo();
            if (pos != null) {
                pos = pos.func_177973_b((Vec3i)TileRitualPedestal.RITUAL_ANCHOR_OFFEST);
                return MiscUtils.getTileAt((IBlockReader)world, pos, TileRitualPedestal.class, false);
            }
        }
        return (te instanceof TileRitualPedestal) ? ((TileRitualPedestal)te) : null;
    }
    
    public ConstellationEffectProperties createProperties(final int mirrors) {
        return new ConstellationEffectProperties((double)this.getConfig().range.get() + mirrors * (double)this.getConfig().rangePerLens.get());
    }
    
    public abstract Config getConfig();
    
    public abstract PlayerAffectionFlags.AffectionFlag getPlayerAffectionFlag();
    
    protected static PlayerAffectionFlags.AffectionFlag makeAffectionFlag(final String constellationName) {
        return new PlayerAffectionFlags.NoOpAffectionFlag(AstralSorcery.key("constellation_effect_" + constellationName));
    }
    
    @Nonnull
    public IWeakConstellation getConstellation() {
        return this.cst;
    }
    
    @Nonnull
    public ILocatable getPos() {
        return this.pos;
    }
    
    public void clearCache() {
    }
    
    public void readFromNBT(final CompoundTag cmp) {
    }
    
    public void writeToNBT(final CompoundTag cmp) {
    }
    
    @Nullable
    public Player getOwningPlayerInWorld(final Level world, final BlockPos pos) {
        final TileRitualPedestal pedestal = this.getPedestal(world, pos);
        if (pedestal != null) {
            return pedestal.getOwner();
        }
        return null;
    }
    
    public void sendConstellationPing(final Level world, final Vector3 at) {
        sendConstellationPing(world, at, this.getConstellation());
    }
    
    public static void sendConstellationPing(final Level world, final Vector3 at, final IConstellation cst) {
        final PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.Type.CONSTELLATION_EFFECT_PING).addData(buf -> {
            ByteBufUtils.writeVector(buf, at);
            ByteBufUtils.writeRegistryEntry(buf, (net.minecraftforge.registries.Object<Object>)cst);
            return;
        });
        PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos(world, (Vec3i)at.toBlockPos(), 32.0));
    }
    
    protected void markPlayerAffected(final Player player) {
        if (player.level()) {
            return;
        }
        PlayerAffectionFlags.markPlayerAffected(player, this.getPlayerAffectionFlag());
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playConstellationPing(final PktPlayEffect pktPlayEffect) {
        final Vector3 at = ByteBufUtils.readVector(pktPlayEffect.getExtraData());
        final IConstellation cst = ByteBufUtils.readRegistryEntry(pktPlayEffect.getExtraData());
        for (int i = 0; i < 6; ++i) {
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at.clone().add(Vector3.random().multiply(0.25f))).setMotion(Vector3.random().multiply(0.015f)).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.constant(cst.getConstellationColor().brighter())).setScaleMultiplier(0.25f + ConstellationEffect.rand.nextFloat() * 0.15f).setMaxAge(35 + ConstellationEffect.rand.nextInt(20));
        }
    }
    
    static {
        rand = new Random();
        BOX = new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    }
    
    public abstract static class Config extends ConfigEntry
    {
        private final boolean defaultEnabled = true;
        private final double defaultRange;
        private final double defaultRangePerLens;
        public ForgeConfigSpec.BooleanValue enabled;
        public ForgeConfigSpec.DoubleValue range;
        public ForgeConfigSpec.DoubleValue rangePerLens;
        
        public Config(final String constellationName, final double defaultRange, final double defaultRangePerLens) {
            super(String.format("constellation.effect.%s", constellationName));
            this.defaultRange = defaultRange;
            this.defaultRangePerLens = defaultRangePerLens;
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Set this to false to disable this ritual effect").translation(this.translationKey("enabled"));
            final String s = "enabled";
            this.getClass();
            this.enabled = translation.define(s, true);
            this.range = cfgBuilder.comment("Defines the radius (in blocks) in which the ritual will do its effects.").translation(this.translationKey("range")).defineInRange("range", this.defaultRange, 1.0, 512.0);
            this.rangePerLens = cfgBuilder.comment("Defines the increase in radius the ritual will get per active lens enhancing the ritual.").translation(this.translationKey("rangePerLens")).defineInRange("rangePerLens", this.defaultRangePerLens, 0.0, 128.0);
        }
    }
}
