package hellfirepvp.astralsorcery.common.data.research;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.network.play.server.PktSyncKnowledge;
import java.util.Collections;
import java.util.Collection;
import java.util.Iterator;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.perk.PerkTree;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import net.minecraft.nbt.ListTag;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import net.minecraft.nbt.CompoundTag;
import java.util.HashSet;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import java.util.List;

public class PlayerProgress
{
    private List<ResourceLocation> knownConstellations;
    private List<ResourceLocation> seenConstellations;
    private ProgressionTier tierReached;
    private Set<ResearchProgression> researchProgression;
    private IMajorConstellation attunedConstellation;
    private boolean wasOnceAttuned;
    private final PlayerPerkData perkData;
    private List<ResourceLocation> storedConstellationPapers;
    private boolean tomeReceived;
    private boolean usePerkAbilities;
    
    public PlayerProgress() {
        this.knownConstellations = new ArrayList<ResourceLocation>();
        this.seenConstellations = new ArrayList<ResourceLocation>();
        this.tierReached = ProgressionTier.DISCOVERY;
        this.researchProgression = new HashSet<ResearchProgression>();
        this.attunedConstellation = null;
        this.wasOnceAttuned = false;
        this.perkData = new PlayerPerkData();
        this.storedConstellationPapers = new ArrayList<ResourceLocation>();
        this.tomeReceived = false;
        this.usePerkAbilities = true;
    }
    
    public void load(final CompoundTag compound) {
        this.knownConstellations.clear();
        this.seenConstellations.clear();
        this.researchProgression.clear();
        this.storedConstellationPapers.clear();
        this.attunedConstellation = null;
        this.tierReached = ProgressionTier.DISCOVERY;
        this.wasOnceAttuned = false;
        this.tomeReceived = false;
        this.usePerkAbilities = true;
        if (compound.contains("seenConstellations")) {
            final ListTag list = compound.getList("seenConstellations", 8);
            for (int i = 0; i < list.size(); ++i) {
                this.seenConstellations.add(new ResourceLocation(list.func_150307_f(i)));
            }
        }
        if (compound.contains("constellations")) {
            final ListTag list = compound.getList("constellations", 8);
            for (int i = 0; i < list.size(); ++i) {
                final ResourceLocation s = new ResourceLocation(list.func_150307_f(i));
                this.knownConstellations.add(s);
                if (!this.seenConstellations.contains(s)) {
                    this.seenConstellations.add(s);
                }
            }
        }
        if (compound.contains("storedConstellationPapers")) {
            final ListTag list = compound.getList("storedConstellationPapers", 8);
            for (int i = 0; i < list.size(); ++i) {
                final ResourceLocation s = new ResourceLocation(list.func_150307_f(i));
                this.storedConstellationPapers.add(s);
                if (!this.seenConstellations.contains(s)) {
                    this.seenConstellations.add(s);
                }
            }
        }
        if (compound.contains("attuned")) {
            final String cst = compound.getString("attuned");
            final IConstellation c = ConstellationRegistry.getConstellation(new ResourceLocation(cst));
            if (!(c instanceof IMajorConstellation)) {
                AstralSorcery.log.warn("Failed to load attuned Constellation: " + cst + " - constellation doesn't exist or isn't major.");
            }
            else {
                this.attunedConstellation = (IMajorConstellation)c;
            }
        }
        this.perkData.load(this, compound);
        if (compound.contains("tierReached")) {
            final int tierOrdinal = compound.getInt("tierReached");
            this.tierReached = MiscUtils.getEnumEntry(ProgressionTier.class, tierOrdinal);
        }
        if (compound.contains("research")) {
            final int[] func_74759_k;
            final int[] research = func_74759_k = compound.func_74759_k("research");
            for (final int resOrdinal : func_74759_k) {
                this.researchProgression.add(MiscUtils.getEnumEntry(ResearchProgression.class, resOrdinal));
            }
        }
        this.wasOnceAttuned = compound.getBoolean("wasAttuned");
        if (!compound.contains("bookReceived")) {
            this.tomeReceived = true;
        }
        else {
            this.tomeReceived = compound.getBoolean("bookReceived");
        }
        if (compound.contains("usePerkAbilities")) {
            this.usePerkAbilities = compound.getBoolean("usePerkAbilities");
        }
    }
    
    public void store(final CompoundTag cmp) {
        final ListTag known = new ListTag();
        for (final ResourceLocation s : this.knownConstellations) {
            known.add((Object)StringTag.valueOf(s.withStyle()));
        }
        final ListTag seen = new ListTag();
        for (final ResourceLocation s2 : this.seenConstellations) {
            seen.add((Object)StringTag.valueOf(s2.withStyle()));
        }
        final ListTag storedPapers = new ListTag();
        for (final ResourceLocation s3 : this.storedConstellationPapers) {
            storedPapers.add((Object)StringTag.valueOf(s3.withStyle()));
        }
        cmp.put("constellations", (Tag)known);
        cmp.put("seenConstellations", (Tag)seen);
        cmp.put("storedConstellationPapers", (Tag)storedPapers);
        cmp.putInt("tierReached", this.tierReached.ordinal());
        cmp.putBoolean("wasAttuned", this.wasOnceAttuned);
        final int[] researchArray = this.researchProgression.stream().mapToInt(Enum::ordinal).toArray();
        cmp.func_74783_a("research", researchArray);
        if (this.attunedConstellation != null) {
            cmp.putString("attuned", this.attunedConstellation.getRegistryName().toString());
        }
        PerkTree.PERK_TREE.getVersion(LogicalSide.SERVER).ifPresent(version -> cmp.func_74772_a("perkTreeVersion", (long)version));
        cmp.putBoolean("bookReceived", this.tomeReceived);
        cmp.putBoolean("usePerkAbilities", this.usePerkAbilities);
        this.perkData.save(cmp);
    }
    
    public void storeKnowledge(final CompoundTag cmp) {
        final ListTag list = new ListTag();
        for (final ResourceLocation s : this.knownConstellations) {
            list.add((Object)StringTag.valueOf(s.withStyle()));
        }
        final ListTag l = new ListTag();
        for (final ResourceLocation s2 : this.seenConstellations) {
            l.add((Object)StringTag.valueOf(s2.withStyle()));
        }
        cmp.put("constellations", (Tag)list);
        cmp.put("seenConstellations", (Tag)l);
        cmp.putInt("tierReached", this.tierReached.ordinal());
        cmp.putBoolean("wasAttuned", this.wasOnceAttuned);
        final int[] researchArray = this.researchProgression.stream().mapToInt(Enum::ordinal).toArray();
        cmp.func_74783_a("research", researchArray);
    }
    
    public void loadKnowledge(final CompoundTag compound) {
        if (compound.contains("seenConstellations")) {
            final ListTag list = compound.getList("seenConstellations", 8);
            for (int i = 0; i < list.size(); ++i) {
                final ResourceLocation cstName = new ResourceLocation(list.func_150307_f(i));
                if (!this.seenConstellations.contains(cstName)) {
                    this.seenConstellations.add(cstName);
                }
            }
        }
        if (compound.contains("constellations")) {
            final ListTag list = compound.getList("constellations", 8);
            for (int i = 0; i < list.size(); ++i) {
                final ResourceLocation cstName = new ResourceLocation(list.func_150307_f(i));
                if (!this.knownConstellations.contains(cstName)) {
                    this.knownConstellations.add(cstName);
                }
                if (!this.seenConstellations.contains(cstName)) {
                    this.seenConstellations.add(cstName);
                }
            }
        }
        if (compound.contains("tierReached")) {
            final int tierOrdinal = compound.getInt("tierReached");
            final ProgressionTier otherTier = MiscUtils.getEnumEntry(ProgressionTier.class, tierOrdinal);
            if (otherTier.isThisLater(this.tierReached)) {
                this.tierReached = otherTier;
            }
        }
        if (compound.contains("research")) {
            final int[] func_74759_k;
            final int[] research = func_74759_k = compound.func_74759_k("research");
            for (final int resOrdinal : func_74759_k) {
                this.researchProgression.add(MiscUtils.getEnumEntry(ResearchProgression.class, resOrdinal));
            }
        }
    }
    
    public boolean isValid() {
        return true;
    }
    
    public PlayerPerkData getPerkData() {
        return this.perkData;
    }
    
    protected void setAttunedConstellation(final IMajorConstellation constellation) {
        this.attunedConstellation = constellation;
        this.wasOnceAttuned = true;
    }
    
    public Collection<ResearchProgression> getResearchProgression() {
        return Collections.unmodifiableCollection((Collection<? extends ResearchProgression>)this.researchProgression);
    }
    
    public boolean hasResearch(final ResearchProgression progression) {
        return this.getResearchProgression().contains(progression);
    }
    
    protected boolean forceGainResearch(final ResearchProgression progression) {
        return this.researchProgression.add(progression);
    }
    
    public ProgressionTier getTierReached() {
        return this.tierReached;
    }
    
    public IMajorConstellation getAttunedConstellation() {
        return this.attunedConstellation;
    }
    
    public boolean isAttuned() {
        return this.getAttunedConstellation() != null;
    }
    
    public boolean wasOnceAttuned() {
        return this.wasOnceAttuned;
    }
    
    protected void setAttunedBefore(final boolean attuned) {
        this.wasOnceAttuned = attuned;
    }
    
    public boolean didReceiveTome() {
        return this.tomeReceived;
    }
    
    protected void setTomeReceived() {
        this.tomeReceived = true;
    }
    
    public boolean doPerkAbilities() {
        return this.usePerkAbilities;
    }
    
    protected void setUsePerkAbilities(final boolean usePerkAbilities) {
        this.usePerkAbilities = usePerkAbilities;
    }
    
    protected void setTierReached(final ProgressionTier tier) {
        this.tierReached = tier;
    }
    
    public List<ResourceLocation> getKnownConstellations() {
        return this.knownConstellations;
    }
    
    public List<ResourceLocation> getSeenConstellations() {
        return this.seenConstellations;
    }
    
    public List<ResourceLocation> getStoredConstellationPapers() {
        return this.storedConstellationPapers;
    }
    
    public boolean hasSeenConstellation(final IConstellation constellation) {
        return this.hasSeenConstellation(constellation.getRegistryName());
    }
    
    public boolean hasSeenConstellation(final ResourceLocation constellation) {
        return this.seenConstellations.contains(constellation);
    }
    
    public boolean hasConstellationDiscovered(final IConstellation constellation) {
        return this.hasConstellationDiscovered(constellation.getRegistryName());
    }
    
    public boolean hasConstellationDiscovered(final ResourceLocation constellation) {
        return this.knownConstellations.contains(constellation);
    }
    
    protected void discoverConstellation(final ResourceLocation name) {
        this.memorizeConstellation(name);
        if (!this.knownConstellations.contains(name)) {
            this.knownConstellations.add(name);
        }
    }
    
    protected void memorizeConstellation(final ResourceLocation name) {
        if (!this.seenConstellations.contains(name)) {
            this.seenConstellations.add(name);
        }
    }
    
    protected void setStoredConstellationPapers(final List<ResourceLocation> names) {
        this.storedConstellationPapers.clear();
        this.storedConstellationPapers.addAll(names);
    }
    
    @OnlyIn(Dist.CLIENT)
    protected void receive(final PktSyncKnowledge message) {
        this.knownConstellations = message.knownConstellations;
        this.seenConstellations = message.seenConstellations;
        this.storedConstellationPapers = message.storedConstellationPapers;
        this.researchProgression = new HashSet<ResearchProgression>(message.researchProgression);
        this.tierReached = MiscUtils.getEnumEntry(ProgressionTier.class, message.progressTier);
        this.attunedConstellation = message.attunedConstellation;
        this.wasOnceAttuned = message.wasOnceAttuned;
        this.perkData.receive(message);
        this.usePerkAbilities = message.doPerkAbilities;
    }
    
    protected PlayerProgress copy() {
        final PlayerProgress copy = new PlayerProgress();
        final CompoundTag saveData = new CompoundTag();
        this.store(saveData);
        copy.load(saveData);
        return copy;
    }
    
    public void acceptMergeFrom(final PlayerProgress toMergeFrom) {
        for (final ResourceLocation seen : toMergeFrom.seenConstellations) {
            this.memorizeConstellation(seen);
        }
        for (final ResourceLocation known : toMergeFrom.knownConstellations) {
            this.discoverConstellation(known);
        }
        if (toMergeFrom.tierReached.isThisLaterOrEqual(this.tierReached)) {
            this.tierReached = toMergeFrom.tierReached;
        }
        for (final ResearchProgression prog : toMergeFrom.researchProgression) {
            this.forceGainResearch(prog);
        }
    }
}
