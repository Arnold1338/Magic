package hellfirepvp.astralsorcery.common.util.loot;

import java.util.Iterator;
import net.minecraft.world.level.storage.loot.LootParameter;
import net.minecraft.world.level.storage.loot.LootParameterSet;
import net.minecraft.world.level.storage.loot.LootContext;

public class LootUtil
{
    private LootUtil() {
    }
    
    public static boolean doesContextFulfillSet(final LootContext ctx, final LootParameterSet set) {
        for (final LootParameter<?> required : set.func_216277_a()) {
            if (!ctx.hasParam((LootParameter)required)) {
                return false;
            }
        }
        return true;
    }
}
