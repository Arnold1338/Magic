package hellfirepvp.astralsorcery.common.util.item;

import java.util.Set;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import hellfirepvp.astralsorcery.common.util.nbt.NBTComparator;
import com.google.common.collect.Sets;
import javax.annotation.Nonnull;
import net.minecraft.world.level.item.ItemStack;

public class ItemComparator
{
    public static boolean compare(@Nonnull final ItemStack thisStack, @Nonnull final ItemStack sampleCompare, final Clause... clauses) {
        final Set<Clause> lClauses = Sets.newHashSet((Object[])clauses);
        if (lClauses.contains(Clause.ITEM)) {
            if (thisStack.isEmpty() && !sampleCompare.isEmpty()) {
                return false;
            }
            if (!thisStack.isEmpty() && !thisStack.getItem().equals(sampleCompare.getItem())) {
                return false;
            }
        }
        if (lClauses.contains(Clause.AMOUNT_EXACT)) {
            if (thisStack.func_190916_E() != sampleCompare.func_190916_E()) {
                return false;
            }
        }
        else if (lClauses.contains(Clause.AMOUNT_LEAST) && thisStack.func_190916_E() > sampleCompare.func_190916_E()) {
            return false;
        }
        final boolean thisHasTag = thisStack.hasTag() && !thisStack.getTag().isEmpty();
        final boolean sampleHasTag = sampleCompare.hasTag() && !sampleCompare.getTag().isEmpty();
        if (lClauses.contains(Clause.NBT_STRICT)) {
            if (!thisHasTag && sampleHasTag) {
                return false;
            }
            if (thisHasTag && (!sampleHasTag || !thisStack.getTag().equals((Object)sampleCompare.getTag()))) {
                return false;
            }
        }
        else if (lClauses.contains(Clause.NBT_LEAST) && thisHasTag) {
            if (!sampleHasTag) {
                return false;
            }
            if (!NBTComparator.contains(thisStack.getTag(), sampleCompare.getTag())) {
                return false;
            }
        }
        return !lClauses.contains(Clause.CAPABILITIES_COMPATIBLE) || thisStack.areCapsCompatible((CapabilityProvider)sampleCompare);
    }
    
    public enum Clause
    {
        ITEM, 
        AMOUNT_EXACT, 
        AMOUNT_LEAST, 
        NBT_STRICT, 
        NBT_LEAST, 
        CAPABILITIES_COMPATIBLE;
        
        public static class Sets
        {
            public static final Clause[] ITEMSTACK_STRICT;
            public static final Clause[] ITEMSTACK_STRICT_NOAMOUNT;
            public static final Clause[] ITEMSTACK_CRAFTING;
            
            static {
                ITEMSTACK_STRICT = new Clause[] { Clause.ITEM, Clause.AMOUNT_EXACT, Clause.NBT_STRICT, Clause.CAPABILITIES_COMPATIBLE };
                ITEMSTACK_STRICT_NOAMOUNT = new Clause[] { Clause.ITEM, Clause.NBT_STRICT, Clause.CAPABILITIES_COMPATIBLE };
                ITEMSTACK_CRAFTING = new Clause[] { Clause.ITEM, Clause.NBT_LEAST };
            }
        }
    }
}
