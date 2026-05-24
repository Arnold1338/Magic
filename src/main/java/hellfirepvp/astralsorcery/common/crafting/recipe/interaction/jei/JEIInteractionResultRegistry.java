package hellfirepvp.astralsorcery.common.crafting.recipe.interaction.jei;

import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.InteractionResultRegistry;
import java.util.HashMap;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public class JEIInteractionResultRegistry
{
    private static final Map<ResourceLocation, JEIInteractionResultHandler> interactionRegistry;
    
    public static void register(final ResourceLocation key, final JEIInteractionResultHandler interactionResultHandler) {
        JEIInteractionResultRegistry.interactionRegistry.put(key, interactionResultHandler);
    }
    
    public static Optional<JEIInteractionResultHandler> get(final ResourceLocation key) {
        return Optional.ofNullable(JEIInteractionResultRegistry.interactionRegistry.get(key));
    }
    
    static {
        interactionRegistry = new HashMap<ResourceLocation, JEIInteractionResultHandler>();
        register(InteractionResultRegistry.ID_DROP_ITEM, new JEIHandlerDropItem());
        register(InteractionResultRegistry.ID_SPAWN_ENTITY, new JEIHandlerSpawnEntity());
    }
}
