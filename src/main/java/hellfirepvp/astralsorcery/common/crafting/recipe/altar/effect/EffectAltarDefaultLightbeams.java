package hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect;

import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightbeam;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;

public class EffectAltarDefaultLightbeams extends AltarRecipeEffect
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onTick(final TileAltar altar, final ActiveSimpleAltarRecipe.CraftingState state) {
        if (state == ActiveSimpleAltarRecipe.CraftingState.ACTIVE && EffectAltarDefaultLightbeams.rand.nextInt(8) == 0) {
            final float scale = (float)AltarRecipeEffect.getRandomPillarOffset(altar.getAltarType()).getX();
            final Vector3 from = new Vector3(altar).add(0.5, 0.0, 0.5);
            MiscUtils.applyRandomOffset(from, EffectAltarDefaultLightbeams.rand, scale * 0.85f);
            from.setY(altar.func_174877_v().getY() - 0.6f);
            EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(from).setup(from.clone().addY(5.0f + EffectAltarDefaultLightbeams.rand.nextFloat() * 3.0f), 1.0, 1.0).setMaxAge(40 + EffectAltarDefaultLightbeams.rand.nextInt(30));
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
