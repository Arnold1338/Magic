package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraft.world.item.ItemStack;
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

public class KeyNoArmor extends KeyPerk
{
    private static final float defaultDamageTakenMultiplier = 0.7f;
    public static final Config CONFIG;
    
    public KeyNoArmor(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener((Consumer)this::onLivingHurt);
    }
    
    private void onLivingHurt(final LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof Player)) {
            return;
        }
        final Player player = (Player)event.getEntityLiving();
        final LogicalSide side = this.getSide((Entity)player);
        final PlayerProgress prog = ResearchHelper.getProgress(player, side);
        if (prog.getPerkData().hasPerkEffect(this)) {
            int eq = 0;
            for (final ItemStack stack : player.func_184193_aE()) {
                if (!stack.isEmpty()) {
                    ++eq;
                }
            }
            if (eq < 2) {
                final float multiplier = ((Double)KeyNoArmor.CONFIG.damageTakenMultiplier.get()).floatValue();
                final float effMulti = PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT);
                event.setAmount(event.getAmount() * (multiplier * (1.0f / effMulti)));
            }
        }
    }
    
    static {
        CONFIG = new Config("key.no_armor");
    }
    
    public static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.DoubleValue damageTakenMultiplier;
        
        private Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.damageTakenMultiplier = cfgBuilder.comment("The multiplier that is applied to damage the player receives. The lower the more damage is negated.").translation(this.translationKey("damageTakenMultiplier")).defineInRange("damageTakenMultiplier", 0.699999988079071, 0.10000000149011612, 1.0);
        }
    }
}
