package hellfirepvp.astralsorcery.common.constellation.effect.aoe;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.FriendlyByteBuf;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.NonNullList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffects;
import hellfirepvp.astralsorcery.common.util.entity.EntityUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import hellfirepvp.astralsorcery.common.lib.EffectsAS;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import hellfirepvp.astralsorcery.common.util.block.BlockUtils;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.world.level.LevelAccessor;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import hellfirepvp.astralsorcery.common.event.PlayerAffectionFlags;
import hellfirepvp.astralsorcery.common.auxiliary.CropHelper;
import hellfirepvp.astralsorcery.common.constellation.effect.base.CEffectAbstractList;

public class CEffectAevitas extends CEffectAbstractList<CropHelper.GrowablePlant>
{
    public static PlayerAffectionFlags.AffectionFlag FLAG;
    public static AevitasConfig CONFIG;
    
    public CEffectAevitas(@Nonnull final ILocatable origin) {
        super(origin, ConstellationsAS.aevitas, (int)CEffectAevitas.CONFIG.maxAmount.get(), (world, pos, state) -> CropHelper.wrapPlant((IWorld)world, pos) != null);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void playClientEffect(final World world, final BlockPos pos, final TileRitualPedestal pedestal, final float alphaMultiplier, final boolean extended) {
        if (CEffectAevitas.rand.nextBoolean()) {
            final ConstellationEffectProperties prop = this.createProperties(pedestal.getMirrorCount());
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3(pos.getX() + CEffectAevitas.rand.nextFloat() * (prop.getSize() / 2.0) * (CEffectAevitas.rand.nextBoolean() ? 1 : -1) + 0.5, pos.getY() + CEffectAevitas.rand.nextFloat() * (prop.getSize() / 4.0) + 0.5, pos.getZ() + CEffectAevitas.rand.nextFloat() * (prop.getSize() / 2.0) * (CEffectAevitas.rand.nextBoolean() ? 1 : -1) + 0.5)).setGravityStrength(-0.005f).setScaleMultiplier(0.45f).color(VFXColorFunction.constant(ColorsAS.RITUAL_CONSTELLATION_AEVITAS)).setMaxAge(35);
        }
    }
    
    @Override
    public boolean playEffect(final World world, final BlockPos pos, final ConstellationEffectProperties properties, @Nullable final IMinorConstellation trait) {
        boolean changed = false;
        final CropHelper.GrowablePlant plant = this.getRandomElementChanced();
        if (plant != null) {
            changed = MiscUtils.executeWithChunk((IWorldReader)world, plant.getPos(), changed, changedFlag -> {
                if (properties.isCorrupted()) {
                    if (world instanceof ServerLevel) {
                        final CropHelper.HarvestablePlant harvestablePlant = CropHelper.wrapHarvestablePlant((IWorld)world, plant.getPos());
                        if (harvestablePlant != null) {
                            final NonNullList<ItemStack> drops = harvestablePlant.harvestDropsAndReplant((ServerLevel)world, CEffectAevitas.rand, 1);
                            drops.forEach(drop -> ItemUtils.dropItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
                            changedFlag = !drops.isEmpty();
                        }
                        else if (BlockUtils.breakBlockWithoutPlayer((ServerLevel)world, plant.getPos())) {
                            changedFlag = true;
                        }
                    }
                    else if (world.func_217377_a(plant.getPos(), false)) {
                        changedFlag = true;
                    }
                }
                else if (!plant.isValid((IWorld)world)) {
                    this.removeElement(plant.getPos());
                    changedFlag = true;
                }
                else if (plant.tryGrow((IWorld)world, CEffectAevitas.rand)) {
                    final PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.Type.CROP_GROWTH).addData(buf -> ByteBufUtils.writeVector(buf, new Vector3((Vector3i)plant.getPos())));
                    PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos(world, (Vector3i)plant.getPos(), 16.0));
                    changedFlag = true;
                }
                return changedFlag;
            }, false);
        }
        if (this.findNewPosition(world, pos, properties).ifRight(attemptedPos -> this.sendConstellationPing(world, new Vector3((Vector3i)attemptedPos).add(0.5, 0.5, 0.5))).left().isPresent()) {
            changed = true;
        }
        if (this.findNewPosition(world, pos, properties).ifRight(attemptedPos -> this.sendConstellationPing(world, new Vector3((Vector3i)attemptedPos).add(0.5, 0.5, 0.5))).left().isPresent()) {
            changed = true;
        }
        final int amplifier = (int)CEffectAevitas.CONFIG.potionAmplifier.get();
        final List<LivingEntity> entities = world.func_217357_a((Class)LivingEntity.class, CEffectAevitas.BOX.func_186670_a(pos).func_186662_g(properties.getSize()));
        for (final LivingEntity entity : entities) {
            if (entity.isAlive()) {
                if (properties.isCorrupted()) {
                    EntityUtils.applyPotionEffectAtHalf(entity, new MobEffectInstance((Effect)EffectsAS.EFFECT_BLEED, 120, amplifier * 2));
                    EntityUtils.applyPotionEffectAtHalf(entity, new MobEffectInstance(Effects.field_76437_t, 120, amplifier * 3));
                    EntityUtils.applyPotionEffectAtHalf(entity, new MobEffectInstance(Effects.field_76438_s, 120, amplifier * 4));
                    EntityUtils.applyPotionEffectAtHalf(entity, new MobEffectInstance(Effects.field_76419_f, 120, amplifier * 2));
                }
                else {
                    EntityUtils.applyPotionEffectAtHalf(entity, new MobEffectInstance(Effects.field_76428_l, 120, amplifier));
                }
                if (!(entity instanceof Player)) {
                    continue;
                }
                this.markPlayerAffected((Player)entity);
            }
        }
        return changed;
    }
    
    @Nullable
    @Override
    public CropHelper.GrowablePlant recreateElement(final CompoundTag tag, final BlockPos pos) {
        return CropHelper.fromNBT(tag, pos);
    }
    
    @Nullable
    @Override
    public CropHelper.GrowablePlant createElement(final World world, final BlockPos pos) {
        return CropHelper.wrapPlant((IWorld)world, pos);
    }
    
    @Override
    public Config getConfig() {
        return CEffectAevitas.CONFIG;
    }
    
    @Override
    public PlayerAffectionFlags.AffectionFlag getPlayerAffectionFlag() {
        return CEffectAevitas.FLAG;
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playParticles(final PktPlayEffect event) {
        final Vector3 at = ByteBufUtils.readVector(event.getExtraData());
        for (int i = 0; i < 8; ++i) {
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at.clone().add(CEffectAevitas.rand.nextFloat(), 0.2, CEffectAevitas.rand.nextFloat())).setMotion(new Vector3(0.0, 0.005 + CEffectAevitas.rand.nextFloat() * 0.01, 0.0)).setScaleMultiplier(0.1f + CEffectAevitas.rand.nextFloat() * 0.1f).color(VFXColorFunction.constant(Color.GREEN));
        }
    }
    
    static {
        CEffectAevitas.FLAG = ConstellationEffect.makeAffectionFlag("aevitas");
        CEffectAevitas.CONFIG = new AevitasConfig();
    }
    
    private static class AevitasConfig extends CountConfig
    {
        private final int defaultPotionAmplifier = 1;
        public ForgeConfigSpec.IntValue potionAmplifier;
        
        public AevitasConfig() {
            super("aevitas", 10.0, 4.0, 200);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Set the amplifier for the potion effects this ritual provides.").translation(this.translationKey("potionAmplifier"));
            final String s = "potionAmplifier";
            this.getClass();
            this.potionAmplifier = translation.defineInRange(s, 1, 0, 10);
        }
    }
}
