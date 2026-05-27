package hellfirepvp.astralsorcery.common.constellation.effect.base;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.Iterator;
import net.minecraft.nbt.ListTag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.INBT;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.tile.TileRitualLink;
import com.mojang.datafixers.util.Either;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.lib.StructuresAS;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import java.util.function.Predicate;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import hellfirepvp.astralsorcery.common.util.block.iterator.BlockRandomPositionGenerator;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import java.util.List;
import hellfirepvp.astralsorcery.common.util.block.iterator.BlockPositionGenerator;
import hellfirepvp.astralsorcery.common.util.block.BlockPredicate;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;

public abstract class CEffectAbstractList<T extends ListEntry> extends ConstellationEffect
{
    protected boolean isLinkedRitual;
    private boolean excludesRitual;
    private boolean excludeRitualColumn;
    protected final BlockPredicate verifier;
    protected final int maxAmount;
    private final BlockPositionGenerator positionStrategy;
    private final List<T> elements;
    
    protected CEffectAbstractList(@Nonnull final ILocatable origin, @Nonnull final IWeakConstellation cst, final int maxAmount, final BlockPredicate verifier) {
        super(origin, cst);
        this.isLinkedRitual = false;
        this.excludesRitual = false;
        this.excludeRitualColumn = false;
        this.elements = new ArrayList<T>();
        this.maxAmount = maxAmount;
        this.verifier = verifier;
        this.positionStrategy = this.createPositionStrategy();
    }
    
    protected void excludeRitualPositions() {
        this.setChunkNeedsToBeLoaded();
        if (!this.excludesRitual) {
            this.excludesRitual = true;
            this.positionStrategy.andFilter(this.createExcludeRitualPredicate());
        }
    }
    
    protected void excludeRitualColumn() {
        this.setChunkNeedsToBeLoaded();
        if (!this.excludeRitualColumn) {
            this.excludeRitualColumn = true;
            this.positionStrategy.andFilter(this.createExcludeRitualColumnPredicate());
        }
    }
    
    protected void selectSphericalPositions() {
        this.positionStrategy.andFilter((pos, radius) -> {
            final double dst = new Vector3((Vec3i)this.getPos().getLocationPos()).add(0.5, 0.5, 0.5).distanceSquared(new Vector3((Vec3i)pos).add((Vec3i)this.getPos().getLocationPos()).add(0.5, 0.5, 0.5));
            return dst <= radius * radius;
        });
    }
    
    @Nullable
    public abstract T recreateElement(final CompoundTag p0, final BlockPos p1);
    
    @Nullable
    public abstract T createElement(final Level p0, final BlockPos p1);
    
    @Nonnull
    protected BlockPositionGenerator createPositionStrategy() {
        return new BlockRandomPositionGenerator();
    }
    
    @Nonnull
    protected BlockPositionGenerator selectPositionStrategy(final BlockPositionGenerator defaultGenerator, final ConstellationEffectProperties properties) {
        return defaultGenerator;
    }
    
    private Predicate<BlockPos> createExcludeRitualPredicate() {
        return pos -> (!pos.equals((Object)BlockPos.field_177992_a) && this.isLinkedRitual) || (!pos.equals((Object)TileRitualPedestal.RITUAL_ANCHOR_OFFEST) && (pos.getY() >= 3 || (!StructuresAS.STRUCT_RITUAL_PEDESTAL.hasBlockAt(pos) && !StructuresAS.STRUCT_RITUAL_PEDESTAL.hasBlockAt(pos.renderItem()) && !StructuresAS.STRUCT_RITUAL_PEDESTAL.hasBlockAt(pos.func_177979_c(2)) && !StructuresAS.STRUCT_RITUAL_PEDESTAL.hasBlockAt(pos.func_177979_c(3)) && !StructuresAS.STRUCT_RITUAL_PEDESTAL.hasBlockAt(pos.func_177979_c(4)))));
    }
    
    private Predicate<BlockPos> createExcludeRitualColumnPredicate() {
        return pos -> {
            final boolean b;
            if (!this.isLinkedRitual || (pos.getX() == 0 && pos.getZ() == 0)) {
                if (!this.isLinkedRitual) {
                    if (!StructuresAS.STRUCT_RITUAL_PEDESTAL.hasBlockAt(new BlockPos(pos.getX(), -1, pos.getZ()))) {
                        return 1 != 0;
                    }
                }
                return b;
            }
            return b;
        };
    }
    
    public int getCount() {
        return this.elements.size();
    }
    
    public void clear() {
        this.elements.clear();
    }
    
    public boolean isValid(final Level world, final T element) {
        return this.verifier.test(world, element.getPos(), world.getBlockState(element.getPos()));
    }
    
    @Nullable
    public T getRandomElement() {
        return MiscUtils.getRandomEntry(this.elements, CEffectAbstractList.rand);
    }
    
    @Nullable
    public T getRandomElementChanced() {
        if (this.elements.isEmpty()) {
            return null;
        }
        float perc = 1.0f - this.getCount() / (float)this.maxAmount;
        perc = 0.1f / (perc / 2.0f + 0.1f);
        if (CEffectAbstractList.rand.nextFloat() < perc) {
            return this.getRandomElement();
        }
        return null;
    }
    
    @Nonnull
    public Either<T, BlockPos> peekNewPosition(final Level world, final BlockPos pos, final ConstellationEffectProperties prop) {
        if (this.excludesRitual || this.excludeRitualColumn) {
            MiscUtils.executeWithChunk((IWorldReader)world, pos, () -> this.isLinkedRitual = (MiscUtils.getTileAt((IBlockReader)world, pos, TileRitualLink.class, true) != null));
        }
        final BlockPositionGenerator gen = this.selectPositionStrategy(this.positionStrategy, prop);
        if (gen != this.positionStrategy) {
            gen.copyFilterFrom(this.positionStrategy);
        }
        final BlockPos at = gen.generateNextPosition(new Vector3(0.5, 0.5, 0.5), prop.getSize());
        final BlockPos actual = at.func_177971_a((Vec3i)pos);
        if (this.getCount() >= this.maxAmount) {
            return (Either<T, BlockPos>)Either.right((Object)actual);
        }
        return (Either<T, BlockPos>)MiscUtils.executeWithChunk((IWorldReader)world, actual, () -> {
            if (this.verifier.test(world, actual, world.getBlockState(actual))) {
                final T element = this.createElement(world, actual);
                if (element == null) {
                    return Either.right((Object)actual);
                }
                else {
                    return Either.left((Object)element);
                }
            }
            else {
                return Either.right((Object)actual);
            }
        }, Either.right((Object)actual));
    }
    
    @Nonnull
    public Either<T, BlockPos> findNewPosition(final Level world, final BlockPos pos, final ConstellationEffectProperties prop) {
        return (Either<T, BlockPos>)this.peekNewPosition(world, pos, prop).ifLeft(entry -> {
            if (!this.hasElement(entry.getPos())) {
                this.elements.add((T)entry);
            }
        });
    }
    
    public boolean removeElement(final T entry) {
        return this.removeElement(entry.getPos());
    }
    
    public boolean removeElement(final BlockPos pos) {
        return this.elements.removeIf(e -> e.getPos().equals((Object)pos));
    }
    
    public boolean hasElement(final BlockPos pos) {
        return MiscUtils.contains(this.elements, e -> e.getPos().equals((Object)pos));
    }
    
    @Override
    public void readFromNBT(final CompoundTag cmp) {
        super.readFromNBT(cmp);
        this.elements.clear();
        final ListTag list = cmp.func_150295_c("elements", 10);
        for (final INBT nbt : list) {
            final CompoundTag tag = (CompoundTag)nbt;
            final BlockPos pos = NBTHelper.readBlockPosFromNBT(tag);
            final CompoundTag tagData = tag.func_74775_l("data");
            final T element = this.recreateElement(tagData, pos);
            if (element != null) {
                element.readFromNBT(tagData);
                this.elements.add(element);
            }
        }
    }
    
    @Override
    public void writeToNBT(final CompoundTag cmp) {
        super.writeToNBT(cmp);
        final ListTag list = new ListTag();
        for (final T element : this.elements) {
            final CompoundTag tag = new CompoundTag();
            NBTHelper.writeBlockPosToNBT(element.getPos(), tag);
            final CompoundTag dataTag = new CompoundTag();
            element.writeToNBT(dataTag);
            tag.func_218657_a("data", (INBT)dataTag);
            list.add((Object)tag);
        }
        cmp.func_218657_a("elements", (INBT)list);
    }
    
    public static class CountConfig extends Config
    {
        private final int defaultMaxAmount;
        public ForgeConfigSpec.IntValue maxAmount;
        
        public CountConfig(final String constellationName, final double defaultRange, final double defaultRangePerLens, final int defaultMaxAmount) {
            super(constellationName, defaultRange, defaultRangePerLens);
            this.defaultMaxAmount = defaultMaxAmount;
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            this.maxAmount = cfgBuilder.comment("Defines the amount of blocks this ritual will try to capture at most.").translation(this.translationKey("maxAmount")).defineInRange("maxAmount", this.defaultMaxAmount, 1, 2048);
        }
    }
    
    public interface ListEntry
    {
        BlockPos getPos();
        
        void writeToNBT(final CompoundTag p0);
        
        void readFromNBT(final CompoundTag p0);
    }
}
