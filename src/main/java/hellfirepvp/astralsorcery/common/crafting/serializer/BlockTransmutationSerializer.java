package hellfirepvp.astralsorcery.common.crafting.serializer;

import net.minecraft.world.item.crafting.Recipe;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomMatcherRecipe;
import java.util.function.BiConsumer;
import java.util.Collection;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.util.block.BlockStateHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import hellfirepvp.astralsorcery.common.util.data.JsonHelper;
import hellfirepvp.astralsorcery.common.util.block.BlockMatchInformation;
import java.util.ArrayList;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import hellfirepvp.astralsorcery.common.crafting.recipe.BlockTransmutation;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeSerializer;

public class BlockTransmutationSerializer extends CustomRecipeSerializer<BlockTransmutation>
{
    public BlockTransmutationSerializer() {
        super(RecipeSerializersAS.BLOCK_TRANSMUTATION);
    }
    
    public BlockTransmutation read(final ResourceLocation recipeId, final JsonObject json) {
        final List<BlockMatchInformation> matchInformation = new ArrayList<BlockMatchInformation>();
        JsonHelper.parseMultipleJsonObjects(json, "input", object -> matchInformation.add(BlockMatchInformation.read(object)));
        if (matchInformation.isEmpty()) {
            throw new IllegalArgumentException("A block transmutation has to have at least 1 input!");
        }
        for (final BlockMatchInformation info : matchInformation) {
            if (!info.isValid()) {
                throw new JsonSyntaxException("Block transmutation must not convert an air-block into something!");
            }
        }
        final BlockState output = BlockStateHelper.deserializeObject(JSONUtils.func_152754_s(json, "output"));
        ItemStack outputDisplay = new ItemStack((ItemLike)output.getBlock());
        if (JSONUtils.func_151204_g(json, "display")) {
            outputDisplay = JsonHelper.getItemStack(json, "display");
        }
        final float starlight = JSONUtils.func_151217_k(json, "starlight");
        IWeakConstellation matchConstellation = null;
        if (json.has("constellation")) {
            final ResourceLocation cstKey = new ResourceLocation(JSONUtils.func_151200_h(json, "constellation"));
            final IConstellation cst = (IConstellation)RegistriesAS.REGISTRY_CONSTELLATIONS.getValue(cstKey);
            if (cst == null) {
                throw new JsonSyntaxException(String.format("Unknown constellation %s!", cstKey));
            }
            if (!(cst instanceof IWeakConstellation)) {
                throw new JsonSyntaxException(String.format("Constellation %s has to be either a major or dim constellation!", cstKey));
            }
            matchConstellation = (IWeakConstellation)cst;
        }
        final BlockTransmutation tr = new BlockTransmutation(recipeId, output, starlight, matchConstellation);
        matchInformation.forEach(tr::addInputOption);
        tr.setOutputDisplay(outputDisplay);
        return tr;
    }
    
    @Nullable
    public BlockTransmutation read(final ResourceLocation recipeId, final FriendlyByteBuf buffer) {
        final List<BlockMatchInformation> matchInformation = ByteBufUtils.readList(buffer, BlockMatchInformation::read);
        final BlockState output = ByteBufUtils.readBlockState(buffer);
        final ItemStack display = ByteBufUtils.readItemStack(buffer);
        final double starlight = buffer.readDouble();
        final IWeakConstellation cst = ByteBufUtils.readOptional(buffer, ByteBufUtils::readRegistryEntry);
        final BlockTransmutation tr = new BlockTransmutation(recipeId, output, starlight, cst);
        matchInformation.forEach(tr::addInputOption);
        tr.setOutputDisplay(display);
        return tr;
    }
    
    @Override
    public void write(final JsonObject object, final BlockTransmutation recipe) {
        final JsonArray inputs = new JsonArray();
        for (final BlockMatchInformation info : recipe.getInputOptions()) {
            inputs.add((JsonElement)info.serializeJson());
        }
        object.add("input", (JsonElement)inputs);
        object.add("output", (JsonElement)BlockStateHelper.serializeObject(recipe.getOutput(), true));
        object.add("display", (JsonElement)JsonHelper.serializeItemStack(recipe.getOutputDisplay()));
        object.addProperty("starlight", (Number)recipe.getStarlightRequired());
        if (recipe.getRequiredConstellation() != null) {
            object.addProperty("constellation", recipe.getRequiredConstellation().getRegistryName().toString());
        }
    }
    
    public void write(final FriendlyByteBuf buffer, final BlockTransmutation recipe) {
        ByteBufUtils.writeCollection(buffer, recipe.getInputOptions(), (buf, match) -> match.serialize(buf));
        ByteBufUtils.writeBlockState(buffer, recipe.getOutput());
        ByteBufUtils.writeItemStack(buffer, recipe.getOutputDisplay());
        buffer.writeDouble(recipe.getStarlightRequired());
        ByteBufUtils.writeOptional(buffer, recipe.getRequiredConstellation(), (BiConsumer<FriendlyByteBuf, Object>)ByteBufUtils::writeRegistryEntry);
    }
}
