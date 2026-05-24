package hellfirepvp.astralsorcery.common.crafting.recipe.interaction;

import java.util.HashMap;
import hellfirepvp.astralsorcery.AstralSorcery;
import javax.annotation.Nullable;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

public class InteractionResultRegistry
{
    public static final ResourceLocation ID_DROP_ITEM;
    public static final ResourceLocation ID_SPAWN_ENTITY;
    private static final Map<ResourceLocation, Supplier<InteractionResult>> interactionRegistry;
    
    private InteractionResultRegistry() {
    }
    
    public static void register(final ResourceLocation key, final Supplier<InteractionResult> supplier) {
        InteractionResultRegistry.interactionRegistry.put(key, supplier);
    }
    
    public static Collection<ResourceLocation> getKeys() {
        return InteractionResultRegistry.interactionRegistry.keySet();
    }
    
    public static Collection<String> getKeysAsStrings() {
        return InteractionResultRegistry.interactionRegistry.keySet().stream().map((Function<? super Object, ?>)ResourceLocation::toString).collect((Collector<? super Object, ?, Collection<String>>)Collectors.toList());
    }
    
    @Nullable
    public static InteractionResult create(final ResourceLocation key) {
        if (!InteractionResultRegistry.interactionRegistry.containsKey(key)) {
            return null;
        }
        return InteractionResultRegistry.interactionRegistry.get(key).get();
    }
    
    static {
        ID_DROP_ITEM = AstralSorcery.key("drop_item");
        ID_SPAWN_ENTITY = AstralSorcery.key("spawn_entity");
        interactionRegistry = new HashMap<ResourceLocation, Supplier<InteractionResult>>();
        register(InteractionResultRegistry.ID_DROP_ITEM, (Supplier<InteractionResult>)ResultDropItem::new);
        register(InteractionResultRegistry.ID_SPAWN_ENTITY, (Supplier<InteractionResult>)ResultSpawnEntity::new);
    }
}
