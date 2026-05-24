package hellfirepvp.astralsorcery.common.crafting.nojson.starlight;

import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import java.util.Random;
import hellfirepvp.astralsorcery.common.crafting.nojson.CustomRecipe;

public abstract class LiquidStarlightRecipe extends CustomRecipe
{
    protected static final Random rand;
    private static final int WORLD_TIME_TOLERANCE = 10;
    
    public LiquidStarlightRecipe(final ResourceLocation key) {
        super(key);
    }
    
    @OnlyIn(Dist.CLIENT)
    public abstract List<Ingredient> getInputForRender();
    
    @OnlyIn(Dist.CLIENT)
    public abstract List<Ingredient> getOutputForRender();
    
    public abstract boolean doesStartRecipe(final ItemStack p0);
    
    public abstract boolean matches(final ItemEntity p0, final World p1, final BlockPos p2);
    
    public abstract void doServerCraftTick(final ItemEntity p0, final World p1, final BlockPos p2);
    
    @OnlyIn(Dist.CLIENT)
    public abstract void doClientEffectTick(final ItemEntity p0, final World p1, final BlockPos p2);
    
    protected final List<Entity> getEntitiesInBlock(final IWorld world, final BlockPos pos) {
        return world.func_217357_a((Class)Entity.class, new AABB(pos));
    }
    
    @Nullable
    protected final ItemStack consumeItemEntityInBlock(final IWorld world, final BlockPos pos, final Item itemClass) {
        return this.consumeItemEntityInBlock(world, pos, 1, stack -> itemClass.getClass().isAssignableFrom(stack.getItem().getClass()));
    }
    
    @Nullable
    protected final ItemStack consumeItemEntityInBlock(final IWorld world, final BlockPos pos, final int count, final Predicate<ItemStack> match) {
        Entity e = null;
        final List<Entity> entities = this.getEntitiesInBlock(world, pos).stream().filter(e -> e instanceof ItemEntity).collect((Collector<? super Object, ?, List<Entity>>)Collectors.toList());
        final Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            e = iterator.next();
            final ItemEntity ie = (ItemEntity)e;
            if (ie.isAlive() && !ie.func_92059_d().isEmpty() && ie.func_92059_d().func_190916_E() >= count && match.test(ie.func_92059_d())) {
                final ItemStack stored = ie.func_92059_d();
                final ItemStack found = ItemUtils.copyStackWithSize(stored, count);
                stored.shrink(count);
                ie.func_92058_a(stored);
                return found;
            }
        }
        return null;
    }
    
    protected final int getAndIncrementCraftingTick(final Entity e) {
        final int tick = this.getCraftingTick(e);
        this.setCraftingTick(e, tick + 1);
        return tick;
    }
    
    protected final void setCraftingTick(final Entity e, final int tick) {
        final long wTick = e.func_130014_f_().getDayTime();
        final CompoundTag nbt = NBTHelper.getPersistentData(e);
        nbt.putInt("craftTick", tick);
        nbt.func_74772_a("wCraftTick", wTick);
    }
    
    protected final int getCraftingTick(final Entity e) {
        final long wTick = e.func_130014_f_().getDayTime();
        final CompoundTag nbt = NBTHelper.getPersistentData(e);
        if (!nbt.func_150297_b("wCraftTick", 4)) {
            return 0;
        }
        final long savedWTick = nbt.getDouble("wCraftTick");
        if (Math.abs(wTick - savedWTick) > 10L) {
            return 0;
        }
        return nbt.getInt("craftTick");
    }
    
    static {
        rand = new Random();
    }
}
