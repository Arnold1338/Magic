package hellfirepvp.astralsorcery.common.perk;

import hellfirepvp.astralsorcery.AstralSorcery;
import com.google.gson.JsonObject;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.Tuple;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.perk.tree.PerkTreePoint;
import java.util.Collections;
import java.util.Collection;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.perk.node.RootPerk;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import java.util.Optional;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.perk.data.PreparedPerkTreeData;
import hellfirepvp.astralsorcery.common.util.SidedReference;
import hellfirepvp.astralsorcery.common.perk.data.PerkTreeData;

public class PerkTree
{
    public static final PerkTree PERK_TREE;
    private PerkTreeData loadedPerkTree;
    private final SidedReference<PreparedPerkTreeData> treeData;
    
    private PerkTree() {
        this.loadedPerkTree = null;
        this.treeData = new SidedReference<PreparedPerkTreeData>();
    }
    
    public Optional<PreparedPerkTreeData> getData(final LogicalSide side) {
        return this.treeData.getData(side);
    }
    
    public Optional<AbstractPerk> getPerk(final LogicalSide side, final ResourceLocation key) {
        return this.getPerk(side, perk -> key.equals((Object)perk.getRegistryName()));
    }
    
    public Optional<AbstractPerk> getPerk(final LogicalSide side, final Predicate<AbstractPerk> test) {
        return this.getData(side).flatMap(data -> data.getPerk(test));
    }
    
    public Optional<? extends AbstractPerk> getPerk(final LogicalSide side, final float x, final float y) {
        return this.getData(side).flatMap(data -> data.getPerk(x, y));
    }
    
    @Nullable
    public RootPerk getRootPerk(final LogicalSide side, final IConstellation constellation) {
        return this.getData(side).map(data -> data.getRootPerk(constellation)).orElse(null);
    }
    
    public Collection<AbstractPerk> getConnectedPerks(final LogicalSide side, final AbstractPerk perk) {
        return this.getData(side).map(data -> data.getConnectedPerks(perk)).orElse((Collection<AbstractPerk>)Collections.emptyList());
    }
    
    public Collection<PerkTreePoint<?>> getPerkPoints(final LogicalSide side) {
        return this.getData(side).map((Function<? super PreparedPerkTreeData, ? extends Collection<PerkTreePoint<?>>>)PreparedPerkTreeData::getPerkPoints).orElse((Collection<PerkTreePoint<?>>)Collections.emptyList());
    }
    
    @OnlyIn(Dist.CLIENT)
    public Collection<Tuple<AbstractPerk, AbstractPerk>> getConnections() {
        return this.getData(LogicalSide.CLIENT).map((Function<? super PreparedPerkTreeData, ? extends Collection<Tuple<AbstractPerk, AbstractPerk>>>)PreparedPerkTreeData::getConnections).orElse((Collection<Tuple<AbstractPerk, AbstractPerk>>)Collections.emptyList());
    }
    
    public Optional<Long> getVersion(final LogicalSide side) {
        return this.getData(side).map((Function<? super PreparedPerkTreeData, ? extends Long>)PreparedPerkTreeData::getVersion);
    }
    
    public void updateOriginPerkTree(final PerkTreeData perkTree) {
        this.loadedPerkTree = perkTree;
    }
    
    public Optional<Collection<JsonObject>> getLoginPerkData() {
        return Optional.ofNullable(this.loadedPerkTree).map((Function<? super PerkTreeData, ? extends Collection<JsonObject>>)PerkTreeData::getAsDataTree);
    }
    
    @OnlyIn(Dist.CLIENT)
    public void receivePerkTree(final PreparedPerkTreeData serverTreeData) {
        this.updateTreeData(LogicalSide.CLIENT, serverTreeData);
    }
    
    public void clearCache(final LogicalSide side) {
        this.getData(side).ifPresent(data -> data.clearPerkCache(side));
        this.updateTreeData(side, null);
    }
    
    public void setupServerPerkTree() {
        if (this.loadedPerkTree != null) {
            this.updateTreeData(LogicalSide.SERVER, this.loadedPerkTree.prepare());
            AstralSorcery.log.info("Loaded PerkTree!");
        }
        else {
            AstralSorcery.log.info("No PerkTree data found!");
        }
    }
    
    private void updateTreeData(final LogicalSide side, @Nullable final PreparedPerkTreeData newData) {
        this.treeData.getData(side).ifPresent(data -> data.getPerkPoints().stream().map((Function<? super PerkTreePoint<?>, ?>)PerkTreePoint::getPerk).forEach(perk -> perk.invalidate(side)));
        this.treeData.setData(side, newData);
        if (newData != null) {
            newData.getPerkPoints().stream().map((Function<? super PerkTreePoint<?>, ?>)PerkTreePoint::getPerk).forEach(perk -> perk.validate(side));
        }
    }
    
    static {
        PERK_TREE = new PerkTree();
    }
}
