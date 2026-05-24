package hellfirepvp.astralsorcery.client.render.tile;

import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.AltarRecipeEffect;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.util.ColorizationHelper;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.crafting.helper.WrappedIngredient;
import hellfirepvp.astralsorcery.client.util.RenderingConstellationUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;

public class RenderAltar extends CustomTileEntityRenderer<TileAltar>
{
    public RenderAltar(final TileEntityRendererDispatcher tileRenderer) {
        super(tileRenderer);
    }
    
    public void render(final TileAltar tile, final float pTicks, final PoseStack renderStack, final MultiBufferSource renderTypeBuffer, final int combinedLight, final int combinedOverlay) {
        if (tile.getAltarType().isThisGEThan(AltarType.RADIANCE) && tile.hasMultiblock()) {
            final IConstellation cst = tile.getFocusedConstellation();
            if (cst != null) {
                final float dayAlpha = DayTimeHelper.getCurrentDaytimeDistribution(tile.func_145831_w()) * 0.6f;
                final int max = 3000;
                final int t = (int)(ClientScheduler.getClientTick() % max);
                final float halfAge = max / 2.0f;
                float tr = 1.0f - Math.abs(halfAge - t) / halfAge;
                tr *= (float)1.3;
                RenderingConstellationUtils.renderConstellationIntoWorldFlat(cst, renderStack, renderTypeBuffer, new Vector3(0.5, 0.03, 0.5), 5.5 + tr, 2.0, 0.1f + dayAlpha);
            }
        }
        final ActiveSimpleAltarRecipe recipe = tile.getActiveRecipe();
        if (recipe != null) {
            recipe.getRecipeToCraft().getCraftingEffects().forEach(effect -> effect.onTESR(tile, recipe.getState(), renderStack, renderTypeBuffer, pTicks, combinedLight));
        }
        if (tile.getAltarType().isThisGEThan(AltarType.RADIANCE) && tile.hasMultiblock()) {
            renderStack.func_227860_a_();
            renderStack.func_227861_a_(0.5, 4.5, 0.5);
            final long id = tile.func_174877_v().func_218275_a();
            if (recipe != null) {
                final List<WrappedIngredient> traitInputs = recipe.getRecipeToCraft().getRelayInputs();
                if (!traitInputs.isEmpty()) {
                    final int amount = 60 / traitInputs.size();
                    for (int i = 0; i < traitInputs.size(); ++i) {
                        final WrappedIngredient ingredient = traitInputs.get(i);
                        final ItemStack traitInput = ingredient.getRandomMatchingStack(ClientScheduler.getClientTick());
                        final Color color = ColorizationHelper.getColor(traitInput).orElse(ColorsAS.CELESTIAL_CRYSTAL);
                        RenderingDrawUtils.renderLightRayFan(renderStack, renderTypeBuffer, color, 0x1231943167156902L | id | i * 20817L, 20, 2.0f, amount);
                    }
                }
                else {
                    RenderingDrawUtils.renderLightRayFan(renderStack, renderTypeBuffer, Color.WHITE, id * 31L, 15, 1.5f, 35);
                    RenderingDrawUtils.renderLightRayFan(renderStack, renderTypeBuffer, ColorsAS.CELESTIAL_CRYSTAL, id * 16L, 10, 1.0f, 25);
                }
                RenderingDrawUtils.renderLightRayFan(renderStack, renderTypeBuffer, Color.WHITE, id * 31L, 10, 1.0f, 10);
            }
            else {
                RenderingDrawUtils.renderLightRayFan(renderStack, renderTypeBuffer, Color.WHITE, id * 31L, 15, 1.5f, 35);
                RenderingDrawUtils.renderLightRayFan(renderStack, renderTypeBuffer, ColorsAS.CELESTIAL_CRYSTAL, id * 16L, 10, 1.0f, 25);
            }
            renderStack.func_227865_b_();
        }
    }
}
