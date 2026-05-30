package hellfirepvp.astralsorcery.common.constellation.effect.aoe;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;
import java.util.Iterator;
import net.minecraft.world.level.LevelReader;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.util.entity.EntityUtils;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.DamageUtil;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.level.effect.MobEffect;
import net.minecraft.world.level.effect.MobEffectInstance;
import hellfirepvp.astralsorcery.common.lib.EffectsAS;
import java.util.List;
import java.util.Collections;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.auxiliary.AnimalHelper;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import hellfirepvp.astralsorcery.common.event.PlayerAffectionFlags;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.constellation.effect.base.ConstellationEffectEntityCollect;

public class CEffectBootes extends ConstellationEffectEntityCollect<LivingEntity>
{
    public static PlayerAffectionFlags.AffectionFlag FLAG;
    public static BootesConfig CONFIG;
    
    public CEffectBootes(@Nonnull final ILocatable origin) {
        super(origin, ConstellationsAS.bootes, LivingEntity.class, entity -> AnimalHelper.getHandler(entity) != null);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void playClientEffect(final Level world, final BlockPos pos, final TileRitualPedestal pedestal, final float alphaMultiplier, final boolean extended) {
        if (CEffectBootes.rand.nextInt(3) == 0) {
            final ConstellationEffectProperties prop = this.createProperties(pedestal.getMirrorCount());
            final Vector3 playAt = new Vector3((Vec3i)pos).add(0.5, 0.5, 0.5).add(CEffectBootes.rand.nextFloat() * (prop.getSize() / 2.0) * (CEffectBootes.rand.nextBoolean() ? 1 : -1), CEffectBootes.rand.nextFloat() * (prop.getSize() / 4.0), CEffectBootes.rand.nextFloat() * (prop.getSize() / 2.0) * (CEffectBootes.rand.nextBoolean() ? 1 : -1));
            final Vector3 motion = Vector3.random().multiply(0.015);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(playAt).setMotion(motion).color(VFXColorFunction.constant(ColorsAS.CONSTELLATION_BOOTES)).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.5f).setMaxAge(30 + CEffectBootes.rand.nextInt(20));
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(playAt).setMotion(motion.clone().negate()).color(VFXColorFunction.constant(ColorsAS.CONSTELLATION_BOOTES)).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.5f).setMaxAge(30 + CEffectBootes.rand.nextInt(20));
        }
    }
    
    @Override
    public boolean playEffect(final Level world, final BlockPos pos, final ConstellationEffectProperties properties, @Nullable final IMinorConstellation trait) {
        boolean didEffect = false;
        final List<LivingEntity> entities = this.collectEntities(world, pos, properties);
        Collections.shuffle(entities);
        entities.subList(0, Math.min(25, entities.size()));
        for (final LivingEntity entity : entities) {
            final AnimalHelper.HerdableAnimal animal = AnimalHelper.getHandler(entity);
            if (animal == null) {

            }
            if (properties.isCorrupted()) {
                entity.field_70172_ad = 0;
                entity.func_195064_c(new MobEffectInstance((MobEffect)EffectsAS.EFFECT_DROP_MODIFIER, 1000, 5));
                if (!DamageUtil.attackEntityFrom((Entity)entity, CommonProxy.DAMAGE_SOURCE_STELLAR, 5000.0f)) {

                }
                didEffect = true;
            }
            else {
                if (CEffectBootes.rand.nextFloat() >= (double)CEffectBootes.CONFIG.herdingChance.get()) {

                }
                didEffect = MiscUtils.executeWithChunk((IWorldReader)world, entity.func_233580_cy_(), didEffect, didEffectFlag -> {
                    final List<ItemStack> rawDrops = EntityUtils.generateLoot(entity, CEffectBootes.rand, CommonProxy.DAMAGE_SOURCE_STELLAR, null);
                    final List<ItemStack> drops = new ArrayList<ItemStack>();
                    ItemStack drop = null;
                    rawDrops.forEach(drop -> {
                        for (int i = 0; i < drop.getCount(); ++i) {
                            drops.add(ItemUtils.copyStackWithSize(drop, 1));
                        }

                    });
                    drops.iterator();
                    final Iterator iterator2;
                    while (iterator2.hasNext()) {
                        drop = iterator2.next();
                        if (CEffectBootes.rand.nextFloat() < (double)CEffectBootes.CONFIG.herdingLootChance.get() && ItemUtils.dropItemNaturally(world, entity.getX(), entity.getY(), entity.getZ(), drop) != null) {
                            didEffectFlag = true;
                        }
                    }
                    return didEffectFlag;
                }, false);
                this.sendConstellationPing(world, Vector3.atEntityCorner((Entity)entity));
            }
        }
        return didEffect;
    }
    
    @Override
    public Config getConfig() {
        return CEffectBootes.CONFIG;
    }
    
    @Override
    public PlayerAffectionFlags.AffectionFlag getPlayerAffectionFlag() {
        return CEffectBootes.FLAG;
    }
    
    static {
        CEffectBootes.FLAG = ConstellationEffect.makeAffectionFlag("bootes");
        CEffectBootes.CONFIG = new BootesConfig();
    }
    
    private static class BootesConfig extends Config
    {
        private final double defaultHerdingChance = 0.05;
        private final double defaultHerdingLootChance = 0.01;
        public ForgeConfigSpec.DoubleValue herdingChance;
        public ForgeConfigSpec.DoubleValue herdingLootChance;
        
        public BootesConfig() {
            super("bootes", 12.0, 4.0);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Set the chance that an registered animal will be considered for generating loot if it is close to the ritual.").translation(this.translationKey("herdingChance"));
            final String s = "herdingChance";
            this.getClass();
            this.herdingChance = translation.defineInRange(s, 0.05, 0.0, 1.0);
            final ForgeConfigSpec.Builder translation2 = cfgBuilder.comment("Set the chance that a drop that has been found on the entity's loot table is actually dropped.").translation(this.translationKey("herdingLootChance"));
            final String s2 = "herdingLootChance";
            this.getClass();
            this.herdingLootChance = translation2.defineInRange(s2, 0.01, 0.0, 1.0);
        }
    }
}
