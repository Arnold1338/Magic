package hellfirepvp.astralsorcery.datagen.data.loot;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import hellfirepvp.astralsorcery.common.base.Mods;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.data.loot.EntityLootTables;

public class EntityLootTableProvider extends EntityLootTables
{
    protected void addTables() {
    }
    
    protected Iterable<EntityType<?>> getKnownEntities() {
        return ForgeRegistries.ENTITIES.getValues().stream().filter((Predicate<? super Object>)Mods.ASTRAL_SORCERY::owns).collect((Collector<? super Object, ?, Iterable<EntityType<?>>>)Collectors.toList());
    }
}
