package hellfirepvp.astralsorcery.common.perk.type;

import net.minecraft.world.damagesource.DamageSource;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.util.DamageUtil;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.perk.modifier.AttributeModifierThorns;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;

public class AttributeTypeThorns extends PerkAttributeType
{
    public AttributeTypeThorns() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_INC_THORNS);
    }
    
    @Override
    protected void attachListeners(final IEventBus eventBus) {
        super.attachListeners(eventBus);
        eventBus.addListener((Consumer)this::onThronsReflect);
    }
    
    @Nonnull
    @Override
    public PerkAttributeModifier createModifier(final float modifier, final ModifierType mode) {
        return new AttributeModifierThorns(this, mode, modifier);
    }
    
    private void onThronsReflect(final LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof Player)) {
            return;
        }
        final Player player = (Player)event.getEntityLiving();
        final LogicalSide side = this.getSide((Entity)player);
        if (!this.hasTypeApplied(player, side)) {
            return;
        }
        final PlayerProgress prog = ResearchHelper.getProgress(player, side);
        float reflectAmount = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, prog, this, 0.0f);
        reflectAmount = AttributeEvent.postProcessModded(player, this, reflectAmount);
        reflectAmount /= 100.0f;
        if (reflectAmount <= 0.0f) {
            return;
        }
        reflectAmount = MathHelper.func_76131_a(reflectAmount, 0.0f, 1.0f);
        final DamageSource source = event.getSource();
        LivingEntity reflectTarget = null;
        if (source.func_76364_f() != null && source.func_76364_f() instanceof LivingEntity && source.func_76364_f().isAlive()) {
            reflectTarget = (LivingEntity)source.func_76364_f();
        }
        if (reflectTarget == null && AttributeEvent.postProcessModded(player, this, PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_THORNS_RANGED)) > 1.0f && source.func_76346_g() != null && source.func_76346_g() instanceof LivingEntity && source.func_76346_g().isAlive()) {
            reflectTarget = (LivingEntity)source.func_76346_g();
        }
        if (reflectTarget != null) {
            final float dmgReflected = event.getAmount() * reflectAmount;
            if (dmgReflected > 0.0f && !event.getEntityLiving().equals((Object)reflectTarget) && MiscUtils.canPlayerAttackServer(event.getEntityLiving(), reflectTarget)) {
                DamageUtil.attackEntityFrom((Entity)reflectTarget, CommonProxy.DAMAGE_SOURCE_REFLECT, dmgReflected, (Entity)player);
            }
        }
    }
}
