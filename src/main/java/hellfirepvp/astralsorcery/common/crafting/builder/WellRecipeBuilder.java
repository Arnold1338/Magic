package hellfirepvp.astralsorcery.common.crafting.builder;

import hellfirepvp.astralsorcery.common.crafting.helper.CustomMatcherRecipe;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeSerializer;
import javax.annotation.Nonnull;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraft.world.level.material.Fluids;
import java.awt.Color;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.crafting.recipe.WellLiquefaction;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeBuilder;

public class WellRecipeBuilder extends CustomRecipeBuilder<WellLiquefaction>
{
    private final ResourceLocation id;
    private Ingredient input;
    private Fluid output;
    private float productionMultiplier;
    private float shatterMultiplier;
    private Color catalystColor;
    
    private WellRecipeBuilder(final ResourceLocation id) {
        this.input = Ingredient.field_193370_a;
        this.output = Fluids.field_204541_a;
        this.productionMultiplier = 0.5f;
        this.shatterMultiplier = 15.0f;
        this.catalystColor = Color.WHITE;
        this.id = id;
    }
    
    public static WellRecipeBuilder builder(final ForgeRegistryEntry<?> nameProvider) {
        return new WellRecipeBuilder(AstralSorcery.key(nameProvider.getRegistryName().addTransientModifier()));
    }
    
    public static WellRecipeBuilder builder(final ResourceLocation id) {
        return new WellRecipeBuilder(id);
    }
    
    public WellRecipeBuilder setItemInput(final ItemLike item) {
        this.input = Ingredient.func_199804_a(new ItemLike[] { item });
        return this;
    }
    
    public WellRecipeBuilder setItemInput(final Tag<Item> tag) {
        this.input = Ingredient.func_199805_a((ITag)tag);
        return this;
    }
    
    public WellRecipeBuilder setItemInput(final Ingredient input) {
        this.input = input;
        return this;
    }
    
    public WellRecipeBuilder setLiquidOutput(final Fluid output) {
        this.output = output;
        return this;
    }
    
    public WellRecipeBuilder color(final Color color) {
        this.catalystColor = color;
        return this;
    }
    
    public WellRecipeBuilder productionMultiplier(final float multiplier) {
        this.productionMultiplier = multiplier;
        return this;
    }
    
    public WellRecipeBuilder shatterMultiplier(final float multiplier) {
        this.shatterMultiplier = multiplier;
        return this;
    }
    
    @Nonnull
    @Override
    protected WellLiquefaction validateAndGet() {
        if (this.input.func_203189_d()) {
            throw new IllegalArgumentException("No valid item for input found!");
        }
        if (this.output == Fluids.field_204541_a) {
            throw new IllegalArgumentException("No output fluid defined!");
        }
        return new WellLiquefaction(this.id, this.input, this.output, this.catalystColor, this.productionMultiplier, this.shatterMultiplier);
    }
    
    @Override
    protected CustomRecipeSerializer<WellLiquefaction> getSerializer() {
        return RecipeSerializersAS.WELL_LIQUEFACTION_SERIALIZER;
    }
}
