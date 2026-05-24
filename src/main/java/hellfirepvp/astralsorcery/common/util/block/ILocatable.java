package hellfirepvp.astralsorcery.common.util.block;

import net.minecraft.core.BlockPos;

public interface ILocatable
{
    BlockPos getLocationPos();
    
    default ILocatable fromPos(final BlockPos pos) {
        return new PosLocatable(pos);
    }
    
    public static class PosLocatable implements ILocatable
    {
        private final BlockPos pos;
        
        private PosLocatable(final BlockPos pos) {
            this.pos = pos;
        }
        
        @Override
        public BlockPos getLocationPos() {
            return this.pos;
        }
    }
}
