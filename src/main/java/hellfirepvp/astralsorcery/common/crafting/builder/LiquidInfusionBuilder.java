package hellfirepvp.astralsorcery.common.crafting.builder;

import hellfirepvp.astralsorcery.common.crafting.helper.CustomMatcherRecipe;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeSerializer;
import javax.annotation.Nonnull;
import net.minecraft.world.item.Item;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInfusion;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeBuilder;

public class LiquidInfusionBuilder extends CustomRecipeBuilder<LiquidInfusion>
{
    private final ResourceLocation id;
    private Fluid liquidInput;
    private Ingredient itemInput;
    private ItemStack output;
    private int craftingTickTime;
    private float consumptionChance;
    private boolean consumeMultipleFluids;
    private boolean acceptChaliceInput;
    private boolean copyNBTToOutputs;
    
    private LiquidInfusionBuilder(final ResourceLocation id) {
        this.liquidInput = null;
        this.itemInput = Ingredient.field_193370_a;
        this.output = ItemStack.EMPTY;
        this.craftingTickTime = 200;
        this.consumptionChance = 0.3f;
        this.consumeMultipleFluids = false;
        this.acceptChaliceInput = true;
        this.copyNBTToOutputs = false;
        this.id = id;
    }
    
    public static LiquidInfusionBuilder builder(final ForgeRegistryEntry<?> nameProvider) {
        return new LiquidInfusionBuilder(AstralSorcery.key(nameProvider.getRegistryName().addTransientModifier()));
    }
    
    public static LiquidInfusionBuilder builder(final ResourceLocation id) {
        return new LiquidInfusionBuilder(id);
    }
    
    public LiquidInfusionBuilder setLiquidInput(final Fluid liquidInput) {
        this.liquidInput = liquidInput;
        return this;
    }
    
    public LiquidInfusionBuilder setItemInput(final ItemLike item) {
        this.itemInput = Ingredient.func_199804_a(new ItemLike[] { item });
        return this;
    }
    
    public LiquidInfusionBuilder setItemInput(final ITag.INamedTag<Item> tag) {
        this.itemInput = Ingredient.func_199805_a((ITag)tag);
        return this;
    }
    
    public LiquidInfusionBuilder setItemInput(final Ingredient input) {
        this.itemInput = input;
        return this;
    }
    
    public LiquidInfusionBuilder setOutput(final ItemLike output) {
        return this.setOutput(new ItemStack(output));
    }
    
    public LiquidInfusionBuilder setOutput(final ItemStack output) {
        this.output = output.copy();
        return this;
    }
    
    public LiquidInfusionBuilder multiplyDuration(final float multiplier) {
        this.craftingTickTime *= (int)multiplier;
        return this;
    }
    
    public LiquidInfusionBuilder setDuration(final int craftingTickTime) {
        this.craftingTickTime = craftingTickTime;
        return this;
    }
    
    public LiquidInfusionBuilder setFluidConsumptionChance(final float consumptionChance) {
        this.consumptionChance = consumptionChance;
        return this;
    }
    
    public LiquidInfusionBuilder setConsumeMultipleFluids(final boolean consumeMultipleFluids) {
        this.consumeMultipleFluids = consumeMultipleFluids;
        return this;
    }
    
    public LiquidInfusionBuilder setAcceptChaliceInput(final boolean acceptChaliceInput) {
        this.acceptChaliceInput = acceptChaliceInput;
        return this;
    }
    
    public LiquidInfusionBuilder setCopyNBTToOutputs(final boolean copyNBTToOutputs) {
        this.copyNBTToOutputs = copyNBTToOutputs;
        return this;
    }
    
    @Nonnull
    @Override
    protected LiquidInfusion validateAndGet() {
        if (this.liquidInput == null) {
            throw new IllegalArgumentException("No fluid input defined!");
        }
        if (this.itemInput.func_203189_d()) {
            throw new IllegalArgumentException("No valid item for input found!");
        }
        if (this.output.isEmpty()) {
            throw new IllegalArgumentException("No output item defined!");
        }
        if (this.craftingTickTime <= 0) {
            throw new IllegalArgumentException("No duration defined!");
        }
        return new LiquidInfusion(this.id, this.craftingTickTime, this.liquidInput, this.itemInput, this.output, this.consumptionChance, this.consumeMultipleFluids, this.acceptChaliceInput, this.copyNBTToOutputs);
    }
    
    @Override
    protected CustomRecipeSerializer<LiquidInfusion> getSerializer() {
        return RecipeSerializersAS.LIQUID_INFUSION_SERIALIZER;
    }
}
