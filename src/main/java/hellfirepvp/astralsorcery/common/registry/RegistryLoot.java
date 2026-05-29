package hellfirepvp.astralsorcery.common.registry;

import net.minecraft.world.level.storage.loot.ILootSerializer;
import net.minecraft.world.level.storage.loot.functions.LootFunctionManager;
import net.minecraft.world.level.storage.loot.LootFunctionType;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.loot.CopyGatewayColor;
import hellfirepvp.astralsorcery.common.loot.CopyConstellation;
import hellfirepvp.astralsorcery.common.loot.CopyCrystalProperties;
import hellfirepvp.astralsorcery.common.loot.RandomCrystalProperty;
import hellfirepvp.astralsorcery.common.lib.LootAS;
import net.minecraft.world.level.storage.loot.LootFunction;
import hellfirepvp.astralsorcery.common.loot.LinearLuckBonus;
import hellfirepvp.astralsorcery.common.loot.global.LootModifierPerkVoidTrash;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.loot.global.LootModifierScorchingHeat;

public class RegistryLoot
{
    private RegistryLoot() {
    }
    
    public static void init() {
        registerGlobalModifier((net.minecraftforge.common.loot.GlobalLootModifierSerializer<IGlobalLootModifier>)new LootModifierScorchingHeat.Serializer(), AstralSorcery.key("scorching_heat"));
        registerGlobalModifier((net.minecraftforge.common.loot.GlobalLootModifierSerializer<IGlobalLootModifier>)new LootModifierPerkVoidTrash.Serializer(), AstralSorcery.key("perk_void_trash"));
        LootAS.Functions.LINEAR_LUCK_BONUS = registerFunction((LootFunction.Serializer<LootFunction>)new LinearLuckBonus.Serializer(), AstralSorcery.key("linear_luck_bonus"));
        LootAS.Functions.RANDOM_CRYSTAL_PROPERTIES = registerFunction((LootFunction.Serializer<LootFunction>)new RandomCrystalProperty.Serializer(), AstralSorcery.key("random_crystal_property"));
        LootAS.Functions.COPY_CRYSTAL_PROPERTIES = registerFunction((LootFunction.Serializer<LootFunction>)new CopyCrystalProperties.Serializer(), AstralSorcery.key("copy_crystal_properties"));
        LootAS.Functions.COPY_CONSTELLATION = registerFunction((LootFunction.Serializer<LootFunction>)new CopyConstellation.Serializer(), AstralSorcery.key("copy_constellation"));
        LootAS.Functions.COPY_GATEWAY_COLOR = registerFunction((LootFunction.Serializer<LootFunction>)new CopyGatewayColor.Serializer(), AstralSorcery.key("copy_gateway_color"));
    }
    
    private static <T extends LootFunction> LootFunctionType registerFunction(final LootFunction.Serializer<T> serializer, final ResourceLocation key) {
        return LootFunctionManager.func_237451_a_(key.toString(), (ILootSerializer)serializer);
    }
    
    private static <T extends IGlobalLootModifier> void registerGlobalModifier(final GlobalLootModifierSerializer<T> modifier, final ResourceLocation key) {
        modifier;
        AstralSorcery.getProxy().getRegistryPrimer().register(modifier);
    }
}
