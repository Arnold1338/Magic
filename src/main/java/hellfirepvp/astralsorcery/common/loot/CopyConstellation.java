package hellfirepvp.astralsorcery.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.constellation.ConstellationItem;
import hellfirepvp.astralsorcery.common.constellation.ConstellationTile;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import net.minecraft.world.level.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.level.storage.loot.LootContext;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.LootAS;
import net.minecraft.world.level.storage.loot.LootFunctionType;
import net.minecraft.world.level.storage.loot.predicates.ILootCondition;
import net.minecraft.world.level.storage.loot.LootFunction;

public class CopyConstellation extends LootFunction
{
    private CopyConstellation(final ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }
    
    public LootFunctionType func_230425_b_() {
        return LootAS.Functions.COPY_CONSTELLATION;
    }
    
    protected ItemStack func_215859_a(final ItemStack stack, final LootContext context) {
        if (context.func_216033_a(LootParameters.field_216288_h)) {
            final BlockEntity tile = (BlockEntity)context.getParamOrNull(LootParameters.field_216288_h);
            if (tile instanceof ConstellationTile && stack.getItem() instanceof ConstellationItem) {
                final IWeakConstellation main = ((ConstellationTile)tile).getAttunedConstellation();
                final IMinorConstellation trait = ((ConstellationTile)tile).getTraitConstellation();
                ((ConstellationItem)stack.getItem()).setAttunedConstellation(stack, main);
                ((ConstellationItem)stack.getItem()).setTraitConstellation(stack, trait);
            }
        }
        return stack;
    }
    
    public static LootFunction.Builder<?> builder() {
        return (LootFunction.Builder<?>)func_215860_a((Function)CopyConstellation::new);
    }
    
    public static class Serializer extends LootFunction.Serializer<CopyConstellation>
    {
        public CopyConstellation deserialize(final JsonObject jsonObject, final JsonDeserializationContext ctx, final ILootCondition[] conditions) {
            return new CopyConstellation(conditions, null);
        }
    }
}
