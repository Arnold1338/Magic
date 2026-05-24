package hellfirepvp.astralsorcery.common.data.sync.client;

import net.minecraft.nbt.ListTag;
import java.util.Iterator;
import java.util.HashSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientDataReader;
import java.util.Collections;
import net.minecraft.world.entity.Entity;
import java.util.HashMap;
import java.util.Set;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientData;

public class ClientTimeFreezeEntities extends ClientData<ClientTimeFreezeEntities>
{
    private final Map<RegistryKey<World>, Set<Integer>> clientActiveEntityFreeze;
    
    public ClientTimeFreezeEntities() {
        this.clientActiveEntityFreeze = new HashMap<RegistryKey<World>, Set<Integer>>();
    }
    
    public boolean isFrozen(final Entity e) {
        return this.clientActiveEntityFreeze.getOrDefault(e.func_130014_f_().dimension(), Collections.emptySet()).contains(e.func_145782_y());
    }
    
    @Override
    public void clear(final RegistryKey<World> dimType) {
        this.clientActiveEntityFreeze.remove(dimType);
    }
    
    @Override
    public void clearClient() {
        this.clientActiveEntityFreeze.clear();
    }
    
    public static class Reader extends ClientDataReader<ClientTimeFreezeEntities>
    {
        @Override
        public void readFromIncomingFullSync(final ClientTimeFreezeEntities data, final CompoundTag compound) {
            this.readEntityInformation(data, compound);
        }
        
        @Override
        public void readFromIncomingDiff(final ClientTimeFreezeEntities data, final CompoundTag compound) {
            this.readEntityInformation(data, compound);
        }
        
        private void readEntityInformation(final ClientTimeFreezeEntities data, final CompoundTag compound) {
            final CompoundTag dimTypes = compound.func_74775_l("dimTypes");
            for (final String key : dimTypes.func_150296_c()) {
                final RegistryKey<World> dim = (RegistryKey<World>)RegistryKey.func_240903_a_(Registry.field_239699_ae_, new ResourceLocation(key));
                final ListTag list = dimTypes.getList(key, 3);
                final Set<Integer> entities = new HashSet<Integer>();
                for (int i = 0; i < list.size(); ++i) {
                    entities.add(list.func_186858_c(i));
                }
                data.clientActiveEntityFreeze.put(dim, entities);
            }
        }
    }
}
