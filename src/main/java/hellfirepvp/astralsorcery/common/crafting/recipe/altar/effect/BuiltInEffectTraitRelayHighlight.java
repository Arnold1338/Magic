package hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect;

import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraft.core.Vec3i;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightbeam;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.item.ItemStack;
import java.util.Iterator;
import java.util.List;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.util.ColorizationHelper;
import java.awt.Color;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileSpectralRelay;
import hellfirepvp.astralsorcery.common.crafting.helper.WrappedIngredient;
import hellfirepvp.astralsorcery.common.crafting.helper.CraftingFocusStack;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;

public class BuiltInEffectTraitRelayHighlight extends AltarRecipeEffect
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onTick(final TileAltar altar, final ActiveSimpleAltarRecipe.CraftingState state) {
        final ActiveSimpleAltarRecipe recipe = altar.getActiveRecipe();
        if (recipe != null) {
            final List<WrappedIngredient> additionalIngredients = recipe.getRecipeToCraft().getRelayInputs();
            for (final CraftingFocusStack stack : recipe.getFocusStacks()) {
                if (stack.getStackIndex() >= 0) {
                    if (stack.getStackIndex() >= additionalIngredients.size()) {
                        continue;
                    }
                    final WrappedIngredient match = additionalIngredients.get(stack.getStackIndex());
                    final TileSpectralRelay relay = MiscUtils.getTileAt((IBlockReader)altar.getLevel(), stack.getRealPosition(), TileSpectralRelay.class, false);
                    if (relay == null) {
                        continue;
                    }
                    final ItemStack in = relay.getInventory().getStackInSlot(0);
                    if (!in.isEmpty() && match.getIngredient().test(in)) {
                        final Color color = ColorizationHelper.getColor(in).orElse(ColorsAS.CELESTIAL_CRYSTAL);
                        this.playLightbeam(altar, relay, color);
                        this.playRelayHighlightParticles(relay, color);
                        if (BuiltInEffectTraitRelayHighlight.rand.nextInt(4) != 0) {
                            continue;
                        }
                        EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3(altar).add(-3 + BuiltInEffectTraitRelayHighlight.rand.nextInt(7), 0.02, -3 + BuiltInEffectTraitRelayHighlight.rand.nextInt(7))).color(VFXColorFunction.constant(color)).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.15f + BuiltInEffectTraitRelayHighlight.rand.nextFloat() * 0.2f);
                    }
                    else {
                        final ItemStack chosen = match.getRandomMatchingStack(this.getClientTick());
                        final Color color2 = ColorizationHelper.getColor(chosen).orElse(ColorsAS.CELESTIAL_CRYSTAL);
                        this.playLightbeam(altar, relay, color2);
                        this.playRelayHighlightParticles(relay, color2);
                    }
                }
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playRelayHighlightParticles(final TileSpectralRelay relay, final Color color) {
        if (BuiltInEffectTraitRelayHighlight.rand.nextBoolean()) {
            final FXFacingParticle particle = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3(relay).add(BuiltInEffectTraitRelayHighlight.rand.nextFloat(), 0.0f, BuiltInEffectTraitRelayHighlight.rand.nextFloat())).setAlphaMultiplier(0.7f).setScaleMultiplier(0.2f + BuiltInEffectTraitRelayHighlight.rand.nextFloat() * 0.1f).setMaxAge(30 + BuiltInEffectTraitRelayHighlight.rand.nextInt(50));
            if (BuiltInEffectTraitRelayHighlight.rand.nextInt(3) == 0) {
                particle.color(VFXColorFunction.WHITE).setScaleMultiplier(0.1f + BuiltInEffectTraitRelayHighlight.rand.nextFloat() * 0.1f);
            }
            else {
                particle.color(VFXColorFunction.constant(color)).setGravityStrength(-0.0015f);
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playLightbeam(final TileAltar from, final TileSpectralRelay to, final Color color) {
        if (this.getClientTick() % 35L == 0L) {
            EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(new Vector3(from).add(0.5, 0.0, 0.5).add(AltarRecipeEffect.getFocusRelayOffset(from.getAltarType()))).setup(new Vector3(to).add(0.5, 0.1, 0.5), 0.800000011920929, 0.800000011920929).color(VFXColorFunction.constant(color));
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onTESR(final TileAltar altar, final ActiveSimpleAltarRecipe.CraftingState state, final PoseStack renderStack, final MultiBufferSource buffer, final float pTicks, final int combinedLight) {
        final ActiveSimpleAltarRecipe activeRecipe = altar.getActiveRecipe();
        if (activeRecipe != null) {
            final List<WrappedIngredient> additionalIngredients = activeRecipe.getRecipeToCraft().getRelayInputs();
            final List<CraftingFocusStack> focusStacks = activeRecipe.getFocusStacks();
            for (final CraftingFocusStack stack : focusStacks) {
                if (stack.getStackIndex() >= 0) {
                    if (stack.getStackIndex() >= additionalIngredients.size()) {
                        continue;
                    }
                    final WrappedIngredient match = additionalIngredients.get(stack.getStackIndex());
                    final BlockPos offset = stack.getRealPosition().func_177973_b((Vec3i)altar.getBlockState());
                    final TileSpectralRelay relay = MiscUtils.getTileAt((IBlockReader)altar.getLevel(), stack.getRealPosition(), TileSpectralRelay.class, false);
                    if (relay != null && match.getIngredient().test(relay.getInventory().getStackInSlot(0))) {
                        continue;
                    }
                    final ItemStack potential = match.getRandomMatchingStack(this.getClientTick());
                    renderStack.popPose();
                    renderStack.translate(0.5 + offset.getX(), 0.35 + offset.getY(), 0.5 + offset.getZ());
                    RenderingUtils.renderTranslucentItemStack(potential, renderStack, pTicks);
                    renderStack.popPose();
                }
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onCraftingFinish(final TileAltar altar, final boolean isChaining) {
    }
}
