package hellfirepvp.astralsorcery.client.registry;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import java.util.List;
import java.util.LinkedList;
import hellfirepvp.astralsorcery.client.effect.vfx.FXColorEffectSphere;
import hellfirepvp.astralsorcery.client.effect.vfx.FXBlock;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.client.effect.vfx.FXCube;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import hellfirepvp.astralsorcery.client.effect.context.RenderContextFacingSprite;
import hellfirepvp.astralsorcery.client.effect.context.RenderContextSpritePlaneDynamic;
import hellfirepvp.astralsorcery.client.effect.context.RenderContextLightbeam;
import hellfirepvp.astralsorcery.client.effect.context.RenderContextCrystal;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import hellfirepvp.astralsorcery.client.effect.context.RenderContextBurst;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import hellfirepvp.astralsorcery.client.effect.context.RenderContextLightning;
import hellfirepvp.astralsorcery.client.effect.context.RenderContextAtlasParticle;
import hellfirepvp.astralsorcery.client.effect.context.RenderContextGenericParticle;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.context.RenderContextGenericDepthParticle;

public class RegistryEffectTemplates
{
    private RegistryEffectTemplates() {
    }
    
    public static void init() {
        EffectTemplatesAS.GENERIC_DEPTH_PARTICLE = register(new RenderContextGenericDepthParticle());
        EffectTemplatesAS.GENERIC_PARTICLE = register(new RenderContextGenericParticle());
        EffectTemplatesAS.GENERIC_ATLAS_PARTICLE = register(new RenderContextAtlasParticle());
        EffectTemplatesAS.LIGHTNING = register(new RenderContextLightning());
        EffectTemplatesAS.CRYSTAL_BURST_1 = register(new RenderContextBurst(SpritesAS.SPR_CRYSTAL_EFFECT_1));
        EffectTemplatesAS.CRYSTAL_BURST_2 = register(new RenderContextBurst(SpritesAS.SPR_CRYSTAL_EFFECT_2));
        EffectTemplatesAS.CRYSTAL_BURST_3 = register(new RenderContextBurst(SpritesAS.SPR_CRYSTAL_EFFECT_3));
        EffectTemplatesAS.CRYSTAL = register(new RenderContextCrystal(TexturesAS.TEX_MODEL_CRYSTAL_WHITE));
        EffectTemplatesAS.COLLECTOR_BURST = register(new RenderContextBurst(SpritesAS.SPR_COLLECTOR_EFFECT));
        EffectTemplatesAS.GEM_CRYSTAL_BURST = register(new RenderContextBurst(SpritesAS.SPR_GEM_CRYSTAL_BURST));
        EffectTemplatesAS.GEM_CRYSTAL_BURST_SKY = register(new RenderContextBurst(SpritesAS.SPR_GEM_CRYSTAL_BURST_SKY));
        EffectTemplatesAS.GEM_CRYSTAL_BURST_DAY = register(new RenderContextBurst(SpritesAS.SPR_GEM_CRYSTAL_BURST_DAY));
        EffectTemplatesAS.GEM_CRYSTAL_BURST_NIGHT = register(new RenderContextBurst(SpritesAS.SPR_GEM_CRYSTAL_BURST_NIGHT));
        EffectTemplatesAS.LIGHTBEAM = register(new RenderContextLightbeam(SpritesAS.SPR_LIGHTBEAM));
        EffectTemplatesAS.LIGHTBEAM_TRANSFER = register(new RenderContextLightbeam(SpritesAS.SPR_LIGHTBEAM_TRANSFER));
        EffectTemplatesAS.TEXTURE_SPRITE = register(new RenderContextSpritePlaneDynamic());
        EffectTemplatesAS.FACING_SPRITE = register(new RenderContextFacingSprite());
        EffectTemplatesAS.CUBE_OPAQUE_ATLAS = register(new BatchRenderContext<FXCube>(RenderTypesAS.EFFECT_FX_CUBE_OPAQUE_ATLAS, (ctx, pos) -> new FXCube(pos)));
        EffectTemplatesAS.CUBE_TRANSLUCENT_ATLAS = register(new BatchRenderContext<FXCube>(RenderTypesAS.EFFECT_FX_CUBE_TRANSLUCENT_ATLAS, (ctx, pos) -> new FXCube(pos)));
        EffectTemplatesAS.CUBE_TRANSLUCENT_ATLAS_IGNORE_DEPTH = register(new BatchRenderContext<FXCube>(RenderTypesAS.EFFECT_FX_CUBE_TRANSLUCENT_ATLAS_DEPTH, (ctx, pos) -> new FXCube(pos)));
        EffectTemplatesAS.CUBE_AREA_OF_EFFECT = register(new BatchRenderContext<FXCube>(RenderTypesAS.EFFECT_FX_CUBE_AREA_OF_EFFECT, (ctx, pos) -> new FXCube(pos)));
        EffectTemplatesAS.BLOCK_TRANSLUCENT = register(new BatchRenderContext<FXBlock>(RenderTypesAS.EFFECT_FX_BLOCK_TRANSLUCENT, (ctx, pos) -> new FXBlock(pos)));
        EffectTemplatesAS.BLOCK_TRANSLUCENT_IGNORE_DEPTH = register(new BatchRenderContext<FXBlock>(RenderTypesAS.EFFECT_FX_BLOCK_TRANSLUCENT_DEPTH, (ctx, pos) -> new FXBlock(pos)));
        (EffectTemplatesAS.COLOR_SPHERE = register(new BatchRenderContext<FXColorEffectSphere>(RenderTypesAS.EFFECT_FX_COLOR_SPHERE, (ctx, pos) -> new FXColorEffectSphere(pos)))).setDrawWithTexture(false);
        EffectTemplatesAS.GENERIC_GATEWAY_PARTICLE = register(new RenderContextGenericParticle());
        setupRenderOrder();
    }
    
    private static void setupRenderOrder() {
        EffectTemplatesAS.GENERIC_PARTICLE.setAfter(EffectTemplatesAS.GENERIC_DEPTH_PARTICLE);
        EffectTemplatesAS.GENERIC_ATLAS_PARTICLE.setAfter(EffectTemplatesAS.GENERIC_PARTICLE);
        EffectTemplatesAS.LIGHTNING.setAfter(EffectTemplatesAS.GENERIC_ATLAS_PARTICLE);
        final List<BatchRenderContext<?>> generalGrp = new LinkedList<BatchRenderContext<?>>();
        generalGrp.add(EffectTemplatesAS.TEXTURE_SPRITE);
        generalGrp.add(EffectTemplatesAS.CRYSTAL_BURST_1);
        generalGrp.add(EffectTemplatesAS.CRYSTAL_BURST_2);
        generalGrp.add(EffectTemplatesAS.CRYSTAL_BURST_3);
        generalGrp.add(EffectTemplatesAS.GEM_CRYSTAL_BURST);
        generalGrp.add(EffectTemplatesAS.GEM_CRYSTAL_BURST_SKY);
        generalGrp.add(EffectTemplatesAS.GEM_CRYSTAL_BURST_DAY);
        generalGrp.add(EffectTemplatesAS.GEM_CRYSTAL_BURST_NIGHT);
        generalGrp.add(EffectTemplatesAS.COLLECTOR_BURST);
        generalGrp.add(EffectTemplatesAS.CRYSTAL);
        generalGrp.add(EffectTemplatesAS.LIGHTBEAM);
        generalGrp.add(EffectTemplatesAS.LIGHTBEAM_TRANSFER);
        generalGrp.add(EffectTemplatesAS.FACING_SPRITE);
        generalGrp.forEach(c -> c.setAfter(EffectTemplatesAS.LIGHTNING));
        EffectTemplatesAS.CUBE_OPAQUE_ATLAS.setAfter(generalGrp);
        EffectTemplatesAS.CUBE_AREA_OF_EFFECT.setAfter(EffectTemplatesAS.CUBE_OPAQUE_ATLAS);
        EffectTemplatesAS.CUBE_TRANSLUCENT_ATLAS.setAfter(EffectTemplatesAS.CUBE_AREA_OF_EFFECT);
        EffectTemplatesAS.CUBE_TRANSLUCENT_ATLAS_IGNORE_DEPTH.setAfter(EffectTemplatesAS.CUBE_TRANSLUCENT_ATLAS);
        EffectTemplatesAS.BLOCK_TRANSLUCENT.setAfter(EffectTemplatesAS.CUBE_TRANSLUCENT_ATLAS_IGNORE_DEPTH);
        EffectTemplatesAS.BLOCK_TRANSLUCENT_IGNORE_DEPTH.setAfter(EffectTemplatesAS.BLOCK_TRANSLUCENT);
        EffectTemplatesAS.COLOR_SPHERE.setAfter(EffectTemplatesAS.BLOCK_TRANSLUCENT_IGNORE_DEPTH);
        EffectTemplatesAS.GENERIC_GATEWAY_PARTICLE.setAfter(EffectTemplatesAS.COLOR_SPHERE);
    }
    
    private static <V extends EntityVisualFX, T extends BatchRenderContext<V>> T register(final T ctx) {
        EffectTemplatesAS.LIST_ALL_RENDER_CONTEXT.add(ctx);
        return ctx;
    }
}
