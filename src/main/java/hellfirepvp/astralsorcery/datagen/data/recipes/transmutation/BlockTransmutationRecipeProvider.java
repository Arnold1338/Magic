package hellfirepvp.astralsorcery.datagen.data.recipes.transmutation;

import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.world.level.level.block.Block;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.world.level.level.block.Blocks;
import net.minecraftforge.common.Tags;
import hellfirepvp.astralsorcery.common.crafting.builder.BlockTransmutationBuilder;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.data.IFinishedRecipe;
import java.util.function.Consumer;

public class BlockTransmutationRecipeProvider
{
    public static void registerTransmutationRecipes(final Consumer<IFinishedRecipe> registrar) {
        BlockTransmutationBuilder.builder(AstralSorcery.key("iron_starmetal")).multiplyStarlightCost(0.5f).addInputCheck((ITag<Block>)Tags.Blocks.ORES_IRON, new ItemStack((ItemLike)Blocks.field_150366_p)).setOutput(BlocksAS.STARMETAL_ORE).build(registrar);
        BlockTransmutationBuilder.builder(AstralSorcery.key("craftingtable_altar")).multiplyStarlightCost(0.3f).addInputCheck(Blocks.field_150462_ai).setOutput((Block)BlocksAS.ALTAR_DISCOVERY).build(registrar);
        BlockTransmutationBuilder.builder(AstralSorcery.key("pumpkin_cake")).multiplyStarlightCost(2.5f).addInputCheck(Blocks.field_150423_aK).setOutput(Blocks.field_150414_aQ).build(registrar);
        BlockTransmutationBuilder.builder(AstralSorcery.key("magma_obsidian")).multiplyStarlightCost(2.0f).addInputCheck(Blocks.field_196814_hQ).setOutput(Blocks.field_150343_Z).build(registrar);
        BlockTransmutationBuilder.builder(AstralSorcery.key("sand_clay")).addInputCheck(Blocks.field_150354_m).setOutput(Blocks.field_150435_aG).build(registrar);
        BlockTransmutationBuilder.builder(AstralSorcery.key("diamond_emerald")).multiplyStarlightCost(5.0f).addInputCheck(Blocks.field_150482_ag).setOutput(Blocks.field_150412_bA).build(registrar);
        BlockTransmutationBuilder.builder(AstralSorcery.key("netherwart_soulsand")).addInputCheck(Blocks.field_189878_dg).setOutput(Blocks.field_150425_aM).build(registrar);
        BlockTransmutationBuilder.builder(AstralSorcery.key("sealantern_lapis")).addInputCheck(Blocks.field_180398_cJ).setOutput(Blocks.field_150368_y).build(registrar);
        BlockTransmutationBuilder.builder(AstralSorcery.key("sandstone_endstone")).addInputCheck(Blocks.field_150322_A).setOutput(Blocks.field_150377_bs).build(registrar);
        BlockTransmutationBuilder.builder(AstralSorcery.key("netherrack_netherbrick")).addInputCheck(Blocks.field_150424_aL).setOutput(Blocks.field_196653_dH).build(registrar);
    }
}
