package hellfirepvp.astralsorcery.common.crafting.recipe;

import net.minecraft.world.item.crafting.IRecipeSerializer;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeSerializer;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.crafting.Ingredient;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomMatcherRecipe;

public class WellLiquefaction extends CustomMatcherRecipe implements GatedRecipe.Progression
{
    private final Color catalystColor;
    private final Ingredient input;
    private final Fluid output;
    private final float productionMultiplier;
    private final float shatterMultiplier;
    
    public WellLiquefaction(final ResourceLocation recipeId, final Ingredient input, final Fluid output, final float productionMultiplier, final float shatterMultiplier) {
        this(recipeId, input, output, null, productionMultiplier, shatterMultiplier);
    }
    
    public WellLiquefaction(final ResourceLocation recipeId, final Ingredient input, final Fluid output, @Nullable final Color catalystColor, final float productionMultiplier, final float shatterMultiplier) {
        super(recipeId);
        this.input = input;
        this.output = output;
        this.catalystColor = catalystColor;
        this.productionMultiplier = productionMultiplier;
        this.shatterMultiplier = shatterMultiplier;
    }
    
    @Nonnull
    @Override
    public ResearchProgression getRequiredProgression() {
        return ResearchProgression.BASIC_CRAFT;
    }
    
    public boolean matches(final ItemStack input) {
        return this.input.test(input);
    }
    
    @Nonnull
    public Ingredient getInput() {
        return this.input;
    }
    
    @Nonnull
    public Fluid getFluidOutput() {
        return this.output;
    }
    
    @Nullable
    public Color getCatalystColor() {
        return this.catalystColor;
    }
    
    public float getProductionMultiplier() {
        return this.productionMultiplier;
    }
    
    public float getShatterMultiplier() {
        return this.shatterMultiplier;
    }
    
    public RecipeType<?> func_222127_g() {
        return RecipeTypesAS.TYPE_WELL.getType();
    }
    
    @Override
    public CustomRecipeSerializer<?> getSerializer() {
        return RecipeSerializersAS.WELL_LIQUEFACTION_SERIALIZER;
    }
}
