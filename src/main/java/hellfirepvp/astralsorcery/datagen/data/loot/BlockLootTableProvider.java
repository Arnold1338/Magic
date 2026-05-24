package hellfirepvp.astralsorcery.datagen.data.loot;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import hellfirepvp.astralsorcery.common.base.Mods;
import net.minecraftforge.registries.ForgeRegistries;
import hellfirepvp.astralsorcery.common.loot.CopyGatewayColor;
import hellfirepvp.astralsorcery.common.loot.CopyConstellation;
import hellfirepvp.astralsorcery.common.block.tile.BlockGemCrystalCluster;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.state.Property;
import hellfirepvp.astralsorcery.common.block.tile.BlockCelestialCrystalCluster;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.ConstantRange;
import hellfirepvp.astralsorcery.common.loot.CopyCrystalProperties;
import net.minecraft.world.item.Items;
import hellfirepvp.astralsorcery.common.loot.RandomCrystalProperty;
import net.minecraft.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.functions.ExplosionDecay;
import hellfirepvp.astralsorcery.common.loot.LinearLuckBonus;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.world.level.ItemLike;
import net.minecraft.loot.ItemLootEntry;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import java.util.function.Function;
import net.minecraft.world.level.block.Block;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.data.loot.BlockLootTables;

public class BlockLootTableProvider extends BlockLootTables
{
    protected void addTables() {
        this.func_218492_c((Block)BlocksAS.MARBLE_ARCH);
        this.func_218492_c((Block)BlocksAS.MARBLE_BRICKS);
        this.func_218492_c((Block)BlocksAS.MARBLE_CHISELED);
        this.func_218492_c((Block)BlocksAS.MARBLE_ENGRAVED);
        this.func_218492_c((Block)BlocksAS.MARBLE_PILLAR);
        this.func_218492_c((Block)BlocksAS.MARBLE_RAW);
        this.func_218492_c((Block)BlocksAS.MARBLE_RUNED);
        this.func_218492_c((Block)BlocksAS.MARBLE_STAIRS);
        this.func_218522_a((Block)BlocksAS.MARBLE_SLAB, (Function)BlockLootTables::func_218513_d);
        this.func_218492_c((Block)BlocksAS.BLACK_MARBLE_ARCH);
        this.func_218492_c((Block)BlocksAS.BLACK_MARBLE_BRICKS);
        this.func_218492_c((Block)BlocksAS.BLACK_MARBLE_CHISELED);
        this.func_218492_c((Block)BlocksAS.BLACK_MARBLE_ENGRAVED);
        this.func_218492_c((Block)BlocksAS.BLACK_MARBLE_PILLAR);
        this.func_218492_c((Block)BlocksAS.BLACK_MARBLE_RAW);
        this.func_218492_c((Block)BlocksAS.BLACK_MARBLE_RUNED);
        this.func_218492_c((Block)BlocksAS.BLACK_MARBLE_STAIRS);
        this.func_218522_a((Block)BlocksAS.BLACK_MARBLE_SLAB, (Function)BlockLootTables::func_218513_d);
        this.func_218492_c((Block)BlocksAS.INFUSED_WOOD);
        this.func_218492_c((Block)BlocksAS.INFUSED_WOOD_ARCH);
        this.func_218492_c((Block)BlocksAS.INFUSED_WOOD_COLUMN);
        this.func_218492_c((Block)BlocksAS.INFUSED_WOOD_ENGRAVED);
        this.func_218492_c((Block)BlocksAS.INFUSED_WOOD_ENRICHED);
        this.func_218492_c((Block)BlocksAS.INFUSED_WOOD_INFUSED);
        this.func_218492_c((Block)BlocksAS.INFUSED_WOOD_PLANKS);
        this.func_218492_c((Block)BlocksAS.INFUSED_WOOD_STAIRS);
        this.func_218522_a((Block)BlocksAS.INFUSED_WOOD_SLAB, (Function)BlockLootTables::func_218513_d);
        this.func_218522_a((Block)BlocksAS.AQUAMARINE_SAND_ORE, block -> func_218519_a(block, (LootEntry.Builder)ItemLootEntry.func_216168_a((ItemLike)ItemsAS.AQUAMARINE).func_212841_b_((ILootFunction.IBuilder)SetCount.func_215932_a((IRandomRange)RandomValueRange.func_215837_a(1.0f, 3.0f))).func_212841_b_((ILootFunction.IBuilder)LinearLuckBonus.builder()).func_212841_b_((ILootFunction.IBuilder)ExplosionDecay.func_215863_b())));
        this.func_218522_a((Block)BlocksAS.ROCK_CRYSTAL_ORE, block -> LootTable.func_216119_b().func_216040_a(LootPool.func_216096_a().func_216046_a((IRandomRange)RandomValueRange.func_215837_a(2.0f, 5.0f)).func_216045_a((LootEntry.Builder)ItemLootEntry.func_216168_a((ItemLike)ItemsAS.ROCK_CRYSTAL).func_212841_b_((ILootFunction.IBuilder)RandomCrystalProperty.builder()).func_212841_b_((ILootFunction.IBuilder)ExplosionDecay.func_215863_b()))));
        this.func_218492_c((Block)BlocksAS.STARMETAL_ORE);
        this.func_218492_c((Block)BlocksAS.STARMETAL);
        this.func_218522_a((Block)BlocksAS.GLOW_FLOWER, block -> func_218511_b(block, (LootEntry.Builder)ItemLootEntry.func_216168_a((ItemLike)Items.field_151114_aO).func_212841_b_((ILootFunction.IBuilder)SetCount.func_215932_a((IRandomRange)RandomValueRange.func_215837_a(2.0f, 4.0f))).func_212841_b_((ILootFunction.IBuilder)LinearLuckBonus.builder()).func_212841_b_((ILootFunction.IBuilder)ExplosionDecay.func_215863_b())));
        this.func_218492_c((Block)BlocksAS.SPECTRAL_RELAY);
        this.func_218492_c((Block)BlocksAS.ALTAR_DISCOVERY);
        this.func_218492_c((Block)BlocksAS.ALTAR_ATTUNEMENT);
        this.func_218492_c((Block)BlocksAS.ALTAR_CONSTELLATION);
        this.func_218492_c((Block)BlocksAS.ALTAR_RADIANCE);
        this.func_218492_c((Block)BlocksAS.ATTUNEMENT_ALTAR);
        this.func_218522_a((Block)BlocksAS.CELESTIAL_CRYSTAL_CLUSTER, block -> LootTable.func_216119_b().func_212841_b_((ILootFunction.IBuilder)ExplosionDecay.func_215863_b()).func_212841_b_((ILootFunction.IBuilder)CopyCrystalProperties.builder()).func_216040_a(LootPool.func_216096_a().func_216046_a((IRandomRange)ConstantRange.func_215835_a(1)).func_216045_a(ItemLootEntry.func_216168_a((ItemLike)ItemsAS.CELESTIAL_CRYSTAL).func_212840_b_((ILootCondition.IBuilder)BlockStateProperty.func_215985_a((Block)BlocksAS.CELESTIAL_CRYSTAL_CLUSTER).func_227567_a_(StatePropertiesPredicate.Builder.func_227191_a_().func_227192_a_((Property)BlockCelestialCrystalCluster.STAGE, 4))))).func_216040_a(LootPool.func_216096_a().func_216046_a((IRandomRange)ConstantRange.func_215835_a(1)).func_216045_a(ItemLootEntry.func_216168_a((ItemLike)ItemsAS.STARDUST).func_212840_b_((ILootCondition.IBuilder)BlockStateProperty.func_215985_a((Block)BlocksAS.CELESTIAL_CRYSTAL_CLUSTER).func_227567_a_(StatePropertiesPredicate.Builder.func_227191_a_().func_227192_a_((Property)BlockCelestialCrystalCluster.STAGE, 1))))).func_216040_a(LootPool.func_216096_a().func_216046_a((IRandomRange)RandomValueRange.func_215837_a(1.0f, 2.0f)).func_216045_a(ItemLootEntry.func_216168_a((ItemLike)ItemsAS.STARDUST).func_212840_b_((ILootCondition.IBuilder)BlockStateProperty.func_215985_a((Block)BlocksAS.CELESTIAL_CRYSTAL_CLUSTER).func_227567_a_(StatePropertiesPredicate.Builder.func_227191_a_().func_227192_a_((Property)BlockCelestialCrystalCluster.STAGE, 2))))).func_216040_a(LootPool.func_216096_a().func_216046_a((IRandomRange)RandomValueRange.func_215837_a(1.0f, 2.0f)).func_216045_a(ItemLootEntry.func_216168_a((ItemLike)ItemsAS.STARDUST).func_212840_b_((ILootCondition.IBuilder)BlockStateProperty.func_215985_a((Block)BlocksAS.CELESTIAL_CRYSTAL_CLUSTER).func_227567_a_(StatePropertiesPredicate.Builder.func_227191_a_().func_227192_a_((Property)BlockCelestialCrystalCluster.STAGE, 3))))).func_216040_a(LootPool.func_216096_a().func_216046_a((IRandomRange)ConstantRange.func_215835_a(2)).func_216045_a(ItemLootEntry.func_216168_a((ItemLike)ItemsAS.STARDUST).func_212840_b_((ILootCondition.IBuilder)BlockStateProperty.func_215985_a((Block)BlocksAS.CELESTIAL_CRYSTAL_CLUSTER).func_227567_a_(StatePropertiesPredicate.Builder.func_227191_a_().func_227192_a_((Property)BlockCelestialCrystalCluster.STAGE, 4))))));
        this.func_218522_a((Block)BlocksAS.GEM_CRYSTAL_CLUSTER, block -> LootTable.func_216119_b().func_212841_b_((ILootFunction.IBuilder)ExplosionDecay.func_215863_b()).func_216040_a(LootPool.func_216096_a().func_216046_a((IRandomRange)ConstantRange.func_215835_a(1)).func_216045_a(ItemLootEntry.func_216168_a((ItemLike)ItemsAS.PERK_GEM_DAY).func_212840_b_((ILootCondition.IBuilder)BlockStateProperty.func_215985_a((Block)BlocksAS.GEM_CRYSTAL_CLUSTER).func_227567_a_(StatePropertiesPredicate.Builder.func_227191_a_().func_227193_a_((Property)BlockGemCrystalCluster.STAGE, (Comparable)BlockGemCrystalCluster.GrowthStageType.STAGE_2_DAY))))).func_216040_a(LootPool.func_216096_a().func_216046_a((IRandomRange)ConstantRange.func_215835_a(1)).func_216045_a(ItemLootEntry.func_216168_a((ItemLike)ItemsAS.PERK_GEM_NIGHT).func_212840_b_((ILootCondition.IBuilder)BlockStateProperty.func_215985_a((Block)BlocksAS.GEM_CRYSTAL_CLUSTER).func_227567_a_(StatePropertiesPredicate.Builder.func_227191_a_().func_227193_a_((Property)BlockGemCrystalCluster.STAGE, (Comparable)BlockGemCrystalCluster.GrowthStageType.STAGE_2_NIGHT))))).func_216040_a(LootPool.func_216096_a().func_216046_a((IRandomRange)ConstantRange.func_215835_a(1)).func_216045_a(ItemLootEntry.func_216168_a((ItemLike)ItemsAS.PERK_GEM_SKY).func_212840_b_((ILootCondition.IBuilder)BlockStateProperty.func_215985_a((Block)BlocksAS.GEM_CRYSTAL_CLUSTER).func_227567_a_(StatePropertiesPredicate.Builder.func_227191_a_().func_227193_a_((Property)BlockGemCrystalCluster.STAGE, (Comparable)BlockGemCrystalCluster.GrowthStageType.STAGE_2_SKY))))));
        this.func_218507_a((Block)BlocksAS.ROCK_COLLECTOR_CRYSTAL, func_218546_a((ItemLike)BlocksAS.ROCK_COLLECTOR_CRYSTAL).func_212841_b_((ILootFunction.IBuilder)CopyCrystalProperties.builder()).func_212841_b_((ILootFunction.IBuilder)CopyConstellation.builder()));
        this.func_218507_a((Block)BlocksAS.CELESTIAL_COLLECTOR_CRYSTAL, func_218546_a((ItemLike)BlocksAS.CELESTIAL_COLLECTOR_CRYSTAL).func_212841_b_((ILootFunction.IBuilder)CopyCrystalProperties.builder()).func_212841_b_((ILootFunction.IBuilder)CopyConstellation.builder()));
        this.func_218507_a((Block)BlocksAS.LENS, func_218546_a((ItemLike)BlocksAS.LENS).func_212841_b_((ILootFunction.IBuilder)CopyCrystalProperties.builder()));
        this.func_218507_a((Block)BlocksAS.PRISM, func_218546_a((ItemLike)BlocksAS.PRISM).func_212841_b_((ILootFunction.IBuilder)CopyCrystalProperties.builder()));
        this.func_218492_c((Block)BlocksAS.RITUAL_LINK);
        this.func_218492_c((Block)BlocksAS.RITUAL_PEDESTAL);
        this.func_218492_c((Block)BlocksAS.ILLUMINATOR);
        this.func_218492_c((Block)BlocksAS.INFUSER);
        this.func_218492_c((Block)BlocksAS.CHALICE);
        this.func_218492_c((Block)BlocksAS.WELL);
        this.func_218492_c((Block)BlocksAS.TELESCOPE);
        this.func_218492_c((Block)BlocksAS.OBSERVATORY);
        this.func_218492_c((Block)BlocksAS.REFRACTION_TABLE);
        this.func_218492_c((Block)BlocksAS.TREE_BEACON);
        this.func_218507_a((Block)BlocksAS.TREE_BEACON_COMPONENT, LootTable.func_216119_b());
        this.func_218507_a((Block)BlocksAS.GATEWAY, BlockLootTables.func_218481_e((Block)BlocksAS.GATEWAY).func_212841_b_((ILootFunction.IBuilder)CopyGatewayColor.builder()));
        this.func_218492_c((Block)BlocksAS.FOUNTAIN);
        this.func_218492_c((Block)BlocksAS.FOUNTAIN_PRIME_LIQUID);
        this.func_218492_c((Block)BlocksAS.FOUNTAIN_PRIME_VORTEX);
        this.func_218492_c((Block)BlocksAS.FOUNTAIN_PRIME_ORE);
        this.func_218507_a((Block)BlocksAS.FLARE_LIGHT, LootTable.func_216119_b());
        this.func_218507_a((Block)BlocksAS.TRANSLUCENT_BLOCK, LootTable.func_216119_b());
        this.func_218507_a((Block)BlocksAS.VANISHING, LootTable.func_216119_b());
        this.func_218507_a((Block)BlocksAS.STRUCTURAL, LootTable.func_216119_b());
    }
    
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getValues().stream().filter((Predicate<? super Object>)Mods.ASTRAL_SORCERY::owns).collect((Collector<? super Object, ?, Iterable<Block>>)Collectors.toList());
    }
}
