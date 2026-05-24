package hellfirepvp.astralsorcery.common.crafting.helper;

import net.minecraft.nbt.Tag;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

public class CraftingFocusStack
{
    private final int stackIndex;
    private final WrappedIngredient input;
    private final BlockPos at;
    
    public CraftingFocusStack(final int stackIndex, final WrappedIngredient input, final BlockPos at) {
        this.stackIndex = stackIndex;
        this.input = input;
        this.at = at;
    }
    
    public CraftingFocusStack(final CompoundTag nbt) {
        this.stackIndex = nbt.getInt("stackIndex");
        this.input = WrappedIngredient.deserialize(nbt.func_74775_l("ingredient"));
        this.at = NBTHelper.readBlockPosFromNBT(nbt);
    }
    
    @Nullable
    public WrappedIngredient getInput() {
        return this.input;
    }
    
    public BlockPos getRealPosition() {
        return this.at;
    }
    
    public int getStackIndex() {
        return this.stackIndex;
    }
    
    public CompoundTag serialize() {
        final CompoundTag nbt = new CompoundTag();
        NBTHelper.writeBlockPosToNBT(this.at, nbt);
        nbt.putInt("stackIndex", this.stackIndex);
        nbt.put("ingredient", (Tag)this.input.serialize());
        return nbt;
    }
}
