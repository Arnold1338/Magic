package hellfirepvp.astralsorcery.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderStateShard;

public class RenderStateUtil
{
    public static class CullState extends RenderStateShard.CullState
    {
        private final boolean enabled;
        
        public CullState(final boolean enabled) {
            super(enabled);
            this.enabled = enabled;
        }
        
        public void func_228547_a_() {
            super.func_228547_a_();
            if (!this.enabled) {
                RenderSystem.disableCull();
            }
        }
        
        public void func_228549_b_() {
            super.func_228549_b_();
        }
    }
    
    public static class WriteMaskState extends RenderStateShard.WriteMaskState
    {
        private final boolean colorMask;
        private final boolean depthMask;
        
        public WriteMaskState(final boolean colorMask, final boolean depthMask) {
            super(colorMask, depthMask);
            this.colorMask = colorMask;
            this.depthMask = depthMask;
        }
        
        public void func_228547_a_() {
            super.func_228547_a_();
            if (this.depthMask) {
                RenderSystem.depthMask(true);
            }
            if (this.colorMask) {
                RenderSystem.colorMask(true, true, true, true);
            }
        }
        
        public void func_228549_b_() {
            super.func_228549_b_();
            if (this.depthMask) {
                RenderSystem.depthMask(true);
            }
            if (this.colorMask) {
                RenderSystem.colorMask(true, true, true, true);
            }
        }
    }
}
