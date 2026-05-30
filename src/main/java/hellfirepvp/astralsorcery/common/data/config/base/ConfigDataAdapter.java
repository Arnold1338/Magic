package hellfirepvp.astralsorcery.common.data.config.base;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.Random;
import java.util.Iterator;
import hellfirepvp.astralsorcery.AstralSorcery;
import java.util.ArrayList;
import net.minecraftforge.fml.config.ModConfig;
import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;

public abstract class ConfigDataAdapter<T extends ConfigDataSet>
{
    private ForgeConfigSpec.ConfigValue<List<? extends String>> registryStore;
    private final ModConfig.Type registryConfigType;
    List<T> configuredValues;
    
    public ConfigDataAdapter() {
        this(ModConfig.Type.SERVER);
    }
    
    public ConfigDataAdapter(final ModConfig.Type type) {
        this.registryStore = null;
        this.configuredValues = null;
        this.registryConfigType = type;
    }
    
    protected String translationKey(final String key) {
        return String.format("config.registry.%s.%s", this.getSectionName(), key);
    }
    
    public final void configBuilt(final ForgeConfigSpec.ConfigValue<List<? extends String>> createdValue) {
        this.registryStore = createdValue;
    }
    
    public synchronized List<T> getConfiguredValues() {
        if (this.registryStore == null) {
            return new ArrayList<T>();
        }
        if (this.configuredValues == null) {
            this.configuredValues = new ArrayList<T>();
            for (final String str : (List)this.registryStore.get()) {
                T val;
                try {
                    val = this.deserialize(str);
                }
                catch (final IllegalArgumentException exc) {
                    AstralSorcery.log.error("Skipping configured entry in " + this.getSectionName() + "!");
                    AstralSorcery.log.error(exc.getMessage());

                }
                if (val != null) {
                    this.configuredValues.add(val);
                }
            }
        }
        return this.configuredValues;
    }
    
    @Nullable
    public synchronized T getRandomValue(final Random rand) {
        return MiscUtils.getRandomEntry(this.getConfiguredValues(), rand);
    }
    
    public abstract List<T> getDefaultValues();
    
    public abstract String getSectionName();
    
    public abstract String getCommentDescription();
    
    public abstract String getTranslationKey();
    
    public abstract Predicate<Object> getValidator();
    
    public ModConfig.Type getRegistryConfigType() {
        return this.registryConfigType;
    }
    
    @Nullable
    public abstract T deserialize(final String p0) throws IllegalArgumentException;
}
