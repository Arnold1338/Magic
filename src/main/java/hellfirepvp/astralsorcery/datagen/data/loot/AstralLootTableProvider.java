package hellfirepvp.astralsorcery.datagen.data.loot;

import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.ValidationTracker;
import java.util.Map;
import com.google.common.collect.Lists;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.resources.ResourceLocation;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;

public final class AstralLootTableProvider extends LootTableProvider
{
    public AstralLootTableProvider(final DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }
    
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return Lists.newArrayList((Object[])new Pair[] { Pair.of((Object)BlockLootTableProvider::new, (Object)LootContextParamSets.ENTITY), Pair.of((Object)EntityLootTableProvider::new, (Object)LootParameterSets.field_216263_d), Pair.of((Object)ChestLootTableProvider::new, (Object)LootParameterSets.field_216261_b), Pair.of((Object)GameplayLootTableProvider::new, (Object)LootParameterSets.field_216264_e) });
    }
    
    protected void validate(final Map<ResourceLocation, LootTable> tables, final ValidationTracker tracker) {
        tables.forEach((key, table) -> LootTableManager.func_227508_a_(tracker, key, table));
    }
}
