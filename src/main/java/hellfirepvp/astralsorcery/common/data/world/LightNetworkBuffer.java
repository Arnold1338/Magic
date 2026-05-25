package hellfirepvp.astralsorcery.common.data.world;

import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionProvider;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionClassRegistry;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionSource;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.SourceClassRegistry;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import java.util.Collections;
import javax.annotation.Nullable;
import hellfirepvp.observerlib.common.data.base.WorldSection;
import java.util.List;
import net.minecraft.world.level.LevelAccessor;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightUpdateHandler;
import hellfirepvp.astralsorcery.common.starlight.IStarlightTransmission;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import java.util.LinkedList;
import hellfirepvp.astralsorcery.common.data.config.entry.LightNetworkConfig;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.starlight.network.TransmissionWorldHandler;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.block.BlockStateHelper;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.starlight.IStarlightSource;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightTransmissionHandler;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import net.minecraft.world.level.Level;
import java.util.HashSet;
import java.util.HashMap;
import hellfirepvp.observerlib.common.data.WorldCacheDomain;
import java.util.Set;
import net.minecraft.util.Tuple;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.starlight.IIndependentStarlightSource;
import net.minecraft.core.BlockPos;
import java.util.Map;
import hellfirepvp.observerlib.common.data.base.SectionWorldData;

public class LightNetworkBuffer extends SectionWorldData<ChunkNetworkData>
{
    private final Map<BlockPos, IIndependentStarlightSource> starlightSources;
    private Collection<Tuple<BlockPos, IIndependentStarlightSource>> cachedSourceTuples;
    private final Set<BlockPos> queueRemoval;
    
    public LightNetworkBuffer(final WorldCacheDomain.SaveKey<?> key) {
        super((WorldCacheDomain.SaveKey)key, 4);
        this.starlightSources = new HashMap<BlockPos, IIndependentStarlightSource>();
        this.cachedSourceTuples = null;
        this.queueRemoval = new HashSet<BlockPos>();
    }
    
    public WorldNetworkHandler getNetworkHandler(final Level world) {
        return new WorldNetworkHandler(this, world);
    }
    
    protected ChunkNetworkData createNewSection(final int sectionX, final int sectionZ) {
        return new ChunkNetworkData(sectionX, sectionZ);
    }
    
    public void updateTick(final Level world) {
        this.cleanupQueuedChunks();
        final TransmissionWorldHandler handle = StarlightTransmissionHandler.getInstance().getWorldHandler(world);
        final Iterator<Map.Entry<BlockPos, IIndependentStarlightSource>> iterator = this.starlightSources.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<BlockPos, IIndependentStarlightSource> entry = iterator.next();
            final BlockPos pos = entry.getKey();
            final IIndependentStarlightSource source = entry.getValue();
            MiscUtils.executeWithChunk((IWorldReader)world, pos, () -> {
                final IStarlightSource<?> te = MiscUtils.getTileAt((IBlockReader)world, pos, IStarlightSource.class, true);
                if (te != null) {
                    if (te.needsToRefreshNetworkChain()) {
                        if (handle != null) {
                            handle.breakSourceNetwork(source);
                        }
                        te.markChainRebuilt();
                    }
                }
                else {
                    final BlockState actual = world.getBlockState(pos);
                    AstralSorcery.log.warn("Cached source at " + pos + " but didn't find the BlockEntity!");
                    AstralSorcery.log.warn("Purging cache entry and removing erroneous block!");
                    AstralSorcery.log.warn("Block that gets purged: " + BlockStateHelper.serialize(actual));
                    iterator.remove();
                    if (world.func_175656_a(pos, actual.getFluidState().func_206883_i())) {
                        final ChunkNetworkData data = (ChunkNetworkData)this.getSection((Vec3i)pos);
                        if (data != null) {
                            data.removeSourceTile(pos);
                        }
                    }
                }
            });
        }
    }
    
    public void onLoad(final Level world) {
        super.onLoad(world);
        if (LightNetworkConfig.CONFIG.performNetworkIntegrityCheck.get()) {
            AstralSorcery.log.info("[LightNetworkIntegrityCheck] Performing StarlightNetwork integrity check for world " + world.dimension().func_240901_a_());
            final List<IPrismTransmissionNode> invalidRemoval = new LinkedList<IPrismTransmissionNode>();
            for (final ChunkNetworkData data : this.getSections()) {
                for (final ChunkSectionNetworkData secData : data.sections.values()) {
                    for (final IPrismTransmissionNode node : secData.getAllTransmissionNodes()) {
                        final IStarlightTransmission<?> te = MiscUtils.getTileAt((IBlockReader)world, node.getLocationPos(), IStarlightTransmission.class, true);
                        if (te == null) {
                            invalidRemoval.add(node);
                        }
                        else {
                            final IPrismTransmissionNode newNode = (IPrismTransmissionNode)te.provideTransmissionNode(node.getLocationPos());
                            if (!node.getClass().isAssignableFrom(newNode.getClass())) {
                                invalidRemoval.add(node);
                            }
                            else {
                                if (node.needsUpdate()) {
                                    StarlightUpdateHandler.getInstance().addNode(world, node);
                                }
                                node.postLoad((IWorld)world);
                            }
                        }
                    }
                }
            }
            AstralSorcery.log.info("[LightNetworkIntegrityCheck] Performed StarlightNetwork integrity check. Found " + invalidRemoval.size() + " invalid transmission nodes.");
            for (final IPrismTransmissionNode node2 : invalidRemoval) {
                this.removeTransmission(node2.getLocationPos());
            }
            AstralSorcery.log.info("[LightNetworkIntegrityCheck] Removed invalid transmission nodes from the network.");
        }
        else {
            for (final ChunkNetworkData data2 : this.getSections()) {
                for (final ChunkSectionNetworkData secData2 : data2.sections.values()) {
                    for (final IPrismTransmissionNode node3 : secData2.getAllTransmissionNodes()) {
                        if (node3.needsUpdate()) {
                            StarlightUpdateHandler.getInstance().addNode(world, node3);
                        }
                        node3.postLoad((IWorld)world);
                    }
                }
            }
        }
    }
    
    private void cleanupQueuedChunks() {
        for (final BlockPos pos : this.queueRemoval) {
            final ChunkNetworkData data = (ChunkNetworkData)this.getSection((Vec3i)pos);
            if (data != null && data.isEmpty()) {
                this.removeSection((WorldSection)data);
            }
        }
        this.queueRemoval.clear();
    }
    
    @Nullable
    public ChunkSectionNetworkData getSectionData(final BlockPos pos) {
        final ChunkNetworkData data = (ChunkNetworkData)this.getSection((Vec3i)pos);
        if (data == null) {
            return null;
        }
        return data.getSection(pos.getY() >> 4);
    }
    
    @Nullable
    public IIndependentStarlightSource getSource(final BlockPos at) {
        return this.starlightSources.get(at);
    }
    
    public Collection<Tuple<BlockPos, IIndependentStarlightSource>> getAllSources() {
        if (this.cachedSourceTuples == null) {
            final Collection<Tuple<BlockPos, IIndependentStarlightSource>> cache = new LinkedList<Tuple<BlockPos, IIndependentStarlightSource>>();
            for (final Map.Entry<BlockPos, IIndependentStarlightSource> entry : this.starlightSources.entrySet()) {
                cache.add((Tuple<BlockPos, IIndependentStarlightSource>)new Tuple((Object)entry.getKey(), (Object)entry.getValue()));
            }
            this.cachedSourceTuples = Collections.unmodifiableCollection((Collection<? extends Tuple<BlockPos, IIndependentStarlightSource>>)cache);
        }
        return this.cachedSourceTuples;
    }
    
    public void readFromNBT(final CompoundTag nbt) {
        this.starlightSources.clear();
        this.cachedSourceTuples = null;
        if (nbt.contains("sources")) {
            final ListTag list = nbt.getList("sources", 10);
            for (int i = 0; i < list.size(); ++i) {
                final CompoundTag sourcePos = list.getCompound(i);
                final BlockPos at = NBTHelper.readBlockPosFromNBT(sourcePos);
                final CompoundTag comp = sourcePos.func_74775_l("source");
                final ResourceLocation identifier = new ResourceLocation(comp.getString("sTypeId"));
                final SourceClassRegistry.SourceProvider provider = SourceClassRegistry.getProvider(identifier);
                if (provider == null) {
                    AstralSorcery.log.warn("Couldn't load source tile at " + at + " - invalid identifier: " + identifier);
                }
                else {
                    final IIndependentStarlightSource source = provider.provideEmptySource();
                    source.readFromNBT(comp);
                    this.starlightSources.put(at, source);
                }
            }
        }
    }
    
    public void writeToNBT(final CompoundTag nbt) {
        this.cleanupQueuedChunks();
        final ListTag sourceList = new ListTag();
        for (final BlockPos pos : this.starlightSources.keySet()) {
            final CompoundTag sourceTag = new CompoundTag();
            NBTHelper.writeBlockPosToNBT(pos, sourceTag);
            final CompoundTag source = new CompoundTag();
            final IIndependentStarlightSource sourceNode = this.starlightSources.get(pos);
            try {
                sourceNode.writeToNBT(source);
            }
            catch (final Exception exc) {
                AstralSorcery.log.warn("Couldn't write source-node data for network node at " + pos.toString() + "!");
                AstralSorcery.log.warn("This is a major problem. To be perfectly save, consider making a backup, then break or mcedit the tileentity out and place a proper/new one...");
                continue;
            }
            source.putString("sTypeId", sourceNode.getProvider().getIdentifier().toString());
            sourceTag.put("source", (Tag)source);
            sourceList.add((Object)sourceTag);
        }
        nbt.put("sources", (Tag)sourceList);
    }
    
    public void addSource(final IStarlightSource<?> source, final BlockPos pos) {
        final ChunkNetworkData data = (ChunkNetworkData)this.getOrCreateSection((Vec3i)pos);
        data.addSourceTile(pos, source);
        final IIndependentStarlightSource newSource = this.addIndependentSource(pos, source);
        if (newSource != null) {
            final Map<BlockPos, IIndependentStarlightSource> copyTr = Collections.unmodifiableMap((Map<? extends BlockPos, ? extends IIndependentStarlightSource>)new HashMap<BlockPos, IIndependentStarlightSource>(this.starlightSources));
            final Thread tr = new Thread(() -> this.threadedUpdateSourceProximity(copyTr));
            tr.setName("StarlightNetwork-UpdateThread");
            tr.start();
        }
        this.markDirty((WorldSection)data);
    }
    
    private void threadedUpdateSourceProximity(final Map<BlockPos, IIndependentStarlightSource> copyTr) {
        try {
            for (final Map.Entry<BlockPos, IIndependentStarlightSource> sourceTuple : copyTr.entrySet()) {
                sourceTuple.getValue().threadedUpdateProximity(sourceTuple.getKey(), copyTr);
            }
        }
        catch (final Exception exc) {
            AstralSorcery.log.warn("Failed to update proximity status for source nodes.");
            exc.printStackTrace();
        }
    }
    
    public void addTransmission(final IStarlightTransmission<?> transmission, final BlockPos pos) {
        final ChunkNetworkData data = (ChunkNetworkData)this.getOrCreateSection((Vec3i)pos);
        data.addTransmissionTile(pos, transmission);
        this.markDirty((WorldSection)data);
    }
    
    public void removeSource(final BlockPos pos) {
        final ChunkNetworkData data = (ChunkNetworkData)this.getSection((Vec3i)pos);
        if (data == null) {
            return;
        }
        data.removeSourceTile(pos);
        this.removeIndependentSource(pos);
        final Map<BlockPos, IIndependentStarlightSource> copyTr = Collections.unmodifiableMap((Map<? extends BlockPos, ? extends IIndependentStarlightSource>)new HashMap<BlockPos, IIndependentStarlightSource>(this.starlightSources));
        final Thread tr = new Thread(() -> this.threadedUpdateSourceProximity(copyTr));
        tr.setName("StarlightNetwork-UpdateThread");
        tr.start();
        this.checkIntegrity(pos);
        this.markDirty((WorldSection)data);
    }
    
    public void removeTransmission(final BlockPos pos) {
        final ChunkNetworkData data = (ChunkNetworkData)this.getSection((Vec3i)pos);
        if (data == null) {
            return;
        }
        data.removeTransmissionTile(pos);
        this.checkIntegrity(pos);
        this.markDirty((WorldSection)data);
    }
    
    private void checkIntegrity(final BlockPos actualPos) {
        final ChunkNetworkData data = (ChunkNetworkData)this.getSection((Vec3i)actualPos);
        if (data == null) {
            return;
        }
        data.checkIntegrity();
        if (data.isEmpty()) {
            this.queueRemoval.add(actualPos);
        }
    }
    
    @Nullable
    private IIndependentStarlightSource addIndependentSource(final BlockPos pos, final IStarlightSource<?> source) {
        this.cachedSourceTuples = null;
        final ITransmissionSource node = source.getNode();
        if (node != null) {
            final IIndependentStarlightSource sourceNode = node.provideNewIndependentSource(source);
            this.starlightSources.put(pos, sourceNode);
            return sourceNode;
        }
        return null;
    }
    
    private void removeIndependentSource(final BlockPos pos) {
        this.starlightSources.remove(pos);
        this.cachedSourceTuples = null;
    }
    
    public static class ChunkNetworkData extends WorldSection
    {
        private final Map<Integer, ChunkSectionNetworkData> sections;
        
        ChunkNetworkData(final int sX, final int sZ) {
            super(sX, sZ);
            this.sections = new HashMap<Integer, ChunkSectionNetworkData>();
        }
        
        public void readFromNBT(final CompoundTag tag) {
            for (final String key : tag.func_150296_c()) {
                int yLevel;
                try {
                    yLevel = Integer.parseInt(key);
                }
                catch (final NumberFormatException exc) {
                    continue;
                }
                final ListTag yData = tag.getList(key, 10);
                final ChunkSectionNetworkData sectionNetData = loadFromNBT(yData);
                this.sections.put(yLevel, sectionNetData);
            }
        }
        
        public void writeToNBT(final CompoundTag data) {
            for (final Integer yLevel : this.sections.keySet()) {
                final ChunkSectionNetworkData sectionData = this.sections.get(yLevel);
                final ListTag sectionTag = new ListTag();
                sectionData.writeToNBT(sectionTag);
                data.put(String.valueOf(yLevel), (Tag)sectionTag);
            }
        }
        
        @Nullable
        public ChunkSectionNetworkData getSection(final int yLevel) {
            return this.sections.get(yLevel);
        }
        
        public void checkIntegrity() {
            final Iterator<Integer> iterator = this.sections.keySet().iterator();
            while (iterator.hasNext()) {
                final Integer yLevel = iterator.next();
                final ChunkSectionNetworkData data = this.sections.get(yLevel);
                if (data.isEmpty()) {
                    iterator.remove();
                }
            }
        }
        
        public boolean isEmpty() {
            return this.sections.isEmpty();
        }
        
        private ChunkSectionNetworkData getOrCreateSection(final int yLevel) {
            ChunkSectionNetworkData section = this.getSection(yLevel);
            if (section == null) {
                section = new ChunkSectionNetworkData();
                this.sections.put(yLevel, section);
            }
            return section;
        }
        
        private void removeSourceTile(final BlockPos pos) {
            final int yLevel = (pos.getY() & 0xFF) >> 4;
            final ChunkSectionNetworkData section = this.getSection(yLevel);
            if (section == null) {
                return;
            }
            section.removeSourceTile(pos);
        }
        
        private void removeTransmissionTile(final BlockPos pos) {
            final int yLevel = (pos.getY() & 0xFF) >> 4;
            final ChunkSectionNetworkData section = this.getSection(yLevel);
            if (section == null) {
                return;
            }
            section.removeTransmissionTile(pos);
        }
        
        private void addSourceTile(final BlockPos pos, final IStarlightSource<?> source) {
            final int yLevel = (pos.getY() & 0xFF) >> 4;
            final ChunkSectionNetworkData section = this.getOrCreateSection(yLevel);
            section.addSourceTile(pos, source);
        }
        
        private void addTransmissionTile(final BlockPos pos, final IStarlightTransmission<?> transmission) {
            final int yLevel = (pos.getY() & 0xFF) >> 4;
            final ChunkSectionNetworkData section = this.getOrCreateSection(yLevel);
            section.addTransmissionTile(pos, transmission);
        }
    }
    
    public static class ChunkSectionNetworkData
    {
        private final Map<BlockPos, IPrismTransmissionNode> nodes;
        
        public ChunkSectionNetworkData() {
            this.nodes = new HashMap<BlockPos, IPrismTransmissionNode>();
        }
        
        private static ChunkSectionNetworkData loadFromNBT(final ListTag sectionData) {
            final ChunkSectionNetworkData netData = new ChunkSectionNetworkData();
            for (int i = 0; i < sectionData.size(); ++i) {
                final CompoundTag nodeComp = sectionData.getCompound(i);
                final BlockPos pos = NBTHelper.readBlockPosFromNBT(nodeComp);
                final CompoundTag prismComp = nodeComp.func_74775_l("nodeTag");
                final ResourceLocation nodeIdentifier = new ResourceLocation(prismComp.getString("trNodeId"));
                final TransmissionProvider provider = TransmissionClassRegistry.getProvider(nodeIdentifier);
                if (provider == null) {
                    AstralSorcery.log.warn("Couldn't load node tile at " + pos + " - invalid identifier: " + nodeIdentifier);
                }
                else {
                    final IPrismTransmissionNode node = provider.get();
                    node.readFromNBT(prismComp);
                    netData.nodes.put(pos, node);
                }
            }
            return netData;
        }
        
        private void writeToNBT(final ListTag sectionData) {
            for (final Map.Entry<BlockPos, IPrismTransmissionNode> node : this.nodes.entrySet()) {
                try {
                    final CompoundTag nodeComp = new CompoundTag();
                    NBTHelper.writeBlockPosToNBT(node.getKey(), nodeComp);
                    final CompoundTag prismComp = new CompoundTag();
                    final IPrismTransmissionNode prismNode = node.getValue();
                    prismNode.writeToNBT(prismComp);
                    prismComp.putString("trNodeId", prismNode.getProvider().getIdentifier().toString());
                    nodeComp.put("nodeTag", (Tag)prismComp);
                    sectionData.add((Object)nodeComp);
                }
                catch (final Exception exc) {
                    try {
                        final BlockPos at = node.getKey();
                        AstralSorcery.log.warn("Couldn't write node data for network node at " + at.toString() + "!");
                        AstralSorcery.log.warn("This is a major problem. To be perfectly save, consider making a backup, then break or mcedit the tileentity out and place a proper/new one...");
                    }
                    catch (final Exception exc2) {
                        try {
                            final BlockPos at2 = node.getValue().getLocationPos();
                            AstralSorcery.log.warn("Couldn't write node data for network node at " + at2.toString() + "!");
                            AstralSorcery.log.warn("This is a major problem. To be perfectly save, consider making a backup, then break or mcedit the tileentity out and place a proper/new one...");
                        }
                        catch (final Exception exc3) {
                            AstralSorcery.log.warn("Couldn't write node data for a network node! Skipping...");
                        }
                    }
                }
            }
        }
        
        public boolean isEmpty() {
            return this.nodes.isEmpty();
        }
        
        public Collection<IPrismTransmissionNode> getAllTransmissionNodes() {
            return Collections.unmodifiableCollection((Collection<? extends IPrismTransmissionNode>)this.nodes.values());
        }
        
        @Nullable
        public IPrismTransmissionNode getTransmissionNode(final BlockPos at) {
            return this.nodes.get(at);
        }
        
        private void removeSourceTile(final BlockPos pos) {
            this.removeNode(pos);
        }
        
        private void removeTransmissionTile(final BlockPos pos) {
            this.removeNode(pos);
        }
        
        private void removeNode(final BlockPos pos) {
            this.nodes.remove(pos);
        }
        
        private void addSourceTile(final BlockPos pos, final IStarlightSource<?> source) {
            this.addNode(pos, source);
        }
        
        private void addTransmissionTile(final BlockPos pos, final IStarlightTransmission<?> transmission) {
            this.addNode(pos, transmission);
        }
        
        private void addNode(final BlockPos pos, final IStarlightTransmission<?> transmission) {
            this.nodes.put(pos, (IPrismTransmissionNode)transmission.provideTransmissionNode(pos));
        }
    }
}
