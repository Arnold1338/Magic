package hellfirepvp.astralsorcery.common.perk.node.root;

import net.minecraft.util.CombatTracker;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.damagesource.DamageSource;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
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
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.DiminishingMultiplier;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.node.RootPerk;

public class RootDiscidia extends RootPerk
{
    public static final Config CONFIG;
    
    public RootDiscidia(final ResourceLocation name, final float x, final float y) {
        super(name, RootDiscidia.CONFIG, ConstellationsAS.discidia, x, y);
    }
    
    @Nonnull
    @Override
    protected DiminishingMultiplier createMultiplier() {
        return new DiminishingMultiplier(6000L, 0.075f, 0.025f, 0.15f);
    }
    
    @Override
    protected void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener(EventPriority.LOWEST, (Consumer)this::onDamage);
    }
    
    private void onDamage(final LivingDamageEvent event) {
        final DamageSource ds = event.getSource();
        Player player = null;
        if (ds.getDirectEntity() != null && ds.getDirectEntity() instanceof Player) {
            player = (Player)ds.getDirectEntity();
        }
        if (player == null && ds.getEnchantments( != null && ds.getDirectEntity() instanceof Player) {
            player = (Player)ds.getDirectEntity();
        }
        if (player == null) {
            return;
        }
        final LogicalSide side = this.getSide((Entity)player);
        if (!side.isServer()) {
            return;
        }
        final PlayerProgress prog = ResearchHelper.getProgress(player, side);
        if (!prog.getPerkData().hasPerkEffect(this)) {
            return;
        }
        float mul = 4.0f;
        final CombatTracker combat = event.getEntityLiving().func_110142_aN();
        if (combat.field_94552_d && combat.func_180134_f() > 2400) {
            mul = 0.01f;
        }
        float expGain = Math.min(event.getAmount() * mul, 100.0f);
        expGain *= (float)this.getExpMultiplier();
        expGain *= this.getDiminishingReturns(player);
        expGain *= PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT);
        expGain *= PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EXP);
        expGain = AttributeEvent.postProcessModded(player, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EXP, expGain);
        ResearchManager.modifyExp(player, expGain);
    }
    
    static {
        CONFIG = new Config("root.discidia");
    }
}
