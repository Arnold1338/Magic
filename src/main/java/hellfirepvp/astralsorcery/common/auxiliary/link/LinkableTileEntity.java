package hellfirepvp.astralsorcery.common.auxiliary.link;

import java.util.List;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;

public interface LinkableTileEntity
{
    default Level getLinkWorld() {
        if (this instanceof BlockEntity) {
            return ((BlockEntity)this).getLevel();
        }
        throw new IllegalStateException("LinkableTileEntity not implemented on BlockEntity: " + this.getClass());
    }
    
    default BlockPos getLinkPos() {
        if (this instanceof BlockEntity) {
            return ((BlockEntity)this).getBlockState();
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
            player.func_145747_a((Component)new Component("astralsorcery.misc.link.unlink.all").withStyle(ChatFormatting.GREEN)), Util.NIL_UUID);
            return false;
        }
        return true;
    }
    
    boolean tryLinkBlock(final Player p0, final BlockPos p1);
    
    boolean tryLinkEntity(final Player p0, final LivingEntity p1);
    
    boolean tryUnlink(final Player p0, final BlockPos p1);
    
    List<BlockPos> getLinkedPositions();
}
