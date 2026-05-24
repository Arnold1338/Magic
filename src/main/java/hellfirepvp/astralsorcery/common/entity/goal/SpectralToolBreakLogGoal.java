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
import net.minecraft.entity.ai.controller.MovementController;
import hellfirepvp.astralsorcery.common.util.block.BlockDiscoverer;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.block.BlockUtils;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.block.BlockPredicate;
import hellfirepvp.astralsorcery.common.entity.EntitySpectralTool;
import net.minecraft.core.BlockPos;

public class SpectralToolBreakLogGoal extends SpectralToolGoal
{
    private BlockPos selectedBreakPos;
    
    public SpectralToolBreakLogGoal(final EntitySpectralTool entity, final double speed) {
        super(entity, speed);
        this.selectedBreakPos = null;
    }
    
    private BlockPredicate breakableLogs() {
        return (world, pos, state) -> {
            final boolean b;
            if (MiscUtils.getTileAt((IBlockReader)world, pos, BlockEntity.class, false) == null && pos.getY() >= this.getEntity().getStartPosition().getY() && !state.isAir((IBlockReader)world, pos) && state.func_185887_b((IBlockReader)world, pos) != -1.0f && state.func_185887_b((IBlockReader)world, pos) <= 10.0f && (state.func_235714_a_((ITag)BlockTags.field_200031_h) || state.func_235714_a_((ITag)BlockTags.field_206952_E))) {
                if (BlockUtils.canToolBreakBlockWithoutPlayer(world, pos, state, new ItemStack((ItemLike)Items.field_151056_x))) {
                    return b;
                }
            }
            return b;
        };
    }
    
    public boolean func_75250_a() {
        final MovementController ctrl = this.getEntity().func_70605_aq();
        if (!ctrl.func_75640_a()) {
            return true;
        }
        final BlockPos validPos = BlockDiscoverer.searchAreaForFirst(this.getEntity().func_130014_f_(), this.getEntity().getStartPosition(), 8, Vector3.atEntityCorner((Entity)this.getEntity()), this.breakableLogs());
        return validPos != null;
    }
    
    public boolean func_75253_b() {
        return this.selectedBreakPos != null;
    }
    
    public void func_75249_e() {
        super.func_75249_e();
        final BlockPos validPos = BlockDiscoverer.searchAreaForFirst(this.getEntity().func_130014_f_(), this.getEntity().getStartPosition(), 10, Vector3.atEntityCorner((Entity)this.getEntity()), this.breakableLogs());
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
        final World world = this.getEntity().func_130014_f_();
        boolean resetTimer = false;
        if (world.isEmptyBlock(this.selectedBreakPos)) {
            this.selectedBreakPos = null;
            resetTimer = true;
        }
        else {
            this.getEntity().func_70605_aq().func_75642_a(this.selectedBreakPos.getX() + 0.5, this.selectedBreakPos.getY() + 0.5, this.selectedBreakPos.getZ() + 0.5, this.getSpeed());
            if (Vector3.atEntityCorner((Entity)this.getEntity()).distanceSquared((Vector3i)this.selectedBreakPos) <= 9.0) {
                ++this.actionCooldown;
                if (this.actionCooldown >= (int)MantleEffectPelotrio.CONFIG.ticksPerAxeLogBreak.get() && world instanceof ServerLevel) {
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
                                ItemUtils.dropItemNaturally(world, owner.func_226277_ct_(), owner.func_226278_cu_(), owner.func_226281_cx_(), remainder);
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
