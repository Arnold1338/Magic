package hellfirepvp.astralsorcery.common.crafting.helper;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class CustomRecipeSerializer<T extends CustomMatcherRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T>
{
    public CustomRecipeSerializer(final ResourceLocation name) {
        this.setRegistryName(name);
    }
    
    public abstract void write(final JsonObject p0, final T p1);
}
