package hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect;

import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;

public class EffectLargeDustSwirl extends AltarRecipeEffect
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onTick(final TileAltar altar, final ActiveSimpleAltarRecipe.CraftingState state) {
        if (state == ActiveSimpleAltarRecipe.CraftingState.ACTIVE) {
            final long tick = this.getClientTick();
            final float interval = 200.0f;
            final float cycle = (float)(tick % interval / interval * 2.0f * 3.141592653589793);
            for (int parts = 5, i = 0; i < parts; ++i) {
                float angleSwirl = 110.0f;
                Vector3 center = new Vector3(altar).add(0.5, 1.1, 0.5);
                Vector3 v = Vector3.RotAxis.X_AXIS.clone();
                float originalAngle = i / (float)parts * 360.0f;
                double angle = originalAngle + Mth.func_76126_a(cycle) * angleSwirl;
                v.rotate(Math.toRadians(angle), Vector3.RotAxis.Y_AXIS).normalize().multiply(2.5);
                Vector3 pos = center.clone().add(v);
                Vector3 mot = center.clone().subtract(pos).normalize().multiply(0.09);
                final EntityVisualFX iEffect = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).setMotion(mot).setScaleMultiplier(0.15f + EffectLargeDustSwirl.rand.nextFloat() * 0.4f);
                if (EffectLargeDustSwirl.rand.nextInt(3) == 0) {
                    iEffect.color(VFXColorFunction.WHITE).setScaleMultiplier(iEffect.getScaleMultiplier() * 1.3f);
                }
                angleSwirl = 180.0f;
                center = new Vector3(altar).add(0.5, 0.1, 0.5);
                v = new Vector3(1, 0, 0);
                originalAngle = i / (float)parts * 360.0f;
                angle = originalAngle + Mth.func_76126_a(cycle) * angleSwirl;
                v.rotate(-Math.toRadians(angle), Vector3.RotAxis.Y_AXIS).normalize().multiply(5);
                pos = center.clone().add(v);
                mot = center.clone().subtract(pos).normalize().multiply(0.15);
                final EntityVisualFX oEffect = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).setMotion(mot).setScaleMultiplier(0.27f + EffectLargeDustSwirl.rand.nextFloat() * 0.4f).setMaxAge(50);
                if (EffectLargeDustSwirl.rand.nextInt(3) == 0) {
                    oEffect.color(VFXColorFunction.WHITE).setScaleMultiplier(iEffect.getScaleMultiplier() * 1.3f);
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
