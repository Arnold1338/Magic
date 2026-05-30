package hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect;

import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;

public class EffectAltarFocusSparkle extends AltarRecipeEffect implements IFocusEffect
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onTick(final TileAltar altar, final ActiveSimpleAltarRecipe.CraftingState state) {
        final ActiveSimpleAltarRecipe recipe = altar.getActiveRecipe();
        if (recipe == null) {

        }
        final IConstellation focus = recipe.getRecipeToCraft().getFocusConstellation();
        final double scale = AltarRecipeEffect.getRandomPillarOffset(altar.getAltarType()).getX();
        final double edgeScale = scale * 2.0 + 1.0;
        final Vector3 at = new Vector3(altar).add(-scale + EffectAltarFocusSparkle.rand.nextFloat() * edgeScale, 0.0, -scale + EffectAltarFocusSparkle.rand.nextFloat() * edgeScale);
        EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.constant(this.getFocusColor(focus, EffectAltarFocusSparkle.rand))).setScaleMultiplier(0.1f + EffectAltarFocusSparkle.rand.nextFloat() * 0.2f).setMaxAge(50);
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
