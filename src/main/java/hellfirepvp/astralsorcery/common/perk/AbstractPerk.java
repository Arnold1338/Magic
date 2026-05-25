package hellfirepvp.astralsorcery.common.perk;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournalPerkTree;
import java.util.Objects;
import hellfirepvp.astralsorcery.common.perk.source.ModifierManager;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModContainer;
import net.minecraft.ChatFormatting;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.client.resources.I18n;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.data.research.PlayerPerkData;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.PerkAllocationType;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nonnull;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.network.chat.MutableComponent;
import java.util.List;
import hellfirepvp.astralsorcery.common.perk.tree.PerkTreePoint;
import java.awt.geom.Point2D;
import hellfirepvp.astralsorcery.common.util.CacheEventBus;
import net.minecraft.resources.ResourceLocation;
import java.util.Random;
import hellfirepvp.astralsorcery.common.perk.source.ModifierSource;

public class AbstractPerk implements ModifierSource
{
    protected static final Random rand;
    public static final PerkCategory CATEGORY_BASE;
    public static final PerkCategory CATEGORY_ROOT;
    public static final PerkCategory CATEGORY_MAJOR;
    public static final PerkCategory CATEGORY_KEY;
    public static final PerkCategory CATEGORY_EPIPHANY;
    public static final PerkCategory CATEGORY_FOCUS;
    private final ResourceLocation registryName;
    private final CacheEventBus busWrapper;
    protected final Point2D.Float offset;
    private String unlocalizedKey;
    private PerkCategory category;
    private boolean hiddenUnlessAllocated;
    private PerkTreePoint<? extends AbstractPerk> treePoint;
    private ResourceLocation customPerkType;
    private List<MutableComponent> tooltipCache;
    private boolean cacheTooltip;
    
    public AbstractPerk(final ResourceLocation name, final float x, final float y) {
        this.category = AbstractPerk.CATEGORY_BASE;
        this.hiddenUnlessAllocated = false;
        this.treePoint = null;
        this.customPerkType = null;
        this.tooltipCache = null;
        this.cacheTooltip = true;
        this.registryName = name;
        this.busWrapper = CacheEventBus.of(MinecraftForge.EVENT_BUS);
        this.offset = new Point2D.Float(x, y);
        this.unlocalizedKey = String.format("perk.%s.%s", name.func_110624_b(), name.addTransientModifier());
    }
    
    protected PerkTreePoint<? extends AbstractPerk> initPerkTreePoint() {
        return new PerkTreePoint<AbstractPerk>((AbstractPerk)this, this.getOffset());
    }
    
    protected void invalidate(final LogicalSide side) {
        this.busWrapper.unregisterAll();
        PerkCooldownHelper.removePerkCooldowns(side, this);
    }
    
    protected void validate(final LogicalSide side) {
        this.attachListeners(side, (IEventBus)this.busWrapper);
    }
    
    protected void attachListeners(final LogicalSide side, final IEventBus bus) {
    }
    
    @Nonnull
    public Point2D.Float getOffset() {
        return this.offset;
    }
    
    public final PerkTreePoint<? extends AbstractPerk> getPoint() {
        if (this.treePoint == null) {
            this.treePoint = this.initPerkTreePoint();
        }
        return this.treePoint;
    }
    
    public ResourceLocation getRegistryName() {
        return this.registryName;
    }
    
    public <T> T setCategory(final PerkCategory category) {
        this.category = category;
        return (T)this;
    }
    
    public <T> T setHiddenUnlessAllocated(final boolean hiddenUnlessAllocated) {
        this.hiddenUnlessAllocated = hiddenUnlessAllocated;
        return (T)this;
    }
    
    @Override
    public boolean canApplySource(final Player player, final LogicalSide dist) {
        return !ResearchHelper.getProgress(player, dist).getPerkData().isPerkSealed(this);
    }
    
    @Override
    public final void onApply(final Player player, final LogicalSide dist) {
        this.applyPerkLogic(player, dist);
    }
    
    @Override
    public final void onRemove(final Player player, final LogicalSide dist) {
        this.removePerkLogic(player, dist);
    }
    
    protected void applyPerkLogic(final Player player, final LogicalSide dist) {
    }
    
    protected void removePerkLogic(final Player player, final LogicalSide dist) {
    }
    
    protected LogicalSide getSide(final Entity entity) {
        return entity.level() ? LogicalSide.CLIENT : LogicalSide.SERVER;
    }
    
    @Nullable
    public CompoundTag getPerkData(final Player player, final LogicalSide dist) {
        return ResearchHelper.getProgress(player, dist).getPerkData().getData(this);
    }
    
    public void onUnlockPerkServer(@Nullable final Player player, final PerkAllocationType allocationType, final PlayerProgress progress, final CompoundTag dataStorage) {
    }
    
    public void onRemovePerkServer(final Player player, final PerkAllocationType allocationType, final PlayerProgress progress, final CompoundTag dataStorage) {
    }
    
    public <T extends AbstractPerk> T setName(final String name) {
        this.unlocalizedKey = name;
        return (T)this;
    }
    
    @Nonnull
    public PerkCategory getCategory() {
        return this.category;
    }
    
    public AllocationStatus getPerkStatus(@Nullable final Player player, final LogicalSide side) {
        if (player == null) {
            return AllocationStatus.UNALLOCATED;
        }
        final PlayerProgress progress = ResearchHelper.getProgress(player, side);
        if (!progress.isValid()) {
            return AllocationStatus.UNALLOCATED;
        }
        final PlayerPerkData perkData = progress.getPerkData();
        if (perkData.hasPerkAllocation(this, PerkAllocationType.UNLOCKED)) {
            return AllocationStatus.ALLOCATED;
        }
        if (perkData.hasPerkAllocation(this)) {
            return AllocationStatus.GRANTED;
        }
        return this.mayUnlockPerk(progress, player) ? AllocationStatus.UNLOCKABLE : AllocationStatus.UNALLOCATED;
    }
    
    public boolean mayUnlockPerk(final PlayerProgress progress, final Player player) {
        final PlayerPerkData perkData = progress.getPerkData();
        if (!perkData.hasFreeAllocationPoint(player, this.getSide((Entity)player))) {
            return false;
        }
        for (final AbstractPerk otherPerks : PerkTree.PERK_TREE.getConnectedPerks(this.getSide((Entity)player), this)) {
            if (perkData.hasPerkAllocation(otherPerks, PerkAllocationType.UNLOCKED)) {
                return true;
            }
        }
        return false;
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean isVisible(final PlayerProgress progress, final Player player) {
        return !this.hiddenUnlessAllocated || progress.getPerkData().hasPerkAllocation(this);
    }
    
    public MutableComponent getName() {
        return new Component(this.unlocalizedKey + ".name").toString()this.getCategory().getTextFormatting());
    }
    
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public Collection<MutableComponent> getDescription() {
        final List<MutableComponent> toolTip = new ArrayList<MutableComponent>();
        if (I18n.func_188566_a(this.unlocalizedKey + ".desc.1")) {
            for (int count = 1; I18n.func_188566_a(this.unlocalizedKey + ".desc." + count); ++count) {
                toolTip.add((MutableComponent)new Component(this.unlocalizedKey + ".desc." + count));
            }
            toolTip.add((MutableComponent)new Component(""));
        }
        else if (I18n.func_188566_a(this.unlocalizedKey + ".desc")) {
            toolTip.add((MutableComponent)new Component(this.unlocalizedKey + ".desc"));
            toolTip.add((MutableComponent)new Component(""));
        }
        return toolTip;
    }
    
    protected void disableTooltipCaching() {
        this.cacheTooltip = false;
        this.tooltipCache = null;
    }
    
    @OnlyIn(Dist.CLIENT)
    public final Collection<MutableComponent> getLocalizedTooltip() {
        if (this.cacheTooltip && this.tooltipCache != null) {
            return this.tooltipCache;
        }
        this.tooltipCache = Lists.newArrayList();
        if (!(this instanceof ProgressGatedPerk) || ((ProgressGatedPerk)this).canSeeClient()) {
            this.tooltipCache.add(this.getName());
            final int prevLength = this.tooltipCache.size();
            final boolean shouldAdd = this.addLocalizedTooltip(this.tooltipCache);
            if (shouldAdd && prevLength != this.tooltipCache.size()) {
                this.tooltipCache.add((MutableComponent)new Component(""));
            }
            this.tooltipCache.addAll(this.getDescription());
        }
        else {
            this.tooltipCache.add(Component.translatable("perk.info.astralsorcery.missing_progress").withStyle(ChatFormatting.RED));

        }
        return this.tooltipCache;
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean addLocalizedTooltip(final Collection<MutableComponent> tooltip) {
        return false;
    }
    
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public Collection<MutableComponent> getSource() {
        final String modid = this.getRegistryName().func_110624_b();
        final ModContainer mod = ModList.get().getModContainerById(modid).orElse(null);
        if (mod != null) {
            return Lists.newArrayList((Object[])new MutableComponent[] { (MutableComponent)new Component(mod.getModInfo().getDisplayName()) });
        }
        return null;
    }
    
    public void clearCaches(final LogicalSide side) {
    }
    
    @OnlyIn(Dist.CLIENT)
    public void clearClientTextCaches() {
        this.tooltipCache = null;
    }
    
    @Override
    public ResourceLocation getProviderName() {
        return ModifierManager.PERK_PROVIDER_KEY;
    }
    
    @Override
    public boolean isEqual(final ModifierSource other) {
        return this.equals(other);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractPerk)) {
            return false;
        }
        final AbstractPerk that = (AbstractPerk)o;
        return Objects.equals(this.getRegistryName(), that.getRegistryName());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.getRegistryName());
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean handleMouseClick(final ScreenJournalPerkTree gui, final double mouseX, final double mouseY) {
        return false;
    }
    
    public void deserializeData(final JsonObject perkData) {
    }
    
    public void serializeData(final JsonObject perkData) {
    }
    
    @Nullable
    public final ResourceLocation getCustomPerkType() {
        return this.customPerkType;
    }
    
    public final void setCustomPerkType(final ResourceLocation customPerkType) {
        this.customPerkType = customPerkType;
    }
    
    public final JsonObject serializePerk() {
        final JsonObject data = new JsonObject();
        data.addProperty("registry_name", this.getRegistryName().toString());
        if (this.getCustomPerkType() != null) {
            data.addProperty("perk_class", this.getCustomPerkType().toString());
        }
        data.addProperty("x", (Number)this.getOffset().x);
        data.addProperty("y", (Number)this.getOffset().y);
        data.addProperty("name", this.unlocalizedKey);
        data.addProperty("hiddenUnlessAllocated", Boolean.valueOf(this.hiddenUnlessAllocated));
        final JsonObject perkData = new JsonObject();
        this.serializeData(perkData);
        data.add("data", (JsonElement)perkData);
        return data;
    }
    
    static {
        rand = new Random();
        CATEGORY_BASE = new PerkCategory("base", ChatFormatting.WHITE);
        CATEGORY_ROOT = new PerkCategory("root", ChatFormatting.WHITE);
        CATEGORY_MAJOR = new PerkCategory("major", ChatFormatting.WHITE);
        CATEGORY_KEY = new PerkCategory("key", ChatFormatting.GOLD);
        CATEGORY_EPIPHANY = new PerkCategory("epiphany", ChatFormatting.GOLD);
        CATEGORY_FOCUS = new PerkCategory("focus", ChatFormatting.GOLD);
    }
    
    public static class PerkCategory
    {
        private final MutableComponent name;
        private final ChatFormatting color;
        
        public PerkCategory(@Nonnull final String unlocName, @Nonnull final ChatFormatting color) {
            this.name = (MutableComponent)new Component("perk.category.astralsorcery." + unlocName + ".name");
            this.color = color;
        }
        
        public ChatFormatting getTextFormatting() {
            return this.color;
        }
        
        public MutableComponent getName() {
            return this.name;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final PerkCategory that = (PerkCategory)o;
            return Objects.equals(this.name, that.name);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.name);
        }
    }
}
