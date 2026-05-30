package hellfirepvp.astralsorcery.common.item.lens;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import net.minecraft.world.level.block.Block;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import hellfirepvp.astralsorcery.common.auxiliary.BlockBreakHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.util.PartialEffectExecutor;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.AstralSorcery;

public class ItemColoredLensBreak extends ItemColoredLens
{
    private static final ColorTypeBreak COLOR_TYPE_BREAK;
    
    public ItemColoredLensBreak() {
        super(ItemColoredLensBreak.COLOR_TYPE_BREAK);
    }
    
    static {
        COLOR_TYPE_BREAK = new ColorTypeBreak();
    }
    
    private static class ColorTypeBreak extends LensColorType
    {
        private ColorTypeBreak() {
            super(AstralSorcery.key("break"), TargetType.BLOCK, () -> new ItemStack((ItemLike)ItemsAS.COLORED_LENS_BREAK), ColorsAS.COLORED_LENS_BREAK, 0.1f, false);
        }
        
        @Override
        public void entityInBeam(final Level world, final Vector3 origin, final Vector3 target, final Entity entity, final PartialEffectExecutor executor) {
        }
        
        @Override
        public void blockInBeam(final Level world, final BlockPos pos, final BlockState state, final PartialEffectExecutor executor) {
            if (world.level()) {

            }
            final boolean ranOnce = executor.executeAll(() -> BlockBreakHelper.addProgress(world, pos, 0.4f, () -> {
                final float hardness = state.func_185887_b((IBlockReader)world, pos);
                if (hardness < 0.0f) {
                    return null;
                }
                else {
                    return Float.valueOf(hardness * Math.max(1, state.getHarvestLevel()));
                }
            }));
            if (ranOnce) {
                final PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.Type.BEAM_BREAK).addData(buf -> {
                    ByteBufUtils.writePos(buf, pos);
                    buf.writeInt(Block.func_196246_j(state));

                });
                PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos(world, (Vec3i)pos, 16.0));
            }
        }
    }
}
