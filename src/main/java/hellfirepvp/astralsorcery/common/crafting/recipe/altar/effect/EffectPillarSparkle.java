package hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect;

import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;

public class EffectPillarSparkle extends AltarRecipeEffect
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onTick(final TileAltar altar, final ActiveSimpleAltarRecipe.CraftingState state) {
        if (state == ActiveSimpleAltarRecipe.CraftingState.ACTIVE) {
            final double scale = Math.abs(AltarRecipeEffect.getRandomPillarOffset(altar.getAltarType()).getX()) + 1.0;
            for (int i = 0; i < 3; ++i) {
                final Vector3 at = new Vector3(altar).add(scale * (EffectPillarSparkle.rand.nextBoolean() ? 1 : -1), 0.0, scale * (EffectPillarSparkle.rand.nextBoolean() ? 1 : -1));
                at.addY(EffectPillarSparkle.rand.nextFloat() * AltarRecipeEffect.getPillarHeight(altar.getAltarType()));
                at.add(-0.3 + 1.6 * EffectPillarSparkle.rand.nextFloat(), 0.0, -0.3 + 1.6 * EffectPillarSparkle.rand.nextFloat());
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.WHITE).setGravityStrength(-0.001f + EffectPillarSparkle.rand.nextFloat() * -0.002f).setScaleMultiplier(0.2f + EffectPillarSparkle.rand.nextFloat() * 0.4f);
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
