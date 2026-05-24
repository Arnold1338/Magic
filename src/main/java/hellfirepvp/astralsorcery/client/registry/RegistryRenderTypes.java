package hellfirepvp.astralsorcery.client.registry;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import hellfirepvp.astralsorcery.client.constellation.ConstellationRenderInfos;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderState;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import hellfirepvp.astralsorcery.client.util.image.SkyImageGenerator;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.client.renderer.RenderType;
import hellfirepvp.astralsorcery.client.resource.AssetLibrary;
import hellfirepvp.astralsorcery.client.resource.AssetLoader;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import hellfirepvp.astralsorcery.client.util.Blending;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import hellfirepvp.astralsorcery.client.render.RenderStateBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;

public class RegistryRenderTypes
{
    public static void init() {
        initEffectTypes();
        initEffects();
        initConstellationTypes();
        initGuiTypes();
        initTERTypes();
        initModels();
    }
    
    private static void initEffectTypes() {
        RenderTypesAS.EFFECT_FX_GENERIC_PARTICLE = createType("effect_fx_generic_particle", DefaultVertexFormat.field_227851_o_, RenderStateBuilder.builder().texture(TexturesAS.TEX_PARTICLE_SMALL).blend(Blending.DEFAULT).disableCull().disableDepthMask().particleShaderTarget().build());
        RenderTypesAS.EFFECT_FX_GENERIC_PARTICLE_DEPTH = createType("effect_fx_generic_particle_depth", DefaultVertexFormat.field_227851_o_, RenderStateBuilder.builder().texture(TexturesAS.TEX_PARTICLE_SMALL).blend(Blending.DEFAULT).disableCull().disableDepthMask().disableDepth().particleShaderTarget().build());
        RenderTypesAS.EFFECT_FX_GENERIC_PARTICLE_ATLAS = createType("effect_fx_generic_particle_atlas", DefaultVertexFormat.field_227851_o_, RenderStateBuilder.builder().altasTexture().blend(Blending.DEFAULT).disableCull().disableDepthMask().particleShaderTarget().build());
        RenderTypesAS.EFFECT_FX_LIGHTNING = createType("effect_fx_lightning", DefaultVertexFormat.field_227851_o_, RenderStateBuilder.builder().texture(TexturesAS.TEX_LIGHTNING_PART).blend(Blending.DEFAULT).disableCull().disableDepthMask().particleShaderTarget().build());
        RenderTypesAS.EFFECT_FX_LIGHTBEAM = createType("effect_fx_lightbeam", DefaultVertexFormat.field_227851_o_, RenderStateBuilder.builder().texture(TexturesAS.TEX_LIGHTBEAM).blend(Blending.ADDITIVE_ALPHA).disableCull().disableDepthMask().particleShaderTarget().build());
        RenderTypesAS.EFFECT_FX_CRYSTAL = createType("effect_fx_crystal", RenderTypesAS.POSITION_COLOR_TEX_NORMAL, 4, 32768, RenderStateBuilder.builder().texture(TexturesAS.TEX_MODEL_CRYSTAL_WHITE).blend(Blending.DEFAULT).defaultAlpha().disableCull().disableDepthMask().particleShaderTarget().build());
        RenderTypesAS.EFFECT_FX_BURST = createType("effect_fx_burst", DefaultVertexFormat.field_227851_o_, RenderStateBuilder.builder().altasTexture().blend(Blending.DEFAULT).disableCull().disableDepthMask().particleShaderTarget().build());
        RenderTypesAS.EFFECT_FX_DYNAMIC_TEXTURE_SPRITE = createType("effect_fx_dynamic_texture_sprite", DefaultVertexFormat.field_227851_o_, RenderStateBuilder.builder().altasTexture().blend(Blending.DEFAULT).alpha(1.0E-4f).disableCull().disableDepthMask().particleShaderTarget().build());
        RenderTypesAS.EFFECT_FX_TEXTURE_SPRITE = createType("effect_fx_texture_sprite", DefaultVertexFormat.field_227851_o_, RenderStateBuilder.builder().altasTexture().blend(Blending.DEFAULT).alpha(1.0E-4f).disableCull().disableDepthMask().particleShaderTarget().build());
        RenderTypesAS.EFFECT_FX_CUBE_OPAQUE_ATLAS = createType("effect_fx_cube_opaque_atlas", DefaultVertexFormat.field_227852_q_, RenderStateBuilder.builder().altasTexture().blend(Blending.DEFAULT).defaultAlpha().disableCull().enableLighting().particleShaderTarget().build());
        RenderTypesAS.EFFECT_FX_BLOCK_TRANSLUCENT = createType("effect_fx_block_translucent", DefaultVertexFormat.field_176600_a, RenderStateBuilder.builder().altasTexture().blend(Blending.ADDITIVEDARK).defaultAlpha().disableCull().particleShaderTarget().build());
        RenderTypesAS.EFFECT_FX_BLOCK_TRANSLUCENT_DEPTH = createType("effect_fx_block_translucent_depth", DefaultVertexFormat.field_176600_a, RenderStateBuilder.builder().altasTexture().blend(Blending.ADDITIVEDARK).defaultAlpha().disableCull().disableDepth().particleShaderTarget().build());
        RenderTypesAS.EFFECT_FX_CUBE_TRANSLUCENT_ATLAS = createType("effect_fx_cube_translucent_atlas", DefaultVertexFormat.field_227852_q_, RenderStateBuilder.builder().altasTexture().blend(Blending.ADDITIVEDARK).defaultAlpha().disableCull().disableDepthMask().particleShaderTarget().build());
        RenderTypesAS.EFFECT_FX_CUBE_TRANSLUCENT_ATLAS_DEPTH = createType("effect_fx_cube_translucent_atlas_depth", DefaultVertexFormat.field_227852_q_, RenderStateBuilder.builder().altasTexture().blend(Blending.ADDITIVEDARK).defaultAlpha().disableCull().disableDepthMask().particleShaderTarget().disableDepth().build());
        RenderTypesAS.EFFECT_FX_CUBE_AREA_OF_EFFECT = createType("effect_fx_cube_area_of_effect", DefaultVertexFormat.field_227852_q_, RenderStateBuilder.builder().texture(TexturesAS.TEX_AREA_OF_EFFECT_CUBE).blend(Blending.DEFAULT).defaultAlpha().disableCull().disableDepthMask().particleShaderTarget().build());
        RenderTypesAS.EFFECT_FX_COLOR_SPHERE = createType("effect_fx_color_sphere", DefaultVertexFormat.field_181706_f, 4, 32768, RenderStateBuilder.builder().blend(Blending.DEFAULT).disableTexture().alpha(1.0E-5f).particleShaderTarget().build());
    }
    
    private static void initEffects() {
        RenderTypesAS.EFFECT_LIGHTRAY_FAN = createType("effect_lightray_fan", DefaultVertexFormat.field_181706_f, 6, 32768, RenderStateBuilder.builder().blend(Blending.ADDITIVE_ALPHA).smoothShade().disableDepthMask().enableDiffuseLighting().build());
        RenderTypesAS.CONSTELLATION_WORLD_STAR = createType("effect_render_cst_star", DefaultVertexFormat.field_227851_o_, RenderStateBuilder.builder().texture(TexturesAS.TEX_STAR_1).blend(Blending.DEFAULT).disableDepthMask().build());
        RenderTypesAS.CONSTELLATION_WORLD_CONNECTION = createType("effect_render_cst_connection", DefaultVertexFormat.field_227851_o_, RenderStateBuilder.builder().texture(TexturesAS.TEX_STAR_CONNECTION).blend(Blending.DEFAULT).disableDepthMask().build());
    }
    
    private static void initConstellationTypes() {
        RenderTypesAS.CONSTELLATION_DISCIDIA_BACKGROUND = createConstellationBackgroundType(ConstellationsAS.discidia, TexturesAS.TEX_DISCIDIA_BACKGROUND);
        RenderTypesAS.CONSTELLATION_ARMARA_BACKGROUND = createConstellationBackgroundType(ConstellationsAS.armara, TexturesAS.TEX_ARMARA_BACKGROUND);
        RenderTypesAS.CONSTELLATION_VICIO_BACKGROUND = createConstellationBackgroundType(ConstellationsAS.vicio, TexturesAS.TEX_VICIO_BACKGROUND);
        RenderTypesAS.CONSTELLATION_AEVITAS_BACKGROUND = createConstellationBackgroundType(ConstellationsAS.aevitas, TexturesAS.TEX_AEVITAS_BACKGROUND);
        RenderTypesAS.CONSTELLATION_EVORSIO_BACKGROUND = createConstellationBackgroundType(ConstellationsAS.evorsio, TexturesAS.TEX_EVORSIO_BACKGROUND);
        RenderTypesAS.CONSTELLATION_LUCERNA_BACKGROUND = createConstellationBackgroundType(ConstellationsAS.lucerna, TexturesAS.TEX_LUCERNA_BACKGROUND);
        RenderTypesAS.CONSTELLATION_MINERALIS_BACKGROUND = createConstellationBackgroundType(ConstellationsAS.mineralis, TexturesAS.TEX_MINERALIS_BACKGROUND);
        RenderTypesAS.CONSTELLATION_HOROLOGIUM_BACKGROUND = createConstellationBackgroundType(ConstellationsAS.horologium, TexturesAS.TEX_HOROLOGIUM_BACKGROUND);
        RenderTypesAS.CONSTELLATION_OCTANS_BACKGROUND = createConstellationBackgroundType(ConstellationsAS.octans, TexturesAS.TEX_OCTANS_BACKGROUND);
        RenderTypesAS.CONSTELLATION_BOOTES_BACKGROUND = createConstellationBackgroundType(ConstellationsAS.bootes, TexturesAS.TEX_BOOTES_BACKGROUND);
        RenderTypesAS.CONSTELLATION_FORNAX_BACKGROUND = createConstellationBackgroundType(ConstellationsAS.fornax, TexturesAS.TEX_FORNAX_BACKGROUND);
        RenderTypesAS.CONSTELLATION_PELOTRIO_BACKGROUND = createConstellationBackgroundType(ConstellationsAS.pelotrio, TexturesAS.TEX_PELOTRIO_BACKGROUND);
        RenderTypesAS.CONSTELLATION_GELU_BACKGROUND = createConstellationBackgroundType(ConstellationsAS.gelu, TexturesAS.TEX_GELU_BACKGROUND);
        RenderTypesAS.CONSTELLATION_ULTERIA_BACKGROUND = createConstellationBackgroundType(ConstellationsAS.ulteria, TexturesAS.TEX_ULTERIA_BACKGROUND);
        RenderTypesAS.CONSTELLATION_ALCARA_BACKGROUND = createConstellationBackgroundType(ConstellationsAS.alcara, TexturesAS.TEX_ALCARA_BACKGROUND);
        RenderTypesAS.CONSTELLATION_VORUX_BACKGROUND = createConstellationBackgroundType(ConstellationsAS.vorux, TexturesAS.TEX_VORUX_BACKGROUND);
    }
    
    private static void initGuiTypes() {
        RenderTypesAS.GUI_MISC_INFO_STAR = createType("gui_misc_info_star", DefaultVertexFormat.field_181707_g, RenderStateBuilder.builder().texture(TexturesAS.TEX_STAR_1).blend(Blending.DEFAULT).defaultAlpha().build());
    }
    
    private static void initTERTypes() {
        RenderTypesAS.TER_WELL_LIQUID = createType("ter_well_liquid", DefaultVertexFormat.field_227851_o_, RenderStateBuilder.builder().altasTexture().blend(Blending.DEFAULT).alpha(1.0E-5f).disableDepthMask().build());
        RenderTypesAS.TER_CHALICE_LIQUID = createType("ter_chalice_liquid", RenderTypesAS.POSITION_COLOR_TEX_NORMAL, RenderStateBuilder.builder().altasTexture().blend(Blending.DEFAULT).alpha(1.0E-5f).disableDepthMask().build());
    }
    
    private static void initModels() {
        RenderTypesAS.MODEL_ATTUNEMENT_ALTAR = createType("model_attunement_altar", DefaultVertexFormat.field_227849_i_, RenderStateBuilder.builder().texture(AssetLibrary.loadTexture(AssetLoader.TextureLocation.BLOCKS, "entity", "attunement_altar")).enableLighting().enableDiffuseLighting().enableOverlay().build());
        RenderTypesAS.MODEL_LENS_SOLID = createType("model_lens", DefaultVertexFormat.field_227849_i_, RenderStateBuilder.builder().texture(AssetLibrary.loadTexture(AssetLoader.TextureLocation.BLOCKS, "entity", "lens_frame")).enableLighting().enableDiffuseLighting().enableOverlay().build());
        RenderTypesAS.MODEL_LENS_GLASS = createType("model_lens_glass", DefaultVertexFormat.field_227849_i_, RenderStateBuilder.builder().texture(AssetLibrary.loadTexture(AssetLoader.TextureLocation.BLOCKS, "entity", "lens_frame")).blend(Blending.DEFAULT).disableDepthMask().enableLighting().enableDiffuseLighting().enableOverlay().build());
        RenderTypesAS.MODEL_LENS_COLORED_SOLID = createType("model_lens_colored", DefaultVertexFormat.field_227849_i_, RenderStateBuilder.builder().texture(AssetLibrary.loadTexture(AssetLoader.TextureLocation.BLOCKS, "entity", "lens_color")).enableLighting().enableDiffuseLighting().enableOverlay().build());
        RenderTypesAS.MODEL_LENS_COLORED_GLASS = createType("model_lens_colored_glass", DefaultVertexFormat.field_227849_i_, RenderStateBuilder.builder().texture(AssetLibrary.loadTexture(AssetLoader.TextureLocation.BLOCKS, "entity", "lens_color")).blend(Blending.DEFAULT).disableDepthMask().enableLighting().enableDiffuseLighting().enableOverlay().build());
        RenderTypesAS.MODEL_OBSERVATORY = createType("model_observatory", DefaultVertexFormat.field_227849_i_, RenderStateBuilder.builder().texture(AssetLibrary.loadTexture(AssetLoader.TextureLocation.BLOCKS, "entity", "observatory")).blend(Blending.DEFAULT).disableCull().enableLighting().enableDiffuseLighting().enableOverlay().build());
        RenderTypesAS.MODEL_REFRACTION_TABLE = createType("model_refraction_table", DefaultVertexFormat.field_227849_i_, RenderStateBuilder.builder().texture(AssetLibrary.loadTexture(AssetLoader.TextureLocation.BLOCKS, "entity", "refraction_table")).enableLighting().enableDiffuseLighting().enableOverlay().build());
        RenderTypesAS.MODEL_REFRACTION_TABLE_GLASS = createType("model_refraction_table_glass", DefaultVertexFormat.field_227849_i_, RenderStateBuilder.builder().texture(AssetLibrary.loadTexture(AssetLoader.TextureLocation.BLOCKS, "entity", "refraction_table")).blend(Blending.DEFAULT).disableDepthMask().enableLighting().enableDiffuseLighting().enableOverlay().build());
        RenderTypesAS.MODEL_TELESCOPE = createType("model_telescope", DefaultVertexFormat.field_227849_i_, RenderStateBuilder.builder().texture(AssetLibrary.loadTexture(AssetLoader.TextureLocation.BLOCKS, "entity", "telescope")).blend(Blending.DEFAULT).disableCull().enableLighting().enableDiffuseLighting().enableOverlay().build());
        RenderTypesAS.MODEL_DEMON_WINGS = createType("model_demon_wings", RenderTypesAS.POSITION_COLOR_TEX_NORMAL, RenderStateBuilder.builder().enableLighting().enableDiffuseLighting().smoothShade().build());
        RenderTypesAS.MODEL_CELESTIAL_WINGS = createType("model_celestial_wings", RenderTypesAS.POSITION_COLOR_TEX_NORMAL, RenderStateBuilder.builder().texture(TexturesAS.TEX_MODEL_CELESTIAL_WINGS).enableDiffuseLighting().smoothShade().build());
        RenderTypesAS.MODEL_WRAITH_WINGS = createType("model_wraith_wings", RenderTypesAS.POSITION_COLOR_TEX_NORMAL, RenderStateBuilder.builder().enableLighting().enableDiffuseLighting().smoothShade().build());
    }
    
    public static RenderType createDepthProjectionType(final int zoom) {
        return createType("player_starry_sky_layer", DefaultVertexFormat.field_181706_f, 7, 256, false, true, RenderStateBuilder.builder().blend(Blending.ADDITIVE).texture(AssetLibrary.loadGeneratedResource(AstralSorcery.key("player_starry_sky_layer"), SkyImageGenerator::generateStarBackground, true)).alpha(0.001f).vanillaBuilder().func_228725_a_((RenderState.TexturingState)new IdentityProjectionModelTexturingState(zoom)).func_228728_a_(false));
    }
    
    private static RenderType createType(final String name, final VertexFormat vertexFormat, final RenderType.State state) {
        return createType(name, vertexFormat, 7, 32768, state);
    }
    
    private static RenderType createType(final String name, final VertexFormat vertexFormat, final int glDrawMode, final int bufferSize, final RenderType.State state) {
        return createType(name, vertexFormat, glDrawMode, bufferSize, false, false, state);
    }
    
    private static RenderType createType(final String name, final VertexFormat vertexFormat, final int glDrawMode, final int bufferSize, final boolean usesDelegateDrawing, final boolean sortVertices, final RenderType.State state) {
        return (RenderType)RenderType.func_228633_a_(AstralSorcery.key(name).toString(), vertexFormat, glDrawMode, bufferSize, usesDelegateDrawing, sortVertices, state);
    }
    
    private static RenderType createConstellationBackgroundType(final IConstellation cst, final AbstractRenderableTexture tex) {
        final RenderType rType = createType("constellation_background_" + cst.getSimpleName(), DefaultVertexFormat.field_227851_o_, RenderStateBuilder.builder().texture(tex).blend(Blending.DEFAULT).disableDepthMask().build());
        ConstellationRenderInfos.registerBackground(cst, rType, tex);
        return rType;
    }
    
    private static class IdentityProjectionModelTexturingState extends RenderState.TexturingState
    {
        private final int zoom;
        
        public IdentityProjectionModelTexturingState(final int zoom) {
            super(AstralSorcery.key("depth_projection_texturing_" + zoom).toString(), () -> {
                final float movementV = Util.func_211177_b() % 200000L / 200000.0f;
                RenderSystem.matrixMode(5890);
                RenderSystem.pushMatrix();
                RenderSystem.loadIdentity();
                RenderSystem.translatef(0.5f, 0.5f, 0.0f);
                RenderSystem.scalef(0.25f, 0.25f, 1.0f);
                RenderSystem.translatef(17.0f / zoom, (2.0f + zoom / 1.5f) * movementV, 0.0f);
                RenderSystem.rotatef((zoom * zoom * 4321.0f + zoom * 9.0f) * 2.0f, 0.0f, 0.0f, 1.0f);
                RenderSystem.scalef(4.5f - zoom / 4.0f, 4.5f - zoom / 4.0f, 1.0f);
                RenderSystem.mulTextureByProjModelView();
                RenderSystem.matrixMode(5888);
                RenderSystem.setupEndPortalTexGen();
                return;
            }, () -> {
                RenderSystem.matrixMode(5890);
                RenderSystem.popMatrix();
                RenderSystem.matrixMode(5888);
                RenderSystem.clearTexGen();
                return;
            });
            this.zoom = zoom;
        }
        
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            if (!super.equals(o)) {
                return false;
            }
            final IdentityProjectionModelTexturingState that = (IdentityProjectionModelTexturingState)o;
            return this.zoom == that.zoom;
        }
        
        public int hashCode() {
            return this.zoom;
        }
    }
}
