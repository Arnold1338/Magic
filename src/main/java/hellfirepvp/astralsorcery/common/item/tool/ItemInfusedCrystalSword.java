package hellfirepvp.astralsorcery.common.item.tool;

import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.util.CelestialStrike;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.perk.modifier.DynamicAttributeModifier;
import hellfirepvp.astralsorcery.common.util.object.CacheReference;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.perk.source.provider.equipment.EquipmentAttributeModifierProvider;

public class ItemInfusedCrystalSword extends ItemCrystalSword implements EquipmentAttributeModifierProvider
{
    private static final UUID MODIFIER_ID;
    private static final CacheReference<DynamicAttributeModifier> BASECRIT_MODIFIER;
    
    public boolean onLeftClickEntity(final ItemStack stack, final Player player, final Entity entity) {
        if (!player.func_130014_f_().func_201670_d() && player instanceof ServerPlayer) {
            final ServerPlayer serverPlayer = (ServerPlayer)player;
            final ItemStack sword = serverPlayer.func_184586_b(InteractionHand.MAIN_HAND);
            if (!MiscUtils.isPlayerFakeMP(serverPlayer) && !sword.isEmpty() && sword.getItem() instanceof ItemInfusedCrystalSword && !serverPlayer.func_225608_bj_() && !serverPlayer.func_184811_cZ().func_185141_a(sword.getItem())) {
                final PlayerProgress prog = ResearchHelper.getProgress(player, LogicalSide.SERVER);
                if (prog.doPerkAbilities()) {
                    CelestialStrike.play((LivingEntity)serverPlayer, serverPlayer.func_71121_q(), Vector3.atEntityCorner(entity), Vector3.atEntityCorner(entity));
                    serverPlayer.func_184811_cZ().func_185145_a(sword.getItem(), 120);
                }
            }
        }
        return false;
    }
    
    @Override
    public Collection<PerkAttributeModifier> getModifiers(final ItemStack stack, final Player player, final LogicalSide side, final boolean ignoreRequirements) {
        return (Collection<PerkAttributeModifier>)Collections.singletonList(ItemInfusedCrystalSword.BASECRIT_MODIFIER.get());
    }
    
    static {
        MODIFIER_ID = UUID.fromString("bf154d57-22ca-4b62-822e-2ad09df5f1e8");
        BASECRIT_MODIFIER = new CacheReference<DynamicAttributeModifier>(() -> new DynamicAttributeModifier(ItemInfusedCrystalSword.MODIFIER_ID, PerkAttributeTypesAS.ATTR_TYPE_INC_CRIT_CHANCE, ModifierType.ADDITION, 5.0f));
    }
}
