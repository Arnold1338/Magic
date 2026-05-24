package hellfirepvp.astralsorcery.common.auxiliary.book;

import java.util.HashMap;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import net.minecraft.world.level.level.ItemLike;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.util.item.ItemComparator;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.level.item.ItemStack;
import java.util.Map;

public class BookLookupRegistry
{
    private static final Map<ItemStack, BookLookupInfo> lookupMap;
    
    private BookLookupRegistry() {
    }
    
    @Nullable
    public static BookLookupInfo findPage(final Player player, final LogicalSide side, final ItemStack search) {
        for (final ItemStack compare : BookLookupRegistry.lookupMap.keySet()) {
            if (ItemComparator.compare(compare, search, ItemComparator.Clause.Sets.ITEMSTACK_CRAFTING)) {
                final BookLookupInfo info = BookLookupRegistry.lookupMap.get(compare);
                final PlayerProgress prog = ResearchHelper.getProgress(player, side);
                if (info.canSee(prog)) {
                    return info;
                }
                continue;
            }
        }
        return null;
    }
    
    public static void registerItemLookup(final ItemLike item, final ResearchNode parentNode, final int nodePage, final ResearchProgression neededProgression) {
        registerItemLookup(new ItemStack(item), parentNode, nodePage, neededProgression);
    }
    
    public static void registerItemLookup(final ItemStack stack, final ResearchNode parentNode, final int nodePage, final ResearchProgression neededProgression) {
        BookLookupRegistry.lookupMap.put(stack, new BookLookupInfo(parentNode, nodePage, neededProgression));
    }
    
    static {
        lookupMap = new HashMap<ItemStack, BookLookupInfo>();
    }
}
