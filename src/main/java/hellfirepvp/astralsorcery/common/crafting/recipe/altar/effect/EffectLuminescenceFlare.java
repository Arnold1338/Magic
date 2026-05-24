package hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect;

import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.client.effect.function.RefreshFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXSpritePlane;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;

public class EffectLuminescenceFlare extends AltarRecipeEffect
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onTick(final TileAltar altar, final ActiveSimpleAltarRecipe.CraftingState state) {
        final ActiveSimpleAltarRecipe recipe = altar.getActiveRecipe();
        if (recipe != null && state == ActiveSimpleAltarRecipe.CraftingState.ACTIVE) {
            final ResourceLocation recipeName = recipe.getRecipeToCraft().func_199560_c();
            final FXSpritePlane spr = recipe.getEffectContained(2, i -> EffectHelper.of(EffectTemplatesAS.TEXTURE_SPRITE).spawn(new Vector3(altar).add(0.5, 0.04, 0.5)).setSprite(SpritesAS.SPR_CRAFT_FLARE).setAxis(Vector3.RotAxis.Y_AXIS).setNoRotation(0.0f).color(VFXColorFunction.constant(ColorsAS.EFFECT_CRAFT_FLARE)).alpha(VFXAlphaFunction.fadeIn(30.0f)).setScaleMultiplier(9.0f).setAlphaMultiplier(0.65f).refresh(RefreshFunction.tileExistsAnd(altar, (tAltar, fx) -> tAltar.getActiveRecipe() != null && tAltar.getActiveRecipe().getState() == ActiveSimpleAltarRecipe.CraftingState.ACTIVE && recipeName.equals((Object)tAltar.getActiveRecipe().getRecipeToCraft().func_199560_c()))));
            EffectHelper.refresh(spr, EffectTemplatesAS.TEXTURE_SPRITE);
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
