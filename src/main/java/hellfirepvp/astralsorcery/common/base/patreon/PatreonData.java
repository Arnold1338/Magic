package hellfirepvp.astralsorcery.common.base.patreon;

import java.util.Collections;
import com.google.common.collect.Lists;
import java.util.List;

public class PatreonData
{
    private final List<EffectEntry> effectList;
    
    public PatreonData() {
        this.effectList = Lists.newArrayList();
    }
    
    public List<EffectEntry> getEffectList() {
        return Collections.unmodifiableList((List<? extends EffectEntry>)this.effectList);
    }
    
    public static class EffectEntry
    {
        private String uuid;
        private String effectClass;
        private final List<String> parameters;
        
        public EffectEntry() {
            this.parameters = Lists.newArrayList();
        }
        
        public String getUuid() {
            return this.uuid;
        }
        
        public String getEffectClass() {
            return this.effectClass;
        }
        
        public List<String> getParameters() {
            return this.parameters;
        }
    }
}
