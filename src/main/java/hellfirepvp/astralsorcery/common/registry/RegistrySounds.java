package hellfirepvp.astralsorcery.common.registry;

import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.sounds.SoundEvent;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import net.minecraft.sounds.SoundSource;
import hellfirepvp.astralsorcery.common.util.sound.CategorizedSoundEvent;

public class RegistrySounds
{
    private RegistrySounds() {
    }
    
    public static void init() {
        SoundsAS.BLOCK_COLOREDLENS_ATTACH = registerSound("block_coloredlens_attach", SoundSource.BLOCKS);
        SoundsAS.CRAFT_ATTUNEMENT = registerSound("craft_attunement", SoundSource.MASTER);
        SoundsAS.CRAFT_FINISH = registerSound("craft_finish", SoundSource.BLOCKS);
        SoundsAS.ALTAR_CRAFT_START = registerSound("altar_craft_start", SoundSource.BLOCKS);
        SoundsAS.ALTAR_CRAFT_FINISH = registerSound("altar_craft_finish", SoundSource.BLOCKS);
        SoundsAS.ALTAR_CRAFT_LOOP_T1 = registerSound("altar_craft_loop_t1", SoundSource.BLOCKS);
        SoundsAS.ALTAR_CRAFT_LOOP_T2 = registerSound("altar_craft_loop_t2", SoundSource.BLOCKS);
        SoundsAS.ALTAR_CRAFT_LOOP_T3 = registerSound("altar_craft_loop_t3", SoundSource.BLOCKS);
        SoundsAS.ALTAR_CRAFT_LOOP_T4 = registerSound("altar_craft_loop_t4", SoundSource.BLOCKS);
        SoundsAS.ALTAR_CRAFT_LOOP_T4_WAITING = registerSound("altar_craft_loop_t4_waiting", SoundSource.BLOCKS);
        SoundsAS.ATTUNEMENT_ATLAR_IDLE = registerSound("attunement_altar_idle_loop", SoundSource.BLOCKS);
        SoundsAS.ATTUNEMENT_ATLAR_PLAYER_ATTUNE = registerSound("attunement_altar_player_attune", SoundSource.BLOCKS);
        SoundsAS.ATTUNEMENT_ATLAR_ITEM_START = registerSound("attunement_altar_item_start", SoundSource.BLOCKS);
        SoundsAS.ATTUNEMENT_ATLAR_ITEM_FINISH = registerSound("attunement_altar_item_finish", SoundSource.BLOCKS);
        SoundsAS.ATTUNEMENT_ATLAR_ITEM_LOOP = registerSound("attunement_altar_item_loop", SoundSource.BLOCKS);
        SoundsAS.INFUSER_CRAFT_START = registerSound("infuser_craft_start", SoundSource.BLOCKS);
        SoundsAS.INFUSER_CRAFT_LOOP = registerSound("infuser_craft_loop", SoundSource.BLOCKS);
        SoundsAS.INFUSER_CRAFT_FINISH = registerSound("infuser_craft_finish", SoundSource.BLOCKS);
        SoundsAS.PERK_SEAL = registerSound("perk_seal", SoundSource.PLAYERS);
        SoundsAS.PERK_UNSEAL = registerSound("perk_unseal", SoundSource.PLAYERS);
        SoundsAS.PERK_UNLOCK = registerSound("perk_unlock", SoundSource.PLAYERS);
        SoundsAS.ILLUMINATION_WAND_HIGHLIGHT = registerSound("illumination_wand_highlight", SoundSource.BLOCKS);
        SoundsAS.ILLUMINATION_WAND_UNHIGHLIGHT = registerSound("illumination_wand_unhighlight", SoundSource.BLOCKS);
        SoundsAS.ILLUMINATION_WAND_LIGHT = registerSound("illumination_wand_light", SoundSource.BLOCKS);
        SoundsAS.GUI_JOURNAL_CLOSE = registerSound("gui_journal_close", SoundSource.MASTER);
        SoundsAS.GUI_JOURNAL_PAGE = registerSound("gui_journal_page", SoundSource.MASTER);
    }
    
    private static <T extends SoundEvent> T registerSound(final String jsonName, final SoundSource predefinedCategory) {
        final ResourceLocation res = AstralSorcery.key(jsonName);
        final CategorizedSoundEvent se = new CategorizedSoundEvent(res, predefinedCategory);

        return registerSound((T)se);
    }
    
    private static <T extends SoundEvent> T registerSound(final String jsonName) {
        final ResourceLocation res = AstralSorcery.key(jsonName);
        final SoundEvent se = new SoundEvent(res);

        return registerSound(se);
    }
    
    private static <T extends SoundEvent> T registerSound(final T soundEvent) {
        AstralSorcery.getProxy().getRegistryPrimer().register(soundEvent);
        return soundEvent;
    }
}
