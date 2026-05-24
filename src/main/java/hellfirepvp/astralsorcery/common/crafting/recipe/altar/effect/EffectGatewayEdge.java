package hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect;

import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;

public class EffectGatewayEdge extends AltarRecipeEffect
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onTick(final TileAltar altar, final ActiveSimpleAltarRecipe.CraftingState state) {
        final double scale = Math.abs(AltarRecipeEffect.getRandomPillarOffset(altar.getAltarType()).getX());
        final double edgeScale = scale * 2.0 + 1.0;
        for (int amount = 0; amount < 6; ++amount) {
            final Vector3 offset = new Vector3(altar).add(-scale, 0.0, -scale);
            if (EffectGatewayEdge.rand.nextBoolean()) {
                offset.add(edgeScale * (double)(EffectGatewayEdge.rand.nextBoolean() ? 1 : 0), 0.0, EffectGatewayEdge.rand.nextFloat() * edgeScale);
            }
            else {
                offset.add(EffectGatewayEdge.rand.nextFloat() * edgeScale, 0.0, edgeScale * (double)(EffectGatewayEdge.rand.nextBoolean() ? 1 : 0));
            }
            final FXFacingParticle particle = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(offset).setGravityStrength(-5.0E-4f).setScaleMultiplier(0.25f + EffectGatewayEdge.rand.nextFloat() * 0.15f).color(VFXColorFunction.constant(ColorsAS.DEFAULT_GENERIC_PARTICLE)).setMaxAge(20 + EffectGatewayEdge.rand.nextInt(30));
            switch (EffectGatewayEdge.rand.nextInt(4)) {
                case 0: {
                    particle.color(VFXColorFunction.WHITE);
                    break;
                }
                case 1: {
                    particle.color(VFXColorFunction.constant(ColorsAS.CELESTIAL_CRYSTAL));
                    break;
                }
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
