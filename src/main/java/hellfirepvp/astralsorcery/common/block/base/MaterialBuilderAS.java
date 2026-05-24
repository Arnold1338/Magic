package hellfirepvp.astralsorcery.common.block.base;

import net.minecraft.world.level.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.world.level.material.MapColor;

public class MaterialBuilderAS
{
    private final MaterialColor color;
    private PushReaction pushReaction;
    private boolean blocksMovement;
    private boolean canBurn;
    private boolean isLiquid;
    private boolean isReplaceable;
    private boolean isSolid;
    private boolean isOpaque;
    
    public MaterialBuilderAS(final MaterialColor color) {
        this.pushReaction = PushReaction.NORMAL;
        this.blocksMovement = true;
        this.canBurn = false;
        this.isLiquid = false;
        this.isReplaceable = false;
        this.isSolid = true;
        this.isOpaque = true;
        this.color = color;
    }
    
    public MaterialBuilderAS liquid() {
        this.isLiquid = true;
        return this;
    }
    
    public MaterialBuilderAS notSolid() {
        this.isSolid = false;
        return this;
    }
    
    public MaterialBuilderAS doesNotBlockMovement() {
        this.blocksMovement = false;
        return this;
    }
    
    public MaterialBuilderAS notOpaque() {
        this.isOpaque = false;
        return this;
    }
    
    public MaterialBuilderAS flammable() {
        this.canBurn = true;
        return this;
    }
    
    public MaterialBuilderAS replaceable() {
        this.isReplaceable = true;
        return this;
    }
    
    public MaterialBuilderAS pushDestroys() {
        this.pushReaction = PushReaction.DESTROY;
        return this;
    }
    
    public MaterialBuilderAS pushBlocks() {
        this.pushReaction = PushReaction.BLOCK;
        return this;
    }
    
    public Material build() {
        return new Material(this.color, this.isLiquid, this.isSolid, this.blocksMovement, this.isOpaque, this.canBurn, this.isReplaceable, this.pushReaction);
    }
}
