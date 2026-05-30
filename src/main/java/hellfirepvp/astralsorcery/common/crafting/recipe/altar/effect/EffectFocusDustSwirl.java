package hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect;

import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;

public class EffectFocusDustSwirl extends AltarRecipeEffect implements IFocusEffect
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onTick(final TileAltar altar, final ActiveSimpleAltarRecipe.CraftingState state) {
        if (state == ActiveSimpleAltarRecipe.CraftingState.ACTIVE) {
            final ActiveSimpleAltarRecipe recipe = altar.getActiveRecipe();
            if (recipe == null) {

            }
            final IConstellation focus = recipe.getRecipeToCraft().getFocusConstellation();
            final long tick = this.getClientTick();
            final float total = 180.0f;
            final float percCycle = (float)(tick % total / total * 2.0f * 3.141592653589793);
            final int parts = 5;
            final Vector3 center = new Vector3(altar).add(0.5, 0.1, 0.5);
            final float angleSwirl = 70.0f;
            final float dst = 3.5f;
            for (int i = 0; i < parts; ++i) {
                final Vector3 v = Vector3.RotAxis.X_AXIS.clone();
                final float originalAngle = i / (float)parts * 360.0f;
                final double angle = originalAngle + Mth.func_76126_a(percCycle) * angleSwirl;
                v.rotate(-Math.toRadians(angle), Vector3.RotAxis.Y_AXIS).normalize().multiply(dst);
                final Vector3 pos = center.clone().add(v);
                final Vector3 mot = center.clone().subtract(pos).normalize().multiply(0.07);
                final Color c = this.getFocusColor(focus, EffectFocusDustSwirl.rand);
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).setScaleMultiplier(0.25f + EffectFocusDustSwirl.rand.nextFloat() * 0.7f).setMotion(mot).color(VFXColorFunction.constant(c)).setMaxAge(50);
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
