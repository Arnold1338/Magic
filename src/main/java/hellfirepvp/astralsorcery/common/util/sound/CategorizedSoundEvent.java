package hellfirepvp.astralsorcery.common.util.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;

public class CategorizedSoundEvent extends SoundEvent
{
    private final SoundSource category;
    
    public CategorizedSoundEvent(final ResourceLocation soundNameIn, final SoundSource category) {
        super(soundNameIn);
        this.category = category;
    }
    
    public SoundSource getCategory() {
        return this.category;
    }
}
