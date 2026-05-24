package hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin;

import java.util.function.BiConsumer;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.astralsorcery.common.constellation.ConstellationItem;
import java.util.function.Consumer;
import java.util.List;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nonnull;
import net.minecraft.world.item.ItemStack;
import com.google.gson.JsonSyntaxException;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import net.minecraft.util.JSONUtils;
import com.google.gson.JsonObject;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeGrid;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;

public class ConstellationItemRecipe extends SimpleAltarRecipe
{
    private static final String KEY_CONSTELLATION_ATTUNE = "attuned_constellation";
    private static final String KEY_CONSTELLATION_TRAIT = "trait_constellation";
    private IWeakConstellation attunedConstellation;
    private IMinorConstellation traitConstellation;
    
    public ConstellationItemRecipe(final ResourceLocation recipeId, final AltarType altarType, final int duration, final int starlightRequirement, final AltarRecipeGrid recipeGrid) {
        super(recipeId, altarType, duration, starlightRequirement, recipeGrid);
        this.attunedConstellation = null;
        this.traitConstellation = null;
    }
    
    public static ConstellationItemRecipe convertToThis(final SimpleAltarRecipe other) {
        return new ConstellationItemRecipe(other.func_199560_c(), other.getAltarType(), other.getDuration(), other.getStarlightRequirement(), other.getInputs());
    }
    
    @Override
    public void deserializeAdditionalJson(final JsonObject recipeObject) throws JsonSyntaxException {
        super.deserializeAdditionalJson(recipeObject);
        if (JSONUtils.func_151204_g(recipeObject, "attuned_constellation")) {
            final ResourceLocation cstName = new ResourceLocation(JSONUtils.func_151200_h(recipeObject, "attuned_constellation"));
            final IConstellation cst = (IConstellation)RegistriesAS.REGISTRY_CONSTELLATIONS.getValue(cstName);
            if (cst instanceof IWeakConstellation) {
                this.attunedConstellation = (IWeakConstellation)cst;
            }
        }
        if (JSONUtils.func_151204_g(recipeObject, "trait_constellation")) {
            final ResourceLocation cstName = new ResourceLocation(JSONUtils.func_151200_h(recipeObject, "trait_constellation"));
            final IConstellation cst = (IConstellation)RegistriesAS.REGISTRY_CONSTELLATIONS.getValue(cstName);
            if (cst instanceof IMinorConstellation) {
                this.setTraitConstellation((IMinorConstellation)cst);
                this.traitConstellation = (IMinorConstellation)cst;
            }
        }
    }
    
    @Override
    public void serializeAdditionalJson(final JsonObject recipeObject) {
        super.serializeAdditionalJson(recipeObject);
        if (this.getAttunedConstellation() != null) {
            recipeObject.addProperty("attuned_constellation", this.getAttunedConstellation().getRegistryName().toString());
        }
        if (this.getTraitConstellation() != null) {
            recipeObject.addProperty("trait_constellation", this.getTraitConstellation().getRegistryName().toString());
        }
    }
    
    public void setAttunedConstellation(final IWeakConstellation attunedConstellation) {
        this.attunedConstellation = attunedConstellation;
    }
    
    public void setTraitConstellation(final IMinorConstellation traitConstellation) {
        this.traitConstellation = traitConstellation;
    }
    
    public IWeakConstellation getAttunedConstellation() {
        return this.attunedConstellation;
    }
    
    public IMinorConstellation getTraitConstellation() {
        return this.traitConstellation;
    }
    
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    @Override
    public ItemStack getOutputForRender(final Iterable<ItemStack> inventoryContents) {
        final ItemStack out = super.getOutputForRender(inventoryContents);
        this.setConstellations(out);
        return out;
    }
    
    @Nonnull
    @Override
    public List<ItemStack> getOutputs(final TileAltar altar) {
        final List<ItemStack> out = super.getOutputs(altar);
        out.forEach(this::setConstellations);
        return out;
    }
    
    private void setConstellations(final ItemStack out) {
        if (out.getItem() instanceof ConstellationItem) {
            if (this.getAttunedConstellation() != null) {
                ((ConstellationItem)out.getItem()).setAttunedConstellation(out, this.getAttunedConstellation());
            }
            if (this.getTraitConstellation() != null) {
                ((ConstellationItem)out.getItem()).setTraitConstellation(out, this.getTraitConstellation());
            }
        }
    }
    
    @Override
    public void writeRecipeSync(final FriendlyByteBuf buf) {
        super.writeRecipeSync(buf);
        ByteBufUtils.writeOptional(buf, this.getAttunedConstellation(), (BiConsumer<FriendlyByteBuf, Object>)ByteBufUtils::writeRegistryEntry);
        ByteBufUtils.writeOptional(buf, this.getTraitConstellation(), (BiConsumer<FriendlyByteBuf, Object>)ByteBufUtils::writeRegistryEntry);
    }
    
    @Override
    public void readRecipeSync(final FriendlyByteBuf buf) {
        super.readRecipeSync(buf);
        this.attunedConstellation = ByteBufUtils.readOptional(buf, ByteBufUtils::readRegistryEntry);
        this.traitConstellation = ByteBufUtils.readOptional(buf, ByteBufUtils::readRegistryEntry);
    }
}
