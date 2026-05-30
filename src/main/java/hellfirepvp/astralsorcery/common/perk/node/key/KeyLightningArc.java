package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.PacketDistributor;
import java.util.List;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.util.DamageUtil;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.data.config.registry.TechnicalEntityRegistry;
import java.util.Collection;
import java.util.function.Predicate;
import hellfirepvp.astralsorcery.common.util.entity.EntityUtils;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.LivingEntity;
import com.google.common.collect.Lists;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.damagesource.DamageSource;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.event.EventFlags;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyLightningArc extends KeyPerk
{
    private static final float defaultArcChance = 0.6f;
    private static final float defaultArcPercent = 0.6f;
    private static final float defaultArcDistance = 7.0f;
    private static final int defaultArcTicks = 3;
    private static final int defaultChargeCost = 60;
    private static final int arcChains = 3;
    public static final Config CONFIG;
    
    public KeyLightningArc(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener(EventPriority.LOWEST, (Consumer)this::onAttack);
    }
    
    private void onAttack(final LivingHurtEvent event) {
        if (EventFlags.LIGHTNING_ARC.isSet()) {

        }
        final DamageSource source = event.getSource();
        if (source.getDirectEntity() != null && source.getDirectEntity() instanceof Player) {
            final Player player = (Player)source.getDirectEntity();
            final LogicalSide side = this.getSide((Entity)player);
            final PlayerProgress prog = ResearchHelper.getProgress(player, side);
            if (side.isServer() && prog.getPerkData().hasPerkEffect(this) && prog.doPerkAbilities()) {
                final float chance = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, ((Double)KeyLightningArc.CONFIG.arcChance.get()).floatValue());
                if (KeyLightningArc.rand.nextFloat() < chance && AlignmentChargeHandler.INSTANCE.drainCharge(player, side, (float)(int)KeyLightningArc.CONFIG.chargeCost.get(), false)) {
                    float dmg = event.getAmount();
                    dmg *= (float)(double)KeyLightningArc.CONFIG.arcPercent.get();
                    new RepetitiveArcEffect(player.level(), player, (int)KeyLightningArc.CONFIG.arcTicks.get(), event.getEntityLiving().func_145782_y(), dmg, (double)KeyLightningArc.CONFIG.arcDistance.get()).fire();
                }
            }
        }
    }
    
    static {
        CONFIG = new Config("key.lightning_arc");
    }
    
    public static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.DoubleValue arcChance;
        private ForgeConfigSpec.DoubleValue arcPercent;
        private ForgeConfigSpec.DoubleValue arcDistance;
        private ForgeConfigSpec.IntValue arcTicks;
        private ForgeConfigSpec.IntValue chargeCost;
        
        public Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.arcChance = cfgBuilder.comment("Sets the chance to spawn a damage-arc effect when an enemy is hit (value is in percent).").translation(this.translationKey("arcChance")).defineInRange("arcChance", 0.6000000238418579, 0.1, 1.0);
            this.arcPercent = cfgBuilder.comment("Defines the damage-multiplier which gets added to the damage dealt initially.").translation(this.translationKey("arcPercent")).defineInRange("arcPercent", 0.6000000238418579, 0.1, 8.0);
            this.arcDistance = cfgBuilder.comment("Defines the distance for how far a single arc can jump/search for nearby entities.").translation(this.translationKey("arcDistance")).defineInRange("arcDistance", 7.0, 0.2, 16.0);
            this.arcTicks = cfgBuilder.defineInRange("arcTicks", 3, 1, 32);
            this.chargeCost = cfgBuilder.comment("Defines the amount of starlight charge consumed per spawned lighning arc.").translation(this.translationKey("chargeCost")).defineInRange("chargeCost", 60, 1, 500);
        }
    }
    
    static class RepetitiveArcEffect
    {
        private final Level world;
        private final Player player;
        private final int entityStartId;
        private final float damage;
        private final double distance;
        private int count;
        
        public RepetitiveArcEffect(final Level world, final Player player, final int count, final int entityStartId, final float damage, final double distance) {
            this.world = world;
            this.player = player;
            this.count = count;
            this.entityStartId = entityStartId;
            this.damage = damage;
            this.distance = distance;
        }
        
        void fire() {
            if (!this.player.isAlive()) {

            }
            int chainTimes = Math.round(PerkAttributeHelper.getOrCreateMap(this.player, LogicalSide.SERVER).modifyValue(this.player, ResearchHelper.getProgress(this.player, LogicalSide.SERVER), PerkAttributeTypesAS.ATTR_TYPE_ARC_CHAINS, 3.0f));
            final List<LivingEntity> visitedEntities = Lists.newArrayList();
            final Entity start = this.world.getEntityById(this.entityStartId);
            if (start instanceof LivingEntity && start.isAlive()) {
                final AABB box = new AABB(-this.distance, -this.distance, -this.distance, this.distance, this.distance, this.distance);
                LivingEntity last = null;
                LivingEntity entity = (LivingEntity)start;
                while (entity != null && entity.isAlive() && chainTimes > 0) {
                    visitedEntities.add(entity);
                    --chainTimes;
                    if (last != null) {
                        final Vector3 from = Vector3.atEntityCenter((Entity)entity);
                        final Vector3 to = Vector3.atEntityCenter((Entity)last);
                        final PacketDistributor.TargetPoint target = PacketChannel.pointFromPos(this.world, (Vec3i)entity.func_233580_cy_(), 16.0);
                        PacketChannel.CHANNEL.sendToAllAround(new PktPlayEffect(PktPlayEffect.Type.LIGHTNING).addData(buf -> {
                            ByteBufUtils.writeVector(buf, from);
                            ByteBufUtils.writeVector(buf, to);
                            buf.writeInt(ColorsAS.EFFECT_LIGHTNING.getRGB());

                        }), target);
                        PacketChannel.CHANNEL.sendToAllAround(new PktPlayEffect(PktPlayEffect.Type.LIGHTNING).addData(buf -> {
                            ByteBufUtils.writeVector(buf, to);
                            ByteBufUtils.writeVector(buf, from);
                            buf.writeInt(ColorsAS.EFFECT_LIGHTNING.getRGB());

                        }), target);
                    }
                    final List<LivingEntity> entities = entity.level().func_175647_a((Class)LivingEntity.class, box.func_191194_a(entity.position()), (Predicate)EntityUtils.selectEntities(LivingEntity.class));
                    entities.remove(entity);
                    if (last != null) {
                        entities.remove(last);
                    }
                    entities.remove(this.player);
                    entities.removeAll(visitedEntities);
                    entities.removeIf(e -> !TechnicalEntityRegistry.INSTANCE.canAffect((Entity)e));
                    entities.removeIf(e -> !MiscUtils.canPlayerAttackServer((LivingEntity)this.player, e));
                    if (!entities.isEmpty()) {
                        final LivingEntity tmpEntity = entity;
                        final LivingEntity closest = EntityUtils.selectClosest((Collection<LivingEntity>)entities, e -> Double.valueOf(e.func_70032_d((Entity)tmpEntity)));
                        if (closest != null && closest.isAlive()) {
                            last = entity;
                            entity = closest;
                        }
                        else {
                            entity = null;
                        }
                    }
                    else {
                        entity = null;
                    }
                }
                if (visitedEntities.size() > 1) {
                    visitedEntities.forEach(e -> EventFlags.LIGHTNING_ARC.executeWithFlag(() -> DamageUtil.attackEntityFrom((Entity)e, CommonProxy.DAMAGE_SOURCE_STELLAR, this.damage, (Entity)this.player)));
                }
            }
            if (this.count > 0) {
                --this.count;
                AstralSorcery.getProxy().scheduleDelayed(this::fire, 12);
            }
        }
    }
}
