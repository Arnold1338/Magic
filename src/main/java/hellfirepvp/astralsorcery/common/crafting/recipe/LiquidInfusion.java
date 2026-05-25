package hellfirepvp.astralsorcery.common.crafting.recipe;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.crafting.IRecipeSerializer;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeSerializer;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import net.minecraft.world.item.crafting.RecipeType;
import com.google.gson.JsonElement;
import hellfirepvp.astralsorcery.common.util.data.JsonHelper;
import com.google.gson.JsonObject;
import net.minecraftforge.registries.IForgeRegistryEntry;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.MapStream;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.tile.TileInfuser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomMatcherRecipe;

public class LiquidInfusion extends CustomMatcherRecipe implements GatedRecipe.Progression
{
    private final int craftingTickTime;
    private final Fluid liquidInput;
    private final Ingredient itemInput;
    private final ItemStack output;
    private final float consumptionChance;
    private final boolean consumeMultipleFluids;
    private final boolean acceptChaliceInput;
    private final boolean copyNBTToOutputs;
    
    public LiquidInfusion(final ResourceLocation recipeId, final int craftingTickTime, final Fluid liquidInput, final Ingredient itemInput, final ItemStack itemOutput, final float consumptionChance, final boolean consumeMultipleFluids, final boolean acceptChaliceInput, final boolean copyNBTToOutputs) {
        super(recipeId);
        this.craftingTickTime = craftingTickTime;
        this.liquidInput = liquidInput;
        this.itemInput = itemInput;
        this.output = itemOutput;
        this.consumptionChance = consumptionChance;
        this.consumeMultipleFluids = consumeMultipleFluids;
        this.acceptChaliceInput = acceptChaliceInput;
        this.copyNBTToOutputs = copyNBTToOutputs;
    }
    
    public boolean matches(final TileInfuser infuser, final Player crafter, final LogicalSide side) {
        if (crafter == null) {
            return false;
        }
        boolean hasProgress;
        if (side.isClient()) {
            hasProgress = this.hasProgressionClient();
        }
        else {
            hasProgress = this.hasProgressionServer(crafter);
        }
        if (!hasProgress) {
            return false;
        }
        final boolean hasFluidInputs = MapStream.of(infuser.getLiquids()).mapKey(pos -> pos.func_177971_a((Vector3i)infuser.getBlockState())).allMatch(tpl -> this.liquidInput.equals(tpl.getB()));
        return hasFluidInputs && this.itemInput.test(infuser.getItemInput());
    }
    
    @Nonnull
    @Override
    public ResearchProgression getRequiredProgression() {
        return ResearchProgression.CONSTELLATION;
    }
    
    public int getCraftingTickTime() {
        return this.craftingTickTime;
    }
    
    @Nonnull
    public Fluid getLiquidInput() {
        return this.liquidInput;
    }
    
    @Nonnull
    public Ingredient getItemInput() {
        return this.itemInput;
    }
    
    public void onRecipeCompletion(final TileInfuser infuser) {
    }
    
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public ItemStack getOutputForRender(final Iterable<ItemStack> inventoryContents) {
        return ItemUtils.copyStackWithSize(this.output, this.output.getCount());
    }
    
    @Nonnull
    public ItemStack getOutput(final ItemStack itemInput) {
        return ItemUtils.copyStackWithSize(this.output, this.output.getCount());
    }
    
    public float getConsumptionChance() {
        return Mth.canEnchant(this.consumptionChance, 0.0f, 1.0f);
    }
    
    public boolean doesConsumeMultipleFluids() {
        return this.consumeMultipleFluids;
    }
    
    public boolean acceptsChaliceInput() {
        return this.acceptChaliceInput;
    }
    
    public boolean doesCopyNBTToOutputs() {
        return this.copyNBTToOutputs;
    }
    
    public static LiquidInfusion read(final ResourceLocation recipeId, final FriendlyByteBuf buffer) {
        final Fluid fluidIn = ByteBufUtils.readRegistryEntry(buffer);
        final Ingredient itemIn = Ingredient.func_199566_b(buffer);
        final ItemStack output = ByteBufUtils.readItemStack(buffer);
        final float consumptionChance = buffer.readFloat();
        final int duration = buffer.readInt();
        final boolean consumeMultiple = buffer.readBoolean();
        final boolean acceptChalice = buffer.readBoolean();
        final boolean copyNBTToOutputs = buffer.readBoolean();
        return new LiquidInfusion(recipeId, duration, fluidIn, itemIn, output, consumptionChance, consumeMultiple, acceptChalice, copyNBTToOutputs);
    }
    
    public final void write(final FriendlyByteBuf buffer) {
        ByteBufUtils.writeRegistryEntry(buffer, (net.minecraftforge.registries.IForgeRegistryEntry<Object>)this.getLiquidInput());
        this.getItemInput().func_199564_a(buffer);
        ByteBufUtils.writeItemStack(buffer, this.output);
        buffer.writeFloat(this.getConsumptionChance());
        buffer.writeInt(this.getCraftingTickTime());
        buffer.writeBoolean(this.doesConsumeMultipleFluids());
        buffer.writeBoolean(this.acceptsChaliceInput());
        buffer.writeBoolean(this.doesCopyNBTToOutputs());
    }
    
    public void write(final JsonObject object) {
        object.addProperty("fluidInput", this.getLiquidInput().getRegistryName().toString());
        object.add("input", this.getItemInput().func_200304_c());
        object.add("output", (JsonElement)JsonHelper.serializeItemStack(this.output));
        object.addProperty("consumptionChance", (Number)this.getConsumptionChance());
        object.addProperty("duration", (Number)this.getCraftingTickTime());
        object.addProperty("consumeMultipleFluids", Boolean.valueOf(this.doesConsumeMultipleFluids()));
        object.addProperty("acceptChaliceInput", Boolean.valueOf(this.acceptsChaliceInput()));
        object.addProperty("copyNBTToOutputs", Boolean.valueOf(this.doesCopyNBTToOutputs()));
    }
    
    public RecipeType<?> func_222127_g() {
        return RecipeTypesAS.TYPE_INFUSION.getType();
    }
    
    @Override
    public CustomRecipeSerializer<?> getSerializer() {
        return RecipeSerializersAS.LIQUID_INFUSION_SERIALIZER;
    }
}
