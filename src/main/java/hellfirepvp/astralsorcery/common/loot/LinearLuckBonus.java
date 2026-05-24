package hellfirepvp.astralsorcery.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.function.Function;
import java.util.Random;
import net.minecraft.world.level.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.item.enchantment.Enchantments;
import net.minecraft.world.level.effect.MobEffects;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.world.level.level.storage.loot.LootContext;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.LootAS;
import net.minecraft.world.level.storage.loot.LootFunctionType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootParameter;
import java.util.Set;
import net.minecraft.world.level.storage.loot.predicates.ILootCondition;
import net.minecraft.world.level.storage.loot.LootFunction;

public class LinearLuckBonus extends LootFunction
{
    private LinearLuckBonus(final ILootCondition[] lootConditions) {
        super(lootConditions);
    }
    
    public Set<LootParameter<?>> func_215855_a() {
        return (Set<LootParameter<?>>)ImmutableSet.of((Object)LootContextParams.TOOL);
    }
    
    public LootFunctionType func_230425_b_() {
        return LootAS.Functions.LINEAR_LUCK_BONUS;
    }
    
    protected ItemStack func_215859_a(final ItemStack itemStack, final LootContext lootContext) {
        final ItemStack tool = (ItemStack)lootContext.getParamOrNull(LootContextParams.TOOL);
        if (tool != null) {
            int luck = 0;
            final Entity e = (Entity)lootContext.getParamOrNull(LootContextParams.THIS_ENTITY);
            if (e instanceof Player && ((Player)e).func_70644_a(Effects.field_188425_z)) {
                luck += ((Player)e).func_70660_b(Effects.field_188425_z).func_76458_c() + 1;
            }
            luck += EnchantmentHelper.func_77506_a(Enchantments.field_185308_t, tool);
            luck += EnchantmentHelper.func_77506_a(Enchantments.field_185304_p, tool);
            final Random rand = lootContext.func_216032_b();
            int size = 0;
            for (int i = 0; i < luck; ++i) {
                size += rand.nextInt(3) + 1;
            }
            itemStack.func_190920_e(itemStack.func_190916_E() + size);
        }
        return itemStack;
    }
    
    public static LootFunction.Builder<?> builder() {
        return (LootFunction.Builder<?>)func_215860_a((Function)LinearLuckBonus::new);
    }
    
    public static class Serializer extends LootFunction.Serializer<LinearLuckBonus>
    {
        public LinearLuckBonus deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final ILootCondition[] iLootConditions) {
            return new LinearLuckBonus(iLootConditions, null);
        }
    }
}
