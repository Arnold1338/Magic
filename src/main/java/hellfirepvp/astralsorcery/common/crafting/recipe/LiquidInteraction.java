package hellfirepvp.astralsorcery.common.crafting.recipe;

import net.minecraft.world.item.crafting.IRecipeSerializer;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import net.minecraft.world.level.item.crafting.RecipeType;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeSerializer;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import joptsimple.internal.Strings;
import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.InteractionResultRegistry;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.JsonToNBT;
import com.google.gson.JsonSyntaxException;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.level.material.Fluid;
import com.google.gson.JsonObject;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.Collection;
import net.minecraftforge.fluids.capability.IFluidHandler;
import hellfirepvp.astralsorcery.common.tile.TileChalice;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.InteractionResult;
import net.minecraftforge.fluids.FluidStack;
import java.util.Random;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomMatcherRecipe;

public class LiquidInteraction extends CustomMatcherRecipe
{
    private static final Random rand;
    private final FluidStack reactant1;
    private final FluidStack reactant2;
    private final float chanceConsumeReactant1;
    private final float chanceConsumeReactant2;
    private final int weight;
    private final InteractionResult result;
    
    public LiquidInteraction(final ResourceLocation recipeId, final FluidStack reactant1, final float chanceConsumeReactant1, final FluidStack reactant2, final float chanceConsumeReactant2, final int weight, final InteractionResult result) {
        super(recipeId);
        this.reactant1 = reactant1;
        this.chanceConsumeReactant1 = chanceConsumeReactant1;
        this.reactant2 = reactant2;
        this.chanceConsumeReactant2 = chanceConsumeReactant2;
        this.weight = weight;
        this.result = result;
    }
    
    public boolean matches(final FluidStack input1, final FluidStack input2) {
        return (input1.containsFluid(this.reactant1) && input2.containsFluid(this.reactant2)) || (input2.containsFluid(this.reactant2) && input1.containsFluid(this.reactant1));
    }
    
    public boolean consumeInputs(final TileChalice chalice1, final TileChalice chalice2) {
        final FluidStack contained1 = chalice1.getTank().getFluid();
        final FluidStack contained2 = chalice2.getTank().getFluid();
        if (!this.matches(contained1, contained2)) {
            return false;
        }
        if (contained1.containsFluid(this.reactant1) && contained2.containsFluid(this.reactant2)) {
            final FluidStack drained1 = chalice1.getTank().drain(this.reactant1, IFluidHandler.FluidAction.SIMULATE);
            final FluidStack drained2 = chalice2.getTank().drain(this.reactant2, IFluidHandler.FluidAction.SIMULATE);
            if (drained1.containsFluid(this.reactant1) && drained2.containsFluid(this.reactant2)) {
                if (LiquidInteraction.rand.nextFloat() < this.chanceConsumeReactant1) {
                    chalice1.getTank().drain(this.reactant1, IFluidHandler.FluidAction.EXECUTE);
                }
                if (LiquidInteraction.rand.nextFloat() < this.chanceConsumeReactant2) {
                    chalice2.getTank().drain(this.reactant2, IFluidHandler.FluidAction.EXECUTE);
                }
                return true;
            }
        }
        if (contained1.containsFluid(this.reactant2) && contained2.containsFluid(this.reactant1)) {
            final FluidStack drained1 = chalice1.getTank().drain(this.reactant2, IFluidHandler.FluidAction.SIMULATE);
            final FluidStack drained2 = chalice2.getTank().drain(this.reactant1, IFluidHandler.FluidAction.SIMULATE);
            if (drained1.containsFluid(this.reactant2) && drained2.containsFluid(this.reactant1)) {
                if (LiquidInteraction.rand.nextFloat() < this.chanceConsumeReactant1) {
                    chalice2.getTank().drain(this.reactant1, IFluidHandler.FluidAction.EXECUTE);
                }
                if (LiquidInteraction.rand.nextFloat() < this.chanceConsumeReactant2) {
                    chalice1.getTank().drain(this.reactant2, IFluidHandler.FluidAction.EXECUTE);
                }
                return true;
            }
        }
        return false;
    }
    
    public FluidStack getReactant1() {
        return this.reactant1;
    }
    
    public FluidStack getReactant2() {
        return this.reactant2;
    }
    
    public InteractionResult getResult() {
        return this.result;
    }
    
    public int getWeight() {
        return this.weight;
    }
    
    @Nullable
    public static LiquidInteraction pickRecipe(final Collection<LiquidInteraction> recipes) {
        return MiscUtils.getWeightedRandomEntry(recipes, LiquidInteraction.rand, interaction -> interaction.weight);
    }
    
    public static LiquidInteraction read(final ResourceLocation recipeId, final JsonObject json) {
        final String fluidKey1 = JSONUtils.func_151200_h(json, "reactant1");
        final Fluid reactant1 = (Fluid)ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidKey1));
        if (reactant1 == null) {
            throw new JsonSyntaxException("Unknown fluid: " + fluidKey1);
        }
        final int amount1 = JSONUtils.func_151203_m(json, "reactant1Amount");
        CompoundTag tag1 = null;
        if (JSONUtils.func_151204_g(json, "reactant1Tag")) {
            final String jsonTag1 = JSONUtils.func_151200_h(json, "reactant1Tag");
            try {
                tag1 = JsonToNBT.func_180713_a(jsonTag1);
            }
            catch (final CommandSyntaxException e) {
                throw new JsonSyntaxException("Invalid Json: " + jsonTag1);
            }
        }
        final FluidStack r1 = new FluidStack(reactant1, amount1, tag1);
        final String fluidKey2 = JSONUtils.func_151200_h(json, "reactant2");
        final Fluid reactant2 = (Fluid)ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidKey2));
        if (reactant2 == null) {
            throw new JsonSyntaxException("Unknown fluid: " + fluidKey2);
        }
        final int amount2 = JSONUtils.func_151203_m(json, "reactant2Amount");
        CompoundTag tag2 = null;
        if (JSONUtils.func_151204_g(json, "reactant2Tag")) {
            final String jsonTag2 = JSONUtils.func_151200_h(json, "reactant2Tag");
            try {
                tag2 = JsonToNBT.func_180713_a(jsonTag2);
            }
            catch (final CommandSyntaxException e2) {
                throw new JsonSyntaxException("Invalid Json: " + jsonTag2);
            }
        }
        final FluidStack r2 = new FluidStack(reactant2, amount2, tag2);
        final float chance1 = JSONUtils.func_151217_k(json, "chanceConsumeReactant1");
        final float chance2 = JSONUtils.func_151217_k(json, "chanceConsumeReactant2");
        final int weight = JSONUtils.func_151203_m(json, "weight");
        final JsonObject ctResult = JSONUtils.func_152754_s(json, "result");
        final ResourceLocation id = new ResourceLocation(JSONUtils.func_151200_h(ctResult, "id"));
        final InteractionResult result = InteractionResultRegistry.create(id);
        if (result == null) {
            throw new JsonSyntaxException("Unknown result type: " + id.toString() + "; expected one of " + Strings.join((Iterable)InteractionResultRegistry.getKeysAsStrings(), ", "));
        }
        final JsonObject resultData = JSONUtils.func_152754_s(ctResult, "data");
        result.read(resultData);
        return new LiquidInteraction(recipeId, r1, chance1, r2, chance2, weight, result);
    }
    
    public final void write(final JsonObject object) {
        object.addProperty("reactant1", this.reactant1.getFluid().getRegistryName().toString());
        object.addProperty("reactant1Amount", (Number)this.reactant1.getAmount());
        if (this.reactant1.hasTag()) {
            object.addProperty("reactant1Tag", this.reactant1.getTag().toString());
        }
        object.addProperty("reactant2", this.reactant2.getFluid().getRegistryName().toString());
        object.addProperty("reactant2Amount", (Number)this.reactant2.getAmount());
        if (this.reactant2.hasTag()) {
            object.addProperty("reactant2Tag", this.reactant2.getTag().toString());
        }
        object.addProperty("chanceConsumeReactant1", (Number)this.chanceConsumeReactant1);
        object.addProperty("chanceConsumeReactant2", (Number)this.chanceConsumeReactant2);
        object.addProperty("weight", (Number)this.weight);
        final JsonObject ctResult = new JsonObject();
        ctResult.addProperty("id", this.result.getId().toString());
        final JsonObject resultObj = new JsonObject();
        this.result.write(resultObj);
        ctResult.add("data", (JsonElement)resultObj);
        object.add("result", (JsonElement)ctResult);
    }
    
    public static LiquidInteraction read(final ResourceLocation recipeId, final FriendlyByteBuf buffer) {
        final FluidStack reactant1 = ByteBufUtils.readFluidStack(buffer);
        final FluidStack reactant2 = ByteBufUtils.readFluidStack(buffer);
        final float chanceConsumeReactant1 = buffer.readFloat();
        final float chanceConsumeReactant2 = buffer.readFloat();
        final int weight = buffer.readInt();
        final ResourceLocation key = new ResourceLocation(ByteBufUtils.readString(buffer));
        final InteractionResult result = InteractionResultRegistry.create(key);
        if (result == null) {
            return null;
        }
        result.read(buffer);
        return new LiquidInteraction(recipeId, reactant1, chanceConsumeReactant1, reactant2, chanceConsumeReactant2, weight, result);
    }
    
    public final void write(final FriendlyByteBuf buffer) {
        ByteBufUtils.writeFluidStack(buffer, this.reactant1);
        ByteBufUtils.writeFluidStack(buffer, this.reactant2);
        buffer.writeFloat(this.chanceConsumeReactant1);
        buffer.writeFloat(this.chanceConsumeReactant2);
        buffer.writeInt(this.weight);
        ByteBufUtils.writeString(buffer, this.result.getId().toString());
        this.result.write(buffer);
    }
    
    @Override
    public CustomRecipeSerializer<?> getSerializer() {
        return RecipeSerializersAS.LIQUID_INTERACTION_SERIALIZER;
    }
    
    public RecipeType<?> func_222127_g() {
        return RecipeTypesAS.TYPE_LIQUID_INTERACTION.getType();
    }
    
    static {
        rand = new Random();
    }
}
