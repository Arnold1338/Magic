package hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect;

import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;

public class BuiltInEffectAttunementSparkle extends AltarRecipeEffect
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onTick(final TileAltar altar, final ActiveSimpleAltarRecipe.CraftingState state) {
        EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3(altar).add(0.5, 0.5, 0.5)).color(VFXColorFunction.constant(ColorsAS.ROCK_CRYSTAL)).setMotion(new Vector3(BuiltInEffectAttunementSparkle.rand.nextFloat() * 0.06 * (BuiltInEffectAttunementSparkle.rand.nextBoolean() ? 1 : -1), BuiltInEffectAttunementSparkle.rand.nextFloat() * 0.06 * (BuiltInEffectAttunementSparkle.rand.nextBoolean() ? 1 : -1), BuiltInEffectAttunementSparkle.rand.nextFloat() * 0.06 * (BuiltInEffectAttunementSparkle.rand.nextBoolean() ? 1 : -1))).setScaleMultiplier(0.7f * BuiltInEffectAttunementSparkle.rand.nextFloat() * 0.3f).setMaxAge(20 + BuiltInEffectAttunementSparkle.rand.nextInt(30));
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
