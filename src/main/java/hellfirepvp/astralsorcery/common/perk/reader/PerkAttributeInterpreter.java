package hellfirepvp.astralsorcery.common.perk.reader;

import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraftforge.fml.LogicalSide;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeMap;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import java.util.Map;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PerkAttributeInterpreter
{
    private final Map<PerkAttributeType, PerkAttributeReader> attributeReaderOverrides;
    private PerkAttributeMap attributeMap;
    private final Player player;
    
    private PerkAttributeInterpreter(final PerkAttributeMap attributeMap, final Player player) {
        this.attributeReaderOverrides = Maps.newHashMap();
        this.attributeMap = attributeMap;
        this.player = player;
    }
    
    public static PerkAttributeInterpreter defaultInterpreter(final Player player) {
        return new Builder(player).build();
    }
    
    @Nullable
    public PerkStatistic getValue(final PerkAttributeType type) {
        if (this.attributeReaderOverrides.containsKey(type)) {
            return this.attributeReaderOverrides.get(type).getStatistics(this.attributeMap, this.player);
        }
        final PerkAttributeReader reader = type.getReader();
        if (reader != null) {
            return reader.getStatistics(this.attributeMap, this.player);
        }
        return null;
    }
    
    public static class Builder
    {
        private final PerkAttributeInterpreter reader;
        
        private Builder(final Player player) {
            this.reader = new PerkAttributeInterpreter(null, player, null);
        }
        
        public static Builder newBuilder(final Player player) {
            return new Builder(player);
        }
        
        public Builder overrideAttributeMap(final PerkAttributeMap map) {
            this.reader.attributeMap = map;
            return this;
        }
        
        public Builder overrideReader(final PerkAttributeType type, final PerkAttributeReader reader) {
            this.reader.attributeReaderOverrides.put(type, reader);
            return this;
        }
        
        public PerkAttributeInterpreter build() {
            if (this.reader.attributeMap == null) {
                this.reader.attributeMap = PerkAttributeHelper.getOrCreateMap(this.reader.player, LogicalSide.CLIENT);
            }
            return this.reader;
        }
    }
}
