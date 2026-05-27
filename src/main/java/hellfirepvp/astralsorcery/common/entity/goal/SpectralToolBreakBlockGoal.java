package hellfirepvp.astralsorcery.common.entity.goal;

import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import hellfirepvp.astralsorcery.common.util.BlockDropCaptureAssist;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectPelotrio;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.ai.control.MoveControl;
import hellfirepvp.astralsorcery.common.util.block.BlockDiscoverer;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.block.BlockUtils;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.block.BlockPredicate;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import hellfirepvp.astralsorcery.common.entity.EntitySpectralTool;
import net.minecraft.core.BlockPos;

public class SpectralToolBreakBlockGoal extends SpectralToolGoal
{
    private BlockPos selectedBreakPos;
    
    public SpectralToolBreakBlockGoal(final EntitySpectralTool entity, final double speed) {
        super(entity, speed);
        this.selectedBreakPos = null;
        this.func_220684_a((EnumSet)EnumSet.of(Goal.Flag.MOVE, Goal.Flag.TARGET, Goal.Flag.LOOK));
    }
    
    private BlockPredicate breakableSimpleBlocks() {
        return (world, pos, state) -> {
            final boolean b;
            if (MiscUtils.getTileAt((IBlockReader)world, pos, BlockEntity.class, false) == null && pos.getY() >= this.getEntity().getStartPosition().getY() && !state.isAir((IBlockReader)world, pos) && state.func_185887_b((IBlockReader)world, pos) != -1.0f && state.func_185887_b((IBlockReader)world, pos) <= 10.0f) {
                if (BlockUtils.canToolBreakBlockWithoutPlayer(world, pos, state, new ItemStack((ItemLike)Items.field_151046_w))) {
                    return b;
                }
            }
            return b;
        };
    }
    
    public boolean func_75250_a() {
        final MoveControl ctrl = this.getEntity().func_70605_aq();
        if (!ctrl.func_75640_a()) {
            return true;
        }
        final BlockPos validPos = BlockDiscoverer.searchAreaForFirst(this.getEntity().level(), this.getEntity().getStartPosition(), 8, Vector3.atEntityCorner((Entity)this.getEntity()), this.breakableSimpleBlocks());
        return validPos != null;
    }
    
    public boolean func_75253_b() {
        return this.selectedBreakPos != null;
    }
    
    public void func_75249_e() {
        super.func_75249_e();
        final BlockPos validPos = BlockDiscoverer.searchAreaForFirst(this.getEntity().level(), this.getEntity().getStartPosition(), 8, Vector3.atEntityCorner((Entity)this.getEntity()), this.breakableSimpleBlocks());
        if (validPos != null) {
            this.selectedBreakPos = validPos;
            this.getEntity().func_70605_aq().func_75642_a(this.selectedBreakPos.getX() + 0.5, this.selectedBreakPos.getY() + 0.5, this.selectedBreakPos.getZ() + 0.5, this.getSpeed());
        }
    }
    
    public void func_75251_c() {
        super.func_75251_c();
        this.selectedBreakPos = null;
        this.actionCooldown = 0;
    }
    
    public void func_75246_d() {
        super.func_75246_d();
        if (!this.func_75253_b()) {
            return;
        }
        if (this.actionCooldown < 0) {
            this.actionCooldown = 0;
        }
        final Level world = this.getEntity().level();
        boolean resetTimer = false;
        if (world.isEmptyBlock(this.selectedBreakPos)) {
            this.selectedBreakPos = null;
            resetTimer = true;
        }
        else {
            this.getEntity().func_70605_aq().func_75642_a(this.selectedBreakPos.getX() + 0.5, this.selectedBreakPos.getY() + 0.5, this.selectedBreakPos.getZ() + 0.5, this.getSpeed());
            if (Vector3.atEntityCorner((Entity)this.getEntity()).distanceSquared((Vec3i)this.selectedBreakPos) <= 9.0) {
                ++this.actionCooldown;
                if (this.actionCooldown >= (int)MantleEffectPelotrio.CONFIG.ticksPerPickaxeBlockBreak.get() && world instanceof ServerLevel) {
                    final LivingEntity owner = this.getEntity().getOwningEntity();
                    if (owner instanceof Player) {
                        BlockDropCaptureAssist.startCapturing();
                    }
                    if (BlockUtils.breakBlockWithoutPlayer((ServerLevel)world, this.selectedBreakPos, world.getBlockState(this.selectedBreakPos), this.getEntity().getItem(), true, true, true)) {
                        resetTimer = true;
                    }
                    if (owner instanceof Player) {
                        for (final ItemStack dropped : BlockDropCaptureAssist.getCapturedStacksAndStop()) {
                            final ItemStack remainder = ItemUtils.dropItemToPlayer((Player)owner, dropped);
                            if (!remainder.isEmpty()) {
                                ItemUtils.dropItemNaturally(world, owner.getX(), owner.getY(), owner.getZ(), remainder);
                            }
                        }
                    }
                }
            }
        }
        if (resetTimer) {
            this.actionCooldown = 0;
        }
    }
}
