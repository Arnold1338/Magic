package hellfirepvp.astralsorcery.common.world.placement.config;

import java.util.function.Function;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Random;
import net.minecraft.util.math.MathHelper;
import com.mojang.serialization.Codec;
import net.minecraft.world.gen.placement.IPlacementConfig;

public class ChanceConfig implements IPlacementConfig
{
    public static final Codec<ChanceConfig> CODEC;
    private final float chance;
    
    public ChanceConfig(final float chance) {
        this.chance = MathHelper.func_76131_a(chance, 0.0f, 1.0f);
    }
    
    public boolean test(final Random rand) {
        return rand.nextFloat() < this.chance;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(codecInstance -> codecInstance.group((App)Codec.FLOAT.fieldOf("chance").forGetter(config -> config.chance)).apply((Applicative)codecInstance, (Function)ChanceConfig::new));
    }
}
