package hellfirepvp.astralsorcery.common.registry;

import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.LazyOptional;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import javax.annotation.Nonnull;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import java.util.concurrent.Callable;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraft.nbt.CompoundTag;
import java.util.function.Supplier;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import hellfirepvp.astralsorcery.common.lib.CapabilitiesAS;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import java.util.function.Consumer;
import net.minecraft.world.level.chunk.LevelChunk;
import hellfirepvp.astralsorcery.common.capability.ChunkFluidEntry;
import net.minecraftforge.eventbus.api.IEventBus;

public class RegistryCapabilities
{
    private RegistryCapabilities() {
    }
    
    public static void init(final IEventBus eventBus) {
        registerDefault(ChunkFluidEntry.class, ChunkFluidEntry::new);
        eventBus.addGenericListener((Class)Chunk.class, (Consumer)RegistryCapabilities::attachChunkCapability);
    }
    
    private static void attachChunkCapability(final AttachCapabilitiesEvent<Chunk> chunkEvent) {
        chunkEvent.addCapability(CapabilitiesAS.CHUNK_FLUID_KEY, (ICapabilityProvider)serializeableProvider(CapabilitiesAS.CHUNK_FLUID.getDefaultInstance()));
    }
    
    private static <T extends INBTSerializable<CompoundTag>> void registerDefault(final Class<T> capabilityClass, final Supplier<T> capProvider) {
        register(capabilityClass, serializeableStorage(), capProvider);
    }
    
    private static <T> void register(final Class<T> capabilityClass, final Capability.IStorage<T> capStorage, final Supplier<T> capProvider) {
        CapabilityManager.INSTANCE.register((Class)capabilityClass, (Capability.IStorage)capStorage, (Callable)capProvider::get);
    }
    
    private static <E extends INBTSerializable<CompoundTag>> ICapabilitySerializable<CompoundTag> serializeableProvider(final E defaultInstance) {
        return (ICapabilitySerializable<CompoundTag>)new ICapabilitySerializable<CompoundTag>() {
            @Nonnull
            public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
                if (cap == CapabilitiesAS.CHUNK_FLUID) {
                    return (LazyOptional<T>)LazyOptional.of(() -> defaultInstance);
                }
                return (LazyOptional<T>)LazyOptional.empty();
            }
            
            public CompoundTag serializeNBT() {
                return (CompoundTag)defaultInstance.serializeNBT();
            }
            
            public void deserializeNBT(final CompoundTag nbt) {
                defaultInstance.deserializeNBT((Tag)nbt);
            }
        };
    }
    
    private static <T extends INBTSerializable<CompoundTag>> Capability.IStorage<T> serializeableStorage() {
        return (Capability.IStorage<T>)new Capability.IStorage<T>() {
            @Nullable
            public Tag writeNBT(final Capability<T> capability, final T instance, final Direction side) {
                return instance.serializeNBT();
            }
            
            public void readNBT(final Capability<T> capability, final T instance, final Direction side, final Tag nbt) {
                instance.deserializeNBT((Tag)nbt);
            }
        };
    }
}
