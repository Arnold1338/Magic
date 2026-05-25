package hellfirepvp.astralsorcery.common.crafting.builder;

import hellfirepvp.astralsorcery.common.crafting.helper.CustomMatcherRecipe;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeSerializer;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraftforge.registries.ForgeRegistryEntry;
import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.InteractionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInteraction;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeBuilder;

public class LiquidInteractionBuilder extends CustomRecipeBuilder<LiquidInteraction>
{
    private final ResourceLocation id;
    private FluidStack reactant1;
    private FluidStack reactant2;
    private float chanceConsume1;
    private float chanceConsume2;
    private int weight;
    private InteractionResult result;
    
    private LiquidInteractionBuilder(final ResourceLocation id) {
        this.reactant1 = FluidStack.EMPTY;
        this.reactant2 = FluidStack.EMPTY;
        this.chanceConsume1 = 1.0f;
        this.chanceConsume2 = 1.0f;
        this.weight = 1;
        this.result = null;
        this.id = id;
    }
    
    public static LiquidInteractionBuilder builder(final ForgeRegistryEntry<?> nameProvider) {
        return new LiquidInteractionBuilder(AstralSorcery.key(nameProvider.getRegistryName().addTransientModifier()));
    }
    
    public static LiquidInteractionBuilder builder(final ResourceLocation id) {
        return new LiquidInteractionBuilder(id);
    }
    
    public LiquidInteractionBuilder setReactant1(final FluidStack reactant1) {
        this.reactant1 = reactant1;
        return this;
    }
    
    public LiquidInteractionBuilder setReactant2(final FluidStack reactant2) {
        this.reactant2 = reactant2;
        return this;
    }
    
    public LiquidInteractionBuilder setChanceConsumeReactant1(final float chanceConsume1) {
        this.chanceConsume1 = chanceConsume1;
        return this;
    }
    
    public LiquidInteractionBuilder setChanceConsumeReactant2(final float chanceConsume2) {
        this.chanceConsume2 = chanceConsume2;
        return this;
    }
    
    public LiquidInteractionBuilder setWeight(final int weight) {
        this.weight = weight;
        return this;
    }
    
    public LiquidInteractionBuilder setResult(final InteractionResult result) {
        this.result = result;
        return this;
    }
    
    @Nonnull
    @Override
    protected LiquidInteraction validateAndGet() {
        if (this.reactant1.isEmpty()) {
            throw new IllegalArgumentException("The 1st reactant must not be empty!");
        }
        if (this.reactant2.isEmpty()) {
            throw new IllegalArgumentException("The 2nd reactant must not be empty!");
        }
        if (this.weight <= 0) {
            throw new IllegalArgumentException("Weight has to be positive!");
        }
        if (this.result == null) {
            throw new IllegalArgumentException("A result must be defined!");
        }
        return new LiquidInteraction(this.id, this.reactant1, this.chanceConsume1, this.reactant2, this.chanceConsume2, this.weight, this.result);
    }
    
    @Override
    protected CustomRecipeSerializer<LiquidInteraction> getSerializer() {
        return RecipeSerializersAS.LIQUID_INTERACTION_SERIALIZER;
    }
}
