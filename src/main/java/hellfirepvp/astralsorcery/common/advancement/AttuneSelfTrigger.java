package hellfirepvp.astralsorcery.common.advancement;

import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.ConditionArrayParser;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.advancement.instance.ConstellationInstance;

public class AttuneSelfTrigger extends ListenerCriterionTrigger<ConstellationInstance>
{
    public static final ResourceLocation ID;
    
    public AttuneSelfTrigger() {
        super(AttuneSelfTrigger.ID);
    }
    
    public ConstellationInstance deserialize(final JsonObject object, final ConditionArrayParser conditions) {
        return ConstellationInstance.deserialize(this.func_192163_a(), object);
    }
    
    public void trigger(final ServerPlayer player, final IMajorConstellation attuned) {
        final Listeners<ConstellationInstance> listeners = (Listeners<ConstellationInstance>)this.listeners.get(player.func_192039_O());
        if (listeners != null) {
            listeners.trigger(i -> i.test(attuned));
        }
    }
    
    static {
        ID = AstralSorcery.key("attune_self");
    }
}
