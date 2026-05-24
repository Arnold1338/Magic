package hellfirepvp.astralsorcery.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeItem;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeTile;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.LootAS;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.LootFunction;

public class CopyCrystalProperties extends LootFunction
{
    private CopyCrystalProperties(final ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }
    
    public LootFunctionType func_230425_b_() {
        return LootAS.Functions.COPY_CRYSTAL_PROPERTIES;
    }
    
    protected ItemStack func_215859_a(final ItemStack stack, final LootContext context) {
        if (context.func_216033_a(LootParameters.field_216288_h)) {
            final BlockEntity tile = (BlockEntity)context.getParamOrNull(LootParameters.field_216288_h);
            if (tile instanceof CrystalAttributeTile && stack.getItem() instanceof CrystalAttributeItem) {
                CrystalAttributes attr = ((CrystalAttributeTile)tile).getAttributes();
                if (attr == null) {
                    attr = ((CrystalAttributeTile)tile).getMissingAttributes();
                }
                ((CrystalAttributeItem)stack.getItem()).setAttributes(stack, attr);
            }
        }
        return stack;
    }
    
    public static LootFunction.Builder<?> builder() {
        return (LootFunction.Builder<?>)func_215860_a((Function)CopyCrystalProperties::new);
    }
    
    public static class Serializer extends LootFunction.Serializer<CopyCrystalProperties>
    {
        public CopyCrystalProperties deserialize(final JsonObject jsonObject, final JsonDeserializationContext ctx, final ILootCondition[] conditions) {
            return new CopyCrystalProperties(conditions, null);
        }
    }
}
