package hellfirepvp.astralsorcery.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.item.DyeColor;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.block.tile.BlockCelestialGateway;
import hellfirepvp.astralsorcery.common.tile.TileCelestialGateway;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import net.minecraft.world.level.level.storage.loot.LootContext;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.LootAS;
import net.minecraft.world.level.storage.loot.LootFunctionType;
import com.google.common.collect.Sets;
import net.minecraft.world.level.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootParameter;
import java.util.Set;
import net.minecraft.world.level.storage.loot.predicates.ILootCondition;
import net.minecraft.world.level.storage.loot.LootFunction;

public class CopyGatewayColor extends LootFunction
{
    private CopyGatewayColor(final ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }
    
    public Set<LootParameter<?>> func_215855_a() {
        return Sets.newHashSet((Object[])new LootParameter[] { LootParameters.field_216288_h });
    }
    
    public LootFunctionType func_230425_b_() {
        return LootAS.Functions.COPY_GATEWAY_COLOR;
    }
    
    protected ItemStack func_215859_a(final ItemStack stack, final LootContext context) {
        final BlockEntity tile = (BlockEntity)context.getParamOrNull(LootParameters.field_216288_h);
        if (tile instanceof TileCelestialGateway) {
            ((TileCelestialGateway)tile).getColor().ifPresent(color -> BlockCelestialGateway.setColor(stack, color));
        }
        return stack;
    }
    
    public static LootFunction.Builder<?> builder() {
        return (LootFunction.Builder<?>)func_215860_a((Function)CopyGatewayColor::new);
    }
    
    public static class Serializer extends LootFunction.Serializer<CopyGatewayColor>
    {
        public CopyGatewayColor deserialize(final JsonObject object, final JsonDeserializationContext deserializationContext, final ILootCondition[] conditions) {
            return new CopyGatewayColor(conditions, null);
        }
    }
}
