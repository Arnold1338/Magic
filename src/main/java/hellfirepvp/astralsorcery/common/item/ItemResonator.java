package hellfirepvp.astralsorcery.common.item;

import hellfirepvp.astralsorcery.common.data.research.ProgressionTier;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import net.minecraftforge.fluids.capability.IFluidHandler;
import hellfirepvp.astralsorcery.common.capability.ChunkFluidEntry;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import javax.annotation.Nonnull;
import net.minecraft.world.level.InteractionResult;
import hellfirepvp.astralsorcery.client.util.AreaOfInfluencePreview;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.base.TileAreaOfInfluence;
import net.minecraft.core.Direction;
import net.minecraft.world.level.InteractionHand;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.world.SkyCollectionHelper;
import hellfirepvp.astralsorcery.client.data.config.entry.RenderingConfig;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.common.util.world.WorldSeedCache;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.common.capabilities.Capability;
import hellfirepvp.astralsorcery.common.lib.CapabilitiesAS;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.item.base.OverrideInteractItem;
import net.minecraft.world.item.Item;

public class ItemResonator extends Item implements OverrideInteractItem
{
    public ItemResonator() {
        super(new Item.Properties().func_200917_a(1).hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
    
    public void func_150895_a(final CreativeModeTab group, final NonNullList<ItemStack> items) {
        if (this.func_194125_a(group)) {
            final ItemStack resonator = new ItemStack((ItemLike)this);
            setUpgradeUnlocked(resonator, ResonatorUpgrade.STARLIGHT);
            items.add((Object)resonator);
            final ItemStack upgradedResonator = new ItemStack((ItemLike)this);
            setUpgradeUnlocked(upgradedResonator, ResonatorUpgrade.values());
            items.add((Object)upgradedResonator);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final Level world, final List<Component> tooltip, final TooltipFlag extended) {
        final ResonatorUpgrade current = getCurrentUpgrade((Player)Minecraft.getInstance().player, stack);
        for (final ResonatorUpgrade upgrade : getUpgrades(stack)) {
            final ChatFormatting color = upgrade.equals(current) ? ChatFormatting.GOLD : ChatFormatting.BLUE;
            tooltip.add(Component.translatable(upgrade.getUnlocalizedTypeName().withStyle(color)));

        }
    }
    
    public void inventoryTick(final ItemStack stack, final Level world, final Entity entity, final int slot, boolean selected) {
        if (!selected) {
            selected = (entity instanceof LivingEntity && ((LivingEntity)entity).getOffhandItem() == stack);
        }
        if (!world.level()) {
            if (selected && entity instanceof ServerPlayer) {
                final ServerPlayer player = (ServerPlayer)entity;
                if (getCurrentUpgrade((Player)player, stack) == ResonatorUpgrade.FLUID_FIELDS) {
                    final float distribution = DayTimeHelper.getCurrentDaytimeDistribution(world);
                    if (distribution <= 1.0E-4) {

                    }
                    if (ItemResonator.count.nextFloat() < distribution && ItemResonator.count.nextInt(12) == 0) {
                        final int offsetX = ItemResonator.count.nextInt(30) * (ItemResonator.count.nextBoolean() ? 1 : -1);
                        final int offsetZ = ItemResonator.count.nextInt(30) * (ItemResonator.count.nextBoolean() ? 1 : -1);
                        final BlockPos pos = world.func_205770_a(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos((Vec3i)entity.func_233580_cy_()).offset(offsetX, 0, offsetZ));
                        if (pos.func_177951_i((Vec3i)entity.func_233580_cy_()) > 5625.0) {

                        }
                        final IChunk ch = world.func_217349_x(pos);
                        if (ch instanceof Chunk) {
                            ((Chunk)ch).getCapability((Capability)CapabilitiesAS.CHUNK_FLUID).ifPresent(entry -> {
                                final FluidStack display = entry.drain(1, IFluidHandler.FluidAction.SIMULATE);
                                if (!display.isEmpty()) {
                                    final PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.Type.LIQUID_FOUNTAIN).addData(buf -> {
                                        ByteBufUtils.writeFluidStack(buf, display);
                                        ByteBufUtils.writeVector(buf, new Vector3((Vec3i)pos));

                                    });
                                    PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos(world, (Vec3i)pos, 32.0));
                                }
                            });
                        }
                    }
                }
            }
        }
        else {
            this.clientInventoryTick(stack, world, entity, slot, selected);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void clientInventoryTick(final ItemStack stack, final Level world, final Entity entity, final int slot, final boolean selected) {
        if (!(entity instanceof Player)) {

        }
        final Player player = (Player)entity;
        if (selected && getCurrentUpgrade(player, stack) == ResonatorUpgrade.STARLIGHT && WorldSeedCache.getSeedIfPresent((ResourceKey<Level>)world.dimension()).isPresent()) {
            final float distribution = DayTimeHelper.getCurrentDaytimeDistribution(world);
            if (distribution <= 1.0E-4) {

            }
            final BlockPos center = player.func_233580_cy_();
            final int offsetX = center.getX();
            final int offsetZ = center.getZ();
            final BlockPos.Mutable mPos = new BlockPos.Mutable();
            final int minY = (int)RenderingConfig.CONFIG.minYFosicDisplay.get();
            for (int xx = -48; xx <= 48; ++xx) {
                for (int zz = -48; zz <= 48; ++zz) {
                    mPos.func_189533_g((Vec3i)world.func_205770_a(Heightmap.Type.WORLD_SURFACE, (BlockPos)mPos.func_181079_c(offsetX + xx, 0, offsetZ + zz)));
                    mPos.func_185336_p(Math.max(mPos.getY(), minY));
                    final float perc = SkyCollectionHelper.getSkyNoiseDistributionClient((ResourceKey<Level>)world.dimension(), (BlockPos)mPos).get();
                    final float fPerc = (float)Math.pow((perc - 0.4f) * 1.65f, 2.0);
                    if (perc >= 0.4f && ItemResonator.count.nextFloat() <= fPerc && ItemResonator.count.nextFloat() <= fPerc && ItemResonator.count.nextInt(6) == 0) {
                        EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3((Vec3i)mPos).add(ItemResonator.count.nextFloat(), 0.15, ItemResonator.count.nextFloat())).color(VFXColorFunction.constant(ColorsAS.RESONATOR_STARFIELD)).setScaleMultiplier(4.0f).setAlphaMultiplier(distribution * fPerc);
                        if (perc >= 0.8f && ItemResonator.count.nextInt(3) == 0) {
                            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3((Vec3i)mPos).add(ItemResonator.count.nextFloat(), 0.15, ItemResonator.count.nextFloat())).setScaleMultiplier(0.3f).color(VFXColorFunction.WHITE).setGravityStrength(-0.001f).setAlphaMultiplier(distribution);
                        }
                    }
                }
            }
        }
    }
    
    public boolean shouldInterceptBlockInteract(final LogicalSide side, final Player player, final Hand hand, final BlockPos pos, final Direction face) {
        final ResonatorUpgrade upgrade = getCurrentUpgrade(player, player.getItemInHand(hand));
        return upgrade == ResonatorUpgrade.AREA_SIZE && MiscUtils.getTileAt((IBlockReader)player.level(), pos, TileAreaOfInfluence.class, false) != null;
    }
    
    public boolean doBlockInteract(final LogicalSide side, final Player player, final Hand hand, final BlockPos pos, final Direction face) {
        final ResonatorUpgrade upgrade = getCurrentUpgrade(player, player.getItemInHand(hand));
        if (upgrade == ResonatorUpgrade.AREA_SIZE && player.level()) {
            final TileAreaOfInfluence aoeTile = MiscUtils.getTileAt((IBlockReader)player.level(), pos, TileAreaOfInfluence.class, false);
            if (aoeTile != null) {
                this.playAreaOfInfluenceEffect(aoeTile);
            }
        }
        return true;
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playAreaOfInfluenceEffect(final TileAreaOfInfluence aoeTile) {
        AreaOfInfluencePreview.INSTANCE.showOrRemoveIdentical(aoeTile);
    }
    
    public InteractionResult<ItemStack> use(final Level world, final Player player, final Hand hand) {
        if (!world.level().isClientSide() && player.isCrouching() && cycleUpgrade(player, player.getItemInHand(hand))) {
            return (InteractionResult<ItemStack>)InteractionResult.func_226248_a_((Object)player.getItemInHand(hand));
        }
        return (InteractionResult<ItemStack>)InteractionResult.func_226250_c_((Object)player.getItemInHand(hand));
    }
    
    public static boolean cycleUpgrade(@Nonnull final Player player, final ItemStack stack) {
        final ResonatorUpgrade current = getCurrentUpgrade(player, stack);
        final ResonatorUpgrade next = getNextSelectableUpgrade(player, stack);
        return next != null && !next.equals(current) && setCurrentUpgrade(player, stack, next);
    }
    
    @Nullable
    public static ResonatorUpgrade getNextSelectableUpgrade(@Nonnull final Player viewing, final ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemResonator)) {
            return null;
        }
        final ResonatorUpgrade current = getCurrentUpgrade(viewing, stack);
        int test;
        final int currentOrd = test = current.ordinal();
        do {
            test = ++test % ResonatorUpgrade.values().length;
            final ResonatorUpgrade testUpgrade = ResonatorUpgrade.values()[test];
            if (testUpgrade.canSwitchTo(viewing, stack) && !testUpgrade.equals(current)) {
                return testUpgrade;
            }
        } while (test != currentOrd);
        return null;
    }
    
    public static boolean setCurrentUpgrade(final Player setting, final ItemStack stack, final ResonatorUpgrade upgrade) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemResonator)) {
            return false;
        }
        if (upgrade.canSwitchTo(setting, stack)) {
            NBTHelper.getPersistentData(stack).putInt("selected_upgrade", upgrade.ordinal());
            return true;
        }
        return false;
    }
    
    public static ItemStack setCurrentUpgradeUnsafe(final ItemStack stack, final ResonatorUpgrade upgrade) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemResonator)) {
            return stack;
        }
        NBTHelper.getPersistentData(stack).putInt("selected_upgrade", upgrade.ordinal());
        return stack;
    }
    
    @Nonnull
    public static ResonatorUpgrade getCurrentUpgrade(@Nullable final Player viewing, final ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemResonator)) {
            return ResonatorUpgrade.STARLIGHT;
        }
        final CompoundTag cmp = NBTHelper.getPersistentData(stack);
        final int current = cmp.getInt("selected_upgrade");
        final ResonatorUpgrade upgrade = ResonatorUpgrade.values()[Mth.getDescriptionId(current, 0, ResonatorUpgrade.values().length - 1)];
        if (viewing != null && !upgrade.canSwitchTo(viewing, stack)) {
            return ResonatorUpgrade.STARLIGHT;
        }
        return upgrade;
    }
    
    public static ItemStack setUpgradeUnlocked(final ItemStack stack, final ResonatorUpgrade... upgrades) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemResonator)) {
            return stack;
        }
        for (final ResonatorUpgrade upgrade : upgrades) {
            upgrade.applyUpgrade(stack);
        }
        return stack;
    }
    
    public static boolean hasUpgrade(final ItemStack stack, final ResonatorUpgrade upgrade) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemResonator && upgrade.hasUpgrade(stack);
    }
    
    public static List<ResonatorUpgrade> getUpgrades(final ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemResonator)) {
            return Lists.newArrayList();
        }
        final List<ResonatorUpgrade> upgrades = Lists.newLinkedList();
        for (final ResonatorUpgrade ru : ResonatorUpgrade.values()) {
            if (ru.hasUpgrade(stack)) {
                upgrades.add(ru);
            }
        }
        return upgrades;
    }
    
    public String func_77667_c(final ItemStack stack) {
        return getCurrentUpgrade(null, stack).getUnlocalizedItemName();
    }
    
    public enum ResonatorUpgrade
    {
        STARLIGHT("starlight", (TriPredicate<Player, LogicalSide, ItemStack>)((player, side, stack) -> true)), 
        FLUID_FIELDS("liquid", (TriPredicate<Player, LogicalSide, ItemStack>)((player, side, stack) -> ResearchHelper.getProgress(player, side).getTierReached().isThisLaterOrEqual(ProgressionTier.TRAIT_CRAFT))), 
        AREA_SIZE("structure", (TriPredicate<Player, LogicalSide, ItemStack>)((player, side, stack) -> ResearchHelper.getProgress(player, side).getTierReached().isThisLaterOrEqual(ProgressionTier.ATTUNEMENT)));
        
        private final TriPredicate<Player, LogicalSide, ItemStack> check;
        private final String appendixUpgrade;
        
        private ResonatorUpgrade(final String appendixUpgrade, final TriPredicate<Player, LogicalSide, ItemStack> check) {
            this.check = check;
            this.appendixUpgrade = appendixUpgrade;
        }
        
        public String getAppendix() {
            return this.appendixUpgrade;
        }
        
        public String getUnlocalizedItemName() {
            return "item.astralsorcery.resonator." + this.appendixUpgrade;
        }
        
        public String getUnlocalizedTypeName() {
            return "item.astralsorcery.resonator.upgrade." + this.appendixUpgrade;
        }
        
        public boolean hasUpgrade(final ItemStack stack) {
            final int id = this.ordinal();
            final CompoundTag cmp = NBTHelper.getPersistentData(stack);
            if (cmp.func_150297_b("upgrades", 9)) {
                final ListTag list = cmp.getList("upgrades", 3);
                for (int i = 0; i < list.size(); ++i) {
                    if (list.func_186858_c(i) == id) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        public boolean canSwitchTo(@Nonnull final Player player, final ItemStack stack) {
            return this.hasUpgrade(stack) && this.check.test((Object)player, (Object)EffectiveSide.get(), (Object)stack);
        }
        
        public void applyUpgrade(final ItemStack stack) {
            if (this.hasUpgrade(stack)) {

            }
            final CompoundTag cmp = NBTHelper.getPersistentData(stack);
            if (!cmp.func_150297_b("upgrades", 9)) {
                cmp.put("upgrades", (Tag)new ListTag());
            }
            final ListTag list = cmp.getList("upgrades", 3);
            list.add((Object)IntTag.func_229692_a_(this.ordinal()));
        }
    }
}
