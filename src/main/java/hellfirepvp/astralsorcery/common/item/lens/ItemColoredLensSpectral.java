package hellfirepvp.astralsorcery.common.item.lens;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.util.PartialEffectExecutor;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.AstralSorcery;

public class ItemColoredLensSpectral extends ItemColoredLens
{
    private static final ColorTypeSpectral COLOR_TYPE_SPECTRAL;
    
    public ItemColoredLensSpectral() {
        super(ItemColoredLensSpectral.COLOR_TYPE_SPECTRAL);
    }
    
    static {
        COLOR_TYPE_SPECTRAL = new ColorTypeSpectral();
    }
    
    private static class ColorTypeSpectral extends LensColorType
    {
        private ColorTypeSpectral() {
            super(AstralSorcery.key("spectral"), TargetType.NONE, () -> new ItemStack((ItemLike)ItemsAS.COLORED_LENS_SPECTRAL), ColorsAS.COLORED_LENS_SPECTRAL, 0.3f, true);
        }
        
        @Override
        public void entityInBeam(final World world, final Vector3 origin, final Vector3 target, final Entity entity, final PartialEffectExecutor executor) {
        }
        
        @Override
        public void blockInBeam(final World world, final BlockPos pos, final BlockState state, final PartialEffectExecutor executor) {
        }
    }
}
