package hellfirepvp.astralsorcery.common.util.block;

import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import hellfirepvp.astralsorcery.common.util.data.JsonHelper;
import net.minecraft.world.level.level.ItemLike;
import com.google.gson.JsonObject;
import javax.annotation.Nonnull;
import net.minecraft.world.level.block.AirBlock;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.level.block.Block;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.world.level.level.block.state.BlockState;
import java.util.function.Predicate;

public class BlockMatchInformation implements Predicate<BlockState>
{
    private final ItemStack display;
    private BlockState matchState;
    private boolean matchExact;
    private ITag<Block> matchTag;
    private ResourceLocation matchTagKey;
    
    public BlockMatchInformation(final ITag<Block> matchTag) {
        this(matchTag, createDisplayStack(matchTag));
    }
    
    public BlockMatchInformation(final ITag<Block> matchTag, final ItemStack display) {
        this.matchTag = matchTag;
        this.matchTagKey = TagCollectionManager.func_242178_a().func_241835_a().func_232973_a_((ITag)matchTag);
        this.display = display;
        if (this.matchTagKey == null) {
            throw new IllegalArgumentException("Unknown block tag name!");
        }
        if (this.display.isEmpty()) {
            throw new IllegalArgumentException("No display ItemStack passed, and unable to create valid itemstack from block tag " + this.matchTagKey.toString() + "!");
        }
    }
    
    public BlockMatchInformation(final BlockState matchState, final boolean matchExact) {
        this(matchState, ItemUtils.createBlockStack(matchState), matchExact);
    }
    
    public BlockMatchInformation(final BlockState matchState, final ItemStack display, final boolean matchExact) {
        this.matchState = matchState;
        this.display = display;
        this.matchExact = matchExact;
        if (this.display.isEmpty()) {
            throw new IllegalArgumentException("No display ItemStack passed, and " + matchState.getBlock().getRegistryName() + " has no associated ItemBlock!");
        }
    }
    
    private static ItemStack createDisplayStack(final ITag<Block> blockTag) {
        for (final Block block : blockTag.func_230236_b_()) {
            final ItemStack blockStack = ItemUtils.createBlockStack(block.defaultBlockState());
            if (!blockStack.isEmpty()) {
                return blockStack;
            }
        }
        return ItemStack.field_190927_a;
    }
    
    public boolean isValid() {
        return this.matchState == null || !(this.matchState.getBlock() instanceof AirBlock);
    }
    
    @Nonnull
    public ItemStack getDisplayStack() {
        return this.display.copy();
    }
    
    @Override
    public boolean test(final BlockState state) {
        if (this.matchState != null) {
            return this.matchExact ? BlockUtils.matchStateExact(state, this.matchState) : state.getBlock().equals(this.matchState.getBlock());
        }
        return this.matchTag != null && this.matchTag.func_230235_a_((Object)state.getBlock());
    }
    
    public static BlockMatchInformation read(final JsonObject object) {
        if (object.has("block")) {
            final BlockState state = BlockStateHelper.deserializeObject(object);
            final boolean fullyDefined = !BlockStateHelper.isMissingStateInformation(object);
            ItemStack display = new ItemStack((ItemLike)state.getBlock());
            if (object.has("display")) {
                display = JsonHelper.getItemStack(object, "display");
            }
            return new BlockMatchInformation(state, display, fullyDefined);
        }
        if (!object.has("tag")) {
            throw new JsonSyntaxException("Neither block nor tag defined for block transmutation match information!");
        }
        final ITag<Block> blockTag = (ITag<Block>)TagCollectionManager.func_242178_a().func_241835_a().func_199910_a(new ResourceLocation(object.get("tag").getAsString()));
        if (object.has("display")) {
            final ItemStack display2 = JsonHelper.getItemStack(object, "display");
            return new BlockMatchInformation(blockTag, display2);
        }
        return new BlockMatchInformation(blockTag);
    }
    
    public JsonObject serializeJson() {
        final JsonObject out = new JsonObject();
        if (this.matchState != null) {
            BlockStateHelper.serializeObject(out, this.matchState, this.matchExact);
            out.add("display", (JsonElement)JsonHelper.serializeItemStack(this.getDisplayStack()));
        }
        else if (this.matchTag != null) {
            out.add("tag", (JsonElement)new JsonPrimitive(this.matchTagKey.toString()));
        }
        return out;
    }
    
    public static BlockMatchInformation read(final FriendlyByteBuf buf) {
        final int type = buf.readInt();
        final ItemStack display = ByteBufUtils.readItemStack(buf);
        switch (type) {
            case 0: {
                final BlockState state = ByteBufUtils.readBlockState(buf);
                final boolean exactMatch = buf.readBoolean();
                return new BlockMatchInformation(state, display, exactMatch);
            }
            case 1: {
                final String tagName = ByteBufUtils.readString(buf);
                final ITag<Block> blockTag = (ITag<Block>)TagCollectionManager.func_242178_a().func_241835_a().func_199910_a(new ResourceLocation(tagName));
                return new BlockMatchInformation(blockTag, display);
            }
            default: {
                throw new IllegalArgumentException("Unknown block transmutation match type: " + type);
            }
        }
    }
    
    public void serialize(final FriendlyByteBuf buf) {
        final int type = (this.matchState == null) ? 1 : 0;
        buf.writeInt(type);
        ByteBufUtils.writeItemStack(buf, this.display);
        switch (type) {
            case 0: {
                ByteBufUtils.writeBlockState(buf, this.matchState);
                buf.writeBoolean(this.matchExact);
                break;
            }
            case 1: {
                ByteBufUtils.writeString(buf, this.matchTagKey.toString());
                break;
            }
        }
    }
}
