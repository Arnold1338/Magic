package hellfirepvp.astralsorcery.common.auxiliary.link;

import java.util.List;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import com.google.common.collect.Lists;
import net.minecraft.world.level.entity.LivingEntity;
import net.minecraft.world.level.entity.player.Player;
import javax.annotation.Nullable;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import net.minecraft.world.level.level.Level;

public interface LinkableTileEntity
{
    default World getLinkWorld() {
        if (this instanceof BlockEntity) {
            return ((BlockEntity)this).func_145831_w();
        }
        throw new IllegalStateException("LinkableTileEntity not implemented on BlockEntity: " + this.getClass());
    }
    
    default BlockPos getLinkPos() {
        if (this instanceof BlockEntity) {
            return ((BlockEntity)this).func_174877_v();
        }
        throw new IllegalStateException("LinkableTileEntity not implemented on BlockEntity: " + this.getClass());
    }
    
    @Nullable
    default String getUnLocalizedDisplayName() {
        if (this instanceof BlockEntity) {
            final BlockState state = ((BlockEntity)this).func_195044_w();
            return state.getBlock().func_149739_a();
        }
        throw new IllegalStateException("LinkableTileEntity not implemented on BlockEntity: " + this.getClass());
    }
    
    default boolean doesAcceptLinks() {
        return true;
    }
    
    void onBlockLinkCreate(final Player p0, final BlockPos p1);
    
    void onEntityLinkCreate(final Player p0, final LivingEntity p1);
    
    default boolean onSelect(final Player player) {
        if (player.func_225608_bj_()) {
            for (final BlockPos linkTo : Lists.newArrayList((Iterable)this.getLinkedPositions())) {
                this.tryUnlink(player, linkTo);
            }
            player.func_145747_a((Component)new Component("astralsorcery.misc.link.unlink.all").func_240699_a_(ChatFormatting.GREEN), Util.NIL_UUID);
            return false;
        }
        return true;
    }
    
    boolean tryLinkBlock(final Player p0, final BlockPos p1);
    
    boolean tryLinkEntity(final Player p0, final LivingEntity p1);
    
    boolean tryUnlink(final Player p0, final BlockPos p1);
    
    List<BlockPos> getLinkedPositions();
}
