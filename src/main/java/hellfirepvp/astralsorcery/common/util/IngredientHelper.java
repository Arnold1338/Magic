package hellfirepvp.astralsorcery.common.util;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Comparator;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.resources.ResourceLocation;
import java.util.ArrayList;
import net.minecraft.world.level.item.Item;
import net.minecraft.tags.TagKey;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.world.level.item.crafting.Ingredient;

public class IngredientHelper
{
    @OnlyIn(Dist.CLIENT)
    public static ItemStack getRandomVisibleStack(final Ingredient ingredient) {
        return getRandomVisibleStack(ingredient, 0L);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static ItemStack getRandomVisibleStack(final Ingredient ingredient, final long tick) {
        final List<ItemStack> applicable = getVisibleItemStacks(ingredient);
        if (applicable.isEmpty()) {
            return ItemStack.field_190927_a;
        }
        final int mod = (int)(tick / 20L % applicable.size());
        return applicable.get(Mth.func_76125_a(mod, 0, applicable.size() - 1));
    }
    
    @OnlyIn(Dist.CLIENT)
    public static List<ItemStack> getVisibleItemStacks(final Ingredient ingredient) {
        if (ingredient.func_203189_d()) {
            return Collections.emptyList();
        }
        return Arrays.asList(ingredient.func_193365_a());
    }
    
    @Nullable
    public static ITag<Item> guessTag(final Ingredient ingredient) {
        final ItemStack[] stacks = ingredient.func_193365_a();
        if (stacks.length == 0) {
            return null;
        }
        final List<ITag<Item>> applicableTags = new ArrayList<ITag<Item>>();
        final ItemStack first = stacks[0];
        for (final ResourceLocation key : first.getItem().getTags()) {
            final ITag<Item> wrapper = (ITag<Item>)TagCollectionManager.func_242178_a().func_241836_b().func_199910_a(key);
            if (wrapper == null) {
                continue;
            }
            boolean containsAllItems = true;
            for (final Item itemInTag : wrapper.func_230236_b_()) {
                if (!ingredient.test(new ItemStack((ItemLike)itemInTag))) {
                    containsAllItems = false;
                    break;
                }
            }
            if (!containsAllItems) {
                continue;
            }
            applicableTags.add(wrapper);
        }
        return (ITag<Item>)applicableTags.stream().max(Comparator.comparingInt(tag -> tag.func_230236_b_().size())).orElse(null);
    }
}
