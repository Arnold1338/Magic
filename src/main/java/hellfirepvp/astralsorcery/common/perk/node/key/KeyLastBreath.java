package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import net.minecraftforge.event.entity.player.PlayerEvent;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.damagesource.DamageSource;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyLastBreath extends KeyPerk
{
    private static final float defaultDigSpeedMultiplier = 1.5f;
    private static final float defaultDamageMultiplier = 3.0f;
    public static final Config CONFIG;
    
    public KeyLastBreath(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener((Consumer)this::onAttack);
        bus.addListener((Consumer)this::onBreakSpeed);
    }
    
    private void onAttack(final LivingHurtEvent event) {
        final DamageSource source = event.getSource();
        if (source.func_76346_g() != null && source.func_76346_g() instanceof Player) {
            final Player player = (Player)source.func_76346_g();
            final LogicalSide side = this.getSide((Entity)player);
            final PlayerProgress prog = ResearchHelper.getProgress(player, side);
            if (prog.getPerkData().hasPerkEffect(this)) {
                final float actIncrease = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, ((Double)KeyLastBreath.CONFIG.damageMultiplier.get()).floatValue());
                final float healthPerc = 1.0f - player.func_110143_aJ() / player.func_110138_aP();
                event.setAmount(event.getAmount() * (1.0f + healthPerc * actIncrease));
            }
        }
    }
    
    private void onBreakSpeed(final PlayerEvent.BreakSpeed event) {
        final Player player = event.getPlayer();
        final LogicalSide side = this.getSide((Entity)player);
        final PlayerProgress prog = ResearchHelper.getProgress(player, side);
        if (prog.getPerkData().hasPerkEffect(this)) {
            final float actIncrease = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, ((Double)KeyLastBreath.CONFIG.digSpeedMultiplier.get()).floatValue());
            final float healthPerc = 1.0f - player.func_110143_aJ() / player.func_110138_aP();
            event.setNewSpeed(event.getNewSpeed() * (1.0f + healthPerc * actIncrease));
        }
    }
    
    static {
        CONFIG = new Config("key.last_breath");
    }
    
    public static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.DoubleValue digSpeedMultiplier;
        private ForgeConfigSpec.DoubleValue damageMultiplier;
        
        private Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.digSpeedMultiplier = cfgBuilder.comment("Defines the dig speed multiplier you get additionally to your normal dig speed when being low on health (25% health = 75% of this additional multiplier)").translation(this.translationKey("digSpeedMultiplier")).defineInRange("digSpeedMultiplier", 1.5, 0.1, 10.0);
            this.damageMultiplier = cfgBuilder.comment("Defines the damage multiplier you get additionally to your normal damage when being low on health (25% health = 75% of this additional multiplier)").translation(this.translationKey("damageMultiplier")).defineInRange("damageMultiplier", 3.0, 0.1, 10.0);
        }
    }
}
