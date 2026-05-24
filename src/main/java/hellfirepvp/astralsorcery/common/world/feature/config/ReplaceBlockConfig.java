package hellfirepvp.astralsorcery.common.world.feature.config;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.template.RuleTest;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.FeatureConfiguration;

public class ReplaceBlockConfig implements FeatureConfiguration
{
    public static final Codec<ReplaceBlockConfig> CODEC;
    public final RuleTest target;
    public final BlockState state;
    
    public ReplaceBlockConfig(final RuleTest target, final BlockState state) {
        this.target = target;
        this.state = state;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(codecInstance -> codecInstance.group((App)RuleTest.field_237127_c_.fieldOf("target").forGetter(config -> config.target), (App)BlockState.field_235877_b_.fieldOf("state").forGetter(config -> config.state)).apply((Applicative)codecInstance, (BiFunction)ReplaceBlockConfig::new));
    }
}
