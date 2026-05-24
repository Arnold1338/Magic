package hellfirepvp.astralsorcery.common.crafting.nojson.attunement;

import net.minecraftforge.fml.LogicalSide;
import java.util.Random;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.tile.TileAttunementAltar;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.crafting.nojson.CustomRecipe;

public abstract class AttunementRecipe<T extends Active<?>> extends CustomRecipe
{
    public AttunementRecipe(final ResourceLocation key) {
        super(key);
    }
    
    public abstract boolean canStartCrafting(final TileAttunementAltar p0);
    
    @Nonnull
    public abstract T createRecipe(final TileAttunementAltar p0);
    
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public T deserialize(final TileAttunementAltar altar, final CompoundTag nbt, @Nullable final T previousInstance) {
        final T activeRecipe = this.createRecipe(altar);
        activeRecipe.readFromNBT(nbt);
        return activeRecipe;
    }
    
    public abstract static class Active<T extends AttunementRecipe<? extends Active<T>>>
    {
        protected final Random rand;
        private T recipe;
        private int tick;
        
        public Active(final T recipe) {
            this.rand = new Random();
            this.tick = 0;
            this.recipe = recipe;
        }
        
        public final T getRecipe() {
            return this.recipe;
        }
        
        protected int getTick() {
            return this.tick;
        }
        
        public final void tick(final LogicalSide side, final TileAttunementAltar altar) {
            this.doTick(side, altar);
            ++this.tick;
        }
        
        public abstract void stopCrafting(final TileAttunementAltar p0);
        
        public abstract void startCrafting(final TileAttunementAltar p0);
        
        public abstract void finishRecipe(final TileAttunementAltar p0);
        
        public abstract void doTick(final LogicalSide p0, final TileAttunementAltar p1);
        
        public abstract boolean isFinished(final TileAttunementAltar p0);
        
        @OnlyIn(Dist.CLIENT)
        public abstract void stopEffects(final TileAttunementAltar p0);
        
        public boolean matches(final TileAttunementAltar altar) {
            return this.recipe.canStartCrafting(altar);
        }
        
        public void writeToNBT(final CompoundTag nbt) {
            nbt.putInt("tick", this.tick);
        }
        
        protected void readFromNBT(final CompoundTag nbt) {
            this.tick = nbt.getInt("tick");
        }
    }
}
