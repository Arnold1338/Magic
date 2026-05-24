package hellfirepvp.astralsorcery.common.crafting.nojson.meltable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.util.block.WorldBlockPos;
import java.util.function.Function;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.util.RecipeHelper;
import hellfirepvp.astralsorcery.AstralSorcery;

public class FurnaceMeltableRecipe extends ItemMeltableRecipe
{
    public FurnaceMeltableRecipe() {
        super(AstralSorcery.key("all_furnace_meltable"), (world, pos, state) -> RecipeHelper.findSmeltingResult(world, state).isPresent(), (worldPos, state) -> RecipeHelper.findSmeltingResult(worldPos.getWorld(), state).map((Function<? super Tuple<ItemStack, Float>, ? extends ItemStack>)Tuple::func_76341_a).orElse(ItemStack.field_190927_a));
    }
}
