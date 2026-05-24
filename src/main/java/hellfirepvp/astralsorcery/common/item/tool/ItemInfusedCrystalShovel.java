package hellfirepvp.astralsorcery.common.item.tool;

import net.minecraft.network.FriendlyByteBuf;
import java.util.List;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.Level;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import hellfirepvp.astralsorcery.common.util.block.BlockDiscoverer;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.event.EventFlags;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public class ItemInfusedCrystalShovel extends ItemCrystalShovel
{
    public boolean onBlockStartBreak(final ItemStack itemstack, final BlockPos pos, final Player player) {
        final World world = player.func_130014_f_();
        if (!world.func_201670_d() && !player.func_225608_bj_() && !player.func_184811_cZ().func_185141_a(itemstack.getItem()) && player instanceof ServerPlayer) {
            final PlayerProgress prog = ResearchHelper.getProgress(player, LogicalSide.SERVER);
            if (prog.doPerkAbilities()) {
                EventFlags.CHAIN_MINING.executeWithFlag(() -> {
                    if (!world.getBlockState(pos).isAir((IBlockReader)world, pos)) {
                        final List<BlockPos> foundBlocks = BlockDiscoverer.discoverBlocksWithSameStateAround(world, pos, true, 8, 200, false);
                        if (!foundBlocks.isEmpty()) {
                            final ServerPlayer serverPlayer = (ServerPlayer)player;
                            foundBlocks.forEach(at -> {
                                final BlockState currentState = world.getBlockState(at);
                                if (!currentState.isAir((IBlockReader)world, at) && serverPlayer.field_71134_c.func_180237_b(at)) {
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
                    }
                    return;
                });
            }
        }
        return super.onBlockStartBreak(itemstack, pos, player);
    }
}
