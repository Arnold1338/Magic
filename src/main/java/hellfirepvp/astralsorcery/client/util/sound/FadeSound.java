package hellfirepvp.astralsorcery.client.util.sound;

import net.minecraft.util.Mth;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.sound.CategorizedSoundEvent;
import java.util.function.Predicate;
import net.minecraft.client.sounds.ISound;
import net.minecraft.client.sounds.ITickableSound;
import net.minecraft.client.sounds.SimpleSoundInstance;

public class FadeSound extends SimpleSoundInstance implements ITickableSound, ISound
{
    private Predicate<FadeSound> func;
    private boolean hasStoppedPlaying;
    private float volumeMultiplier;
    private float fadeInTicks;
    private float fadeOutTicks;
    private int tick;
    private int stopTick;
    
    public FadeSound(final CategorizedSoundEvent sound, final float volume, final float pitch, final Vector3 pos, final boolean isGlobal) {
        this(sound, sound.getCategory(), volume, pitch, pos, isGlobal);
    }
    
    public FadeSound(final SoundEvent sound, final SoundSource category, final float volume, final float pitch, final Vector3 pos, final boolean isGlobal) {
        super(sound.func_187503_a(), category, volume, pitch, true, 0, ISound.AttenuationType.LINEAR, (double)(float)pos.getX(), (double)(float)pos.getY(), (double)(float)pos.getZ(), isGlobal);
        this.func = null;
        this.hasStoppedPlaying = false;
        this.volumeMultiplier = 1.0f;
        this.fadeInTicks = 40.0f;
        this.fadeOutTicks = 1.0f;
        this.tick = 0;
        this.stopTick = 0;
    }
    
    public void setRefreshFunction(final Predicate<FadeSound> func) {
        this.func = func;
    }
    
    public <T extends FadeSound> T setFadeInTicks(final float fadeInTicks) {
        this.fadeInTicks = fadeInTicks;
        return (T)this;
    }
    
    public <T extends FadeSound> T setFadeOutTicks(final float fadeOutTicks) {
        this.fadeOutTicks = fadeOutTicks;
        return (T)this;
    }
    
    public boolean func_147667_k() {
        final boolean hasStoppedPlaying = this.func == null || this.func.test(this);
        this.hasStoppedPlaying = hasStoppedPlaying;
        return hasStoppedPlaying && this.stopTick > this.fadeOutTicks;
    }
    
    public boolean hasStoppedPlaying() {
        return this.hasStoppedPlaying || !Minecraft.func_71410_x().func_147118_V().func_215294_c((ISound)this);
    }
    
    public void setVolumeMultiplier(final float volumeMultiplier) {
        this.volumeMultiplier = Mth.func_76131_a(volumeMultiplier, 0.0f, 1.0f);
    }
    
    public float func_147653_e() {
        final float mulFadeIn = Mth.func_76131_a(this.tick / this.fadeInTicks, 0.0f, 1.0f);
        final float mulFadeOut = Mth.func_76131_a(1.0f - this.stopTick / this.fadeOutTicks, 0.0f, 1.0f);
        return mulFadeIn * mulFadeOut * super.func_147653_e() * this.volumeMultiplier;
    }
    
    public void func_73660_a() {
        ++this.tick;
        if (this.hasStoppedPlaying) {
            ++this.stopTick;
        }
    }
    
    public boolean func_211503_n() {
        return true;
    }
}
