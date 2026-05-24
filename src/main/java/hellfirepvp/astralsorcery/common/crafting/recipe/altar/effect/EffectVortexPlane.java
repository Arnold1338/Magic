package hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect;

import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.function.impl.RenderOffsetNoisePlane;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;

public class EffectVortexPlane extends AltarRecipeEffect
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onTick(final TileAltar altar, final ActiveSimpleAltarRecipe.CraftingState state) {
        final ActiveSimpleAltarRecipe recipe = altar.getActiveRecipe();
        if (recipe != null && state == ActiveSimpleAltarRecipe.CraftingState.ACTIVE) {
            final Vector3 at = new Vector3(altar).add(0.5, 0.0, 0.5);
            final Vector3 target = at.clone().add(AltarRecipeEffect.getFocusRelayOffset(altar.getAltarType()));
            RenderOffsetNoisePlane plane = recipe.getEffectContained(0, i -> new RenderOffsetNoisePlane(1.2f));
            for (int i = 0; i < 3; ++i) {
                final FXFacingParticle p = plane.createParticle(target);
                p.alpha(VFXAlphaFunction.FADE_OUT).setMotion(new Vector3(EffectVortexPlane.rand.nextFloat() * 0.005 * (EffectVortexPlane.rand.nextBoolean() ? 1 : -1), EffectVortexPlane.rand.nextFloat() * 0.005 * (EffectVortexPlane.rand.nextBoolean() ? 1 : -1), EffectVortexPlane.rand.nextFloat() * 0.005 * (EffectVortexPlane.rand.nextBoolean() ? 1 : -1))).setScaleMultiplier(0.15f + EffectVortexPlane.rand.nextFloat() * 0.1f).setMaxAge(20 + EffectVortexPlane.rand.nextInt(15));
            }
            plane = recipe.getEffectContained(1, i -> new RenderOffsetNoisePlane(1.6f));
            for (int i = 0; i < 3; ++i) {
                final FXFacingParticle p = plane.createParticle(target);
                p.alpha(VFXAlphaFunction.FADE_OUT).setMotion(new Vector3(EffectVortexPlane.rand.nextFloat() * 0.005 * (EffectVortexPlane.rand.nextBoolean() ? 1 : -1), EffectVortexPlane.rand.nextFloat() * 0.005 * (EffectVortexPlane.rand.nextBoolean() ? 1 : -1), EffectVortexPlane.rand.nextFloat() * 0.005 * (EffectVortexPlane.rand.nextBoolean() ? 1 : -1))).setScaleMultiplier(0.15f + EffectVortexPlane.rand.nextFloat() * 0.1f).setMaxAge(20 + EffectVortexPlane.rand.nextInt(15));
            }
            final double scale = AltarRecipeEffect.getRandomPillarOffset(altar.getAltarType()).getX();
            final double edgeScale = scale * 2.0 + 1.0;
            for (int j = 0; j < 2; ++j) {
                final Vector3 pos = new Vector3(altar).add(-scale + EffectVortexPlane.rand.nextFloat() * edgeScale, 0.02, -scale + EffectVortexPlane.rand.nextFloat() * edgeScale);
                final Vector3 mot = pos.vectorFromHereTo(target).normalize().multiply(0.1f);
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.WHITE).setScaleMultiplier(0.2f + EffectVortexPlane.rand.nextFloat() * 0.1f).setMotion(mot);
            }
            for (int j = 0; j < 2; ++j) {
                final Vector3 pos = target.clone().add(Vector3.random().multiply(4.0f));
                final Vector3 dst = pos.vectorFromHereTo(target);
                final Vector3 mot2 = dst.clone().multiply(dst.length() / 250.0);
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).alpha(VFXAlphaFunction.PYRAMID).color(VFXColorFunction.WHITE).setScaleMultiplier(0.2f + EffectVortexPlane.rand.nextFloat() * 0.1f).setMotion(mot2);
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onTESR(final TileAltar altar, final ActiveSimpleAltarRecipe.CraftingState state, final PoseStack renderStack, final MultiBufferSource buffer, final float pTicks, final int combinedLight) {
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onCraftingFinish(final TileAltar altar, final boolean isChaining) {
    }
}
