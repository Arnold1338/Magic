package hellfirepvp.astralsorcery.mixin;

import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import net.minecraft.world.level.entity.LivingEntity;
import net.minecraft.world.level.entity.ai.attributes.AttributeMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import javax.annotation.Nullable;

@Mixin(AttributeMap.class)
public class MixinAttributeModifierManager implements AttributeEvent.EntityModifierManager {
    @Unique public LivingEntity astralSorceryEntityReference;

    @Nullable @Override
    public LivingEntity getLivingEntity() { return this.astralSorceryEntityReference; }

    @Override
    public void setLivingEntity(LivingEntity entity) { this.astralSorceryEntityReference = entity; }
}
