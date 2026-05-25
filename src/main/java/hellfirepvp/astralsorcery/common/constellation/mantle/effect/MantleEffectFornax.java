package hellfirepvp.astralsorcery.common.constellation.mantle.effect;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.item.armor.ItemMantle;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;

public class MantleEffectFornax extends MantleEffect
{
    public static FornaxConfig CONFIG;
    
    public MantleEffectFornax() {
        super(ConstellationsAS.fornax);
    }
    
    @Override
    protected void attachEventListeners(final IEventBus bus) {
        super.attachEventListeners(bus);
        bus.addListener((Consumer)this::onHurt);
    }
    
    private void onHurt(final LivingHurtEvent event) {
        final Level world = event.getEntityLiving().level();
        if (world.level()) {
            return;
        }
        final LivingEntity attacked = event.getEntityLiving();
        final Entity attacker = event.getSource().getDirectEntity();
        if (attacker instanceof LivingEntity) {
            if (attacked instanceof ServerPlayer && MiscUtils.isPlayerFakeMP((ServerPlayer)attacked)) {
                return;
            }
            if (attacker.func_70027_ad() && ItemMantle.getEffect((LivingEntity)attacker, ConstellationsAS.fornax) != null) {
                event.setAmount((float)(event.getAmount() * (double)MantleEffectFornax.CONFIG.damageIncreaseInFire.get()));
            }
        }
        if (event.getSource().func_76347_k() && ItemMantle.getEffect(attacked, ConstellationsAS.fornax) != null) {
            if ((double)MantleEffectFornax.CONFIG.healPercentFromFireDamage.get() > 0.0) {
                attacked.heal((float)(event.getAmount() * (double)MantleEffectFornax.CONFIG.healPercentFromFireDamage.get()));
            }
            event.setAmount((float)(event.getAmount() * (double)MantleEffectFornax.CONFIG.damageReductionInFire.get()));
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void tickClient(final Player player) {
        super.tickClient(player);
        if (player.func_70027_ad()) {
            this.playCapeSparkles(player, 0.75f);
        }
        else {
            this.playCapeSparkles(player, 0.25f);
        }
    }
    
    @Override
    protected boolean usesTickMethods() {
        return true;
    }
    
    @Override
    public Config getConfig() {
        return MantleEffectFornax.CONFIG;
    }
    
    static {
        MantleEffectFornax.CONFIG = new FornaxConfig();
    }
    
    public static class FornaxConfig extends Config
    {
        private final double defaultDamageReductionInFire = 0.4000000059604645;
        private final double defaultDamageIncreaseInFire = 1.600000023841858;
        private final double defaultHealPercentFromFireDamage = 0.6000000238418579;
        public ForgeConfigSpec.DoubleValue damageReductionInFire;
        public ForgeConfigSpec.DoubleValue damageIncreaseInFire;
        public ForgeConfigSpec.DoubleValue healPercentFromFireDamage;
        
        public FornaxConfig() {
            super("fornax");
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Sets the multiplier for how much damage you take from fire damage while wearing a fornax mantle.").translation(this.translationKey("damageReductionInFire"));
            final String s = "damageReductionInFire";
            this.getClass();
            this.damageReductionInFire = translation.defineInRange(s, 0.4000000059604645, 0.0, 1.0);
            final ForgeConfigSpec.Builder translation2 = cfgBuilder.comment("Sets the multiplier for how much more damage the player deals when ignited while wearing a fornax mantle.").translation(this.translationKey("damageIncreaseInFire"));
            final String s2 = "damageIncreaseInFire";
            this.getClass();
            this.damageIncreaseInFire = translation2.defineInRange(s2, 1.600000023841858, 1.0, 3.0);
            final ForgeConfigSpec.Builder translation3 = cfgBuilder.comment("Sets the multiplier for how much healing the player receives from the original damage when being hit by fire damage.").translation(this.translationKey("healPercentFromFireDamage"));
            final String s3 = "healPercentFromFireDamage";
            this.getClass();
            this.healPercentFromFireDamage = translation3.defineInRange(s3, 0.6000000238418579, 0.0, 3.0);
        }
    }
}
