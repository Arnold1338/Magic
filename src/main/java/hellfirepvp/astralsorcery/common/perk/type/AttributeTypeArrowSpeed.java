package hellfirepvp.astralsorcery.common.perk.type;

import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.entity.projectile.ArrowEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;

public class AttributeTypeArrowSpeed extends PerkAttributeType
{
    public AttributeTypeArrowSpeed() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_PROJ_SPEED, true);
    }
    
    @Override
    protected void attachListeners(final IEventBus eventBus) {
        super.attachListeners(eventBus);
        eventBus.addListener((Consumer)this::onArrowFire);
    }
    
    private void onArrowFire(final EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ArrowEntity) {
            final ArrowEntity arrow = (ArrowEntity)event.getEntity();
            final Entity shooter = arrow.func_234616_v_();
            if (shooter instanceof Player) {
                final Player player = (Player)shooter;
                final LogicalSide side = this.getSide((Entity)player);
                if (!this.hasTypeApplied(player, side)) {
                    return;
                }
                Vector3 motion = new Vector3(arrow.func_213322_ci());
                float mul = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, ResearchHelper.getProgress(player, side), this, 1.0f);
                mul = AttributeEvent.postProcessModded(player, this, mul);
                motion = MiscUtils.limitVelocityToMinecraftLimit(motion.multiply(mul));
                arrow.func_213317_d(motion.toVector3d());
            }
        }
    }
}
