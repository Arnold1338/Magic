package hellfirepvp.astralsorcery.common.crafting.builder;

import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraftforge.registries.ForgeRegistryEntry;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomMatcherRecipe;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeSerializer;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import java.util.function.Consumer;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.AltarRecipeEffect;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.world.level.item.crafting.Ingredient;
import net.minecraft.world.level.item.Item;
import net.minecraft.tags.TagKey;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeGrid;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeTypeHandler;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeBuilder;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;

public class SimpleAltarRecipeBuilder<T extends SimpleAltarRecipe> extends CustomRecipeBuilder<T>
{
    private final T recipe;
    
    private SimpleAltarRecipeBuilder(final T recipe) {
        this.recipe = recipe;
    }
    
    public static TypedRecipeBuilder<SimpleAltarRecipe> builder() {
        return new TypedRecipeBuilder<SimpleAltarRecipe>((AltarRecipeTypeHandler.Type)null);
    }
    
    public static <T extends SimpleAltarRecipe> TypedRecipeBuilder<T> ofType(final AltarRecipeTypeHandler.Type<T> type) {
        return new TypedRecipeBuilder<T>((AltarRecipeTypeHandler.Type)type);
    }
    
    public SimpleAltarRecipeBuilder<T> setInputs(final AltarRecipeGrid.Builder gridBuilder) {
        this.recipe.setInputs(gridBuilder.build());
        return this;
    }
    
    public SimpleAltarRecipeBuilder<T> setFocusConstellation(final IConstellation focusConstellation) {
        this.recipe.setFocusConstellation(focusConstellation);
        return this;
    }
    
    public SimpleAltarRecipeBuilder<T> addRelayInput(final ITag.INamedTag<Item> tag) {
        return this.addRelayInput(Ingredient.func_199805_a((ITag)tag));
    }
    
    public SimpleAltarRecipeBuilder<T> addRelayInput(final ItemLike item) {
        return this.addRelayInput(Ingredient.func_199804_a(new ItemLike[] { item }));
    }
    
    public SimpleAltarRecipeBuilder<T> addRelayInput(final Ingredient ingredient) {
        this.recipe.addRelayInput(ingredient);
        return this;
    }
    
    public SimpleAltarRecipeBuilder<T> addAltarEffect(final AltarRecipeEffect effect) {
        this.recipe.addAltarEffect(effect);
        return this;
    }
    
    public SimpleAltarRecipeBuilder<T> setStarlightRequirement(final float percentOfAltarBar) {
        this.recipe.setStarlightRequirement((int)(this.recipe.getAltarType().getStarlightCapacity() * Mth.func_76131_a(percentOfAltarBar, 0.0f, 1.0f)));
        return this;
    }
    
    public SimpleAltarRecipeBuilder<T> setStarlightRequirement(final int requiredStarlight) {
        this.recipe.setStarlightRequirement(requiredStarlight);
        return this;
    }
    
    public SimpleAltarRecipeBuilder<T> multiplyDuration(final float duration) {
        this.recipe.setDuration((int)(this.recipe.getAltarType().getDefaultAltarCraftingDuration() * duration));
        return this;
    }
    
    public SimpleAltarRecipeBuilder<T> setDuration(final int duration) {
        this.recipe.setDuration(duration);
        return this;
    }
    
    public SimpleAltarRecipeBuilder<T> addOutput(final ItemLike output) {
        this.addOutput(new ItemStack(output));
        return this;
    }
    
    public SimpleAltarRecipeBuilder<T> addOutput(final ItemStack output) {
        this.recipe.addOutput(output);
        return this;
    }
    
    public SimpleAltarRecipeBuilder<T> modify(final Consumer<T> recipeFn) {
        recipeFn.accept(this.recipe);
        return this;
    }
    
    @Nonnull
    @Override
    protected T validateAndGet() {
        final AltarType type = this.recipe.getAltarType();
        this.recipe.getInputs().validate(type);
        if (!type.isThisGEThan(AltarType.RADIANCE)) {
            if (this.recipe.getRelayInputs().size() > 0) {
                throw new IllegalArgumentException("Cannot make a altar recipe require relay inputs, if the recipe isn't for a T4 altar or higher.");
            }
            if (this.recipe.getFocusConstellation() != null) {
                throw new IllegalArgumentException("Cannot make a altar recipe require a constellation focus, if the recipe isn't for a T4 altar or higher.");
            }
        }
        if (this.recipe.getDuration() <= 0) {
            throw new IllegalArgumentException("Cannot make a altar recipe with 0 or less ticks duration!");
        }
        if (this.recipe.getStarlightRequirement() > type.getStarlightCapacity()) {
            throw new IllegalArgumentException("Cannot make a recipe require more starlight than the capacity of the corresponding altar allows for.");
        }
        return this.recipe;
    }
    
    @Override
    protected CustomRecipeSerializer<T> getSerializer() {
        return (CustomRecipeSerializer<T>)RecipeSerializersAS.ALTAR_RECIPE_SERIALIZER;
    }
    
    public static class TypedRecipeBuilder<T extends SimpleAltarRecipe>
    {
        @Nullable
        private final AltarRecipeTypeHandler.Type<T> type;
        
        private TypedRecipeBuilder(@Nullable final AltarRecipeTypeHandler.Type<T> type) {
            this.type = type;
        }
        
        public SimpleAltarRecipeBuilder<T> createRecipe(final ForgeRegistryEntry<?> nameProvider, final AltarType altarType) {
            return this.createRecipe(AstralSorcery.key(nameProvider.getRegistryName().func_110623_a()), altarType);
        }
        
        public SimpleAltarRecipeBuilder<T> createRecipe(final ResourceLocation recipeId, final AltarType altarType) {
            final SimpleAltarRecipe recipe = new SimpleAltarRecipe(recipeId, altarType);
            T converted;
            if (this.type == null) {
                converted = (T)AltarRecipeTypeHandler.DEFAULT.convert(recipe);
            }
            else {
                converted = this.type.convert(recipe);
                converted.setCustomRecipeType(this.type.getKey());
            }
            return new SimpleAltarRecipeBuilder<T>(converted, null);
        }
    }
}
