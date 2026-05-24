package hellfirepvp.astralsorcery.datagen.data.loot;

import net.minecraft.world.level.level.storage.loot.LootTable;
import net.minecraft.resources.ResourceLocation;
import java.util.function.BiConsumer;
import net.minecraft.data.loot.GiftLootTables;

public class GameplayLootTableProvider extends GiftLootTables
{
    public void accept(final BiConsumer<ResourceLocation, LootTable.Builder> registrar) {
    }
}
