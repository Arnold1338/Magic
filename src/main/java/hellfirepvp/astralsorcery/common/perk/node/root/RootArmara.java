package hellfirepvp.astralsorcery.common.perk.node.root;

import net.minecraft.util.CombatTracker;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.DiminishingMultiplier;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.node.RootPerk;

public class RootArmara extends RootPerk
{
    public static final Config CONFIG;
    
    public RootArmara(final ResourceLocation name, final float x, final float y) {
        super(name, RootArmara.CONFIG, ConstellationsAS.armara, x, y);
    }
    
    @Nonnull
    @Override
    protected DiminishingMultiplier createMultiplier() {
        return new DiminishingMultiplier(2000L, 0.3f, 0.2f, 0.01f);
    }
    
    @Override
    protected void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener(EventPriority.HIGHEST, (Consumer)this::onHurt);
    }
    
    private void onHurt(final LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof Player)) {
            return;
        }
        final Player player = (Player)event.getEntityLiving();
        final LogicalSide side = this.getSide((Entity)player);
        if (!side.isServer()) {
            return;
        }
        final PlayerProgress prog = ResearchHelper.getProgress(player, side);
        if (!prog.getPerkData().hasPerkEffect(this)) {
            return;
        }
        float mul = 0.5f;
        final CombatTracker combat = player.func_110142_aN();
        if (combat.field_94552_d) {
            if (combat.func_180134_f() <= 4800) {
                mul = 10.0f;
            }
            else {
                mul = 0.05f;
            }
        }
        else if (event.getSource().func_76346_g() instanceof LivingEntity) {
            mul = 3.0f;
        }
        float expGain = Math.min(event.getAmount() * mul, 70.0f);
        expGain *= (float)this.getExpMultiplier();
        expGain *= this.getDiminishingReturns(player);
        expGain *= PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT);
        expGain *= PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EXP);
        expGain = AttributeEvent.postProcessModded(player, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EXP, expGain);
        ResearchManager.modifyExp(player, expGain);
    }
    
    static {
        CONFIG = new Config("root.armara");
    }
}
