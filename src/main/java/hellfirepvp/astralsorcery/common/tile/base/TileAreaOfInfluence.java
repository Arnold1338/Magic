package hellfirepvp.astralsorcery.common.tile.base;

import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.BlockPos;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import javax.annotation.Nullable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.awt.Color;

public interface TileAreaOfInfluence
{
    @OnlyIn(Dist.CLIENT)
    @Nullable
    Color getEffectColor();
    
    @Nonnull
    Vector3 getEffectPosition();
    
    float getRadius();
    
    @Nonnull
    BlockPos getEffectOriginPosition();
    
    @Nonnull
    RegistryKey<Level> getDimension();
    
    boolean providesEffect();
}
