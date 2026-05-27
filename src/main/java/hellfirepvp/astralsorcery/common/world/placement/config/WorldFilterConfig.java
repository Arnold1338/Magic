package hellfirepvp.astralsorcery.common.world.placement.config;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.IServerWorld;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.List;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

public class WorldFilterConfig implements PlacementModifier
{
    public static final Codec<WorldFilterConfig> CODEC;
    private final Supplier<Boolean> ignoreFilter;
    private final Supplier<List<ResourceKey<Level>>> worldFilter;
    
    public WorldFilterConfig(final boolean ignoreFilter, final List<ResourceKey<Level>> worldFilter) {
        this(() -> ignoreFilter, () -> worldFilter);
    }
    
    public WorldFilterConfig(final Supplier<Boolean> ignoreFilter, final Supplier<List<ResourceKey<Level>>> worldFilter) {
        this.ignoreFilter = ignoreFilter;
        this.worldFilter = worldFilter;
    }
    
    public boolean generatesIn(final IServerWorld world) {
        return this.ignoreFilter.get() || this.worldFilter.get().contains(world.func_201672_e().dimension());
    }
    
    static {
        CODEC = RecordCodecBuilder.create(codecInstance -> codecInstance.group((App)Codec.BOOL.fieldOf("ignoreFilter").forGetter(config -> config.ignoreFilter.get()), (App)World.field_234917_f_.listOf().fieldOf("worldFilter").forGetter(config -> config.worldFilter.get())).apply((Applicative)codecInstance, (BiFunction)WorldFilterConfig::new));
    }
}
