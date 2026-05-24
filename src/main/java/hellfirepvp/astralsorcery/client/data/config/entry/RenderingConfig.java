package hellfirepvp.astralsorcery.client.data.config.entry;

import java.util.Random;
import java.util.function.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import net.minecraft.world.level.Level;
import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;

public class RenderingConfig extends ConfigEntry
{
    public static final RenderingConfig CONFIG;
    public ForgeConfigSpec.DoubleValue maxEffectRenderDistance;
    public ForgeConfigSpec.EnumValue<ParticleAmount> particleAmount;
    public ForgeConfigSpec.BooleanValue patreonEffects;
    public ForgeConfigSpec.IntValue minYFosicDisplay;
    public ForgeConfigSpec.ConfigValue<List<? extends String>> dimensionsWithSkyRendering;
    public ForgeConfigSpec.ConfigValue<List<? extends String>> dimensionsWithOnlyConstellationRendering;
    
    private RenderingConfig() {
        super("rendering");
    }
    
    @Override
    public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
        this.maxEffectRenderDistance = cfgBuilder.comment("Defines how close to the position of a particle/floating texture you have to be in order for it to render.").translation(this.translationKey("maxEffectRenderDistance")).defineInRange("maxEffectRenderDistance", 64.0, 1.0, 512.0);
        this.particleAmount = (ForgeConfigSpec.EnumValue<ParticleAmount>)cfgBuilder.comment("Sets the amount of particles/effects").translation(this.translationKey("particleAmount")).defineEnum("particleAmount", (Enum)ParticleAmount.ALL);
        this.patreonEffects = cfgBuilder.comment("Enables/Disables all patreon effects.").translation(this.translationKey("patreonEffects")).define("patreonEffects", true);
        this.minYFosicDisplay = cfgBuilder.comment("Defines the minimum y-level the fosic resonator will display the fosic field on.").translation(this.translationKey("minYFosicDisplay")).defineInRange("minYFosicDisplay", 0, 0, 256);
        this.dimensionsWithSkyRendering = (ForgeConfigSpec.ConfigValue<List<? extends String>>)cfgBuilder.comment("Whitelist of dimension ID's that will have special astral sorcery sky rendering").translation(this.translationKey("skyRenderingEnabled")).defineList("skyRenderingEnabled", (List)Lists.newArrayList((Object[])new String[] { World.field_234918_g_.func_240901_a_().toString() }), (Predicate)Predicates.alwaysTrue());
        this.dimensionsWithOnlyConstellationRendering = (ForgeConfigSpec.ConfigValue<List<? extends String>>)cfgBuilder.comment("If a dimension is listed here, the skyrender will only render constellations on top of the existing skybox.").translation(this.translationKey("skyRenderingConstellations")).defineList("skyRenderingConstellations", (List)Lists.newArrayList(), (Predicate)Predicates.alwaysTrue());
    }
    
    public double getMaxEffectRenderDistanceSq() {
        final double val = (double)this.maxEffectRenderDistance.get();
        return val * val;
    }
    
    static {
        CONFIG = new RenderingConfig();
    }
    
    public enum ParticleAmount
    {
        NONE(455) {
            @Override
            public boolean shouldSpawn(final Random r) {
                return false;
            }
        }, 
        MINIMAL(10), 
        LOWERED(4), 
        ALL(1);
        
        private final int rChance;
        
        private ParticleAmount(final int rChance) {
            this.rChance = rChance;
        }
        
        public boolean shouldSpawn(final Random r) {
            return r.nextInt(this.rChance) == 0;
        }
        
        public ParticleAmount less() {
            return values()[Math.max(this.ordinal() - 1, 0)];
        }
    }
}
