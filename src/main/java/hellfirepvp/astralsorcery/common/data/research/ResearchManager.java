package hellfirepvp.astralsorcery.common.data.research;

import hellfirepvp.astralsorcery.common.block.tile.BlockAltar;
import net.minecraft.world.level.item.Item;
import net.minecraft.world.level.level.block.Block;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.crafting.recipe.infusion.ActiveLiquidInfusionRecipe;
import hellfirepvp.astralsorcery.common.tile.TileInfuser;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.network.play.server.PktSyncModifierSource;
import hellfirepvp.astralsorcery.common.network.play.server.PktSyncPerkActivity;
import hellfirepvp.astralsorcery.common.perk.source.ModifierSource;
import hellfirepvp.astralsorcery.common.perk.PerkEffectHelper;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.perk.PerkTree;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import net.minecraft.resources.ResourceLocation;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import net.minecraftforge.registries.IForgeRegistryEntry;
import java.util.List;
import hellfirepvp.astralsorcery.common.lib.AdvancementsAS;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.play.server.PktProgressionUpdate;
import java.util.Collection;
import java.util.LinkedList;
import net.minecraft.world.level.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.server.level.ServerPlayer;

public class ResearchManager
{
    public static void unsafeForceGiveResearch(final ServerPlayer player, final ResearchProgression prog) {
        final PlayerProgress progress = ResearchHelper.getProgress((Player)player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return;
        }
        final ProgressionTier reqTier = prog.getRequiredProgress();
        if (!progress.getTierReached().isThisLaterOrEqual(reqTier)) {
            progress.setTierReached(reqTier);
        }
        final LinkedList<ResearchProgression> progToGive = new LinkedList<ResearchProgression>();
        progToGive.add(prog);
        while (!progToGive.isEmpty()) {
            final ResearchProgression give = progToGive.pop();
            if (!progress.hasResearch(give)) {
                progress.forceGainResearch(give);
            }
            progToGive.addAll(give.getPreConditions());
        }
        final PktProgressionUpdate pkt = new PktProgressionUpdate();
        PacketChannel.CHANNEL.sendToPlayer((Player)player, pkt);
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, (Player)player);
        ResearchHelper.savePlayerKnowledge((Player)player);
    }
    
    public static boolean grantResearch(final Player player, final ResearchProgression prog) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        final ProgressionTier tier = prog.getRequiredProgress();
        if (!progress.getTierReached().isThisLaterOrEqual(tier)) {
            return false;
        }
        for (final ResearchProgression other : prog.getPreConditions()) {
            if (!progress.hasResearch(other)) {
                return false;
            }
        }
        if (progress.forceGainResearch(prog)) {
            final PktProgressionUpdate pkt = new PktProgressionUpdate(prog);
            PacketChannel.CHANNEL.sendToPlayer(player, pkt);
        }
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean grantProgress(final Player player, final ProgressionTier tier) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        final ProgressionTier t = progress.getTierReached();
        if (!t.hasNextTier()) {
            return false;
        }
        final ProgressionTier next = t.next();
        if (!next.equals(tier)) {
            return false;
        }
        progress.setTierReached(next);
        final PktProgressionUpdate pkt = new PktProgressionUpdate(next);
        PacketChannel.CHANNEL.sendToPlayer(player, pkt);
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean discoverConstellations(final Collection<IConstellation> constellations, final Player player) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        for (final IConstellation cst : constellations) {
            progress.discoverConstellation(cst.getRegistryName());
            AdvancementsAS.DISCOVER_CONSTELLATION.trigger((ServerPlayer)player, cst);
        }
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean discoverConstellation(final IConstellation constellation, final Player player) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        progress.discoverConstellation(constellation.getRegistryName());
        AdvancementsAS.DISCOVER_CONSTELLATION.trigger((ServerPlayer)player, constellation);
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean memorizeConstellation(final IConstellation c, final Player player) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        progress.memorizeConstellation(c.getRegistryName());
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean updateConstellationPapers(final List<IConstellation> papers, final Player player) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        progress.setStoredConstellationPapers((List<ResourceLocation>)papers.stream().map((Function<? super Object, ?>)IForgeRegistryEntry::getRegistryName).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean maximizeTier(final Player player) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        progress.setTierReached(ProgressionTier.values()[ProgressionTier.values().length - 1]);
        final PktProgressionUpdate pkt = new PktProgressionUpdate();
        PacketChannel.CHANNEL.sendToPlayer(player, pkt);
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean setAttunedBefore(final Player player, final boolean wasAttunedBefore) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        progress.setAttunedBefore(wasAttunedBefore);
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean setAttunedConstellation(final Player player, @Nullable final IMajorConstellation constellation) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        if (constellation != null && !progress.hasConstellationDiscovered(constellation)) {
            return false;
        }
        final PlayerPerkData perkData = progress.getPerkData();
        removeAllAllocatedPerks(progress, player);
        perkData.setExp(0.0);
        progress.setAttunedConstellation(constellation);
        final AbstractPerk root;
        if (constellation != null && (root = PerkTree.PERK_TREE.getRootPerk(LogicalSide.SERVER, constellation)) != null) {
            doApplyPerk(progress, perkData, player, root, PlayerPerkAllocation.unlock());
        }
        AdvancementsAS.ATTUNE_SELF.trigger((ServerPlayer)player, constellation);
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean setPerkData(final Player player, @Nonnull final AbstractPerk perk, final CompoundTag prevoiusData, final CompoundTag newData) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        final PlayerPerkData perkData = progress.getPerkData();
        if (!perkData.hasPerkAllocation(perk)) {
            return false;
        }
        PerkEffectHelper.modifySource(player, LogicalSide.SERVER, perk, PerkEffectHelper.Action.REMOVE);
        progress.getPerkData().updatePerkData(perk, newData);
        PerkEffectHelper.modifySource(player, LogicalSide.SERVER, perk, PerkEffectHelper.Action.ADD);
        PacketChannel.CHANNEL.sendToPlayer(player, new PktSyncPerkActivity(perk, prevoiusData, newData));
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean applyPerk(final Player player, @Nonnull final AbstractPerk perk, final PlayerPerkAllocation allocation) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        final PlayerPerkData perkData = progress.getPerkData();
        if (allocation.getType() == PerkAllocationType.UNLOCKED && !perkData.hasFreeAllocationPoint(player, LogicalSide.SERVER)) {
            return false;
        }
        if (!doApplyPerk(progress, perkData, player, perk, allocation)) {
            return false;
        }
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean applyPerkSeal(final Player player, @Nonnull final AbstractPerk perk) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        final PlayerPerkData perkData = progress.getPerkData();
        if (!perkData.hasPerkAllocation(perk)) {
            return false;
        }
        if (perkData.isPerkSealed(perk)) {
            return false;
        }
        if (!perkData.canSealPerk(perk)) {
            return false;
        }
        PerkEffectHelper.modifySource(player, LogicalSide.SERVER, perk, PerkEffectHelper.Action.REMOVE);
        PacketChannel.CHANNEL.sendToPlayer(player, PktSyncModifierSource.remove(perk));
        if (!perkData.sealPerk(perk)) {
            return false;
        }
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean breakPerkSeal(final Player player, @Nonnull final AbstractPerk perk) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        final PlayerPerkData perkData = progress.getPerkData();
        if (!perkData.isPerkSealed(perk)) {
            return false;
        }
        if (!perkData.breakSeal(perk)) {
            return false;
        }
        PerkEffectHelper.modifySource(player, LogicalSide.SERVER, perk, PerkEffectHelper.Action.ADD);
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        PacketChannel.CHANNEL.sendToPlayer(player, PktSyncModifierSource.add(perk));
        return true;
    }
    
    public static boolean grantFreePerkPoint(final Player player, final ResourceLocation token) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        if (!progress.getPerkData().grantFreeAllocationPoint(token)) {
            return false;
        }
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean revokeFreePoint(final Player player, final ResourceLocation token) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        if (!progress.getPerkData().tryRevokeAllocationPoint(token)) {
            return false;
        }
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean forceApplyPerk(final Player player, @Nonnull final AbstractPerk perk, final PlayerPerkAllocation allocation) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        final PlayerPerkData perkData = progress.getPerkData();
        if (!doApplyPerk(progress, perkData, player, perk, allocation)) {
            return false;
        }
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    @Deprecated
    public static boolean removePerk(final Player player, final AbstractPerk perk, final PlayerPerkAllocation allocation) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        final PlayerPerkData perkData = progress.getPerkData();
        if (!perkData.hasPerkAllocation(perk, allocation.getType())) {
            return false;
        }
        if (!doRemovePerk(progress, player, LogicalSide.SERVER, perk, allocation, true)) {
            return false;
        }
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean resetPerks(final Player player) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        removeAllAllocatedPerks(progress, player);
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    private static void removeAllAllocatedPerks(final PlayerProgress progress, final Player player) {
        final PlayerPerkData perkData = progress.getPerkData();
        final List<AbstractPerk> allocatedPerks = new ArrayList<AbstractPerk>(perkData.getAllocatedPerks(PerkAllocationType.UNLOCKED));
        final List<AbstractPerk> syncRemovable = new ArrayList<AbstractPerk>();
        for (final AbstractPerk perk : allocatedPerks) {
            if (doRemovePerk(progress, player, LogicalSide.SERVER, perk, PlayerPerkAllocation.unlock(), false)) {
                syncRemovable.add(perk);
            }
        }
        final List<ResourceLocation> removals = syncRemovable.stream().map((Function<? super Object, ?>)AbstractPerk::getRegistryName).collect((Collector<? super Object, ?, List<ResourceLocation>>)Collectors.toList());
        PacketChannel.CHANNEL.sendToPlayer(player, new PktSyncPerkActivity(removals));
    }
    
    private static boolean doRemovePerk(final PlayerProgress progress, final Player player, final LogicalSide side, final AbstractPerk perk, final PlayerPerkAllocation allocation, final boolean sync) {
        final PlayerPerkData perkData = progress.getPerkData();
        if (perkData.hasPerkAllocation(perk, allocation.getType())) {
            final CompoundTag data = perkData.getData(perk);
            if (data != null) {
                final PerkRemovalResult removeResult = perkData.removePerkAllocation(perk, allocation, true);
                if (removeResult.isFailure()) {
                    return false;
                }
                if (removeResult.removesPerk()) {
                    PerkEffectHelper.modifySource(player, side, perk, PerkEffectHelper.Action.REMOVE);
                }
                if (removeResult.removesAllocationType()) {
                    perk.onRemovePerkServer(player, allocation.getType(), progress, data);
                }
                final PerkRemovalResult actualResult = perkData.removePerkAllocation(perk, allocation, false);
                if (actualResult.removesPerk() && sync) {
                    PacketChannel.CHANNEL.sendToPlayer(player, PktSyncModifierSource.remove(perk));
                }
                return true;
            }
        }
        return false;
    }
    
    private static boolean doApplyPerk(final PlayerProgress progress, final PlayerPerkData perkData, final Player player, final AbstractPerk perk, final PlayerPerkAllocation allocation) {
        if (!perkData.applyPerkAllocation(perk, allocation, true)) {
            return false;
        }
        if (perkData.hasPerkAllocation(perk)) {
            if (!perkData.hasPerkAllocation(perk, allocation.getType())) {
                final CompoundTag data = perkData.getData(perk);
                perk.onUnlockPerkServer(player, allocation.getType(), progress, data);
            }
            return perkData.applyPerkAllocation(perk, allocation, false);
        }
        final CompoundTag data = new CompoundTag();
        perk.onUnlockPerkServer(player, allocation.getType(), progress, data);
        perkData.applyPerkAllocation(perk, allocation, false);
        perkData.updatePerkData(perk, data);
        PerkEffectHelper.modifySource(player, LogicalSide.SERVER, perk, PerkEffectHelper.Action.ADD);
        PacketChannel.CHANNEL.sendToPlayer(player, PktSyncModifierSource.add(perk));
        return true;
    }
    
    public static boolean setTomeReceived(final Player player) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        progress.setTomeReceived();
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean togglePerkAbilities(final Player player) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        progress.setUsePerkAbilities(!progress.doPerkAbilities());
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean setExp(final Player player, final long exp) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        progress.getPerkData().setExp((double)exp);
        AdvancementsAS.PERK_LEVEL.trigger((ServerPlayer)player);
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean modifyExp(final Player player, final double exp) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        progress.getPerkData().modifyExp(exp, player);
        AdvancementsAS.PERK_LEVEL.trigger((ServerPlayer)player);
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean forceMaximizeAll(final Player player) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        final ProgressionTier before = progress.getTierReached();
        discoverConstellations(ConstellationRegistry.getAllConstellations(), player);
        maximizeTier(player);
        forceMaximizeResearch(player);
        setAttunedBefore(player, true);
        if (progress.getTierReached().isThisLater(before)) {
            final PktProgressionUpdate pkt = new PktProgressionUpdate(progress.getTierReached());
            PacketChannel.CHANNEL.sendToPlayer(player, pkt);
        }
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static boolean forceMaximizeResearch(final Player player) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        for (final ResearchProgression progression : ResearchProgression.values()) {
            progress.forceGainResearch(progression);
        }
        final PktProgressionUpdate pkt = new PktProgressionUpdate();
        PacketChannel.CHANNEL.sendToPlayer(player, pkt);
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        ResearchHelper.savePlayerKnowledge(player);
        return true;
    }
    
    public static void informCraftedInfuser(@Nonnull final TileInfuser infuser, @Nonnull final ActiveLiquidInfusionRecipe recipe, @Nonnull final ItemStack crafted) {
        final Player crafter = recipe.tryGetCraftingPlayerServer();
        if (!(crafter instanceof ServerPlayer)) {
            AstralSorcery.log.warn("Infusion finished, player that initialized crafting could not be found!");
            AstralSorcery.log.warn("Affected tile: " + infuser.func_174877_v() + " in dim " + infuser.func_145831_w().dimension().func_240901_a_());
            return;
        }
        informCrafted(crafter, crafted);
    }
    
    public static void informCraftedAltar(@Nonnull final TileAltar altar, @Nonnull final ActiveSimpleAltarRecipe recipe, @Nonnull final ItemStack crafted) {
        final Player crafter = recipe.tryGetCraftingPlayerServer();
        if (!(crafter instanceof ServerPlayer)) {
            AstralSorcery.log.warn("Crafting finished, player that initialized crafting could not be found!");
            AstralSorcery.log.warn("Affected tile: " + altar.func_174877_v() + " in dim " + altar.func_145831_w().dimension().func_240901_a_());
            return;
        }
        informCrafted(crafter, crafted);
        AdvancementsAS.ALTAR_CRAFT.trigger((ServerPlayer)crafter, recipe.getRecipeToCraft(), crafted);
    }
    
    public static void informCrafted(@Nonnull final Player player, @Nonnull final ItemStack out) {
        if (!out.isEmpty()) {
            informCraftCompletion(player, out, out.getItem(), Block.func_149634_a(out.getItem()));
        }
    }
    
    private static void informCraftCompletion(@Nonnull final Player crafter, @Nonnull final ItemStack crafted, @Nonnull final Item itemCrafted, @Nonnull final Block blockCrafted) {
        if (blockCrafted instanceof BlockAltar) {
            grantProgress(crafter, ProgressionTier.BASIC_CRAFT);
            grantResearch(crafter, ResearchProgression.BASIC_CRAFT);
            switch (((BlockAltar)blockCrafted).getAltarType()) {
                case RADIANCE: {
                    grantProgress(crafter, ProgressionTier.TRAIT_CRAFT);
                    grantResearch(crafter, ResearchProgression.RADIANCE);
                }
                case CONSTELLATION: {
                    grantProgress(crafter, ProgressionTier.CONSTELLATION_CRAFT);
                    grantResearch(crafter, ResearchProgression.CONSTELLATION);
                }
                case ATTUNEMENT: {
                    grantProgress(crafter, ProgressionTier.ATTUNEMENT);
                    grantResearch(crafter, ResearchProgression.ATTUNEMENT);
                    break;
                }
            }
        }
    }
}
