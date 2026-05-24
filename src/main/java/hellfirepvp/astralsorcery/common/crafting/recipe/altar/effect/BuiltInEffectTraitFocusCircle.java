package hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect;

import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;

public class BuiltInEffectTraitFocusCircle extends AltarRecipeEffect
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onTick(final TileAltar altar, final ActiveSimpleAltarRecipe.CraftingState state) {
        if (state == ActiveSimpleAltarRecipe.CraftingState.ACTIVE) {
            final Vector3 altarPos = new Vector3(altar);
            final double scale = AltarRecipeEffect.getRandomPillarOffset(altar.getAltarType()).getX();
            final double edgeScale = scale * 2.0 + 1.0;
            if (BuiltInEffectTraitFocusCircle.rand.nextInt(4) == 0) {
                final Vector3 at = altarPos.clone().add(-scale + BuiltInEffectTraitFocusCircle.rand.nextFloat() * edgeScale, 0.01, -scale + BuiltInEffectTraitFocusCircle.rand.nextFloat() * edgeScale);
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).color(VFXColorFunction.WHITE).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.15f + BuiltInEffectTraitFocusCircle.rand.nextFloat() * 0.2f);
            }
            for (int i = 0; i < 1; ++i) {
                final Vector3 r = Vector3.random().setY(0).normalize().multiply(1.3f + BuiltInEffectTraitFocusCircle.rand.nextFloat() * 0.5f).add(new Vector3(altarPos).add(0.5, 2.0f + BuiltInEffectTraitFocusCircle.rand.nextFloat() * 0.4f, 0.5));
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(r).color(VFXColorFunction.WHITE).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.1f + BuiltInEffectTraitFocusCircle.rand.nextFloat() * 0.2f);
            }
            for (int i = 0; i < 2; ++i) {
                final Vector3 r = Vector3.random().setY(0).normalize().multiply(2.0f + BuiltInEffectTraitFocusCircle.rand.nextFloat() * 0.5f).add(new Vector3(altarPos).add(0.5, 1.1f + BuiltInEffectTraitFocusCircle.rand.nextFloat() * 0.4f, 0.5));
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(r).color(VFXColorFunction.WHITE).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.1f + BuiltInEffectTraitFocusCircle.rand.nextFloat() * 0.2f);
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
