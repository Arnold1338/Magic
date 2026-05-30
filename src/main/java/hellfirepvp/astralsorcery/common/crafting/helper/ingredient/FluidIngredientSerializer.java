package hellfirepvp.astralsorcery.common.crafting.helper.ingredient;

import net.minecraft.world.item.crafting.Ingredient;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonElement;
import java.util.List;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;
import net.minecraftforge.fluids.FluidStack;
import java.util.ArrayList;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonObject;
import net.minecraftforge.common.crafting.IIngredientSerializer;

public class FluidIngredientSerializer implements IIngredientSerializer<FluidIngredient>
{
    public FluidIngredient parse(final JsonObject json) {
        if (!json.has("fluid")) {
            throw new JsonSyntaxException("Expected an array at 'fluid' or a single fluid defined at key 'fluid'.");
        }
        final List<FluidStack> foundFluids = new ArrayList<FluidStack>();
        final JsonElement element = json.get("fluid");
        if (element.isJsonArray()) {
            element.getAsJsonArray().forEach(e -> {
                if (e.isJsonObject()) {
                    final JsonObject object = e.getAsJsonObject();
                    final ResourceLocation key2 = new ResourceLocation(JSONUtils.func_151200_h(object, "fluid"));
                    if (!ForgeRegistries.FLUIDS.containsKey(key2)) {
                        throw new JsonSyntaxException("Unknown fluid '" + key2 + "'");
                    }
                    else {
                        int amount2 = 1000;
                        if (object.has("amount")) {
                            amount2 = JSONUtils.func_151203_m(object, "amount");
                        }
                        final Fluid fluid2 = (Fluid)ForgeRegistries.FLUIDS.getValue(key2);
                        foundFluids.add(new FluidStack(fluid2, amount2));
                    }
                }
                else if (e.isJsonPrimitive()) {
                    final ResourceLocation key3 = new ResourceLocation(JSONUtils.func_151206_a(element, "fluid"));
                    if (!ForgeRegistries.FLUIDS.containsKey(key3)) {
                        throw new JsonSyntaxException("Unknown fluid '" + key3 + "'");
                    }
                    else {
                        final Fluid fluid3 = (Fluid)ForgeRegistries.FLUIDS.getValue(key3);
                        foundFluids.add(new FluidStack(fluid3, 1000));
                    }
                }
                else {
                    throw new JsonSyntaxException("Value at key 'fluid' has to be a fluid name or an array of fluid names or objects containing 'fluid'.");
                }

            });
        }
        else {
            if (!element.isJsonPrimitive()) {
                throw new JsonSyntaxException("Value at key 'fluid' has to be a fluid name or an array of fluid names or objects containing 'fluid'.");
            }
            final ResourceLocation key = new ResourceLocation(JSONUtils.func_151206_a(element, "fluid"));
            if (!ForgeRegistries.FLUIDS.containsKey(key)) {
                throw new JsonSyntaxException("Unknown fluid '" + key + "'");
            }
            int amount = 1000;
            if (json.has("amount")) {
                amount = JSONUtils.func_151203_m(json, "amount");
            }
            final Fluid fluid = (Fluid)ForgeRegistries.FLUIDS.getValue(key);
            foundFluids.add(new FluidStack(fluid, amount));
        }
        return new FluidIngredient(foundFluids);
    }
    
    public FluidIngredient parse(final FriendlyByteBuf buffer) {
        return new FluidIngredient(ByteBufUtils.readList(buffer, ByteBufUtils::readFluidStack));
    }
    
    public void write(final FriendlyByteBuf buffer, final FluidIngredient ingredient) {
        ByteBufUtils.writeCollection(buffer, ingredient.getFluids(), ByteBufUtils::writeFluidStack);
    }
}
