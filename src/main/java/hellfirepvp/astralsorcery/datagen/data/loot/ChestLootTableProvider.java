package hellfirepvp.astralsorcery.datagen.data.loot;

import net.minecraft.world.level.item.Items;
import net.minecraft.world.level.storage.loot.functions.ILootFunction;
import net.minecraft.world.level.storage.loot.functions.SetCount;
import net.minecraft.world.level.storage.loot.LootEntry;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.world.level.storage.loot.ItemLootEntry;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.world.level.storage.loot.IRandomRange;
import net.minecraft.world.level.storage.loot.RandomValueRange;
import net.minecraft.world.level.storage.loot.LootPool;
import hellfirepvp.astralsorcery.common.lib.LootAS;
import net.minecraft.world.level.level.storage.loot.LootTable;
import net.minecraft.resources.ResourceLocation;
import java.util.function.BiConsumer;
import net.minecraft.data.loot.ChestLootTables;

public class ChestLootTableProvider extends ChestLootTables
{
    public void accept(final BiConsumer<ResourceLocation, LootTable.Builder> registrar) {
        registrar.accept(LootAS.SHRINE_CHEST, LootTable.func_216119_b().func_216040_a(LootPool.func_216096_a().func_216046_a((IRandomRange)RandomValueRange.func_215837_a(3.0f, 5.0f)).bonusRolls(1.0f, 2.0f).func_216045_a((LootEntry.Builder)ItemLootEntry.func_216168_a((ItemLike)ItemsAS.CONSTELLATION_PAPER).func_216086_a(18)).func_216045_a((LootEntry.Builder)ItemLootEntry.func_216168_a((ItemLike)ItemsAS.AQUAMARINE).func_216086_a(12).func_212841_b_((ILootFunction.IBuilder)SetCount.func_215932_a((IRandomRange)RandomValueRange.func_215837_a(1.0f, 3.0f)))).func_216045_a((LootEntry.Builder)ItemLootEntry.func_216168_a((ItemLike)Items.field_151103_aS).func_216086_a(10).func_212841_b_((ILootFunction.IBuilder)SetCount.func_215932_a((IRandomRange)RandomValueRange.func_215837_a(1.0f, 3.0f)))).func_216045_a((LootEntry.Builder)ItemLootEntry.func_216168_a((ItemLike)Items.field_151043_k).func_216086_a(5).func_212841_b_((ILootFunction.IBuilder)SetCount.func_215932_a((IRandomRange)RandomValueRange.func_215837_a(1.0f, 2.0f)))).func_216045_a((LootEntry.Builder)ItemLootEntry.func_216168_a((ItemLike)Items.field_151042_j).func_216086_a(15).func_212841_b_((ILootFunction.IBuilder)SetCount.func_215932_a((IRandomRange)RandomValueRange.func_215837_a(1.0f, 3.0f)))).func_216045_a((LootEntry.Builder)ItemLootEntry.func_216168_a((ItemLike)Items.field_151045_i).func_216086_a(2)).func_216045_a((LootEntry.Builder)ItemLootEntry.func_216168_a((ItemLike)Items.field_151114_aO).func_216086_a(8).func_212841_b_((ILootFunction.IBuilder)SetCount.func_215932_a((IRandomRange)RandomValueRange.func_215837_a(1.0f, 3.0f)))).func_216045_a((LootEntry.Builder)ItemLootEntry.func_216168_a((ItemLike)Items.field_151166_bC).func_216086_a(1)).func_216045_a((LootEntry.Builder)ItemLootEntry.func_216168_a((ItemLike)Items.field_151079_bi).func_216086_a(2))));
    }
}
