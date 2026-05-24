package hellfirepvp.astralsorcery.common.data.research;

import java.util.Objects;
import java.util.stream.IntStream;
import java.util.UUID;
import java.util.Locale;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.network.play.server.PktSyncKnowledge;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import java.util.Iterator;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.ListTag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.perk.PerkTree;
import net.minecraft.util.math.MathHelper;
import hellfirepvp.astralsorcery.common.perk.PerkLevelManager;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.util.MapStream;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import java.util.function.Predicate;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import java.util.Set;

public class PlayerPerkData
{
    private Set<ResourceLocation> freePointTokens;
    private Map<AbstractPerk, AppliedPerk> perks;
    private double perkExp;
    
    public PlayerPerkData() {
        this.freePointTokens = new HashSet<ResourceLocation>();
        this.perks = new HashMap<AbstractPerk, AppliedPerk>();
        this.perkExp = 0.0;
    }
    
    public Collection<AbstractPerk> getSealedPerks() {
        return this.perks.values().stream().filter(AppliedPerk::isSealed).map((Function<? super AppliedPerk, ?>)AppliedPerk::getPerk).collect((Collector<? super Object, ?, Collection<AbstractPerk>>)Collectors.toList());
    }
    
    public Collection<AbstractPerk> getEffectGrantingPerks() {
        return this.perks.values().stream().filter(appliedPerk -> !appliedPerk.isSealed()).map((Function<? super AppliedPerk, ?>)AppliedPerk::getPerk).collect((Collector<? super Object, ?, Collection<AbstractPerk>>)Collectors.toList());
    }
    
    public Collection<AbstractPerk> getAllocatedPerks(final PerkAllocationType type) {
        return this.perks.values().stream().filter(appliedPerk -> appliedPerk.isAllocated(type)).map((Function<? super AppliedPerk, ?>)AppliedPerk::getPerk).collect((Collector<? super Object, ?, Collection<AbstractPerk>>)Collectors.toList());
    }
    
    public Collection<PerkAllocationType> getAllocationTypes(final AbstractPerk perk) {
        return this.findAppliedPerk(perk).map((Function<? super AppliedPerk, ? extends Collection<PerkAllocationType>>)AppliedPerk::getApplicationTypes).orElse((Collection<PerkAllocationType>)Collections.emptySet());
    }
    
    public boolean hasPerkEffect(final Predicate<AbstractPerk> perkMatch) {
        return this.hasPerkAllocation(perkMatch) && !this.isPerkSealed(perkMatch);
    }
    
    public boolean hasPerkEffect(final AbstractPerk perk) {
        return this.hasPerkAllocation(perk) && !this.isPerkSealed(perk);
    }
    
    public boolean hasPerkAllocation(final Predicate<AbstractPerk> perkMatch) {
        return this.findAppliedPerk(perkMatch).isPresent();
    }
    
    public boolean hasPerkAllocation(final AbstractPerk perk) {
        return this.findAppliedPerk(perk).isPresent();
    }
    
    public boolean hasPerkAllocation(final AbstractPerk perk, final PerkAllocationType type) {
        return this.findAppliedPerk(perk).map(appliedPerk -> appliedPerk.isAllocated(type)).orElse(false);
    }
    
    protected boolean canSealPerk(final AbstractPerk perk) {
        return !this.isPerkSealed(perk) && this.hasPerkAllocation(perk);
    }
    
    public boolean isPerkSealed(final AbstractPerk perk) {
        return this.findAppliedPerk(perk).map((Function<? super AppliedPerk, ? extends Boolean>)AppliedPerk::isSealed).orElse(false);
    }
    
    public boolean isPerkSealed(final Predicate<AbstractPerk> perkMatch) {
        return this.findAppliedPerk(perkMatch).map((Function<? super AppliedPerk, ? extends Boolean>)AppliedPerk::isSealed).orElse(false);
    }
    
    protected boolean sealPerk(final AbstractPerk perk) {
        return this.canSealPerk(perk) && this.findAppliedPerk(perk).map(appliedPerk -> appliedPerk.setSealed(true)).orElse(false);
    }
    
    protected boolean breakSeal(final AbstractPerk perk) {
        return this.findAppliedPerk(perk).filter(AppliedPerk::isSealed).map(appliedPerk -> appliedPerk.setSealed(false)).orElse(false);
    }
    
    public boolean updatePerkData(final AbstractPerk perk, final CompoundTag data) {
        final AppliedPerk appliedPerk = this.perks.get(perk);
        if (appliedPerk == null) {
            return false;
        }
        appliedPerk.perkData = data.func_74737_b();
        return true;
    }
    
    public boolean applyPerkAllocation(final AbstractPerk perk, final PlayerPerkAllocation allocation, final boolean simulate) {
        if (simulate && !this.perks.containsKey(perk)) {
            return true;
        }
        final AppliedPerk appliedPerk = this.perks.computeIfAbsent(perk, AppliedPerk::new);
        return appliedPerk.addAllocation(allocation, simulate);
    }
    
    public PerkRemovalResult removePerkAllocation(final AbstractPerk perk, final PlayerPerkAllocation allocation, final boolean simulate) {
        final AppliedPerk appliedPerk = this.perks.get(perk);
        if (appliedPerk == null) {
            return PerkRemovalResult.FAILURE;
        }
        if (!appliedPerk.isAllocated(allocation.getType())) {
            return PerkRemovalResult.FAILURE;
        }
        final PerkRemovalResult result = appliedPerk.removeAllocation(allocation, simulate);
        if (result.isFailure()) {
            return result;
        }
        if (!simulate && result == PerkRemovalResult.REMOVE_PERK) {
            this.perks.remove(perk);
        }
        return result;
    }
    
    @Nullable
    public CompoundTag getData(final AbstractPerk perk) {
        return this.findAppliedPerk(perk).map((Function<? super AppliedPerk, ?>)AppliedPerk::getPerkData).map((Function<? super Object, ? extends CompoundTag>)CompoundTag::func_74737_b).orElse(null);
    }
    
    @Nullable
    public CompoundTag getMetaData(final AbstractPerk perk) {
        return this.findAppliedPerk(perk).map((Function<? super AppliedPerk, ? extends CompoundTag>)AppliedPerk::getApplicationData).orElse(null);
    }
    
    private Optional<AppliedPerk> findAppliedPerk(final AbstractPerk perk) {
        return Optional.ofNullable(this.perks.get(perk));
    }
    
    private Optional<AppliedPerk> findAppliedPerk(final Predicate<AbstractPerk> perkFilter) {
        return MapStream.of(this.perks).filterKey(perkFilter).valueStream().findFirst();
    }
    
    protected boolean grantFreeAllocationPoint(final ResourceLocation freePointToken) {
        if (this.freePointTokens.contains(freePointToken)) {
            return false;
        }
        this.freePointTokens.add(freePointToken);
        return true;
    }
    
    protected boolean tryRevokeAllocationPoint(final ResourceLocation token) {
        return this.freePointTokens.remove(token);
    }
    
    public Collection<ResourceLocation> getFreePointTokens() {
        return Collections.unmodifiableCollection((Collection<? extends ResourceLocation>)this.freePointTokens);
    }
    
    public int getAvailablePerkPoints(final Player player, final LogicalSide side) {
        final int allocatedPerks = (int)this.perks.values().stream().filter(perk -> perk.isAllocated(PerkAllocationType.UNLOCKED)).count() - 1;
        final int allocationLevels = PerkLevelManager.getLevel(this.getPerkExp(), player, side);
        return allocationLevels + this.freePointTokens.size() - allocatedPerks;
    }
    
    public boolean hasFreeAllocationPoint(final Player player, final LogicalSide side) {
        return this.getAvailablePerkPoints(player, side) > 0;
    }
    
    public double getPerkExp() {
        return this.perkExp;
    }
    
    public int getPerkLevel(final Player player, final LogicalSide side) {
        return PerkLevelManager.getLevel(this.getPerkExp(), player, side);
    }
    
    public float getPercentToNextLevel(final Player player, final LogicalSide side) {
        return PerkLevelManager.getNextLevelPercent(this.getPerkExp(), player, side);
    }
    
    protected void modifyExp(double exp, final Player player) {
        final int currLevel = PerkLevelManager.getLevel(this.getPerkExp(), player, LogicalSide.SERVER);
        if (exp >= 0.0 && currLevel >= PerkLevelManager.getLevelCap(LogicalSide.SERVER, player)) {
            return;
        }
        final long expThisLevel = PerkLevelManager.getExpForLevel(currLevel, player, LogicalSide.SERVER);
        final long expNextLevel = PerkLevelManager.getExpForLevel(currLevel + 1, player, LogicalSide.SERVER);
        final long cap = MathHelper.func_76124_d((double)((expNextLevel - expThisLevel) * 0.08f));
        if (exp > cap) {
            exp = (double)cap;
        }
        this.perkExp = Math.max(this.perkExp + exp, 0.0);
    }
    
    protected void setExp(final double exp) {
        this.perkExp = Math.max(exp, 0.0);
    }
    
    void load(final PlayerProgress progress, final CompoundTag tag) {
        this.perks.clear();
        this.freePointTokens.clear();
        this.perkExp = 0.0;
        if (this.isLegacyData(tag)) {
            this.loadLegacyData(progress, tag);
            return;
        }
        this.perkExp = tag.putDouble("perkExp");
        final long perkTreeVersion = tag.getDouble("perkTreeVersion");
        if (PerkTree.PERK_TREE.getVersion(LogicalSide.SERVER).map(v -> !v.equals(perkTreeVersion)).orElse(true)) {
            AstralSorcery.log.info("Clearing perk-tree because the player's skill-tree version was outdated!");
            if (progress.getAttunedConstellation() != null) {
                final AbstractPerk root = PerkTree.PERK_TREE.getRootPerk(LogicalSide.SERVER, progress.getAttunedConstellation());
                if (root != null) {
                    final AppliedPerk newPerk = new AppliedPerk(root);
                    newPerk.addAllocation(PlayerPerkAllocation.unlock(), false);
                    root.onUnlockPerkServer(null, PerkAllocationType.UNLOCKED, progress, newPerk.getPerkData());
                    this.perks.put(root, newPerk);
                }
            }
            return;
        }
        CompoundTag nbt = null;
        this.freePointTokens.addAll(NBTHelper.readList(tag, "tokens", 8, nbt -> new ResourceLocation(nbt.func_150285_a_().replace("-", "_"))));
        final ListTag list = tag.getList("perks", 10);
        for (int i = 0; i < list.size(); ++i) {
            nbt = list.getCompound(i);
            deserialize(nbt).ifPresent(perk -> {
                final AppliedPerk appliedPerk = this.perks.put(perk.getPerk(), perk);
                return;
            });
        }
    }
    
    void save(final CompoundTag tag) {
        PerkTree.PERK_TREE.getVersion(LogicalSide.SERVER).ifPresent(version -> tag.func_74772_a("perkTreeVersion", (long)version));
        tag.func_74780_a("perkExp", this.perkExp);
        final ListTag tokens = new ListTag();
        for (final ResourceLocation key : this.freePointTokens) {
            tokens.add((Object)StringTag.func_229705_a_(key.toString()));
        }
        tag.put("tokens", (Tag)tokens);
        final ListTag perks = new ListTag();
        for (final AppliedPerk perk : this.perks.values()) {
            perks.add((Object)perk.serialize());
        }
        tag.put("perks", (Tag)perks);
    }
    
    public void write(final FriendlyByteBuf buf) {
        buf.writeDouble(this.perkExp);
        ByteBufUtils.writeCollection(buf, this.freePointTokens, ByteBufUtils::writeResourceLocation);
        ByteBufUtils.writeCollection(buf, this.perks.values(), (buffer, perk) -> {
            ByteBufUtils.writeResourceLocation(buffer, perk.getPerk().getRegistryName());
            perk.write(buffer);
        });
    }
    
    public static PlayerPerkData read(final FriendlyByteBuf buf, final LogicalSide side) {
        final PlayerPerkData data = new PlayerPerkData();
        data.perkExp = buf.readDouble();
        data.freePointTokens = ByteBufUtils.readSet(buf, ByteBufUtils::readResourceLocation);
        final Set<AppliedPerk> appliedPerks = ByteBufUtils.readSet(buf, buffer -> {
            final ResourceLocation key = ByteBufUtils.readResourceLocation(buffer);
            return (AppliedPerk)PerkTree.PERK_TREE.getPerk(side, key).map((Function<? super AbstractPerk, ?>)AppliedPerk::new).map(perk -> {
                perk.read(buffer);
                return perk;
            }).orElseThrow(() -> {
                new IllegalArgumentException("Unknown perk: " + key);
                final IllegalArgumentException ex;
                return (AppliedPerk)ex;
            });
        });
        appliedPerks.forEach(appliedPerk -> {
            final AppliedPerk appliedPerk2 = data.perks.put(appliedPerk.getPerk(), appliedPerk);
            return;
        });
        return data;
    }
    
    @OnlyIn(Dist.CLIENT)
    void receive(final PktSyncKnowledge message) {
        final PlayerPerkData copyFrom = message.perkData;
        this.perkExp = copyFrom.perkExp;
        this.freePointTokens = copyFrom.freePointTokens;
        this.perks = copyFrom.perks;
    }
    
    private boolean isLegacyData(final CompoundTag tag) {
        return tag.contains("sealedPerks");
    }
    
    private void loadLegacyData(final PlayerProgress progress, final CompoundTag compound) {
        final long perkTreeLevel = compound.getDouble("perkTreeVersion");
        if (PerkTree.PERK_TREE.getVersion(LogicalSide.SERVER).map(v -> !v.equals(perkTreeLevel)).orElse(true)) {
            AstralSorcery.log.info("Clearing perk-tree because the player's skill-tree version was outdated!");
            if (progress.getAttunedConstellation() != null) {
                final AbstractPerk root = PerkTree.PERK_TREE.getRootPerk(LogicalSide.SERVER, progress.getAttunedConstellation());
                if (root != null) {
                    final AppliedPerk newPerk = new AppliedPerk(root);
                    newPerk.addAllocation(PlayerPerkAllocation.unlock(), false);
                    root.onUnlockPerkServer(null, PerkAllocationType.UNLOCKED, progress, newPerk.getPerkData());
                    this.perks.put(root, newPerk);
                }
            }
        }
        else {
            if (compound.contains("perks")) {
                final ListTag list = compound.getList("perks", 10);
                for (int i = 0; i < list.size(); ++i) {
                    final CompoundTag tag = list.getCompound(i);
                    final String perkRegName = tag.getString("perkName");
                    final CompoundTag data = tag.func_74775_l("perkData");
                    PerkTree.PERK_TREE.getPerk(LogicalSide.SERVER, new ResourceLocation(perkRegName)).ifPresent(perk -> {
                        final AppliedPerk appliedPerk = new AppliedPerk(perk);
                        appliedPerk.addAllocation(PlayerPerkAllocation.unlock(), false);
                        appliedPerk.perkData = data;
                        this.perks.put(perk, appliedPerk);
                        return;
                    });
                }
            }
            if (compound.contains("sealedPerks")) {
                final ListTag list = compound.getList("sealedPerks", 10);
                for (int i = 0; i < list.size(); ++i) {
                    final CompoundTag tag = list.getCompound(i);
                    final String perkRegName = tag.getString("perkName");
                    PerkTree.PERK_TREE.getPerk(LogicalSide.SERVER, new ResourceLocation(perkRegName)).ifPresent(perk -> {
                        final AppliedPerk newPerk2 = this.perks.get(perk);
                        if (newPerk2 != null) {
                            newPerk2.setSealed(true);
                        }
                        return;
                    });
                }
            }
            if (compound.contains("pointTokens")) {
                final ListTag list = compound.getList("pointTokens", 8);
                for (int i = 0; i < list.size(); ++i) {
                    final String[] resource = legacySplitKey(list.func_150307_f(i).toLowerCase(Locale.ROOT));
                    resource[1] = resource[1].replace("-", "_").replace(":", "_");
                    this.freePointTokens.add(AstralSorcery.key(resource[1]));
                }
            }
        }
        if (compound.contains("perkExp")) {
            this.perkExp = compound.putDouble("perkExp");
        }
    }
    
    private static String[] legacySplitKey(final String resource) {
        final String[] keyParts = { "minecraft", resource };
        final int i = resource.indexOf(":");
        if (i >= 0) {
            keyParts[1] = resource.substring(i + 1);
        }
        return keyParts;
    }
    
    public static class AppliedPerk
    {
        private static final String SEALED_KEY = "sealed";
        private static final String APPLICATION_KEYS = "application";
        private final AbstractPerk perk;
        private CompoundTag perkData;
        private CompoundTag applicationData;
        private Set<PerkAllocationType> applicationTypes;
        
        public AppliedPerk(final AbstractPerk perk) {
            this.perkData = new CompoundTag();
            this.applicationData = new CompoundTag();
            this.applicationTypes = new HashSet<PerkAllocationType>();
            this.perk = perk;
        }
        
        public boolean isSealed() {
            return this.applicationData.contains("sealed");
        }
        
        public boolean setSealed(final boolean sealed) {
            if (sealed) {
                this.applicationData.putBoolean("sealed", true);
            }
            else {
                this.applicationData.func_82580_o("sealed");
            }
            return true;
        }
        
        public AbstractPerk getPerk() {
            return this.perk;
        }
        
        public CompoundTag getPerkData() {
            return this.perkData;
        }
        
        public CompoundTag getApplicationData() {
            return this.applicationData;
        }
        
        private int getTotalAllocationCount() {
            int sum = 0;
            for (final PerkAllocationType type : PerkAllocationType.values()) {
                sum += this.getAllocationCount(type);
            }
            return sum;
        }
        
        private int getAllocationCount(final PerkAllocationType type) {
            final CompoundTag metaData = this.getApplicationData();
            if (!metaData.func_150297_b("application", 10)) {
                return 0;
            }
            final CompoundTag applicationMeta = metaData.func_74775_l("application");
            final ListTag allocations = applicationMeta.getList(type.getSaveKey(), 10);
            return allocations.size();
        }
        
        public boolean isAllocated(final PerkAllocationType type) {
            return this.applicationTypes.contains(type);
        }
        
        private PerkRemovalResult removeAllocation(final PlayerPerkAllocation type, final boolean simulate) {
            final CompoundTag metaData = this.getApplicationData();
            if (!metaData.func_150297_b("application", 10)) {
                return PerkRemovalResult.FAILURE;
            }
            final CompoundTag applicationMeta = metaData.func_74775_l("application");
            final ListTag allocations = applicationMeta.getList(type.getType().getSaveKey(), 10);
            if (allocations.isEmpty()) {
                return PerkRemovalResult.FAILURE;
            }
            boolean removedMatch = false;
            final UUID removeUUID = type.getLockUUID();
            for (int i = 0; i < allocations.size(); ++i) {
                final CompoundTag tag = allocations.getCompound(i);
                final UUID lockUUID = tag.getUUID("uuid");
                if (lockUUID.equals(removeUUID)) {
                    if (!simulate) {
                        allocations.remove(i);
                    }
                    removedMatch = true;
                    break;
                }
            }
            if (!removedMatch) {
                return PerkRemovalResult.FAILURE;
            }
            if (simulate && allocations.size() <= 1) {
                if (this.applicationTypes.size() > 1) {
                    return PerkRemovalResult.REMOVE_ALLOCATION_TYPE;
                }
                return PerkRemovalResult.REMOVE_PERK;
            }
            else {
                if (!allocations.isEmpty()) {
                    return PerkRemovalResult.REMOVE_ALLOCATION;
                }
                this.applicationTypes.remove(type.getType());
                if (this.applicationTypes.isEmpty()) {
                    return PerkRemovalResult.REMOVE_PERK;
                }
                return PerkRemovalResult.REMOVE_ALLOCATION_TYPE;
            }
        }
        
        public boolean addAllocation(final PlayerPerkAllocation type, final boolean simulate) {
            if (!simulate) {
                this.applicationTypes.add(type.getType());
            }
            final CompoundTag metaData = this.getApplicationData();
            if (!metaData.func_150297_b("application", 10)) {
                if (simulate) {
                    return true;
                }
                metaData.put("application", (Tag)new CompoundTag());
            }
            final CompoundTag applicationMeta = metaData.func_74775_l("application");
            final String key = type.getType().getSaveKey();
            if (!applicationMeta.func_150297_b(key, 9)) {
                if (simulate) {
                    return true;
                }
                applicationMeta.put(key, (Tag)new ListTag());
            }
            final ListTag allocations = applicationMeta.getList(key, 10);
            final UUID newUUID = type.getLockUUID();
            final CompoundTag newKeyTag = new CompoundTag();
            newKeyTag.putUUID("uuid", newUUID);
            if (allocations.isEmpty()) {
                if (!simulate) {
                    allocations.add((Object)newKeyTag);
                }
                return true;
            }
            for (int i = 0; i < allocations.size(); ++i) {
                final CompoundTag tag = allocations.getCompound(i);
                final UUID lockUUID = tag.getUUID("uuid");
                if (lockUUID.equals(newUUID)) {
                    return false;
                }
            }
            return simulate || allocations.add((Object)newKeyTag);
        }
        
        public Set<PerkAllocationType> getApplicationTypes() {
            return this.applicationTypes;
        }
        
        private CompoundTag serialize() {
            final CompoundTag out = new CompoundTag();
            out.putString("perk", this.perk.getRegistryName().toString());
            out.put("perkData", (Tag)this.perkData);
            out.put("applicationData", (Tag)this.applicationData);
            final int[] types = this.applicationTypes.stream().mapToInt(Enum::ordinal).toArray();
            out.func_74783_a("applicationTypes", types);
            return out;
        }
        
        private static Optional<AppliedPerk> deserialize(final CompoundTag tag) {
            final ResourceLocation key = new ResourceLocation(tag.getString("perk"));
            return PerkTree.PERK_TREE.getPerk(LogicalSide.SERVER, key).map((Function<? super AbstractPerk, ?>)AppliedPerk::new).map(appliedPerk -> {
                appliedPerk.perkData = tag.func_74775_l("perkData");
                appliedPerk.applicationData = tag.func_74775_l("applicationData");
                final int[] types = tag.func_74759_k("applicationTypes");
                appliedPerk.applicationTypes = IntStream.of(types).mapToObj(type -> PerkAllocationType.values()[type]).collect((Collector<? super Object, ?, Set<PerkAllocationType>>)Collectors.toSet());
                return appliedPerk;
            });
        }
        
        private void write(final FriendlyByteBuf buf) {
            ByteBufUtils.writeNBTTag(buf, this.perkData);
            ByteBufUtils.writeNBTTag(buf, this.applicationData);
            ByteBufUtils.writeCollection(buf, (Collection<Enum<PerkAllocationType>>)this.applicationTypes, ByteBufUtils::writeEnumValue);
        }
        
        private void read(final FriendlyByteBuf buf) {
            this.perkData = ByteBufUtils.readNBTTag(buf);
            this.applicationData = ByteBufUtils.readNBTTag(buf);
            this.applicationTypes = ByteBufUtils.readSet(buf, buffer -> ByteBufUtils.readEnumValue(buffer, PerkAllocationType.class));
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final AppliedPerk that = (AppliedPerk)o;
            return Objects.equals(this.perk.getRegistryName(), that.perk.getRegistryName());
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.perk.getRegistryName());
        }
    }
}
