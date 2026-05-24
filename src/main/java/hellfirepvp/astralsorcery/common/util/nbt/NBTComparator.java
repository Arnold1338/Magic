package hellfirepvp.astralsorcery.common.util.nbt;

import java.util.List;
import java.util.ArrayList;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import java.util.Iterator;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;

public class NBTComparator
{
    public static boolean contains(@Nonnull final CompoundTag thisCompound, @Nonnull final CompoundTag otherCompound) {
        for (final String key : thisCompound.func_150296_c()) {
            if (!otherCompound.contains(key)) {
                return false;
            }
            final Tag thisNBT = thisCompound.func_74781_a(key);
            final Tag otherNBT = otherCompound.func_74781_a(key);
            if (!compare(thisNBT, otherNBT)) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean containList(final ListTag base, final ListTag other) {
        if (base.size() > other.size()) {
            return false;
        }
        final List<Integer> matched = new ArrayList<Integer>();
    Label_0026:
        for (final Tag thisNbt : base) {
            for (int matchIndex = 0; matchIndex < other.size(); ++matchIndex) {
                final Tag matchNBT = other.get(matchIndex);
                if (!matched.contains(matchIndex) && compare(thisNbt, matchNBT)) {
                    matched.add(matchIndex);
                    continue Label_0026;
                }
            }
            return false;
        }
        return true;
    }
    
    private static boolean compare(final Tag thisEntry, final Tag thatEntry) {
        if (thisEntry instanceof CompoundTag && thatEntry instanceof CompoundTag) {
            return contains((CompoundTag)thisEntry, (CompoundTag)thatEntry);
        }
        if (thisEntry instanceof ListTag && thatEntry instanceof ListTag) {
            return containList((ListTag)thisEntry, (ListTag)thatEntry);
        }
        return thisEntry.equals(thatEntry);
    }
}
