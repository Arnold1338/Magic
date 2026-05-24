package hellfirepvp.astralsorcery.common.item.tool;

import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import java.util.Collection;
import java.util.List;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.network.play.server.PktOreScan;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.block.BlockDiscoverer;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.TagKey;
import hellfirepvp.astralsorcery.common.util.block.BlockPredicates;
import hellfirepvp.astralsorcery.common.lib.TagsAS;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.item.ItemUseContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.perk.modifier.DynamicAttributeModifier;
import hellfirepvp.astralsorcery.common.util.object.CacheReference;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.perk.source.provider.equipment.EquipmentAttributeModifierProvider;

public class ItemInfusedCrystalPickaxe extends ItemCrystalPickaxe implements EquipmentAttributeModifierProvider
{
    private static final UUID MODIFIER_ID;
    private static final CacheReference<DynamicAttributeModifier> MINING_SIZE_MODIFIER;
    
    public InteractionResult<ItemStack> func_77659_a(final World world, final Player player, final Hand hand) {
        final ItemStack held = player.func_184586_b(hand);
        if (this.doOreScan(world, player.func_233580_cy_(), player, held)) {
            return (InteractionResult<ItemStack>)InteractionResult.func_226248_a_((Object)held);
        }
        return (InteractionResult<ItemStack>)super.func_77659_a(world, player, hand);
    }
    
    public InteractionResult func_195939_a(final ItemUseContext ctx) {
        final Player player = ctx.func_195999_j();
        if (player != null && this.doOreScan(ctx.func_195991_k(), ctx.func_195995_a(), player, player.func_184586_b(ctx.func_221531_n()))) {
            return InteractionResult.SUCCESS;
        }
        return super.func_195939_a(ctx);
    }
    
    private boolean doOreScan(final World world, final BlockPos origin, final Player player, final ItemStack stack) {
        if (!world.func_201670_d() && player instanceof ServerPlayer && !MiscUtils.isPlayerFakeMP((ServerPlayer)player) && stack.getItem() instanceof ItemInfusedCrystalPickaxe && !player.func_184811_cZ().func_185141_a(stack.getItem())) {
            final PlayerProgress prog = ResearchHelper.getProgress(player, LogicalSide.SERVER);
            if (prog.doPerkAbilities()) {
                final List<BlockPos> orePositions = BlockDiscoverer.searchForBlocksAround(world, origin, 16, BlockPredicates.isInTag((ITag<Block>)TagsAS.Blocks.ORES));
                PacketChannel.CHANNEL.sendToPlayer(player, new PktOreScan(orePositions));
                player.func_184811_cZ().func_185145_a(stack.getItem(), 120);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Collection<PerkAttributeModifier> getModifiers(final ItemStack stack, final Player player, final LogicalSide side, final boolean ignoreRequirements) {
        return (Collection<PerkAttributeModifier>)Collections.singletonList(ItemInfusedCrystalPickaxe.MINING_SIZE_MODIFIER.get());
    }
    
    static {
        MODIFIER_ID = UUID.fromString("ecf80c60-3da6-4952-90d0-5db5429ea44a");
        MINING_SIZE_MODIFIER = new CacheReference<DynamicAttributeModifier>(() -> new DynamicAttributeModifier(ItemInfusedCrystalPickaxe.MODIFIER_ID, PerkAttributeTypesAS.ATTR_TYPE_MINING_SIZE, ModifierType.ADDITION, 1.0f));
    }
}
