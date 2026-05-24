package hellfirepvp.astralsorcery.common.crafting.recipe.interaction;

import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;

public abstract class InteractionResult
{
    private final ResourceLocation id;
    
    protected InteractionResult(final ResourceLocation id) {
        this.id = id;
    }
    
    public final ResourceLocation getId() {
        return this.id;
    }
    
    public abstract void doResult(final World p0, final Vector3 p1);
    
    public abstract void read(final JsonObject p0) throws JsonParseException;
    
    public abstract void write(final JsonObject p0);
    
    public abstract void read(final FriendlyByteBuf p0);
    
    public abstract void write(final FriendlyByteBuf p0);
}
