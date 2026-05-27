package hellfirepvp.astralsorcery.client.effect.function;

import javax.annotation.Nullable;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.BiPredicate;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;

public interface RefreshFunction<T extends EntityComplexFX>
{
    public static final RefreshFunction<?> DESPAWN = fx -> false;
    
    default <E extends BlockEntity, T extends EntityComplexFX> RefreshFunction<T> tileExists(final E tile) {
        return new TileExists<Object, T>(tile);
    }
    
    default <E extends BlockEntity, T extends EntityComplexFX> RefreshFunction<T> tileExistsAnd(final E tile, final BiPredicate<E, T> refreshFct) {
        final TileExists<E, T> fct = new TileExists<E, T>(tile);
        return fx -> Optional.ofNullable(fct.getTileIfValid()).map(t -> refreshFct.test(t, fx)).orElse(false);
    }
    
    boolean shouldRefresh(@Nonnull final T p0);
    
    public static class TileExists<E extends BlockEntity, T extends EntityComplexFX> implements RefreshFunction<T>
    {
        private final ResourceKey<Level> dimType;
        private final BlockPos pos;
        private final Class<E> clazzExpected;
        
        public TileExists(final E tile) {
            this.dimType = (ResourceKey<Level>)tile.getLevel().dimension();
            this.pos = tile.getBlockState();
            this.clazzExpected = (Class<E>)tile.getClass();
        }
        
        @Override
        public boolean shouldRefresh(@Nonnull final T fx) {
            return this.getTileIfValid() != null;
        }
        
        @Nullable
        protected E getTileIfValid() {
            final Level clWorld = (Level)Minecraft.getInstance().level;
            final E tile;
            if (clWorld != null && clWorld.dimension().equals(this.dimType) && (tile = MiscUtils.getTileAt((IBlockReader)clWorld, this.pos, this.clazzExpected, true)) != null && !tile.func_145837_r()) {
                return tile;
            }
            return null;
        }
    }
}
