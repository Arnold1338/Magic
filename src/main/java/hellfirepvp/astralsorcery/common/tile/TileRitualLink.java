package hellfirepvp.astralsorcery.common.tile;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Iterator;
import java.util.Collection;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.auxiliary.link.LinkableTileEntity;
import hellfirepvp.astralsorcery.common.tile.base.TileEntityTick;

public class TileRitualLink extends TileEntityTick implements LinkableTileEntity
{
    private BlockPos linkedTo;
    
    public TileRitualLink() {
        super(TileEntityTypesAS.RITUAL_LINK);
        this.linkedTo = null;
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (this.func_145831_w().func_201670_d()) {
            this.playClientEffects();
        }
        else if (this.linkedTo != null) {
            MiscUtils.executeWithChunk((IWorldReader)this.func_145831_w(), this.linkedTo, () -> {
                final TileRitualLink link = MiscUtils.getTileAt((IBlockReader)this.func_145831_w(), this.linkedTo, TileRitualLink.class, true);
                if (link == null) {
                    this.linkedTo = null;
                    this.markForUpdate();
                }
            });
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playClientEffects() {
        if (this.linkedTo != null) {
            if (this.ticksExisted % 4 == 0) {
                final Collection<Vector3> positions = MiscUtils.getCirclePositions(new Vector3(this).add(0.5, 0.5, 0.5), Vector3.RotAxis.Y_AXIS, 0.4f - TileRitualLink.rand.nextFloat() * 0.1f, 10 + TileRitualLink.rand.nextInt(10));
                for (final Vector3 v : positions) {
                    final FXFacingParticle particle = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(v).setScaleMultiplier(0.15f).setMotion(new Vector3(0.0, (TileRitualLink.rand.nextBoolean() ? 1 : -1) * TileRitualLink.rand.nextFloat() * 0.01, 0.0));
                    if (TileRitualLink.rand.nextBoolean()) {
                        particle.color(VFXColorFunction.WHITE);
                    }
                }
            }
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3(this).add(0.5, 0.5, 0.5)).setScaleMultiplier(0.3f).setMotion(new Vector3(0.0, (TileRitualLink.rand.nextBoolean() ? 1 : -1) * TileRitualLink.rand.nextFloat() * 0.015, 0.0)).color(VFXColorFunction.random());
        }
    }
    
    @Nullable
    public BlockPos getLinkedTo() {
        return this.linkedTo;
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.linkedTo = NBTHelper.readFromSubTag(compound, "posLink", NBTHelper::readBlockPosFromNBT);
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        if (this.linkedTo != null) {
            NBTHelper.setAsSubTag(compound, "posLink", nbt -> NBTHelper.writeBlockPosToNBT(this.linkedTo, nbt));
        }
    }
    
    @Override
    public void onBlockLinkCreate(final Player player, final BlockPos other) {
        if (this.linkedTo != null) {
            final TileRitualLink otherLink = MiscUtils.getTileAt((IBlockReader)player.func_130014_f_(), this.linkedTo, TileRitualLink.class, true);
            if (otherLink != null) {
                otherLink.linkedTo = null;
                otherLink.markForUpdate();
            }
        }
        this.linkedTo = other;
        final TileRitualLink otherLink = MiscUtils.getTileAt((IBlockReader)player.func_130014_f_(), other, TileRitualLink.class, true);
        if (otherLink != null) {
            otherLink.linkedTo = this.func_174877_v();
            otherLink.markForUpdate();
        }
        this.markForUpdate();
    }
    
    @Override
    public void onEntityLinkCreate(final Player player, final LivingEntity linked) {
    }
    
    @Override
    public boolean tryLinkBlock(final Player player, final BlockPos other) {
        final TileRitualLink otherLink = MiscUtils.getTileAt((IBlockReader)player.func_130014_f_(), other, TileRitualLink.class, true);
        return otherLink != null && otherLink.linkedTo == null && !other.equals((Object)this.func_174877_v());
    }
    
    @Override
    public boolean tryLinkEntity(final Player player, final LivingEntity other) {
        return false;
    }
    
    @Override
    public boolean tryUnlink(final Player player, final BlockPos other) {
        final TileRitualLink otherLink = MiscUtils.getTileAt((IBlockReader)player.func_130014_f_(), other, TileRitualLink.class, true);
        if (otherLink == null || otherLink.linkedTo == null) {
            return false;
        }
        if (otherLink.linkedTo.equals((Object)this.func_174877_v())) {
            this.linkedTo = null;
            otherLink.linkedTo = null;
            otherLink.markForUpdate();
            this.markForUpdate();
            return true;
        }
        return false;
    }
    
    @Override
    public List<BlockPos> getLinkedPositions() {
        return (this.linkedTo != null) ? Lists.newArrayList((Object[])new BlockPos[] { this.linkedTo }) : Lists.newArrayList();
    }
}
