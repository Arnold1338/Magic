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

public class BuiltInEffectConstellationLines extends AltarRecipeEffect
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onTick(final TileAltar altar, final ActiveSimpleAltarRecipe.CraftingState state) {
        if (state == ActiveSimpleAltarRecipe.CraftingState.ACTIVE) {
            final Vector3 thisAltar = new Vector3(altar).clone().add(0.5, 0.5, 0.5);
            for (int i = 0; i < 4; ++i) {
                final Vector3 at = AltarRecipeEffect.getRandomPillarOffset(altar.getAltarType()).clone().addY(AltarRecipeEffect.getPillarHeight(altar.getAltarType()));
                at.multiply(BuiltInEffectConstellationLines.rand.nextFloat()).add(thisAltar);
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).color(VFXColorFunction.randomBetween(ColorsAS.CONSTELLATION_TYPE_MAJOR, ColorsAS.CONSTELLATION_TYPE_MINOR)).setScaleMultiplier(0.2f + BuiltInEffectConstellationLines.rand.nextFloat() * 0.2f);
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
