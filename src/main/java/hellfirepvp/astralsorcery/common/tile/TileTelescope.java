package hellfirepvp.astralsorcery.common.tile;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TileEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import hellfirepvp.astralsorcery.common.util.tile.NamedInventoryTile;
import hellfirepvp.astralsorcery.common.tile.base.TileEntitySynchronized;

public class TileTelescope extends TileEntitySynchronized implements NamedInventoryTile
{
    private TelescopeRotation rotation;
    
    public TileTelescope() {
        super(TileEntityTypesAS.TELESCOPE);
        this.rotation = TelescopeRotation.N;
    }
    
    public TelescopeRotation getRotation() {
        return this.rotation;
    }
    
    public void setRotation(final TelescopeRotation rotation) {
        this.rotation = rotation;
        this.markForUpdate();
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.rotation = TelescopeRotation.values()[compound.func_74762_e("rotation")];
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        compound.func_74768_a("rotation", this.rotation.ordinal());
    }
    
    @Override
    public ITextComponent getDisplayName() {
        return (ITextComponent)new TranslationTextComponent("screen.astralsorcery.telescope");
    }
    
    public enum TelescopeRotation
    {
        N, 
        N_E, 
        E, 
        S_E, 
        S, 
        S_W, 
        W, 
        N_W;
        
        public TelescopeRotation nextClockWise() {
            return values()[(this.ordinal() + 1) % values().length];
        }
        
        public TelescopeRotation nextCounterClockWise() {
            return values()[(this.ordinal() + 7) % values().length];
        }
    }
}
