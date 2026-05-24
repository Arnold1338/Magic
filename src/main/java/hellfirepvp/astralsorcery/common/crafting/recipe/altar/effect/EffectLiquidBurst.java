package hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect;

import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightbeam;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;

public class EffectLiquidBurst extends AltarRecipeEffect
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onTick(final TileAltar altar, final ActiveSimpleAltarRecipe.CraftingState state) {
        if (state == ActiveSimpleAltarRecipe.CraftingState.ACTIVE && this.getClientTick() % 10L == 0L && EffectLiquidBurst.rand.nextBoolean()) {
            final float height = 5.0f;
            final Vector3 position = new Vector3(altar).add(0.5 + EffectLiquidBurst.rand.nextFloat() * 3.0f * (EffectLiquidBurst.rand.nextBoolean() ? 1 : -1), 0.0, 0.5 + EffectLiquidBurst.rand.nextFloat() * 3.0f * (EffectLiquidBurst.rand.nextBoolean() ? 1 : -1));
            final Vector3 target = position.clone().addY(height);
            EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(position).setup(target, 0.800000011920929, 0.800000011920929).setAlphaMultiplier(1.0f).alpha(VFXAlphaFunction.FADE_OUT).setMaxAge(20);
            for (int i = 0; i < 170; ++i) {
                final float perc = EffectLiquidBurst.rand.nextFloat();
                final Vector3 mot = new Vector3(EffectLiquidBurst.rand.nextFloat() * 0.08 * (EffectLiquidBurst.rand.nextBoolean() ? 1 : -1) * (1.0f - perc), 0.0, EffectLiquidBurst.rand.nextFloat() * 0.08 * (EffectLiquidBurst.rand.nextBoolean() ? 1 : -1) * (1.0f - perc));
                final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(position.clone().addY(height * perc)).setMotion(mot).alpha(VFXAlphaFunction.FADE_OUT).setAlphaMultiplier(1.0f).setScaleMultiplier(0.2f + EffectLiquidBurst.rand.nextFloat() * 0.1f).setMaxAge(20 + EffectLiquidBurst.rand.nextInt(5));
                if (EffectLiquidBurst.rand.nextBoolean()) {
                    p.color(VFXColorFunction.WHITE);
                }
                else {
                    p.color(VFXColorFunction.constant(ColorsAS.DEFAULT_GENERIC_PARTICLE));
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
