package hellfirepvp.astralsorcery.common.item.lens;

import java.util.HashMap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.util.PartialEffectExecutor;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import java.awt.Color;
import net.minecraft.world.item.ItemStack;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public abstract class LensColorType
{
    private static final Map<ResourceLocation, LensColorType> BY_NAME;
    private final Supplier<ItemStack> itemSupplier;
    private final ResourceLocation name;
    private final TargetType type;
    private final Color color;
    private final float flowReduction;
    private final boolean ignoresBlockCollision;
    
    public LensColorType(final ResourceLocation name, final TargetType type, final Supplier<ItemStack> itemSupplier, final Color color, final float flowReduction, final boolean ignoresBlockCollision) {
        this.name = name;
        this.type = type;
        this.color = color;
        this.flowReduction = flowReduction;
        this.ignoresBlockCollision = ignoresBlockCollision;
        this.itemSupplier = itemSupplier;
        LensColorType.BY_NAME.put(name, this);
    }
    
    public ResourceLocation getName() {
        return this.name;
    }
    
    public TargetType getType() {
        return this.type;
    }
    
    public float getFlowMultiplier() {
        return this.flowReduction;
    }
    
    public boolean doesIgnoreBlockCollision() {
        return this.ignoresBlockCollision;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public ItemStack getStack() {
        return this.itemSupplier.get();
    }
    
    @Nullable
    public static LensColorType byName(final ResourceLocation name) {
        return LensColorType.BY_NAME.get(name);
    }
    
    public abstract void entityInBeam(final Level p0, final Vector3 p1, final Vector3 p2, final Entity p3, final PartialEffectExecutor p4);
    
    public abstract void blockInBeam(final Level p0, final BlockPos p1, final BlockState p2, final PartialEffectExecutor p3);
    
    static {
        BY_NAME = new HashMap<ResourceLocation, LensColorType>();
    }
    
    public enum TargetType
    {
        ANY, 
        ENTITY, 
        BLOCK, 
        NONE;
        
        public boolean doEntityInteraction() {
            return this == TargetType.ANY || this == TargetType.ENTITY;
        }
        
        public boolean doBlockInteraction() {
            return this == TargetType.ANY || this == TargetType.BLOCK;
        }
    }
}
