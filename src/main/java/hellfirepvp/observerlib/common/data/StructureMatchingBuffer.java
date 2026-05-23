package hellfirepvp.observerlib.common.data;

import com.google.common.collect.Maps;
import hellfirepvp.observerlib.ObserverLib;
import hellfirepvp.observerlib.api.ChangeObserver;
import hellfirepvp.observerlib.api.ChangeSubscriber;
import hellfirepvp.observerlib.api.ObservableArea;
import hellfirepvp.observerlib.api.ObserverProvider;
import hellfirepvp.observerlib.common.change.MatchChangeSubscriber;
import hellfirepvp.observerlib.common.data.base.SectionWorldData;
import hellfirepvp.observerlib.common.data.base.WorldSection;
import hellfirepvp.observerlib.common.util.NBTHelper;
import hellfirepvp.observerlib.common.registry.RegistryProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class StructureMatchingBuffer extends SectionWorldData<StructureMatchingBuffer.MatcherSectionData> {
    public StructureMatchingBuffer(WorldCacheDomain.SaveKey<? extends StructureMatchingBuffer> key) {
        super(key, 4);
    }

    @Override
    public MatcherSectionData createNewSection(int sectionX, int sectionZ) { return new MatcherSectionData(sectionX, sectionZ); }

    @Override public void updateTick(Level world) {}

    @Nonnull
    public <T extends ChangeObserver> MatchChangeSubscriber<T> observeArea(Level world, BlockPos center, ObserverProvider provider) {
        MatchChangeSubscriber<T> existing = (MatchChangeSubscriber<T>) getSubscriber(center);
        if (existing != null) {
            if (existing.getObserver().getProviderRegistryName().equals(provider.getRegistryName())) return existing;
            ObserverLib.log.warn("Already observing at " + center + " by " + existing.getObserver().getProviderRegistryName() + ". Replacing.");
            write(() -> removeSubscriber(center));
        }
        T observer = (T) provider.provideObserver();
        MatchChangeSubscriber<T> subscriber = new MatchChangeSubscriber<>(center, observer);
        for (ChunkPos chPos : subscriber.getObservableChunks()) {
            MatcherSectionData data = getOrCreateSection(new net.minecraft.core.Vec3i(chPos.x << 4, 0, chPos.z << 4));
            write(() -> data.addSubscriber(center, subscriber));
            markDirty(data);
        }
        observer.initialize((LevelAccessor) world, center);
        return subscriber;
    }

    public boolean removeSubscriber(BlockPos pos) {
        MatcherSectionData data = getOrCreateSection(pos);
        ChangeSubscriber<? extends ChangeObserver> removed = write(() -> data.removeSubscriber(pos));
        if (removed != null) {
            ObservableArea area = removed.getObserver().getObservableArea();
            for (ChunkPos chPos : area.getAffectedChunks(pos)) {
                MatcherSectionData matchData = getOrCreateSection(new net.minecraft.core.Vec3i(chPos.x << 4, 0, chPos.z << 4));
                write(() -> matchData.removeSubscriber(pos));
                markDirty(matchData);
            }
        }
        return removed != null;
    }

    @Nullable
    public ChangeSubscriber<? extends ChangeObserver> getSubscriber(BlockPos pos) {
        return write(() -> getOrCreateSection(pos).getSubscriber(pos));
    }

    public void markDirty(BlockPos pos) { markDirty(new net.minecraft.core.Vec3i(pos.getX(), pos.getY(), pos.getZ())); }

    @Nonnull
    public Collection<MatchChangeSubscriber<?>> getSubscribers(ChunkPos pos) {
        MatcherSectionData data = getOrCreateSection(new net.minecraft.core.Vec3i(pos.x << 4, 0, pos.z << 4));
        return read(() -> new ArrayList<>(data.requestSubscribers.values()));
    }

    @Override public void writeToNBT(CompoundTag nbt) {}
    @Override public void readFromNBT(CompoundTag nbt) {}

    public static class MatcherSectionData extends WorldSection {
        private final Map<BlockPos, MatchChangeSubscriber<? extends ChangeObserver>> requestSubscribers = Maps.newHashMap();

        private MatcherSectionData(int sX, int sZ) { super(sX, sZ); }

        @Nullable MatchChangeSubscriber<? extends ChangeObserver> getSubscriber(BlockPos pos) { return requestSubscribers.get(pos); }
        @Nullable ChangeSubscriber<? extends ChangeObserver> removeSubscriber(BlockPos pos) { return requestSubscribers.remove(pos); }
        @Nullable ChangeSubscriber<? extends ChangeObserver> addSubscriber(BlockPos pos, MatchChangeSubscriber<? extends ChangeObserver> subscriber) { return requestSubscribers.put(pos, subscriber); }

        @Override
        public void writeToNBT(CompoundTag tag) {
            ListTag list = new ListTag();
            for (MatchChangeSubscriber<? extends ChangeObserver> sub : requestSubscribers.values()) {
                CompoundTag ct = new CompoundTag();
                NBTHelper.writeBlockPosToNBT(sub.getCenter(), ct);
                ct.putString("identifier", sub.getObserver().getProviderRegistryName().toString());
                NBTHelper.setAsSubTag(ct, "matchData", sub::writeToNBT);
                list.add(ct);
            }
            tag.put("subscribers", list);
        }

        @Override
        public void readFromNBT(CompoundTag tag) {
            requestSubscribers.clear();
            ListTag list = tag.getList("subscribers", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag ct = list.getCompound(i);
                BlockPos requester = NBTHelper.readBlockPosFromNBT(ct);
                ResourceLocation matchIdentifier = new ResourceLocation(ct.getString("identifier"));
                ObserverProvider observer = RegistryProviders.getProvider(matchIdentifier);
                if (observer == null) { ObserverLib.log.warn("Unknown Observer Provider: " + matchIdentifier + "! Skipping..."); continue; }
                MatchChangeSubscriber<?> subscriber = new MatchChangeSubscriber<>(requester, observer.provideObserver());
                subscriber.readFromNBT(ct.getCompound("matchData"));
                requestSubscribers.put(subscriber.getCenter(), subscriber);
            }
        }
    }
}
