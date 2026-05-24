package hellfirepvp.astralsorcery.common.tile;

import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraft.nbt.Tag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.item.ItemGlassLens;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import hellfirepvp.astralsorcery.common.tile.altar.AltarCollectionCategory;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXMotionController;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.List;
import hellfirepvp.astralsorcery.common.util.block.BlockDiscoverer;
import hellfirepvp.astralsorcery.common.block.tile.BlockSpectralRelay;
import java.util.function.Consumer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.phys.Vec3;
import net.minecraft.world.level.level.Level;
import net.minecraft.world.level.level.LevelReader;
import net.minecraft.world.level.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import hellfirepvp.astralsorcery.common.tile.base.TileEntitySynchronized;
import net.minecraft.core.Direction;
import net.minecraft.world.level.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.util.tile.TileInventory;
import hellfirepvp.astralsorcery.common.tile.base.TileEntityTick;

public class TileSpectralRelay extends TileEntityTick
{
    private TileInventory inventory;
    private BlockPos altarPos;
    private BlockPos closestRelayPos;
    private float proximityMultiplier;
    
    public TileSpectralRelay() {
        super(TileEntityTypesAS.SPECTRAL_RELAY);
        this.proximityMultiplier = 1.0f;
        this.inventory = new TileInventory(this, () -> 1, new Direction[0]);
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (!this.func_145831_w().func_201670_d()) {
            if (!this.func_145831_w().isEmptyBlock(this.func_174877_v().above())) {
                final ItemStack in = this.getInventory().getStackInSlot(0);
                if (!in.isEmpty()) {
                    final ItemStack out = ItemUtils.copyStackWithSize(in, in.func_190916_E());
                    ItemUtils.dropItem(this.func_145831_w(), this.func_174877_v().getX(), this.func_174877_v().getY(), this.func_174877_v().getZ(), out);
                    this.getInventory().setStackInSlot(0, ItemStack.field_190927_a);
                }
            }
            if (this.hasMultiblock() && this.hasGlassLens() && this.altarPos != null) {
                MiscUtils.executeWithChunk((IWorldReader)this.func_145831_w(), this.altarPos, () -> {
                    final TileAltar ta = MiscUtils.getTileAt((IBlockReader)this.func_145831_w(), this.altarPos, TileAltar.class, true);
                    if (ta == null) {
                        this.updateAltarLinkState();
                    }
                    else {
                        this.provideStarlight(ta);
                    }
                });
            }
        }
        else if (this.hasMultiblock() && this.hasGlassLens()) {
            this.playStructureParticles();
            if (this.altarPos != null && this.doesSeeSky()) {
                this.playAltarParticles();
            }
        }
    }
    
    @Override
    protected void onFirstTick() {
        this.updateRelayProximity();
    }
    
    public static void cascadeRelayProximityUpdates(final World world, final BlockPos pos) {
        if (world.func_201670_d()) {
            return;
        }
        foreachNearbyRelay(world, pos, TileSpectralRelay::updateRelayProximity);
    }
    
    private void updateRelayProximity() {
        if (this.func_145831_w().func_201670_d() || !this.hasGlassLens()) {
            return;
        }
        this.setClosestRelayPos(null);
        final BlockPos thisPos = this.func_174877_v();
        final Vec3 thisVPos = Vec3.func_237491_b_((Vector3i)thisPos);
        foreachNearbyRelay(this.func_145831_w(), thisPos, relay -> {
            final BlockPos relayPos = relay.func_174877_v();
            if (!relayPos.equals((Object)thisPos)) {
                final Vec3 relayVPos = Vec3.func_237491_b_((Vector3i)relayPos);
                final BlockPos otherClosestPos = relay.closestRelayPos;
                if (otherClosestPos == null || thisPos.func_218138_a((IPosition)relayVPos, false) < otherClosestPos.func_218138_a((IPosition)relayVPos, false)) {
                    relay.setClosestRelayPos(thisPos);
                }
                if (this.closestRelayPos == null || relayPos.func_218138_a((IPosition)thisVPos, false) < this.closestRelayPos.func_218138_a((IPosition)thisVPos, false)) {
                    this.setClosestRelayPos(relayPos);
                }
            }
        });
    }
    
    private static void foreachNearbyRelay(final World world, final BlockPos pos, final Consumer<TileSpectralRelay> relayConsumer) {
        final List<BlockPos> nearbyRelays = BlockDiscoverer.searchForBlocksAround(world, pos, 8, (world1, pos1, state) -> {
            final TileSpectralRelay relay;
            return state.getBlock() instanceof BlockSpectralRelay && (relay = MiscUtils.getTileAt((IBlockReader)world1, pos1, TileSpectralRelay.class, false)) != null && relay.hasGlassLens() && relay.hasMultiblock();
        });
        nearbyRelays.forEach(relayPos -> {
            final TileSpectralRelay relay2 = MiscUtils.getTileAt((IBlockReader)world, relayPos, TileSpectralRelay.class, false);
            if (relay2 != null) {
                relayConsumer.accept(relay2);
            }
        });
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playAltarParticles() {
        final Vector3 pos = new Vector3(this).add(0.5, 0.35, 0.5);
        final Vector3 target = new Vector3((Vector3i)this.altarPos).add(0.5, 0.5, 0.5);
        int maxAge = 30;
        maxAge *= (int)Math.max(pos.distance(target) / 3.0, 1.0);
        final EntityVisualFX vfx = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).alpha(VFXAlphaFunction.proximity(target::clone, 2.0f).andThen(VFXAlphaFunction.FADE_OUT)).motion(VFXMotionController.target(target::clone, 0.08f)).setMotion(Vector3.random().normalize().multiply(0.1f + TileSpectralRelay.rand.nextFloat() * 0.05f)).setScaleMultiplier(0.15f + TileSpectralRelay.rand.nextFloat() * 0.05f).setMaxAge(maxAge);
        if (TileSpectralRelay.rand.nextBoolean()) {
            vfx.color(VFXColorFunction.WHITE);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playStructureParticles() {
        if (TileSpectralRelay.rand.nextBoolean()) {
            final Vector3 pos = new Vector3(this).add(0.5, 0.0, 0.5);
            final Vector3 offset = new Vector3(0, 0, 0);
            MiscUtils.applyRandomOffset(offset, TileSpectralRelay.rand, 1.25f);
            pos.add(offset.getX(), 0.0, offset.getZ());
            final EntityVisualFX vfx = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.15f + TileSpectralRelay.rand.nextFloat() * 0.1f).setGravityStrength(-0.001f).setMaxAge(30 + TileSpectralRelay.rand.nextInt(20));
            if (TileSpectralRelay.rand.nextBoolean()) {
                vfx.color(VFXColorFunction.WHITE);
            }
        }
    }
    
    private void provideStarlight(final TileAltar ta) {
        if (this.doesSeeSky()) {
            float heightAmount = Mth.func_76131_a((float)Math.pow(this.func_174877_v().getY() / 7.0f, 1.5) / 60.0f, 0.0f, 1.0f);
            heightAmount = 0.7f + heightAmount * 0.3f;
            heightAmount *= DayTimeHelper.getCurrentDaytimeDistribution(this.func_145831_w());
            heightAmount *= this.proximityMultiplier;
            if (heightAmount > 1.0E-4) {
                ta.collectStarlight(heightAmount * 45.0f, AltarCollectionCategory.RELAY);
            }
        }
    }
    
    @Nullable
    @Override
    public StructureType getRequiredStructureType() {
        if (this.hasGlassLens()) {
            return StructureTypesAS.PTYPE_SPECTRAL_RELAY;
        }
        return null;
    }
    
    @Override
    protected void notifyMultiblockStateUpdate(final boolean hadMultiblockPrev, final boolean hasMultiblockNow) {
        if (!hasMultiblockNow && this.altarPos != null) {
            this.altarPos = null;
        }
        if (hasMultiblockNow && this.hasGlassLens()) {
            this.updateAltarPos();
        }
    }
    
    public void updateAltarLinkState() {
        if (!this.hasGlassLens() || !this.hasMultiblock()) {
            this.altarPos = null;
            this.markForUpdate();
            return;
        }
        this.updateAltarPos();
    }
    
    private void updateAltarPos() {
        final Set<BlockPos> altarPositions = BlockDiscoverer.searchForTileEntitiesAround(this.func_145831_w(), this.func_174877_v(), 16, tile -> tile instanceof TileAltar);
        final Vec3 thisPos = Vec3.func_237491_b_((Vector3i)this.func_174877_v());
        BlockPos closestAltar = null;
        for (final BlockPos other : altarPositions) {
            if (closestAltar == null || other.func_218138_a((IPosition)thisPos, false) < closestAltar.func_218138_a((IPosition)thisPos, false)) {
                closestAltar = other;
            }
        }
        this.altarPos = closestAltar;
        this.markForUpdate();
    }
    
    private void setClosestRelayPos(@Nullable final BlockPos closestRelayPos) {
        this.closestRelayPos = closestRelayPos;
        this.markForUpdate();
        if (this.closestRelayPos == null) {
            this.proximityMultiplier = 1.0f;
        }
        else {
            this.proximityMultiplier = Mth.func_76131_a((float)new Vector3((Vector3i)this.func_174877_v()).distance((Vector3i)this.closestRelayPos) / 8.0f, 0.0f, 1.0f);
        }
    }
    
    public boolean hasGlassLens() {
        return this.getInventory().getStackInSlot(0).getItem() instanceof ItemGlassLens;
    }
    
    @Nonnull
    public TileInventory getInventory() {
        return this.inventory;
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.inventory = this.inventory.deserialize(compound.func_74775_l("inventory"));
        if (compound.contains("altarPos")) {
            this.altarPos = NBTHelper.readBlockPosFromNBT(compound.func_74775_l("altarPos"));
        }
        else {
            this.altarPos = null;
        }
        if (compound.contains("closestRelayPos")) {
            this.setClosestRelayPos(NBTHelper.readBlockPosFromNBT(compound.func_74775_l("closestRelayPos")));
        }
        else {
            this.setClosestRelayPos(null);
        }
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        compound.put("inventory", (Tag)this.inventory.serialize());
        if (this.altarPos != null) {
            compound.put("altarPos", (Tag)NBTHelper.writeBlockPosToNBT(this.altarPos, new CompoundTag()));
        }
        if (this.closestRelayPos != null) {
            compound.put("closestRelayPos", (Tag)NBTHelper.writeBlockPosToNBT(this.closestRelayPos, new CompoundTag()));
        }
    }
    
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if (this.inventory.hasCapability(cap, side)) {
            return (LazyOptional<T>)this.inventory.getCapability().cast();
        }
        return (LazyOptional<T>)super.getCapability((Capability)cap, side);
    }
}
