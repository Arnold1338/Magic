package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyCullingAttack extends KeyPerk
{
    private static final float defaultCullHealth = 0.15f;
    private static final int defaultChargeCost = 250;
    public static final Config CONFIG;
    
    public KeyCullingAttack(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener(EventPriority.LOW, (Consumer)this::onDamage);
    }
    
    private void onDamage(final LivingDamageEvent event) {
        final DamageSource source = event.getSource();
        if (source.getDirectEntity() != null && source.getDirectEntity() instanceof Player) {
            final Player player = (Player)source.getDirectEntity();
            final LogicalSide side = this.getSide((Entity)player);
            final PlayerProgress prog = ResearchHelper.getProgress(player, side);
            if (side.isServer() && prog.getPerkData().hasPerkEffect(this)) {
                final LivingEntity attacked = event.getEntityLiving();
                final float actCull = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, ((Double)KeyCullingAttack.CONFIG.cullHealth.get()).floatValue());
                final float lifePerc = attacked.getMaxHealth() / attacked.func_110138_aP();
                if (lifePerc < actCull && AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)KeyCullingAttack.CONFIG.chargeCost.get(), false)) {
                    attacked.func_70606_j(0.0f);
                    attacked.func_184212_Q().func_187227_b(LivingEntity.field_184632_c, (Object)0.0f);
                }
            }
        }
    }
    
    static {
        CONFIG = new Config("key.culling");
    }
    
    public static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.DoubleValue cullHealth;
        private ForgeConfigSpec.IntValue chargeCost;
        
        private Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.cullHealth = cfgBuilder.comment("Defines the percentage at how low the entities' health as to be to then cull the entity.").translation(this.translationKey("cullHealth")).defineInRange("cullHealth", 0.15000000596046448, 0.05000000074505806, 0.5);
            this.chargeCost = cfgBuilder.comment("Defines the amount of starlight charge consumed per culling attempt.").translation(this.translationKey("chargeCost")).defineInRange("chargeCost", 250, 1, 500);
        }
    }
}
