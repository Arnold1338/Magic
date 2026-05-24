package hellfirepvp.astralsorcery.common.advancement;

import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.ConditionArrayParser;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.advancement.instance.PerkLevelInstance;

public class PerkLevelTrigger extends ListenerCriterionTrigger<PerkLevelInstance>
{
    public static final ResourceLocation ID;
    
    public PerkLevelTrigger() {
        super(PerkLevelTrigger.ID);
    }
    
    public PerkLevelInstance deserialize(final JsonObject object, final ConditionArrayParser conditions) {
        return PerkLevelInstance.deserialize(this.func_192163_a(), object);
    }
    
    public void trigger(final ServerPlayer player) {
        final Listeners<PerkLevelInstance> listeners = (Listeners<PerkLevelInstance>)this.listeners.get(player.func_192039_O());
        if (listeners != null) {
            listeners.trigger(i -> i.test(player));
        }
    }
    
    static {
        ID = AstralSorcery.key("perk_level");
    }
}
