package hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin;

import java.util.function.BiConsumer;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.astralsorcery.common.constellation.ConstellationBaseItem;
import java.util.function.Consumer;
import java.util.List;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nonnull;
import net.minecraft.world.item.ItemStack;
import com.google.gson.JsonSyntaxException;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import net.minecraft.util.JSONUtils;
import com.google.gson.JsonObject;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeGrid;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;

public class ConstellationBaseItemRecipe extends SimpleAltarRecipe
{
    private static final String KEY_CONSTELLATION = "constellation";
    private IConstellation constellation;
    
    public ConstellationBaseItemRecipe(final ResourceLocation recipeId, final AltarType altarType, final int duration, final int starlightRequirement, final AltarRecipeGrid recipeGrid) {
        super(recipeId, altarType, duration, starlightRequirement, recipeGrid);
        this.constellation = null;
    }
    
    public static ConstellationBaseItemRecipe convertToThis(final SimpleAltarRecipe other) {
        return new ConstellationBaseItemRecipe(other.func_199560_c(), other.getAltarType(), other.getDuration(), other.getStarlightRequirement(), other.getInputs());
    }
    
    @Override
    public void deserializeAdditionalJson(final JsonObject recipeObject) throws JsonSyntaxException {
        super.deserializeAdditionalJson(recipeObject);
        if (JSONUtils.func_151204_g(recipeObject, "constellation")) {
            final ResourceLocation cstName = new ResourceLocation(JSONUtils.func_151200_h(recipeObject, "constellation"));
            final IConstellation cst = (IConstellation)RegistriesAS.REGISTRY_CONSTELLATIONS.getValue(cstName);
            if (cst != null) {
                this.setConstellation(cst);
            }
        }
    }
    
    @Override
    public void serializeAdditionalJson(final JsonObject recipeObject) {
        super.serializeAdditionalJson(recipeObject);
        if (this.getConstellation() != null) {
            recipeObject.addProperty("constellation", this.getConstellation().getRegistryName().toString());
        }
    }
    
    public void setConstellation(final IConstellation constellation) {
        this.constellation = constellation;
    }
    
    public IConstellation getConstellation() {
        return this.constellation;
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
        if (out.getItem() instanceof ConstellationBaseItem && this.getConstellation() != null) {
            ((ConstellationBaseItem)out.getItem()).setConstellation(out, this.getConstellation());
        }
    }
    
    @Override
    public void writeRecipeSync(final FriendlyByteBuf buf) {
        super.writeRecipeSync(buf);
        ByteBufUtils.writeOptional(buf, this.getConstellation(), (BiConsumer<FriendlyByteBuf, Object>)ByteBufUtils::writeRegistryEntry);
    }
    
    @Override
    public void readRecipeSync(final FriendlyByteBuf buf) {
        super.readRecipeSync(buf);
        this.setConstellation(ByteBufUtils.readOptional(buf, ByteBufUtils::readRegistryEntry));
    }
}
