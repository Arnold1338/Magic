package hellfirepvp.astralsorcery.common.tile.altar;

import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.AltarRecipeEffect;
import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionReceiver;
import java.util.Collection;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.phys.AABB;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import hellfirepvp.astralsorcery.common.item.base.IConstellationFocus;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import javax.annotation.Nullable;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.util.block.BlockDiscoverer;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.tile.TileSpectralRelay;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import hellfirepvp.astralsorcery.common.util.world.SkyCollectionHelper;
import net.minecraft.world.level.ISeedReader;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipeContext;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.entity.EntityFlare;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeHooks;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import hellfirepvp.astralsorcery.common.util.sound.CategorizedSoundEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import hellfirepvp.astralsorcery.client.util.sound.PositionedLoopSound;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.tile.base.TileEntitySynchronized;
import net.minecraft.core.Direction;
import java.util.HashSet;
import java.util.HashMap;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import net.minecraft.resources.ResourceLocation;
import java.util.Set;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import java.util.Map;
import hellfirepvp.astralsorcery.common.util.tile.TileInventoryFiltered;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import hellfirepvp.astralsorcery.common.item.wand.WandInteractable;
import hellfirepvp.astralsorcery.common.tile.network.StarlightReceiverAltar;
import hellfirepvp.astralsorcery.common.tile.base.network.TileReceiverBase;

public class TileAltar extends TileReceiverBase<StarlightReceiverAltar> implements WandInteractable
{
    private float posDistribution;
    private AltarType altarType;
    private TileInventoryFiltered inventory;
    private final Map<AltarCollectionCategory, Float> tickStarlightCollectionMap;
    private ActiveSimpleAltarRecipe activeRecipe;
    private ItemStack focusItem;
    private Set<ResourceLocation> knownRecipes;
    private final DeferredStarlightStorage starlightStorage;
    private int starlightNextTick;
    private Object clientCraftSound;
    private Object clientWaitSound;
    
    public TileAltar() {
        super(TileEntityTypesAS.ALTAR);
        this.posDistribution = -1.0f;
        this.altarType = AltarType.DISCOVERY;
        this.tickStarlightCollectionMap = new HashMap<AltarCollectionCategory, Float>();
        this.activeRecipe = null;
        this.focusItem = ItemStack.EMPTY;
        this.knownRecipes = new HashSet<ResourceLocation>();
        this.starlightStorage = new DeferredStarlightStorage(2);
        this.starlightNextTick = 0;
        this.clientCraftSound = null;
        this.clientWaitSound = null;
        this.inventory = new TileInventoryFiltered(this, () -> 25, new Direction[0]);
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (!this.getLevel().level()) {
            this.doesSeeSky();
            this.hasMultiblock();
            this.gatherStarlight();
            this.doCraftingCycle();
        }
        else if (this.getActiveRecipe() != null) {
            this.doCraftEffects();
            this.doCraftSound();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void doCraftEffects() {
        this.activeRecipe.getRecipeToCraft().getCraftingEffects().forEach(effect -> effect.onTick(this, this.activeRecipe.getState()));
    }
    
    @OnlyIn(Dist.CLIENT)
    private void doCraftSound() {
        if (SoundHelper.getSoundVolume(SoundSource.BLOCKS) > 0.0f) {
            final ActiveSimpleAltarRecipe activeRecipe = this.getActiveRecipe();
            final AltarType type = this.getAltarType();
            if (this.clientCraftSound == null || ((PositionedLoopSound)this.clientCraftSound).hasStoppedPlaying()) {
                CategorizedSoundEvent sound = SoundsAS.ALTAR_CRAFT_LOOP_T1;
                switch (type) {
                    case ATTUNEMENT: {
                        sound = SoundsAS.ALTAR_CRAFT_LOOP_T2;
                        break;
                    }
                    case CONSTELLATION: {
                        sound = SoundsAS.ALTAR_CRAFT_LOOP_T3;
                        break;
                    }
                    case RADIANCE: {
                        sound = SoundsAS.ALTAR_CRAFT_LOOP_T4;
                        break;
                    }
                }
                this.clientCraftSound = SoundHelper.playSoundLoopFadeInClient(sound, new Vector3(this).add(0.5, 0.5, 0.5), 0.6f, 1.0f, false, s -> this.func_145837_r() || SoundHelper.getSoundVolume(SoundSource.BLOCKS) <= 0.0f || this.getActiveRecipe() == null).setFadeInTicks(40.0f).setFadeOutTicks(20.0f);
            }
            if (activeRecipe.getState() == ActiveSimpleAltarRecipe.CraftingState.WAITING && type.isThisGEThan(AltarType.RADIANCE)) {
                if (this.clientWaitSound == null || ((PositionedLoopSound)this.clientWaitSound).hasStoppedPlaying()) {
                    this.clientWaitSound = SoundHelper.playSoundLoopFadeInClient(SoundsAS.ALTAR_CRAFT_LOOP_T4_WAITING, new Vector3(this).add(0.5, 0.5, 0.5), 0.7f, 1.0f, false, s -> this.func_145837_r() || SoundHelper.getSoundVolume(SoundSource.BLOCKS) <= 0.0f || this.getActiveRecipe() == null || this.getActiveRecipe().getState() != ActiveSimpleAltarRecipe.CraftingState.WAITING).setFadeInTicks(30.0f).setFadeOutTicks(10.0f);
                }
                ((PositionedLoopSound)this.clientCraftSound).setVolumeMultiplier(0.75f);
            }
            else {
                ((PositionedLoopSound)this.clientCraftSound).setVolumeMultiplier(1.0f);
            }
        }
        else {
            this.clientWaitSound = null;
            this.clientCraftSound = null;
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void finishCraftingEffects(final PktPlayEffect pkt) {
        final ResourceLocation recipeName = ByteBufUtils.readResourceLocation(pkt.getExtraData());
        final BlockPos at = ByteBufUtils.readPos(pkt.getExtraData());
        final boolean isChaining = pkt.getExtraData().readBoolean();
        final Level world = (Level)Minecraft.getInstance().level;
        if (world == null) {
            return;
        }
        final TileAltar thisAltar = MiscUtils.getTileAt((IBlockReader)world, at, TileAltar.class, false);
        if (thisAltar != null) {
            final Recipe<?> recipe = (Recipe<?>)world.func_199532_z().getRecipeFor((RecipeType)RecipeTypesAS.TYPE_ALTAR.getType()).get(recipeName);
            if (recipe instanceof SimpleAltarRecipe) {
                ((SimpleAltarRecipe)recipe).getCraftingEffects().forEach(effect -> effect.onCraftingFinish(thisAltar, isChaining));
            }
            if (!isChaining) {
                SoundHelper.playSoundClientWorld(SoundsAS.ALTAR_CRAFT_FINISH, at, 0.6f, 1.0f);
            }
        }
    }
    
    private void doCraftingCycle() {
        if (this.activeRecipe == null) {
            return;
        }
        if (!this.hasMultiblock() || !this.activeRecipe.matches(this, false, false)) {
            this.abortCrafting();
            return;
        }
        if (this.activeRecipe.isFinished()) {
            this.finishRecipe();
            return;
        }
        this.activeRecipe.setState(this.activeRecipe.tick(this));
    }
    
    private void finishRecipe() {
        final ActiveSimpleAltarRecipe finishedRecipe = this.activeRecipe;
        ForgeHooks.setCraftingPlayer(finishedRecipe.tryGetCraftingPlayerServer());
        finishedRecipe.createItemOutputs(this, this::dropItemOnTop);
        finishedRecipe.consumeInputs(this);
        ForgeHooks.setCraftingPlayer((Player)null);
        final ResourceLocation recipeName = finishedRecipe.getRecipeToCraft().func_199560_c();
        final boolean isChaining;
        if (!(isChaining = finishedRecipe.matches(this, false, true))) {
            this.abortCrafting();
            EntityFlare.spawnAmbientFlare(this.getLevel(), this.getBlockState().offset(-3 + TileAltar.rand.nextInt(7), 1 + TileAltar.rand.nextInt(3), -3 + TileAltar.rand.nextInt(7)));
            EntityFlare.spawnAmbientFlare(this.getLevel(), this.getBlockState().offset(-3 + TileAltar.rand.nextInt(7), 1 + TileAltar.rand.nextInt(3), -3 + TileAltar.rand.nextInt(7)));
        }
        final PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.Type.ALTAR_RECIPE_FINISH).addData(buf -> {
            ByteBufUtils.writeResourceLocation(buf, recipeName);
            ByteBufUtils.writePos(buf, this.getBlockState());
            buf.writeBoolean(isChaining);
            return;
        });
        PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos(this.getLevel(), (Vector3i)this.getBlockState(), 32.0));
        this.knownRecipes.add(recipeName);
        this.markForUpdate();
    }
    
    private void abortCrafting() {
        this.activeRecipe = null;
        this.markForUpdate();
    }
    
    protected SimpleAltarRecipe findRecipe(final Player crafter) {
        return RecipeTypesAS.TYPE_ALTAR.findRecipe(new SimpleAltarRecipeContext(crafter, LogicalSide.SERVER, this).setIgnoreStarlightRequirement(false));
    }
    
    protected boolean startCrafting(final SimpleAltarRecipe recipe, final Player crafter) {
        if (this.getActiveRecipe() != null) {
            return false;
        }
        int divisor = Math.max(0, this.getAltarType().ordinal() - recipe.getAltarType().ordinal());
        divisor = (int)Math.round(Math.pow(2.0, divisor));
        this.activeRecipe = new ActiveSimpleAltarRecipe(recipe, divisor, crafter.getUUID());
        this.markForUpdate();
        SoundHelper.playSoundAround(SoundsAS.ALTAR_CRAFT_START, SoundSource.BLOCKS, this.level, new Vector3(this).add(0.5, 0.5, 0.5), 0.6f, 1.0f);
        return true;
    }
    
    @Override
    public boolean onInteract(final Level world, final BlockPos pos, final Player player, final Direction side, final boolean sneak) {
        if (!world.level().isClientSide() && this.hasMultiblock()) {
            if (this.getActiveRecipe() != null) {
                if (this.getActiveRecipe().matches(this, false, false)) {
                    return true;
                }
                this.abortCrafting();
            }
            final SimpleAltarRecipe recipe = this.findRecipe(player);
            if (recipe != null) {
                this.startCrafting(recipe, player);
            }
            return true;
        }
        return false;
    }
    
    private void gatherStarlight() {
        this.tickStarlightCollectionMap.clear();
        final WorldContext ctx = SkyHandler.getContext(this.getLevel());
        if (ctx == null) {
            if (this.starlightNextTick > 0) {
                this.starlightNextTick = 0;
                this.markForUpdate();
            }
            return;
        }
        this.starlightNextTick *= (int)0.9f;
        if (this.doesSeeSky()) {
            final int altarTier = this.getAltarType().ordinal() + 1;
            float heightAmount = Mth.canEnchant((float)Math.pow(this.getBlockState().getY() / 7.0f, 1.5) / 65.0f, 0.0f, 1.0f);
            heightAmount *= DayTimeHelper.getCurrentDaytimeDistribution(this.getLevel());
            this.collectStarlight(heightAmount * altarTier * 60.0f, AltarCollectionCategory.HEIGHT);
            if (this.posDistribution == -1.0f) {
                if (this.level instanceof ISeedReader) {
                    this.posDistribution = SkyCollectionHelper.getSkyNoiseDistribution((ISeedReader)this.level, this.field_174879_c);
                }
                else {
                    this.posDistribution = 0.3f;
                }
            }
            float fieldAmount = Mth.func_76129_c(this.posDistribution);
            fieldAmount *= DayTimeHelper.getCurrentDaytimeDistribution(this.getLevel());
            this.collectStarlight(fieldAmount * altarTier * 65.0f, AltarCollectionCategory.FOSIC_FIELD);
        }
        this.starlightStorage.setStoredStarlight(this.starlightNextTick);
    }
    
    public void collectStarlight(final float percent, final AltarCollectionCategory category) {
        final int collectable = Mth.func_76141_d(Math.min(percent, this.getRemainingCollectionCapacity(category)));
        this.starlightNextTick = Mth.getDescriptionId(this.starlightNextTick + collectable, 0, this.getAltarType().getStarlightCapacity());
        this.tickStarlightCollectionMap.computeIfPresent(category, (cat, remaining) -> Math.max(remaining - collectable, 0.0f));
        this.markForUpdate();
        this.preventNetworkSync();
    }
    
    public float getRemainingCollectionCapacity(final AltarCollectionCategory category) {
        return this.tickStarlightCollectionMap.computeIfAbsent(category, this::getCollectionCap);
    }
    
    public float getCollectionCap(final AltarCollectionCategory category) {
        return this.getAltarType().getStarlightCapacity() / 8.5f / this.getAltarType().getMinimumSources();
    }
    
    @Nonnull
    public Set<BlockPos> nearbyRelays() {
        final Set<BlockPos> eligableRelayOffsets = new HashSet<BlockPos>();
        for (int xx = -3; xx <= 3; ++xx) {
            for (int zz = -3; zz <= 3; ++zz) {
                if (xx != 0 || zz != 0) {
                    final BlockPos offset = new BlockPos(xx, 0, zz);
                    final TileSpectralRelay tar = MiscUtils.getTileAt((IBlockReader)this.getLevel(), this.getBlockState().func_177971_a((Vector3i)offset), TileSpectralRelay.class, true);
                    if (tar != null) {
                        eligableRelayOffsets.add(this.getBlockState().func_177971_a((Vector3i)offset));
                    }
                }
            }
        }
        return eligableRelayOffsets;
    }
    
    @Override
    public void onBreak() {
        super.onBreak();
        if (!this.getLevel().level() && !this.getFocusItem().isEmpty()) {
            ItemUtils.dropItemNaturally(this.getLevel(), this.getBlockState().getX() + 0.5, this.getBlockState().getY() + 0.5, this.getBlockState().getZ() + 0.5, this.focusItem);
            this.focusItem = ItemStack.EMPTY;
        }
    }
    
    @Override
    protected void onFirstTick() {
        super.onFirstTick();
        this.updateNearbyRelayLinkStates();
    }
    
    private void updateNearbyRelayLinkStates() {
        final Set<BlockPos> relayPositions = BlockDiscoverer.searchForTileEntitiesAround(this.getLevel(), this.getBlockState(), 16, tile -> tile instanceof TileSpectralRelay);
        for (final BlockPos relayPos : relayPositions) {
            final TileSpectralRelay tsr = MiscUtils.getTileAt((IBlockReader)this.getLevel(), relayPos, TileSpectralRelay.class, true);
            if (tsr != null) {
                tsr.updateAltarLinkState();
            }
        }
    }
    
    public int getStoredStarlight() {
        return this.starlightStorage.getStoredStarlight();
    }
    
    public float getAmbientStarlightPercent() {
        return this.getStoredStarlight() / (float)this.getAltarType().getStarlightCapacity();
    }
    
    public AltarType getAltarType() {
        return this.altarType;
    }
    
    @Nullable
    public ActiveSimpleAltarRecipe getActiveRecipe() {
        return this.activeRecipe;
    }
    
    @Nonnull
    public ItemStack getFocusItem() {
        return this.focusItem;
    }
    
    public void setFocusItem(@Nonnull final ItemStack focusItem) {
        this.focusItem = focusItem;
        this.markForUpdate();
    }
    
    @Nullable
    public IConstellation getFocusedConstellation() {
        final ItemStack focus = this.getFocusItem();
        if (focus.getItem() instanceof IConstellationFocus) {
            return ((IConstellationFocus)focus.getItem()).getFocusConstellation(focus);
        }
        return null;
    }
    
    @Nullable
    @Override
    public StructureType getRequiredStructureType() {
        return this.altarType.getRequiredStructure();
    }
    
    @Nonnull
    public TileInventoryFiltered getInventory() {
        return this.inventory;
    }
    
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        AABB box = super.getRenderBoundingBox().func_72321_a(0.0, 5.0, 0.0);
        if (this.getAltarType().isThisGEThan(AltarType.RADIANCE)) {
            box = box.func_72314_b(3.0, 0.0, 3.0);
        }
        return box;
    }
    
    public <T extends TileAltar> T updateType(final AltarType newType, final boolean initialPlacement) {
        if (!initialPlacement) {
            this.abortCrafting();
        }
        this.altarType = newType;
        final CompoundTag thisTag = new CompoundTag();
        this.writeCustomNBT(thisTag);
        this.readCustomNBT(thisTag);
        if (!initialPlacement) {
            this.markForUpdate();
            this.hasMultiblock();
        }
        return (T)this;
    }
    
    @Override
    public void readNetNBT(final CompoundTag compound) {
        super.readNetNBT(compound);
        this.starlightStorage.readNBT(compound);
    }
    
    @Override
    public void writeNetNBT(final CompoundTag compound) {
        super.writeNetNBT(compound);
        this.starlightStorage.writeNBT(compound);
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.altarType = AltarType.values()[compound.getInt("altarType")];
        this.inventory = this.inventory.deserialize(compound.func_74775_l("inventory"));
        this.focusItem = NBTHelper.getStack(compound, "focusItem");
        this.knownRecipes = NBTHelper.readSet(compound, "knownRecipes", 8, nbt -> new ResourceLocation(nbt.func_150285_a_()));
        if (compound.func_150297_b("activeRecipe", 10)) {
            this.activeRecipe = ActiveSimpleAltarRecipe.deserialize(compound.func_74775_l("activeRecipe"), this.activeRecipe);
        }
        else {
            this.activeRecipe = null;
        }
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        compound.putInt("altarType", this.altarType.ordinal());
        compound.put("inventory", (Tag)this.inventory.serialize());
        NBTHelper.setStack(compound, "focusItem", this.focusItem);
        NBTHelper.writeList(compound, "knownRecipes", (Collection<ResourceLocation>)this.knownRecipes, key -> StringTag.valueOf(key.toString()));
        if (this.activeRecipe != null) {
            compound.put("activeRecipe", (Tag)this.activeRecipe.serialize());
        }
    }
    
    @Nonnull
    @Override
    public StarlightReceiverAltar provideEndpoint(final BlockPos at) {
        return new StarlightReceiverAltar(at);
    }
}
