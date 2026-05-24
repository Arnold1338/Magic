package hellfirepvp.astralsorcery.common.block.base.template;

import javax.annotation.Nonnull;
import net.minecraft.world.level.effect.MobEffect;
import net.minecraft.world.level.effect.MobEffects;
import net.minecraft.world.level.level.block.state.BlockBehaviour;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.block.FlowerBlock;

public abstract class BlockFlowerTemplate extends FlowerBlock implements CustomItemBlock
{
    public BlockFlowerTemplate(final AbstractBlock.Properties properties) {
        super(Effects.field_76432_h, 0, properties);
    }
    
    @Nonnull
    public abstract Effect func_220094_d();
    
    public abstract int func_220095_e();
}
