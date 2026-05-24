package hellfirepvp.astralsorcery.common.advancement.instance;

import com.google.gson.JsonObject;
import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.ConditionArraySerializer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import java.util.Collection;
import java.util.Arrays;
import hellfirepvp.astralsorcery.common.advancement.AltarCraftTrigger;
import java.util.ArrayList;
import java.util.HashSet;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import java.util.Set;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;

public class AltarRecipeInstance extends CriterionInstance
{
    private final Set<ResourceLocation> recipeNames;
    private final List<Ingredient> recipeOutputs;
    
    private AltarRecipeInstance(final ResourceLocation id) {
        super(id, EntityPredicate.AndPredicate.field_234582_a_);
        this.recipeNames = new HashSet<ResourceLocation>();
        this.recipeOutputs = new ArrayList<Ingredient>();
    }
    
    public static AltarRecipeInstance craftRecipe(final ResourceLocation... recipeIds) {
        final AltarRecipeInstance instance = new AltarRecipeInstance(AltarCraftTrigger.ID);
        instance.recipeNames.addAll(Arrays.asList(recipeIds));
        return instance;
    }
    
    public static AltarRecipeInstance craftRecipe(final SimpleAltarRecipe... recipes) {
        final AltarRecipeInstance instance = new AltarRecipeInstance(AltarCraftTrigger.ID);
        Arrays.asList(recipes).forEach(recipe -> instance.recipeNames.add(recipe.func_199560_c()));
        return instance;
    }
    
    public static AltarRecipeInstance withOutput(final ItemLike... outputs) {
        return withOutput(Ingredient.func_199804_a(outputs));
    }
    
    public static AltarRecipeInstance withOutput(final ItemStack... outputs) {
        return withOutput(Ingredient.func_193369_a(outputs));
    }
    
    public static AltarRecipeInstance withOutput(final Tag<Item>... outputs) {
        return withOutput((List<Ingredient>)Arrays.stream(outputs).map((Function<? super Tag<Item>, ?>)Ingredient::func_199805_a).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
    }
    
    public static AltarRecipeInstance withOutput(final Ingredient... outputs) {
        return withOutput(Arrays.asList(outputs));
    }
    
    public static AltarRecipeInstance withOutput(final List<Ingredient> outputs) {
        final AltarRecipeInstance instance = new AltarRecipeInstance(AltarCraftTrigger.ID);
        instance.recipeOutputs.addAll(outputs);
        return instance;
    }
    
    public JsonObject func_230240_a_(final ConditionArraySerializer conditions) {
        final JsonObject out = super.func_230240_a_(conditions);
        if (!this.recipeNames.isEmpty()) {
            final JsonArray names = new JsonArray();
            for (final ResourceLocation name : this.recipeNames) {
                names.add(name.toString());
            }
            out.add("recipeNames", (JsonElement)names);
        }
        if (!this.recipeOutputs.isEmpty()) {
            final JsonArray outputs = new JsonArray();
            for (final Ingredient output : this.recipeOutputs) {
                outputs.add(output.func_200304_c());
            }
            out.add("recipeOutputs", (JsonElement)outputs);
        }
        return out;
    }
    
    public static AltarRecipeInstance deserialize(final ResourceLocation id, final JsonObject json) {
        final AltarRecipeInstance instance = new AltarRecipeInstance(id);
        final JsonArray recipeNames = JSONUtils.func_151213_a(json, "recipeNames", new JsonArray());
        for (int idx = 0; idx < recipeNames.size(); ++idx) {
            final JsonElement element = recipeNames.get(idx);
            final String key = JSONUtils.func_151206_a(element, String.format("recipeNames[%s]", idx));
            instance.recipeNames.add(new ResourceLocation(key));
        }
        final Iterator iterator = JSONUtils.func_151213_a(json, "recipeOutputs", new JsonArray()).iterator();
        while (iterator.hasNext()) {
            final JsonElement element = (JsonElement)iterator.next();
            instance.recipeOutputs.add(Ingredient.func_199802_a(element));
        }
        return instance;
    }
    
    public boolean test(final SimpleAltarRecipe recipe, final ItemStack output) {
        if (this.recipeNames.isEmpty() && this.recipeOutputs.isEmpty()) {
            return true;
        }
        final ResourceLocation recipeName = recipe.func_199560_c();
        if (this.recipeNames.contains(recipeName)) {
            return true;
        }
        for (final Ingredient i : this.recipeOutputs) {
            if (i.test(output)) {
                return true;
            }
        }
        return false;
    }
}
