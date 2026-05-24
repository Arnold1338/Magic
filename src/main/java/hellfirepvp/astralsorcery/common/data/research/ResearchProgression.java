package hellfirepvp.astralsorcery.common.data.research;

import javax.annotation.Nonnull;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.Locale;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.List;
import net.minecraftforge.common.IExtensibleEnum;

public enum ResearchProgression implements IExtensibleEnum
{
    DISCOVERY(ProgressionTier.DISCOVERY, new ResearchProgression[0]), 
    BASIC_CRAFT(ProgressionTier.BASIC_CRAFT, new ResearchProgression[] { ResearchProgression.DISCOVERY }), 
    ATTUNEMENT(ProgressionTier.ATTUNEMENT, new ResearchProgression[] { ResearchProgression.BASIC_CRAFT }), 
    CONSTELLATION(ProgressionTier.CONSTELLATION_CRAFT, new ResearchProgression[] { ResearchProgression.ATTUNEMENT }), 
    RADIANCE(ProgressionTier.TRAIT_CRAFT, new ResearchProgression[] { ResearchProgression.CONSTELLATION }), 
    BRILLIANCE(ProgressionTier.BRILLIANCE, new ResearchProgression[] { ResearchProgression.RADIANCE });
    
    private final List<ResearchProgression> preConditions;
    private final List<ResearchNode> researchNodes;
    private final ProgressionTier requiredProgress;
    private final String unlocName;
    
    private ResearchProgression(final ProgressionTier requiredProgress, final ResearchProgression[] preConditions) {
        this(requiredProgress, Arrays.asList(preConditions));
    }
    
    private ResearchProgression(final ProgressionTier requiredProgress, final List<ResearchProgression> preConditions) {
        this.preConditions = new LinkedList<ResearchProgression>();
        this.researchNodes = new LinkedList<ResearchNode>();
        this.preConditions.addAll(preConditions);
        this.requiredProgress = requiredProgress;
        this.unlocName = "astralsorcery.journal.research." + this.name().toLowerCase(Locale.ROOT);
    }
    
    public Consumer<ResearchNode> getRegistrar() {
        return this::addResearchToGroup;
    }
    
    void addResearchToGroup(final ResearchNode res) {
        for (final ResearchNode node : this.researchNodes) {
            if (node.renderPosX == res.renderPosX && node.renderPosZ == res.renderPosZ) {
                throw new IllegalArgumentException("Tried to register 2 Research Nodes at the same position at x=" + res.renderPosX + ", z=" + res.renderPosZ + "! Present: " + node.getKey() + " - Tried to set: " + res.getKey());
            }
        }
        this.researchNodes.add(res);
    }
    
    public static ResearchProgression create(final String name, final ProgressionTier tier, final List<ResearchProgression> preConditions) {
        throw new IllegalStateException("Enum not extended");
    }
    
    public List<ResearchNode> getResearchNodes() {
        return this.researchNodes;
    }
    
    public ProgressionTier getRequiredProgress() {
        return this.requiredProgress;
    }
    
    public List<ResearchProgression> getPreConditions() {
        return Collections.unmodifiableList((List<? extends ResearchProgression>)this.preConditions);
    }
    
    public Component getName() {
        return (Component)new Component(this.unlocName);
    }
    
    @Nullable
    public static ResearchNode findNode(final String name) {
        for (final ResearchProgression prog : values()) {
            for (final ResearchNode node : prog.getResearchNodes()) {
                if (node.getKey().equals(name)) {
                    return node;
                }
            }
        }
        return null;
    }
    
    @Nonnull
    public static Collection<ResearchProgression> findProgression(final ResearchNode n) {
        final Collection<ResearchProgression> progressions = Lists.newArrayList();
        for (final ResearchProgression prog : values()) {
            if (prog.getResearchNodes().contains(n)) {
                progressions.add(prog);
            }
        }
        return progressions;
    }
}
