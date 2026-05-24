package hellfirepvp.astralsorcery.common.item.lens;

import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import net.minecraft.world.level.LevelAccessor;
import hellfirepvp.astralsorcery.common.auxiliary.CropHelper;
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
import java.util.Random;

public class ItemColoredLensGrowth extends ItemColoredLens
{
    private static final ColoredTypeGrowth COLORED_TYPE_GROWTH;
    
    public ItemColoredLensGrowth() {
        super(ItemColoredLensGrowth.COLORED_TYPE_GROWTH);
    }
    
    static {
        COLORED_TYPE_GROWTH = new ColoredTypeGrowth();
    }
    
    private static class ColoredTypeGrowth extends LensColorType
    {
        private ColoredTypeGrowth() {
            super(AstralSorcery.key("growth"), TargetType.BLOCK, () -> new ItemStack((ItemLike)ItemsAS.COLORED_LENS_GROWTH), ColorsAS.COLORED_LENS_GROWTH, 0.1f, false);
        }
        
        @Override
        public void entityInBeam(final World world, final Vector3 origin, final Vector3 target, final Entity entity, final PartialEffectExecutor executor) {
        }
        
        @Override
        public void blockInBeam(final World world, final BlockPos pos, final BlockState state, final PartialEffectExecutor executor) {
            if (world.func_201670_d()) {
                return;
            }
            final CropHelper.GrowablePlant plant = CropHelper.wrapPlant((IWorld)world, pos);
            if (plant != null) {
                executor.executeAll(() -> {
                    if (ItemColoredLensGrowth.field_77697_d.nextInt(18) == 0) {
                        plant.tryGrow((IWorld)world, ItemColoredLensGrowth.field_77697_d);
                        final PktPlayEffect packet = new PktPlayEffect(PktPlayEffect.Type.CROP_GROWTH).addData(buf -> ByteBufUtils.writeVector(buf, new Vector3((Vector3i)pos)));
                        PacketChannel.CHANNEL.sendToAllAround(packet, PacketChannel.pointFromPos(world, (Vector3i)pos, 16.0));
                    }
                });
            }
        }
    }
}
