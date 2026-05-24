package hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin;

import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import com.google.gson.JsonSyntaxException;
import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.item.Item;
import net.minecraft.tags.Tag;
import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeGrid;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.item.crafting.Ingredient;
import java.util.List;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;

public class NBTCopyRecipe extends SimpleAltarRecipe
{
    private static final String KEY_SEARCH_ITEMS = "copy_nbt_from_items_matching";
    private List<Ingredient> searchIngredients;
    
    public NBTCopyRecipe(final ResourceLocation recipeId, final AltarType altarType, final int duration, final int starlightRequirement, final AltarRecipeGrid recipeGrid) {
        super(recipeId, altarType, duration, starlightRequirement, recipeGrid);
        this.searchIngredients = Lists.newArrayList();
    }
    
    public static NBTCopyRecipe convertToThis(final SimpleAltarRecipe other) {
        return new NBTCopyRecipe(other.func_199560_c(), other.getAltarType(), other.getDuration(), other.getStarlightRequirement(), other.getInputs());
    }
    
    public <T extends NBTCopyRecipe> T addNBTCopyMatchIngredient(final Tag<Item> tag) {
        return this.addNBTCopyMatchIngredient(Ingredient.func_199805_a((ITag)tag));
    }
    
    public <T extends NBTCopyRecipe> T addNBTCopyMatchIngredient(final ItemStack... items) {
        return this.addNBTCopyMatchIngredient(Ingredient.func_193369_a(items));
    }
    
    public <T extends NBTCopyRecipe> T addNBTCopyMatchIngredient(final ItemLike... items) {
        return this.addNBTCopyMatchIngredient(Ingredient.func_199804_a(items));
    }
    
    public <T extends NBTCopyRecipe> T addNBTCopyMatchIngredient(final Ingredient ingredient) {
        this.searchIngredients.add(ingredient);
        return (T)this;
    }
    
    @Override
    public void deserializeAdditionalJson(final JsonObject recipeObject) throws JsonSyntaxException {
        super.deserializeAdditionalJson(recipeObject);
        final JsonArray list = JSONUtils.func_151213_a(recipeObject, "copy_nbt_from_items_matching", new JsonArray());
        for (final JsonElement element : list) {
            this.searchIngredients.add(Ingredient.func_199802_a(element));
        }
    }
    
    @Override
    public void serializeAdditionalJson(final JsonObject recipeObject) {
        super.serializeAdditionalJson(recipeObject);
        final JsonArray list = new JsonArray();
        for (final Ingredient ingredient : this.searchIngredients) {
            list.add(ingredient.func_200304_c());
        }
        recipeObject.add("copy_nbt_from_items_matching", (JsonElement)list);
    }
    
    @Nonnull
    @Override
    public List<ItemStack> getOutputs(final TileAltar altar) {
        final List<ItemStack> outputs = super.getOutputs(altar);
        final List<CompoundTag> foundTags = Lists.newArrayList();
        for (final ItemStack existing : altar.getInventory()) {
            for (final Ingredient match : this.searchIngredients) {
                if (match.test(existing) && existing.hasTag()) {
                    foundTags.add(existing.getTag().func_74737_b());
                }
            }
        }
        for (final ItemStack output : outputs) {
            final CompoundTag tag = output.getOrCreateTag();
            for (final CompoundTag foundTag : foundTags) {
                NBTHelper.deepMerge(tag, foundTag, true);
            }
        }
        return outputs;
    }
    
    @Override
    public void readRecipeSync(final FriendlyByteBuf buf) {
        super.readRecipeSync(buf);
        this.searchIngredients = ByteBufUtils.readList(buf, Ingredient::func_199566_b);
    }
    
    @Override
    public void writeRecipeSync(final FriendlyByteBuf buf) {
        super.writeRecipeSync(buf);
        ByteBufUtils.writeCollection(buf, this.searchIngredients, (buffer, ingredient) -> ingredient.func_199564_a(buffer));
    }
}
