package hellfirepvp.astralsorcery.common.crafting.recipe.interaction;

import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import hellfirepvp.astralsorcery.common.util.data.JsonHelper;
import com.google.gson.JsonObject;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;

public class ResultDropItem extends InteractionResult
{
    private ItemStack output;
    
    ResultDropItem() {
        super(InteractionResultRegistry.ID_DROP_ITEM);
        this.output = ItemStack.EMPTY;
    }
    
    public static ResultDropItem dropItem(final ItemStack output) {
        final ResultDropItem drop = new ResultDropItem();
        drop.output = output.copy();
        return drop;
    }
    
    public ItemStack getOutput() {
        return this.output.copy();
    }
    
    @Override
    public void doResult(final World world, final Vector3 at) {
        ItemUtils.dropItemNaturally(world, at.getX(), at.getY(), at.getZ(), this.output.copy());
    }
    
    @Override
    public void read(final JsonObject json) throws JsonParseException {
        this.output = JsonHelper.getItemStack(json, "output");
        if (this.output.isEmpty()) {
            throw new JsonParseException("Output stack must not be empty!");
        }
    }
    
    @Override
    public void write(final JsonObject json) {
        json.add("output", (JsonElement)JsonHelper.serializeItemStack(this.output));
    }
    
    @Override
    public void read(final FriendlyByteBuf buf) {
        this.output = ByteBufUtils.readItemStack(buf);
    }
    
    @Override
    public void write(final FriendlyByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, this.output);
    }
}
