package hellfirepvp.astralsorcery.common.crafting.helper.ingredient;

import com.google.gson.JsonArray;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import hellfirepvp.astralsorcery.common.lib.IngredientSerializersAS;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import javax.annotation.Nullable;
import java.util.Comparator;
import it.unimi.dsi.fastutil.ints.IntComparators;
import net.minecraft.world.item.crafting.RecipeItemHelper;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.Iterator;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraft.core.NonNullList;
import java.util.Arrays;
import java.util.stream.Stream;
import net.minecraft.world.level.item.ItemStack;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraftforge.fluids.FluidStack;
import java.util.List;
import net.minecraft.world.level.item.crafting.Ingredient;

public class FluidIngredient extends Ingredient
{
    private final List<FluidStack> fluids;
    private IntList itemIds;
    private ItemStack[] itemArray;
    private int cacheItemStacks;
    private int cacheItemIds;
    
    public FluidIngredient(final List<FluidStack> fluidStacks) {
        super((Stream)Stream.empty());
        this.itemIds = null;
        this.itemArray = null;
        this.cacheItemStacks = -1;
        this.cacheItemIds = -1;
        this.fluids = fluidStacks;
    }
    
    public FluidIngredient(final FluidStack... fluidStacks) {
        super((Stream)Stream.empty());
        this.itemIds = null;
        this.itemArray = null;
        this.cacheItemStacks = -1;
        this.cacheItemIds = -1;
        this.fluids = Arrays.asList(fluidStacks);
    }
    
    public List<FluidStack> getFluids() {
        return this.fluids;
    }
    
    public ItemStack[] func_193365_a() {
        if (this.itemArray == null || this.cacheItemStacks != this.fluids.size()) {
            final NonNullList<ItemStack> lst = (NonNullList<ItemStack>)NonNullList.func_191196_a();
            for (final FluidStack fluid : this.fluids) {
                lst.add((Object)FluidUtil.getFilledBucket(fluid));
            }
            this.itemArray = (ItemStack[])lst.toArray((Object[])new ItemStack[lst.size()]);
            this.cacheItemStacks = this.fluids.size();
        }
        return this.itemArray;
    }
    
    public IntList func_194139_b() {
        if (this.itemIds == null || this.cacheItemIds != this.fluids.size()) {
            this.itemIds = (IntList)new IntArrayList(this.fluids.size());
            for (final FluidStack fluid : this.fluids) {
                final ItemStack bucketFluid = FluidUtil.getFilledBucket(fluid);
                this.itemIds.add(RecipeItemHelper.func_194113_b(bucketFluid));
            }
            this.itemIds.sort((Comparator)IntComparators.NATURAL_COMPARATOR);
            this.cacheItemIds = this.fluids.size();
        }
        return this.itemIds;
    }
    
    public boolean test(@Nullable final ItemStack input) {
        if (input == null) {
            return false;
        }
        final FluidStack contained = FluidUtil.getFluidContained(input).orElse(FluidStack.EMPTY);
        if (contained.isEmpty() || contained.getFluid() == null || contained.getAmount() <= 0) {
            return false;
        }
        for (final FluidStack target : this.fluids) {
            if (contained.containsFluid(target)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean func_203189_d() {
        return this.fluids.isEmpty();
    }
    
    protected void invalidate() {
        super.invalidate();
        this.itemIds = null;
        this.itemArray = null;
    }
    
    public boolean isSimple() {
        return false;
    }
    
    public JsonElement func_200304_c() {
        final JsonObject object = new JsonObject();
        object.addProperty("type", CraftingHelper.getID((IIngredientSerializer)IngredientSerializersAS.FLUID_SERIALIZER).toString());
        final JsonArray array = new JsonArray();
        for (final FluidStack stack : this.fluids) {
            final JsonObject fluidStackObject = new JsonObject();
            fluidStackObject.addProperty("fluid", stack.getFluid().getRegistryName().toString());
            fluidStackObject.addProperty("amount", (Number)stack.getAmount());
            array.add((JsonElement)fluidStackObject);
        }
        object.add("fluid", (JsonElement)array);
        return (JsonElement)object;
    }
    
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return IngredientSerializersAS.FLUID_SERIALIZER;
    }
}
