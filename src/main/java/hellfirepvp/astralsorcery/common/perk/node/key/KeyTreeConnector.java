package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.data.research.PlayerPerkAllocation;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.data.research.PlayerPerkData;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.perk.tree.PerkTreePoint;
import hellfirepvp.astralsorcery.common.data.research.PerkAllocationType;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.perk.PerkTree;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.node.MajorPerk;

public class KeyTreeConnector extends MajorPerk
{
    public KeyTreeConnector(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
        this.setCategory(KeyTreeConnector.CATEGORY_EPIPHANY);
    }
    
    @Override
    public boolean mayUnlockPerk(final PlayerProgress progress, final Player player) {
        if (!progress.getPerkData().hasFreeAllocationPoint(player, this.getSide((Entity)player)) || !this.canSee(player, progress)) {
            return false;
        }
        final PlayerPerkData perkData = progress.getPerkData();
        final LogicalSide side = this.getSide((Entity)player);
        boolean hasAllAdjacent = true;
        for (final AbstractPerk otherPerks : PerkTree.PERK_TREE.getConnectedPerks(side, this)) {
            if (!perkData.hasPerkAllocation(otherPerks, PerkAllocationType.UNLOCKED)) {
                hasAllAdjacent = false;
                break;
            }
        }
        return hasAllAdjacent || PerkTree.PERK_TREE.getPerkPoints(this.getSide((Entity)player)).stream().map((Function<? super PerkTreePoint<?>, ?>)PerkTreePoint::getPerk).filter(perk -> perk instanceof KeyTreeConnector).anyMatch(perk -> perkData.hasPerkAllocation(perk, PerkAllocationType.UNLOCKED));
    }
    
    @Override
    public void onUnlockPerkServer(@Nullable final Player player, final PerkAllocationType allocationType, final PlayerProgress progress, final CompoundTag dataStorage) {
        super.onUnlockPerkServer(player, allocationType, progress, dataStorage);
        if (allocationType == PerkAllocationType.UNLOCKED) {
            final ListTag listTokens = new ListTag();
            for (final AbstractPerk otherPerk : PerkTree.PERK_TREE.getConnectedPerks(LogicalSide.SERVER, this)) {
                if (ResearchManager.forceApplyPerk(player, otherPerk, PlayerPerkAllocation.unlock())) {
                    final ResourceLocation token = AstralSorcery.key("connector_tk_" + otherPerk.getRegistryName().func_110623_a());
                    if (!ResearchManager.grantFreePerkPoint(player, token)) {
                        continue;
                    }
                    listTokens.add((Object)StringTag.func_229705_a_(token.toString()));
                }
            }
            dataStorage.put("pointtokens", (Tag)listTokens);
        }
    }
    
    @Override
    public void onRemovePerkServer(final Player player, final PerkAllocationType allocationType, final PlayerProgress progress, final CompoundTag dataStorage) {
        super.onRemovePerkServer(player, allocationType, progress, dataStorage);
        if (allocationType == PerkAllocationType.UNLOCKED) {
            final ListTag list = dataStorage.getList("pointtokens", 8);
            for (int i = 0; i < list.size(); ++i) {
                ResearchManager.revokeFreePoint(player, new ResourceLocation(list.func_150307_f(i)));
            }
        }
    }
    
    @Override
    public void clearCaches(final LogicalSide side) {
        super.clearCaches(side);
    }
}
