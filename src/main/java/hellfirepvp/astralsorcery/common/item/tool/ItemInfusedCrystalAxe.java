package hellfirepvp.astralsorcery.common.item.tool;

import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import net.minecraft.network.FriendlyByteBuf;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import java.util.Collection;
import hellfirepvp.observerlib.api.util.BlockArray;
import net.minecraft.world.level.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.level.Level;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import hellfirepvp.astralsorcery.common.util.block.TreeDiscoverer;
import hellfirepvp.astralsorcery.common.event.EventFlags;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.perk.modifier.DynamicAttributeModifier;
import hellfirepvp.astralsorcery.common.util.object.CacheReference;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.perk.source.provider.equipment.EquipmentAttributeModifierProvider;

public class ItemInfusedCrystalAxe extends ItemCrystalAxe implements EquipmentAttributeModifierProvider
{
    private static final UUID MODIFIER_ID;
    private static final CacheReference<DynamicAttributeModifier> MINING_SPEED_MODIFIER;
    
    public boolean onBlockStartBreak(final ItemStack itemstack, final BlockPos pos, final Player player) {
        final World world = player.func_130014_f_();
        if (!world.func_201670_d() && !player.func_225608_bj_() && !player.func_184811_cZ().func_185141_a(itemstack.getItem()) && player instanceof ServerPlayer) {
            final PlayerProgress prog = ResearchHelper.getProgress(player, LogicalSide.SERVER);
            if (prog.doPerkAbilities()) {
                EventFlags.CHAIN_MINING.executeWithFlag(() -> {
                    final BlockArray tree = TreeDiscoverer.findTreeAt(world, pos, true, 9);
                    if (!tree.getContents().isEmpty()) {
                        final ServerPlayer serverPlayer = (ServerPlayer)player;
                        tree.getContents().keySet().forEach(at -> {
                            final BlockState currentState = world.getBlockState(at);
                            if (serverPlayer.field_71134_c.func_180237_b(at)) {
                                final PktPlayEffect ev = new PktPlayEffect(PktPlayEffect.Type.BLOCK_EFFECT).addData(buf -> {
                                    ByteBufUtils.writePos(buf, at);
                                    ByteBufUtils.writeBlockState(buf, currentState);
                                    return;
                                });
                                PacketChannel.CHANNEL.sendToAllAround(ev, PacketChannel.pointFromPos(world, (Vector3i)at, 32.0));
                            }
                            return;
                        });
                        serverPlayer.func_184811_cZ().func_185145_a(itemstack.getItem(), 120);
                    }
                    return;
                });
            }
        }
        return super.onBlockStartBreak(itemstack, pos, player);
    }
    
    @Override
    public Collection<PerkAttributeModifier> getModifiers(final ItemStack stack, final Player player, final LogicalSide side, final boolean ignoreRequirements) {
        return (Collection<PerkAttributeModifier>)Collections.singletonList(ItemInfusedCrystalAxe.MINING_SPEED_MODIFIER.get());
    }
    
    static {
        MODIFIER_ID = UUID.fromString("85c65b91-f44c-4aba-841d-7785eae32831");
        MINING_SPEED_MODIFIER = new CacheReference<DynamicAttributeModifier>(() -> new DynamicAttributeModifier(ItemInfusedCrystalAxe.MODIFIER_ID, PerkAttributeTypesAS.ATTR_TYPE_INC_HARVEST_SPEED, ModifierType.ADDED_MULTIPLY, 0.1f));
    }
}
