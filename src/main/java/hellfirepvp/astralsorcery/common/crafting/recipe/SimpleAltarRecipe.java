package hellfirepvp.astralsorcery.common.crafting.recipe;

import net.minecraft.world.item.crafting.IRecipeSerializer;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeSerializer;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import net.minecraft.world.item.crafting.RecipeType;
import com.google.gson.JsonElement;
import hellfirepvp.astralsorcery.common.util.data.JsonHelper;
import com.google.gson.JsonArray;
import java.util.function.BiConsumer;
import java.util.Iterator;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeTypeHandler;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonObject;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.item.crafting.Ingredient;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import com.google.common.collect.Iterables;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import java.util.Collection;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import hellfirepvp.astralsorcery.common.lib.AltarRecipeEffectsAS;
import java.util.HashSet;
import java.util.LinkedList;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.AltarRecipeEffect;
import java.util.Set;
import hellfirepvp.astralsorcery.common.crafting.helper.WrappedIngredient;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeGrid;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomMatcherRecipe;

public class SimpleAltarRecipe extends CustomMatcherRecipe implements GatedRecipe.Progression
{
    private final AltarType altarType;
    private int duration;
    private int starlightRequirement;
    private AltarRecipeGrid altarRecipeGrid;
    private final List<ItemStack> outputs;
    private ResourceLocation customRecipeType;
    private IConstellation focusConstellation;
    private final List<WrappedIngredient> relayInputs;
    private final Set<AltarRecipeEffect> craftingEffects;
    
    public SimpleAltarRecipe(final ResourceLocation recipeId, final AltarType altarType) {
        this(recipeId, altarType, altarType.getDefaultAltarCraftingDuration());
    }
    
    public SimpleAltarRecipe(final ResourceLocation recipeId, final AltarType altarType, final int duration) {
        this(recipeId, altarType, duration, 0);
    }
    
    public SimpleAltarRecipe(final ResourceLocation recipeId, final AltarType altarType, final int duration, final int starlightRequirement) {
        this(recipeId, altarType, duration, starlightRequirement, AltarRecipeGrid.EMPTY);
    }
    
    public SimpleAltarRecipe(final ResourceLocation recipeId, final AltarType altarType, final int duration, final int starlightRequirement, final AltarRecipeGrid grid) {
        super(recipeId);
        this.outputs = new LinkedList<ItemStack>();
        this.customRecipeType = null;
        this.focusConstellation = null;
        this.relayInputs = new LinkedList<WrappedIngredient>();
        this.craftingEffects = new HashSet<AltarRecipeEffect>();
        this.altarType = altarType;
        this.duration = duration;
        this.starlightRequirement = starlightRequirement;
        this.altarRecipeGrid = grid;
        this.addDefaultEffects();
    }
    
    private void addDefaultEffects() {
        switch (this.getAltarType()) {
            case RADIANCE: {
                this.addAltarEffect(AltarRecipeEffectsAS.BUILTIN_TRAIT_FOCUS_CIRCLE);
                this.addAltarEffect(AltarRecipeEffectsAS.BUILTIN_TRAIT_RELAY_HIGHLIGHT);
            }
            case CONSTELLATION: {
                this.addAltarEffect(AltarRecipeEffectsAS.BUILTIN_CONSTELLATION_LINES);
                this.addAltarEffect(AltarRecipeEffectsAS.BUILTIN_CONSTELLATION_FINISH);
                this.addAltarEffect(AltarRecipeEffectsAS.ALTAR_DEFAULT_SPARKLE);
            }
            case ATTUNEMENT: {
                this.addAltarEffect(AltarRecipeEffectsAS.BUILTIN_ATTUNEMENT_SPARKLE);
            }
            case DISCOVERY: {
                this.addAltarEffect(AltarRecipeEffectsAS.BUILTIN_DISCOVERY_CENTRAL_BEAM);
                break;
            }
        }
    }
    
    @Nonnull
    @Override
    public ResearchProgression getRequiredProgression() {
        return this.getAltarType().getAssociatedTier();
    }
    
    @Nullable
    public ResourceLocation getCustomRecipeType() {
        return this.customRecipeType;
    }
    
    public void setCustomRecipeType(@Nullable final ResourceLocation customRecipeType) {
        this.customRecipeType = customRecipeType;
    }
    
    @Nullable
    public IConstellation getFocusConstellation() {
        return this.focusConstellation;
    }
    
    public void setInputs(final AltarRecipeGrid inputGrid) {
        this.altarRecipeGrid = inputGrid;
    }
    
    public AltarRecipeGrid getInputs() {
        return this.altarRecipeGrid;
    }
    
    public Collection<AltarRecipeEffect> getCraftingEffects() {
        return this.craftingEffects;
    }
    
    public void setDuration(final int duration) {
        this.duration = duration;
    }
    
    public int getDuration() {
        return this.duration;
    }
    
    public void setStarlightRequirement(final int starlightRequirement) {
        this.starlightRequirement = starlightRequirement;
    }
    
    public int getStarlightRequirement() {
        return this.starlightRequirement;
    }
    
    public void onRecipeCompletion(final TileAltar altar, final ActiveSimpleAltarRecipe activeRecipe) {
    }
    
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public ItemStack getOutputForRender(final Iterable<ItemStack> inventoryContents) {
        final ItemStack first = (ItemStack)Iterables.getFirst((Iterable)this.outputs, (Object)ItemStack.EMPTY);
        return ItemUtils.copyStackWithSize(first, first.getCount());
    }
    
    @Nonnull
    public List<ItemStack> getOutputs(final TileAltar altar) {
        return MiscUtils.transformList(this.outputs, stack -> ItemUtils.copyStackWithSize(stack, stack.getCount()));
    }
    
    public void setFocusConstellation(final IConstellation focusConstellation) {
        this.focusConstellation = focusConstellation;
    }
    
    @Nonnull
    public List<WrappedIngredient> getRelayInputs() {
        return this.relayInputs;
    }
    
    public boolean addRelayInput(final Ingredient i) {
        return this.relayInputs.add(new WrappedIngredient(i));
    }
    
    public void addAltarEffect(final AltarRecipeEffect effect) {
        this.craftingEffects.add(effect);
    }
    
    public void addOutput(final ItemStack output) {
        this.outputs.add(ItemUtils.copyStackWithSize(output, output.getCount()));
    }
    
    public boolean matches(final LogicalSide side, final Player crafter, final TileAltar altar, final boolean ignoreStarlightRequirement) {
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
        if (!this.getAltarType().isThisLEThan(altar.getAltarType())) {
            return false;
        }
        if (this.getFocusConstellation() != null) {
            final IConstellation focus = altar.getFocusedConstellation();
            if (focus == null || !focus.equals(this.getFocusConstellation())) {
                return false;
            }
        }
        return (ignoreStarlightRequirement || altar.getStoredStarlight() >= this.getStarlightRequirement()) && this.altarRecipeGrid.containsInputs((IItemHandlerModifiable)altar.getInventory(), true);
    }
    
    public void deserializeAdditionalJson(final JsonObject recipeObject) throws JsonSyntaxException {
    }
    
    public void serializeAdditionalJson(final JsonObject recipeObject) {
    }
    
    public void writeRecipeSync(final FriendlyByteBuf buf) {
    }
    
    public void readRecipeSync(final FriendlyByteBuf buf) {
    }
    
    public static SimpleAltarRecipe read(final ResourceLocation recipeId, final FriendlyByteBuf buffer) {
        final AltarType type = ByteBufUtils.readEnumValue(buffer, AltarType.class);
        final int duration = buffer.readInt();
        final int starlight = buffer.readInt();
        final AltarRecipeGrid grid = AltarRecipeGrid.read(buffer);
        SimpleAltarRecipe recipe = new SimpleAltarRecipe(recipeId, type, duration, starlight, grid);
        final ResourceLocation customType = ByteBufUtils.readOptional(buffer, ByteBufUtils::readResourceLocation);
        if (customType != null) {
            recipe = AltarRecipeTypeHandler.convert(recipe, customType);
            recipe.setCustomRecipeType(customType);
        }
        final List<ItemStack> outputs = ByteBufUtils.readList(buffer, ByteBufUtils::readItemStack);
        outputs.forEach(recipe::addOutput);
        recipe.setFocusConstellation(ByteBufUtils.readOptional(buffer, ByteBufUtils::readRegistryEntry));
        ByteBufUtils.readList(buffer, Ingredient::func_199566_b).forEach(recipe::addRelayInput);
        final List<AltarRecipeEffect> effects = ByteBufUtils.readList(buffer, ByteBufUtils::readRegistryEntry);
        for (final AltarRecipeEffect effect : effects) {
            recipe.addAltarEffect(effect);
        }
        recipe.readRecipeSync(buffer);
        return recipe;
    }
    
    public final void write(final FriendlyByteBuf buffer) {
        ByteBufUtils.writeEnumValue(buffer, this.getAltarType());
        buffer.writeInt(this.getDuration());
        buffer.writeInt(this.getStarlightRequirement());
        this.getInputs().write(buffer);
        ByteBufUtils.writeOptional(buffer, this.getCustomRecipeType(), ByteBufUtils::writeResourceLocation);
        ByteBufUtils.writeCollection(buffer, this.outputs, ByteBufUtils::writeItemStack);
        ByteBufUtils.writeOptional(buffer, this.getFocusConstellation(), (BiConsumer<FriendlyByteBuf, Object>)ByteBufUtils::writeRegistryEntry);
        ByteBufUtils.writeCollection(buffer, this.getRelayInputs(), (buf, ingredient) -> ingredient.getIngredient().func_199564_a(buf));
        ByteBufUtils.writeCollection(buffer, (Collection<Object>)this.getCraftingEffects(), (BiConsumer<FriendlyByteBuf, Object>)ByteBufUtils::writeRegistryEntry);
        this.writeRecipeSync(buffer);
    }
    
    public final void write(final JsonObject object) {
        object.addProperty("altar_type", (Number)this.getAltarType().ordinal());
        object.addProperty("duration", (Number)this.getDuration());
        object.addProperty("starlight", (Number)this.getStarlightRequirement());
        this.getInputs().serialize(object);
        if (this.getCustomRecipeType() != null) {
            object.addProperty("recipe_class", this.getCustomRecipeType().toString());
        }
        final JsonArray outputs = new JsonArray();
        for (final ItemStack output : this.outputs) {
            outputs.add((JsonElement)JsonHelper.serializeItemStack(output));
        }
        object.add("output", (JsonElement)outputs);
        final JsonObject options = new JsonObject();
        this.serializeAdditionalJson(options);
        if (!options.entrySet().isEmpty()) {
            object.add("options", (JsonElement)options);
        }
        if (this.getFocusConstellation() != null) {
            object.addProperty("focus_constellation", this.getFocusConstellation().getRegistryName().toString());
        }
        if (!this.getRelayInputs().isEmpty()) {
            final JsonArray inputs = new JsonArray();
            for (final WrappedIngredient traitInput : this.getRelayInputs()) {
                inputs.add(traitInput.getIngredient().func_200304_c());
            }
            object.add("relay_inputs", (JsonElement)inputs);
        }
        if (!this.getCraftingEffects().isEmpty()) {
            final JsonArray effects = new JsonArray();
            for (final AltarRecipeEffect effect : this.getCraftingEffects()) {
                effects.add(effect.getRegistryName().toString());
            }
            object.add("effects", (JsonElement)effects);
        }
    }
    
    public AltarType getAltarType() {
        return this.altarType;
    }
    
    public RecipeType<?> func_222127_g() {
        return RecipeTypesAS.TYPE_ALTAR.getType();
    }
    
    @Override
    public CustomRecipeSerializer<?> getSerializer() {
        return RecipeSerializersAS.ALTAR_RECIPE_SERIALIZER;
    }
}
