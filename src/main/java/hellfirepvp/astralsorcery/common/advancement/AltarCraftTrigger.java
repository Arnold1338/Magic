package hellfirepvp.astralsorcery.common.advancement;

import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.loot.ConditionArrayParser;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.advancement.instance.AltarRecipeInstance;

public class AltarCraftTrigger extends ListenerCriterionTrigger<AltarRecipeInstance>
{
    public static final ResourceLocation ID;
    
    public AltarCraftTrigger() {
        super(AltarCraftTrigger.ID);
    }
    
    public AltarRecipeInstance deserialize(final JsonObject object, final ConditionArrayParser conditions) {
        return AltarRecipeInstance.deserialize(this.func_192163_a(), object);
    }
    
    public void trigger(final ServerPlayer player, final SimpleAltarRecipe recipe, final ItemStack output) {
        final Listeners<AltarRecipeInstance> listeners = (Listeners<AltarRecipeInstance>)this.listeners.get(player.func_192039_O());
        if (listeners != null) {
            listeners.trigger(i -> i.test(recipe, output));
        }
    }
    
    static {
        ID = AstralSorcery.key("altar_craft");
    }
}
