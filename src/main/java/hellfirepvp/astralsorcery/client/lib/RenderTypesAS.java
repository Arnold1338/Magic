package hellfirepvp.astralsorcery.client.lib;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class RenderTypesAS
{
    public static VertexFormat POSITION_COLOR_TEX_NORMAL;
    public static RenderType EFFECT_FX_GENERIC_PARTICLE;
    public static RenderType EFFECT_FX_GENERIC_PARTICLE_DEPTH;
    public static RenderType EFFECT_FX_GENERIC_PARTICLE_ATLAS;
    public static RenderType EFFECT_FX_LIGHTNING;
    public static RenderType EFFECT_FX_LIGHTBEAM;
    public static RenderType EFFECT_FX_CRYSTAL;
    public static RenderType EFFECT_FX_BURST;
    public static RenderType EFFECT_FX_DYNAMIC_TEXTURE_SPRITE;
    public static RenderType EFFECT_FX_TEXTURE_SPRITE;
    public static RenderType EFFECT_FX_CUBE_OPAQUE_ATLAS;
    public static RenderType EFFECT_FX_BLOCK_TRANSLUCENT;
    public static RenderType EFFECT_FX_BLOCK_TRANSLUCENT_DEPTH;
    public static RenderType EFFECT_FX_CUBE_TRANSLUCENT_ATLAS;
    public static RenderType EFFECT_FX_CUBE_TRANSLUCENT_ATLAS_DEPTH;
    public static RenderType EFFECT_FX_CUBE_AREA_OF_EFFECT;
    public static RenderType EFFECT_FX_COLOR_SPHERE;
    public static RenderType EFFECT_LIGHTRAY_FAN;
    public static RenderType CONSTELLATION_WORLD_STAR;
    public static RenderType CONSTELLATION_WORLD_CONNECTION;
    public static RenderType CONSTELLATION_DISCIDIA_BACKGROUND;
    public static RenderType CONSTELLATION_ARMARA_BACKGROUND;
    public static RenderType CONSTELLATION_VICIO_BACKGROUND;
    public static RenderType CONSTELLATION_AEVITAS_BACKGROUND;
    public static RenderType CONSTELLATION_EVORSIO_BACKGROUND;
    public static RenderType CONSTELLATION_LUCERNA_BACKGROUND;
    public static RenderType CONSTELLATION_MINERALIS_BACKGROUND;
    public static RenderType CONSTELLATION_HOROLOGIUM_BACKGROUND;
    public static RenderType CONSTELLATION_OCTANS_BACKGROUND;
    public static RenderType CONSTELLATION_BOOTES_BACKGROUND;
    public static RenderType CONSTELLATION_FORNAX_BACKGROUND;
    public static RenderType CONSTELLATION_PELOTRIO_BACKGROUND;
    public static RenderType CONSTELLATION_GELU_BACKGROUND;
    public static RenderType CONSTELLATION_ULTERIA_BACKGROUND;
    public static RenderType CONSTELLATION_ALCARA_BACKGROUND;
    public static RenderType CONSTELLATION_VORUX_BACKGROUND;
    public static RenderType MODEL_ATTUNEMENT_ALTAR;
    public static RenderType MODEL_LENS_SOLID;
    public static RenderType MODEL_LENS_GLASS;
    public static RenderType MODEL_LENS_COLORED_SOLID;
    public static RenderType MODEL_LENS_COLORED_GLASS;
    public static RenderType MODEL_OBSERVATORY;
    public static RenderType MODEL_REFRACTION_TABLE;
    public static RenderType MODEL_REFRACTION_TABLE_GLASS;
    public static RenderType MODEL_TELESCOPE;
    public static RenderType MODEL_DEMON_WINGS;
    public static RenderType MODEL_CELESTIAL_WINGS;
    public static RenderType MODEL_WRAITH_WINGS;
    public static RenderType TER_WELL_LIQUID;
    public static RenderType TER_CHALICE_LIQUID;
    public static RenderType GUI_MISC_INFO_STAR;
    
    static {
        RenderTypesAS.POSITION_COLOR_TEX_NORMAL = new VertexFormat(ImmutableList.of((Object)DefaultVertexFormats.field_181713_m, (Object)DefaultVertexFormats.field_181714_n, (Object)DefaultVertexFormats.field_181715_o, (Object)DefaultVertexFormats.field_181717_q));
    }
}
