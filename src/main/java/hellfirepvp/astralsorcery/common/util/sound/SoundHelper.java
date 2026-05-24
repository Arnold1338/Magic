package hellfirepvp.astralsorcery.common.util.sound;

import net.minecraft.core.BlockPos;
import net.minecraft.client.player.ClientPlayerEntity;
import hellfirepvp.astralsorcery.client.util.sound.FadeSound;
import hellfirepvp.astralsorcery.client.util.sound.FadeLoopSound;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.sounds.ISound;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.client.util.sound.PositionedLoopSound;
import java.util.function.Predicate;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.level.Level;
import net.minecraft.sounds.SoundEvent;

public class SoundHelper
{
    public static void playSoundAround(final SoundEvent sound, final World world, final Vector3i position, final float volume, final float pitch) {
        playSoundAround(sound, SoundSource.MASTER, world, position.getX(), position.getY(), position.getZ(), volume, pitch);
    }
    
    public static void playSoundAround(final SoundEvent sound, final SoundSource category, final World world, final Vector3i position, final float volume, final float pitch) {
        playSoundAround(sound, category, world, position.getX(), position.getY(), position.getZ(), volume, pitch);
    }
    
    public static void playSoundAround(final SoundEvent sound, final World world, final Vector3 position, final float volume, final float pitch) {
        playSoundAround(sound, SoundSource.MASTER, world, position.getX(), position.getY(), position.getZ(), volume, pitch);
    }
    
    public static void playSoundAround(final SoundEvent sound, final SoundSource category, final World world, final Vector3 position, final float volume, final float pitch) {
        playSoundAround(sound, category, world, position.getX(), position.getY(), position.getZ(), volume, pitch);
    }
    
    public static void playSoundAround(final SoundEvent sound, SoundSource category, final World world, final double posX, final double posY, final double posZ, final float volume, final float pitch) {
        if (sound instanceof CategorizedSoundEvent) {
            category = ((CategorizedSoundEvent)sound).getCategory();
        }
        world.func_184148_a((Player)null, posX, posY, posZ, sound, category, volume, pitch);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static PositionedLoopSound playSoundLoopClient(final SoundEvent sound, final Vector3 pos, final float volume, final float pitch, final boolean isGlobal, final Predicate<PositionedLoopSound> func) {
        SoundSource cat = SoundSource.MASTER;
        if (sound instanceof CategorizedSoundEvent) {
            cat = ((CategorizedSoundEvent)sound).getCategory();
        }
        final PositionedLoopSound posSound = new PositionedLoopSound(sound, cat, volume, pitch, pos, isGlobal);
        posSound.setRefreshFunction(func);
        Minecraft.func_71410_x().func_147118_V().func_147682_a((ISound)posSound);
        return posSound;
    }
    
    @OnlyIn(Dist.CLIENT)
    public static FadeLoopSound playSoundLoopFadeInClient(final SoundEvent sound, final Vector3 pos, final float volume, final float pitch, final boolean isGlobal, final Predicate<PositionedLoopSound> func) {
        SoundSource cat = SoundSource.MASTER;
        if (sound instanceof CategorizedSoundEvent) {
            cat = ((CategorizedSoundEvent)sound).getCategory();
        }
        final FadeLoopSound posSound = new FadeLoopSound(sound, cat, volume, pitch, pos, isGlobal);
        posSound.setRefreshFunction(func);
        Minecraft.func_71410_x().func_147118_V().func_147682_a((ISound)posSound);
        return posSound;
    }
    
    @OnlyIn(Dist.CLIENT)
    public static FadeSound playSoundFadeInClient(final SoundEvent sound, final Vector3 pos, final float volume, final float pitch, final boolean isGlobal, final Predicate<FadeSound> func) {
        SoundSource cat = SoundSource.MASTER;
        if (sound instanceof CategorizedSoundEvent) {
            cat = ((CategorizedSoundEvent)sound).getCategory();
        }
        final FadeSound posSound = new FadeSound(sound, cat, volume, pitch, pos, isGlobal);
        posSound.setRefreshFunction(func);
        Minecraft.func_71410_x().func_147118_V().func_147682_a((ISound)posSound);
        return posSound;
    }
    
    @OnlyIn(Dist.CLIENT)
    public static float getSoundVolume(final SoundSource cat) {
        return Minecraft.func_71410_x().field_71474_y.func_186711_a(cat);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playSoundClient(final SoundEvent sound, final float volume, final float pitch) {
        final ClientPlayerEntity player = Minecraft.func_71410_x().field_71439_g;
        if (player != null) {
            player.func_184185_a(sound, volume, pitch);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playSoundClientWorld(final CategorizedSoundEvent sound, final BlockPos pos, final float volume, final float pitch) {
        playSoundClientWorld(sound, sound.getCategory(), pos, volume, pitch);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playSoundClientWorld(final SoundEvent sound, final SoundSource cat, final BlockPos pos, final float volume, final float pitch) {
        if (Minecraft.func_71410_x().field_71441_e != null) {
            Minecraft.func_71410_x().field_71441_e.func_184148_a((Player)Minecraft.func_71410_x().field_71439_g, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), sound, cat, volume, pitch);
        }
    }
}
