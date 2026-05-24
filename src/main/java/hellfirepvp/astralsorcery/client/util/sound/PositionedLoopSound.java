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

public class PositionedLoopSound extends SimpleSoundInstance implements ITickableSound, ISound
{
    private Predicate<PositionedLoopSound> func;
    private boolean hasStoppedPlaying;
    private float volumeMultiplier;
    
    public PositionedLoopSound(final CategorizedSoundEvent sound, final float volume, final float pitch, final Vector3 pos, final boolean isGlobal) {
        this(sound, sound.getCategory(), volume, pitch, pos, isGlobal);
    }
    
    public PositionedLoopSound(final SoundEvent sound, final SoundSource category, final float volume, final float pitch, final Vector3 pos, final boolean isGlobal) {
        super(sound.func_187503_a(), category, volume, pitch, true, 0, ISound.AttenuationType.LINEAR, (double)(float)pos.getX(), (double)(float)pos.getY(), (double)(float)pos.getZ(), isGlobal);
        this.func = null;
        this.hasStoppedPlaying = false;
        this.volumeMultiplier = 1.0f;
    }
    
    public void setRefreshFunction(final Predicate<PositionedLoopSound> func) {
        this.func = func;
    }
    
    public boolean func_147667_k() {
        return this.hasStoppedPlaying = (this.func == null || this.func.test(this));
    }
    
    public boolean hasStoppedPlaying() {
        return this.hasStoppedPlaying || !Minecraft.func_71410_x().func_147118_V().func_215294_c((ISound)this);
    }
    
    public void setVolumeMultiplier(final float volumeMultiplier) {
        this.volumeMultiplier = Mth.func_76131_a(volumeMultiplier, 0.0f, 1.0f);
    }
    
    public float func_147653_e() {
        return super.func_147653_e() * this.volumeMultiplier;
    }
    
    public void func_73660_a() {
    }
}
