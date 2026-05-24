package hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect;

import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightbeam;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;

public class EffectFocusEdge extends AltarRecipeEffect implements IFocusEffect
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onTick(final TileAltar altar, final ActiveSimpleAltarRecipe.CraftingState state) {
        if (state == ActiveSimpleAltarRecipe.CraftingState.ACTIVE) {
            final ActiveSimpleAltarRecipe recipe = altar.getActiveRecipe();
            if (recipe == null) {
                return;
            }
            final IConstellation focus = recipe.getRecipeToCraft().getFocusConstellation();
            final double offsetLength = AltarRecipeEffect.getPillarOffset(altar.getAltarType(), 0).getX();
            final double edgeLength = offsetLength * 2.0 + 1.0;
            final float total = 200.0f;
            final float partDone = this.getClientTick() % total / total;
            float xSh = (partDone >= 0.5f) ? 1.0f : (partDone / 0.5f);
            float zSh = (partDone < 0.5f) ? 1.0f : (1.0f - (partDone - 0.5f) / 0.5f);
            Vector3 offset = new Vector3(altar).add(-offsetLength, 0.1, -offsetLength);
            offset.add(xSh * edgeLength, 0.0, zSh * edgeLength);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(offset).setScaleMultiplier(0.1f + EffectFocusEdge.rand.nextFloat() * 0.5f).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.constant(this.getFocusColor(focus, EffectFocusEdge.rand))).setMaxAge(50);
            if (EffectFocusEdge.rand.nextInt(12) == 0) {
                EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(offset).setup(offset.clone().addY(3.0f + EffectFocusEdge.rand.nextFloat() * 2.0f), 1.0, 1.0).color(VFXColorFunction.constant(this.getFocusColor(focus, EffectFocusEdge.rand))).setMaxAge(48);
            }
            xSh = ((partDone < 0.5f) ? (partDone / 0.5f) : 1.0f);
            zSh = ((partDone >= 0.5f) ? (1.0f - (partDone - 0.5f) / 0.5f) : 1.0f);
            offset = new Vector3(altar).add(offsetLength + 1.0, 0.1, offsetLength + 1.0);
            offset.add(-xSh * edgeLength, 0.0, -zSh * edgeLength);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(offset).setScaleMultiplier(0.1f + EffectFocusEdge.rand.nextFloat() * 0.5f).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.constant(this.getFocusColor(focus, EffectFocusEdge.rand))).setMaxAge(50);
            if (EffectFocusEdge.rand.nextInt(12) == 0) {
                EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(offset).setup(offset.clone().addY(3.0f + EffectFocusEdge.rand.nextFloat() * 2.0f), 1.0, 1.0).color(VFXColorFunction.constant(this.getFocusColor(focus, EffectFocusEdge.rand))).setMaxAge(48);
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
