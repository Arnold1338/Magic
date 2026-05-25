package hellfirepvp.astralsorcery.common.tile;

import net.minecraftforge.eventbus.api.Event;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.base.TreeType;
import net.minecraftforge.event.world.SaplingGrowTreeEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionReceiver;
import net.minecraft.nbt.Tag;
import hellfirepvp.astralsorcery.common.util.MapStream;
import net.minecraft.nbt.ListTag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.resources.ResourceKey;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.CalendarUtils;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.base.patreon.types.TypeTreeBeaconColor;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffectHelper;
import java.util.Optional;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.client.effect.function.VFXMotionController;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightbeam;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.BlockTags;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.world.level.block.Blocks;
import java.util.Comparator;
import java.util.function.Supplier;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import java.util.List;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import hellfirepvp.astralsorcery.common.util.block.BlockUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import net.minecraft.world.level.BlockGetter;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.util.Mth;
import java.util.HashMap;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import java.util.Map;
import hellfirepvp.astralsorcery.common.tile.base.TileAreaOfInfluence;
import hellfirepvp.astralsorcery.common.tile.network.StarlightReceiverTreeBeacon;
import hellfirepvp.astralsorcery.common.tile.base.network.TileReceiverBase;

public class TileTreeBeacon extends TileReceiverBase<StarlightReceiverTreeBeacon> implements TileAreaOfInfluence
{
    private final Map<BlockPos, Integer> treeComponents;
    private UUID playerUUID;
    private float starlight;
    
    public TileTreeBeacon() {
        super(TileEntityTypesAS.TREE_BEACON);
        this.treeComponents = new HashMap<BlockPos, Integer>();
        this.playerUUID = null;
        this.starlight = 0.0f;
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (this.getLevel().level()) {
            this.playEffects();
        }
        else {
            this.doHarvestCycle();
        }
    }
    
    private void doHarvestCycle() {
        final boolean changed = this.starlight > 0.0f || !this.treeComponents.isEmpty();
        final int cycles = Math.max(1, Mth.func_76123_f(this.starlight * 0.8f));
        this.starlight = 0.0f;
        for (int i = 0; i < cycles; ++i) {
            final float filled = this.treeComponents.size() / (float)Config.CONFIG.maxCount.get();
            if (TileTreeBeacon.rand.nextFloat() < filled * 0.25f) {
                final BlockPos pos = MiscUtils.getWeightedRandomEntry(this.treeComponents.keySet(), TileTreeBeacon.rand, this.treeComponents::get);
                if (pos != null) {
                    final TileTreeBeaconComponent component = MiscUtils.getTileAt((IBlockReader)this.getLevel(), pos, TileTreeBeaconComponent.class, false);
                    if (component != null && this.harvestTree(component)) {
                        final int breakChance = (int)Config.CONFIG.breakChance.get();
                        if (breakChance > 0 && TileTreeBeacon.rand.nextInt(breakChance) == 0 && component.removeSelf()) {
                            this.treeComponents.remove(pos);
                        }
                        final PktPlayEffect effect = new PktPlayEffect(PktPlayEffect.Type.BLOCK_HARVEST_DRAW).addData(buf -> {
                            ByteBufUtils.writeVector(buf, new Vector3((Vector3i)pos).add(0.5, 0.5, 0.5));
                            ByteBufUtils.writeVector(buf, new Vector3((Vector3i)this.getBlockState()).add(0.5, 0.5, 0.5));
                            buf.writeInt(this.getColor(LogicalSide.SERVER).getRGB());
                            return;
                        });
                        PacketChannel.CHANNEL.sendToAllAround(effect, PacketChannel.pointFromPos(this.getLevel(), (Vector3i)this.getBlockState(), 32.0));
                    }
                }
            }
        }
        if (changed) {
            this.markForUpdate();
        }
    }
    
    private boolean harvestTree(final TileTreeBeaconComponent harvest) {
        if (TileTreeBeacon.rand.nextFloat() > (double)Config.CONFIG.dropChance.get()) {
            return true;
        }
        final Level world = this.getLevel();
        if (!(world instanceof ServerLevel)) {
            return false;
        }
        if (!MiscUtils.canEntityTickAt((IWorld)world, harvest.getBlockState())) {
            return false;
        }
        final List<ItemStack> drops = BlockUtils.getDrops((ServerLevel)world, harvest.getBlockState(), harvest.getFakedState(), 2, TileTreeBeacon.rand, ItemStack.EMPTY);
        drops.forEach(drop -> {
            if (drop.isEmpty()) {
                return;
            }
            else {
                final Vector3 offset = new Vector3(0.5, 0.5, 0.5);
                MiscUtils.applyRandomOffset(offset, TileTreeBeacon.rand, 2.0f);
                offset.setY(Math.abs(offset.getY()));
                final Vector3 at = new Vector3((Vector3i)this.getBlockState()).add(offset);
                ItemUtils.dropItemNaturally(world, at.getX(), at.getY(), at.getZ(), drop);
                return;
            }
        });
        return false;
    }
    
    public void receiveStarlight(final double amount, final IWeakConstellation type) {
        float mul = 1.0f;
        if (type.equals(ConstellationsAS.aevitas)) {
            mul = 1.4f;
        }
        this.starlight += (float)(Math.sqrt(amount) * mul);
    }
    
    private void captureTree(final Supplier<List<BlockPos>> treeGenerator) {
        final List<BlockPos> tree = treeGenerator.get();
        tree.stream().sorted(Comparator.comparing(pos -> pos.func_177951_i((Vector3i)this.getBlockState()))).filter(pos -> !this.addComponent(pos)).forEach(pos -> this.level.markAndNotifyBlock(pos, this.level.func_175726_f(pos), Blocks.AIR.defaultBlockState(), this.level.getBlockState(pos), 11, 512));
    }
    
    private boolean addComponent(final BlockPos pos) {
        if (this.treeComponents.size() >= (int)Config.CONFIG.maxCount.get()) {
            return false;
        }
        final Level world = this.getLevel();
        final BlockState state = world.getBlockState(pos);
        if (state.isAir((IBlockReader)world, pos) || !this.getLevel().func_180501_a(pos, BlocksAS.TREE_BEACON_COMPONENT.defaultBlockState(), 3)) {
            return false;
        }
        final TileTreeBeaconComponent tfs = MiscUtils.getTileAt((IBlockReader)world, pos, TileTreeBeaconComponent.class, true);
        if (tfs == null) {
            this.getLevel().func_180501_a(pos, state, 3);
            return false;
        }
        final boolean isLog = state.getBlock().func_203417_a((ITag)BlockTags.field_200031_h);
        tfs.setFakedState(state);
        tfs.setTreeBeaconPos(this.getBlockState());
        tfs.setOverlayColor(this.getColor(LogicalSide.SERVER));
        return this.treeComponents.put(pos, isLog ? ((Integer)Config.CONFIG.logWeight.get()) : ((Integer)Config.CONFIG.leafWeight.get())) == null;
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playEffects() {
        final Color color = this.getColor(LogicalSide.CLIENT);
        final VFXColorFunction<?> colorFn = VFXColorFunction.constant(color);
        final float radius = ((Double)Config.CONFIG.range.get()).floatValue();
        final Vector3 thisPos = new Vector3((Vector3i)this.getBlockState()).add(0.5, 0.5, 0.5);
        final int amt = Mth.func_76128_c(radius * 3.141592653589793 / 8.0);
        for (int i = 0; i < amt; ++i) {
            final Vector3 at = MiscUtils.getRandomCirclePosition(thisPos, Vector3.RotAxis.Y_AXIS, radius);
            MiscUtils.applyRandomOffset(at, TileTreeBeacon.rand, 0.35f);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).color(colorFn).setGravityStrength(-0.0015f + TileTreeBeacon.rand.nextFloat() * -0.001f).setScaleMultiplier(0.3f + TileTreeBeacon.rand.nextFloat() * 0.1f).setMaxAge(30 + TileTreeBeacon.rand.nextInt(20));
        }
        for (int i = 0; i < Math.ceil(amt * 1.5f); ++i) {
            final Vector3 offset = new Vector3(0.5, 0.5, 0.5);
            MiscUtils.applyRandomCircularOffset(offset, TileTreeBeacon.rand, radius);
            offset.setY(offset.getY() * 0.75);
            final Vector3 at2 = new Vector3((Vector3i)this.getBlockState()).add(offset);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at2).color(colorFn).setGravityStrength(TileTreeBeacon.rand.nextBoolean() ? -0.0015f : 0.0f).setScaleMultiplier(0.2f + TileTreeBeacon.rand.nextFloat() * 0.1f).setMaxAge(25 + TileTreeBeacon.rand.nextInt(10));
        }
        if (TileTreeBeacon.rand.nextInt(20) == 0) {
            float alphaDaytime = DayTimeHelper.getCurrentDaytimeDistribution(this.getLevel());
            alphaDaytime *= 0.8f;
            final Vector3 at = new Vector3(this).add(0.5, 0.05, 0.5);
            MiscUtils.applyRandomOffset(at, TileTreeBeacon.rand, 0.05f);
            EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).setOwner(this.playerUUID).spawn(at).setup(at.clone().addY(7.0), 1.5, 1.5).color(colorFn).setAlphaMultiplier(0.5f + 0.5f * alphaDaytime).setMaxAge(64);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playDrawParticles(final PktPlayEffect pkt) {
        final Vector3 from = ByteBufUtils.readVector(pkt.getExtraData());
        final Vector3 to = ByteBufUtils.readVector(pkt.getExtraData());
        final Color c = new Color(pkt.getExtraData().readInt());
        final VFXColorFunction<?> colorFn = VFXColorFunction.constant(c);
        for (int i = 0; i < 10; ++i) {
            final Vector3 at = new Vector3((Vector3i)from.toBlockPos()).add(TileTreeBeacon.rand.nextFloat(), TileTreeBeacon.rand.nextFloat(), TileTreeBeacon.rand.nextFloat());
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).motion(VFXMotionController.target(to::clone, 0.04f + TileTreeBeacon.rand.nextFloat() * 0.05f)).setScaleMultiplier(0.15f + TileTreeBeacon.rand.nextFloat() * 0.05f).color((TileTreeBeacon.rand.nextFloat() > 0.8f) ? VFXColorFunction.WHITE : colorFn).setMaxAge(30 + TileTreeBeacon.rand.nextInt(20));
        }
    }
    
    @Nullable
    public UUID getPlayerUUID() {
        return this.playerUUID;
    }
    
    public void setPlayerUUID(final UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.markForUpdate();
    }
    
    public Color getColor(final LogicalSide side) {
        return Optional.ofNullable(this.playerUUID).flatMap(uuid -> PatreonEffectHelper.getPatreonEffects(side, this.playerUUID).stream().filter(effect -> effect instanceof TypeTreeBeaconColor).map(effect -> (TypeTreeBeaconColor)effect).findFirst()).map((Function<? super Object, ? extends Color>)TypeTreeBeaconColor::getTreeBeaconColor).orElse(CalendarUtils.isAprilFirst() ? Color.getHSBColor(TileTreeBeacon.rand.nextFloat(), 1.0f, 1.0f) : ConstellationsAS.aevitas.getConstellationColor());
    }
    
    @Nonnull
    @Override
    public StarlightReceiverTreeBeacon provideEndpoint(final BlockPos at) {
        return new StarlightReceiverTreeBeacon(at);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public Color getEffectColor() {
        return this.getColor(LogicalSide.CLIENT);
    }
    
    @Nonnull
    @Override
    public Vector3 getEffectPosition() {
        return new Vector3(this).add(0.5, 0.5, 0.5);
    }
    
    @Override
    public float getRadius() {
        return ((Double)Config.CONFIG.range.get()).floatValue();
    }
    
    @Nonnull
    @Override
    public BlockPos getEffectOriginPosition() {
        return this.getBlockState();
    }
    
    @Nonnull
    @Override
    public RegistryKey<Level> getDimension() {
        return (RegistryKey<Level>)this.getLevel().dimension();
    }
    
    @Override
    public boolean providesEffect() {
        return !this.func_145837_r();
    }
    
    public void func_145829_t() {
        super.func_145829_t();
        TreeWatcher.WATCHERS.computeIfAbsent(this.getDimension(), type -> new HashSet()).add(this.getBlockState());
    }
    
    @Override
    public void func_145843_s() {
        super.func_145843_s();
        TreeWatcher.WATCHERS.computeIfAbsent(this.getDimension(), type -> new HashSet()).remove(this.getBlockState());
    }
    
    @Override
    public void onBreak() {
        super.onBreak();
        this.treeComponents.keySet().forEach(pos -> {
            final TileTreeBeaconComponent component = MiscUtils.getTileAt((IBlockReader)this.getLevel(), pos, TileTreeBeaconComponent.class, true);
            if (component != null) {
                component.revert();
            }
            return;
        });
        this.treeComponents.clear();
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.treeComponents.clear();
        final ListTag componentList = compound.getList("components", 10);
        for (int i = 0; i < componentList.size(); ++i) {
            final CompoundTag tag = componentList.getCompound(i);
            this.treeComponents.put(NBTHelper.readBlockPosFromNBT(tag), tag.getInt("weight"));
        }
        this.starlight = compound.getFloat("starlight");
        this.playerUUID = NBTHelper.getUUID(compound, "playerUUID", null);
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        final ListTag componentList = new ListTag();
        MapStream.forEach(this.treeComponents, (pos, weight) -> {
            final CompoundTag tag = new CompoundTag();
            NBTHelper.writeBlockPosToNBT(pos, tag);
            tag.putInt("weight", (int)weight);
            componentList.add((Object)tag);
            return;
        });
        compound.put("components", (Tag)componentList);
        compound.func_74776_a("starlight", this.starlight);
        if (this.playerUUID != null) {
            compound.putUUID("playerUUID", this.playerUUID);
        }
    }
    
    public static class Config extends ConfigEntry
    {
        public static final Config CONFIG;
        private static final float defaultRange = 12.0f;
        private static final int defaultMaxCount = 450;
        private static final float defaultDropChance = 0.15f;
        private static final int defaultBreakChance = 1000;
        private static final int defaultLogWeight = 2;
        private static final int defaultLeafWeight = 1;
        public ForgeConfigSpec.DoubleValue range;
        public ForgeConfigSpec.IntValue maxCount;
        public ForgeConfigSpec.DoubleValue dropChance;
        public ForgeConfigSpec.IntValue breakChance;
        public ForgeConfigSpec.IntValue logWeight;
        public ForgeConfigSpec.IntValue leafWeight;
        
        private Config() {
            super("tree_beacon");
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.range = cfgBuilder.comment("Set the radius of the tree beacon.").translation(this.translationKey("range")).defineInRange("range", 12.0, 3.0, 32.0);
            this.maxCount = cfgBuilder.comment("Set the maximum amount of tree-components the tree beacon may allocate.").translation(this.translationKey("maxCount")).defineInRange("maxCount", 450, 50, 1500);
            this.dropChance = cfgBuilder.comment("Set the chance per harvest-tick for drops to get created.").translation(this.translationKey("dropChance")).defineInRange("dropChance", 0.15000000596046448, 0.001, 1.0);
            this.breakChance = cfgBuilder.comment("Set the chance per harvest-tick for the block to get broken (1 in <configured chance>). 0 = blocks never break.").translation(this.translationKey("breakChance")).defineInRange("breakChance", 1000, 0, Integer.MAX_VALUE);
            this.logWeight = cfgBuilder.comment("Set the weight to pick a log-block to harvest instead of a leaf-block, compared to 'leafWeight'.").translation(this.translationKey("logWeight")).defineInRange("logWeight", 2, 1, 200);
            this.leafWeight = cfgBuilder.comment("Set the weight to pick a leaf-block (strictly speaking, any non-log block) to harvest instead of a log-block, compared to 'logWeight'.").translation(this.translationKey("leafWeight")).defineInRange("leafWeight", 1, 1, 200);
        }
        
        static {
            CONFIG = new Config();
        }
    }
    
    public static class TreeWatcher
    {
        private static final Map<RegistryKey<Level>, Set<BlockPos>> WATCHERS;
        
        public static void clearServerCache() {
            TreeWatcher.WATCHERS.clear();
        }
        
        public static void onGrow(final SaplingGrowTreeEvent event) {
            if (event.getWorld().level() || !(event.getWorld() instanceof ServerLevel)) {
                return;
            }
            final ServerLevel world = (ServerLevel)event.getWorld();
            final BlockPos treePos = event.getPos();
            final TreeType type = TreeType.isTree((Level)world, treePos);
            if (type == null) {
                return;
            }
            final double rangeSq = (double)Config.CONFIG.range.get() * (double)Config.CONFIG.range.get();
            final BlockPos closestBeacon = TreeWatcher.WATCHERS.getOrDefault(world.dimension(), Collections.emptySet()).stream().filter(pos -> pos.func_177951_i((Vector3i)treePos) < rangeSq).min(Comparator.comparing(pos -> pos.func_177951_i((Vector3i)treePos))).orElse(null);
            if (closestBeacon == null) {
                return;
            }
            final TileTreeBeacon ttb = MiscUtils.getTileAt((IBlockReader)world, closestBeacon, TileTreeBeacon.class, false);
            if (ttb == null) {
                return;
            }
            event.setResult(Event.Result.DENY);
            final Supplier<List<BlockPos>> generator = type.getTreeGenerator(world, treePos, event.getRand());
            ttb.captureTree(generator);
        }
        
        static {
            WATCHERS = new HashMap<RegistryKey<Level>, Set<BlockPos>>();
        }
    }
}
