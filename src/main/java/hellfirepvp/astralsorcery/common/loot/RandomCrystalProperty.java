package hellfirepvp.astralsorcery.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.crystal.CrystalGenerator;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeGenItem;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.LootAS;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.LootFunction;

public class RandomCrystalProperty extends LootFunction
{
    private RandomCrystalProperty(final ILootCondition[] conditions) {
        super(conditions);
    }
    
    public LootFunctionType func_230425_b_() {
        return LootAS.Functions.RANDOM_CRYSTAL_PROPERTIES;
    }
    
    protected ItemStack func_215859_a(final ItemStack itemStack, final LootContext lootContext) {
        if (itemStack.getItem() instanceof CrystalAttributeGenItem) {
            final CrystalAttributes attr = CrystalGenerator.generateNewAttributes(itemStack);
            ((CrystalAttributeGenItem)itemStack.getItem()).setAttributes(itemStack, attr);
        }
        return itemStack;
    }
    
    public static LootFunction.Builder<?> builder() {
        return (LootFunction.Builder<?>)func_215860_a((Function)RandomCrystalProperty::new);
    }
    
    public static class Serializer extends LootFunction.Serializer<RandomCrystalProperty>
    {
        public RandomCrystalProperty deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final ILootCondition[] iLootConditions) {
            return new RandomCrystalProperty(iLootConditions, null);
        }
    }
}
