package hellfirepvp.astralsorcery.client.util.sound;

import net.minecraft.util.math.MathHelper;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.sound.CategorizedSoundEvent;

public class FadeLoopSound extends PositionedLoopSound
{
    private float fadeInTicks;
    private float fadeOutTicks;
    private int tick;
    private int stopTick;
    private boolean shouldStop;
    
    public FadeLoopSound(final CategorizedSoundEvent sound, final float volume, final float pitch, final Vector3 pos, final boolean isGlobal) {
        super(sound, volume, pitch, pos, isGlobal);
        this.fadeInTicks = 40.0f;
        this.fadeOutTicks = 1.0f;
        this.tick = 0;
        this.stopTick = 0;
        this.shouldStop = false;
    }
    
    public FadeLoopSound(final SoundEvent sound, final SoundSource category, final float volume, final float pitch, final Vector3 pos, final boolean isGlobal) {
        super(sound, category, volume, pitch, pos, isGlobal);
        this.fadeInTicks = 40.0f;
        this.fadeOutTicks = 1.0f;
        this.tick = 0;
        this.stopTick = 0;
        this.shouldStop = false;
    }
    
    public <T extends FadeLoopSound> T setFadeInTicks(final float fadeInTicks) {
        this.fadeInTicks = fadeInTicks;
        return (T)this;
    }
    
    public <T extends FadeLoopSound> T setFadeOutTicks(final float fadeOutTicks) {
        this.fadeOutTicks = fadeOutTicks;
        return (T)this;
    }
    
    @Override
    public boolean func_147667_k() {
        final boolean func_147667_k = super.func_147667_k();
        this.shouldStop = func_147667_k;
        return func_147667_k && this.stopTick > this.fadeOutTicks;
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        ++this.tick;
        if (this.shouldStop) {
            ++this.stopTick;
        }
    }
    
    public boolean func_211503_n() {
        return true;
    }
    
    @Override
    public float func_147653_e() {
        final float mulFadeIn = MathHelper.func_76131_a(this.tick / this.fadeInTicks, 0.0f, 1.0f);
        final float mulFadeOut = MathHelper.func_76131_a(1.0f - this.stopTick / this.fadeOutTicks, 0.0f, 1.0f);
        return mulFadeIn * mulFadeOut * super.func_147653_e();
    }
}
