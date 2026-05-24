package hellfirepvp.astralsorcery.common.crafting.nojson.attunement;

import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.constellation.ConstellationItem;
import hellfirepvp.astralsorcery.common.item.crystal.ItemCrystalBase;
import java.util.List;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.entity.EntityUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import hellfirepvp.astralsorcery.common.tile.TileAttunementAltar;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.level.phys.AABB;
import hellfirepvp.astralsorcery.common.crafting.nojson.attunement.active.ActiveCrystalAttunementRecipe;

public class AttuneCrystalRecipe extends AttunementRecipe<ActiveCrystalAttunementRecipe>
{
    private static final AABB BOX;
    
    public AttuneCrystalRecipe() {
        super(AstralSorcery.key("attune_crystal"));
    }
    
    @Override
    public boolean canStartCrafting(final TileAttunementAltar altar) {
        final World world = altar.func_145831_w();
        return DayTimeHelper.isNight(world) && findApplicableCrystal(altar) != null;
    }
    
    @Nonnull
    @Override
    public ActiveCrystalAttunementRecipe createRecipe(final TileAttunementAltar altar) {
        final ItemEntity crystal = findApplicableCrystal(altar);
        return new ActiveCrystalAttunementRecipe(this, altar.getActiveConstellation(), crystal.func_145782_y());
    }
    
    @Nonnull
    @Override
    public ActiveCrystalAttunementRecipe deserialize(final TileAttunementAltar altar, final CompoundTag nbt, @Nullable final ActiveCrystalAttunementRecipe previousInstance) {
        return new ActiveCrystalAttunementRecipe(this, nbt);
    }
    
    @Nullable
    private static ItemEntity findApplicableCrystal(final TileAttunementAltar altar) {
        final IConstellation cst = altar.getActiveConstellation();
        if (cst == null) {
            return null;
        }
        final AABB boxAt = AttuneCrystalRecipe.BOX.func_186670_a(altar.func_174877_v().above()).func_186662_g(1.0);
        final Vector3 thisVec = new Vector3(altar).add(0.5, 1.5, 0.5);
        final List<ItemEntity> items = altar.func_145831_w().func_217357_a((Class)ItemEntity.class, boxAt);
        if (!items.isEmpty()) {
            final ItemEntity item = EntityUtils.selectClosest((Collection<ItemEntity>)items, iEntity -> thisVec.distanceSquared(iEntity.func_213303_ch()));
            if (isApplicableCrystal(item, cst)) {
                return item;
            }
        }
        return null;
    }
    
    public static boolean isApplicableCrystal(final ItemEntity entity, final IConstellation cst) {
        final ItemStack stack;
        if (entity.isAlive() && !(stack = entity.func_92059_d()).isEmpty() && stack.getItem() instanceof ItemCrystalBase) {
            if (!(stack.getItem() instanceof ConstellationItem)) {
                return cst instanceof IWeakConstellation;
            }
            final IWeakConstellation attuned = ((ConstellationItem)stack.getItem()).getAttunedConstellation(stack);
            final IMinorConstellation trait = ((ConstellationItem)stack.getItem()).getTraitConstellation(stack);
            if (attuned == null && cst instanceof IWeakConstellation) {
                return true;
            }
            if (trait == null && cst instanceof IMinorConstellation) {
                return true;
            }
        }
        return false;
    }
    
    static {
        BOX = new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    }
}
