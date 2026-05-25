package hellfirepvp.astralsorcery.common.perk;

import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import com.google.gson.JsonObject;
import com.google.gson.JsonObject;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.data.research.ProgressionTier;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.List;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.entity.player.Player;
import java.util.function.BiPredicate;

public class ProgressGatedPerk extends AbstractPerk
{
    private BiPredicate<Player, PlayerProgress> unlockFunction;
    private final List<IConstellation> neededConstellations;
    private final List<ResearchProgression> neededResearch;
    private final List<ProgressionTier> neededProgression;
    
    public ProgressGatedPerk(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
        this.unlockFunction = ((player, progress) -> true);
        this.neededConstellations = new ArrayList<IConstellation>();
        this.neededResearch = new ArrayList<ResearchProgression>();
        this.neededProgression = new ArrayList<ProgressionTier>();
    }
    
    public void addRequireConstellation(final IConstellation cst) {
        this.addResearchPreRequisite((player, progress) -> progress.hasConstellationDiscovered(cst));
        this.neededConstellations.add(cst);
    }
    
    public void addRequireProgress(final ResearchProgression research) {
        this.addResearchPreRequisite((player, progress) -> progress.hasResearch(research));
        this.neededResearch.add(research);
    }
    
    public void addRequireTier(final ProgressionTier tier) {
        this.addResearchPreRequisite((player, progress) -> progress.getTierReached().isThisLaterOrEqual(tier));
        this.neededProgression.add(tier);
    }
    
    public void addResearchPreRequisite(final BiPredicate<Player, PlayerProgress> unlockFunction) {
        this.unlockFunction = this.unlockFunction.and(unlockFunction);
        this.disableTooltipCaching();
    }
    
    @Override
    public boolean mayUnlockPerk(final PlayerProgress progress, final Player player) {
        return this.canSee(player, progress) && super.mayUnlockPerk(progress, player);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean addLocalizedTooltip(final Collection<MutableComponent> tooltip) {
        if (!this.canSeeClient()) {
            tooltip.add(new Component("perk.info.astralsorcery.missing_progress").withStyle(ChatFormatting.RED)));
            return false;
        }
        return super.addLocalizedTooltip(tooltip);
    }
    
    @OnlyIn(Dist.CLIENT)
    public final boolean canSeeClient() {
        return this.canSee((Player)Minecraft.getInstance().player, LogicalSide.CLIENT);
    }
    
    public final boolean canSee(final Player player, final LogicalSide side) {
        final PlayerProgress prog = ResearchHelper.getProgress(player, side);
        return prog.isValid() && this.canSee(player, prog);
    }
    
    public final boolean canSee(final Player player, final PlayerProgress progress) {
        return this.unlockFunction.test(player, progress);
    }
    
    @Override
    public void deserializeData(final JsonObject perkData) {
        super.deserializeData(perkData);
        this.neededConstellations.clear();
        this.neededResearch.clear();
        this.neededProgression.clear();
        if (JSONUtils.func_151204_g(perkData, "neededConstellations")) {
            final JsonArray array = JSONUtils.func_151214_t(perkData, "neededConstellations");
            for (int i = 0; i < array.size(); ++i) {
                final JsonElement el = array.get(i);
                final String key = JSONUtils.func_151206_a(el, String.format("neededConstellations[%s]", i));
                final IConstellation cst = ConstellationRegistry.getConstellation(new ResourceLocation(key));
                if (cst == null) {
                    throw new JsonParseException("Unknown constellation: " + key);
                }
                this.addRequireConstellation(cst);
            }
        }
        if (JSONUtils.func_151204_g(perkData, "neededResearch")) {
            final JsonArray array = JSONUtils.func_151214_t(perkData, "neededResearch");
            for (int i = 0; i < array.size(); ++i) {
                final JsonElement el = array.get(i);
                final String key = JSONUtils.func_151206_a(el, String.format("neededResearch[%s]", i));
                try {
                    this.addRequireProgress(ResearchProgression.valueOf(key));
                }
                catch (final Exception exc) {
                    throw new JsonParseException("Unknown research: " + key);
                }
            }
        }
        if (JSONUtils.func_151204_g(perkData, "neededProgression")) {
            final JsonArray array = JSONUtils.func_151214_t(perkData, "neededProgression");
            for (int i = 0; i < array.size(); ++i) {
                final JsonElement el = array.get(i);
                final String key = JSONUtils.func_151206_a(el, String.format("neededProgression[%s]", i));
                try {
                    this.addRequireTier(ProgressionTier.valueOf(key));
                }
                catch (final Exception exc) {
                    throw new JsonParseException("Unknown progress: " + key);
                }
            }
        }
    }
    
    @Override
    public void serializeData(final JsonObject perkData) {
        super.serializeData(perkData);
        if (!this.neededConstellations.isEmpty()) {
            final JsonArray array = new JsonArray();
            for (final IConstellation cst : this.neededConstellations) {
                array.add(cst.getRegistryName().toString());
            }
            perkData.add("neededConstellations", (JsonElement)array);
        }
        if (!this.neededResearch.isEmpty()) {
            final JsonArray array = new JsonArray();
            for (final ResearchProgression research : this.neededResearch) {
                array.add(research.name());
            }
            perkData.add("neededResearch", (JsonElement)array);
        }
        if (!this.neededProgression.isEmpty()) {
            final JsonArray array = new JsonArray();
            for (final ProgressionTier progress : this.neededProgression) {
                array.add(progress.name());
            }
            perkData.add("neededProgression", (JsonElement)array);
        }
    }
}
