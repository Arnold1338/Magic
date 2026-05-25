package hellfirepvp.astralsorcery.client.screen.journal.page;

import java.util.List;
import hellfirepvp.astralsorcery.common.data.research.ProgressionTier;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.network.chat.Component;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraftforge.fluids.FluidStack;
import hellfirepvp.astralsorcery.client.resource.BlockAtlasTexture;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInfusion;

public class RenderPageLiquidInfusion extends RenderPageRecipeTemplate
{
    private final LiquidInfusion recipe;
    
    public RenderPageLiquidInfusion(@Nullable final ResearchNode node, final int nodePage, final LiquidInfusion recipe) {
        super(node, nodePage);
        this.recipe = recipe;
    }
    
    @Override
    public void render(final PoseStack renderStack, final float x, final float y, final float z, final float pTicks, final float mouseX, final float mouseY) {
        this.clearFrameRectangles();
        this.renderRecipeGrid(renderStack, x, y, z, TexturesAS.TEX_GUI_BOOK_GRID_INFUSION);
        this.renderExpectedItemStackOutput(renderStack, x + 78.0f, y + 25.0f, z, 1.4f, this.recipe.getOutput(ItemStack.EMPTY));
        this.renderInfoStar(renderStack, x, y, z, pTicks);
        final float renderX = x + 80.0f;
        final float renderY = y + 128.0f;
        this.renderItemStack(renderStack, renderX, renderY + 15.0f, z, 1.2f, new ItemStack((ItemLike)BlocksAS.INFUSER));
        this.renderExpectedIngredientInput(renderStack, renderX, renderY, z, 1.2f, 0L, this.recipe.getItemInput());
        BlockAtlasTexture.getInstance().bindTexture();
        final TextureAtlasSprite tas = RenderingUtils.getParticleTexture(new FluidStack(this.recipe.getLiquidInput(), 1000));
        RenderingUtils.draw(7, DefaultVertexFormat.POSITION_TEX_COLOR, buf -> {
            renderStack.popPose();
            renderStack.translate((double)x, (double)y, (double)z);
            this.renderLiquidInput(buf, renderStack, tas, 1, 0);
            this.renderLiquidInput(buf, renderStack, tas, 2, 0);
            this.renderLiquidInput(buf, renderStack, tas, 3, 0);
            this.renderLiquidInput(buf, renderStack, tas, 1, 4);
            this.renderLiquidInput(buf, renderStack, tas, 2, 4);
            this.renderLiquidInput(buf, renderStack, tas, 3, 4);
            this.renderLiquidInput(buf, renderStack, tas, 0, 1);
            this.renderLiquidInput(buf, renderStack, tas, 0, 2);
            this.renderLiquidInput(buf, renderStack, tas, 0, 3);
            this.renderLiquidInput(buf, renderStack, tas, 4, 1);
            this.renderLiquidInput(buf, renderStack, tas, 4, 2);
            this.renderLiquidInput(buf, renderStack, tas, 4, 3);
            renderStack.popPose();
        });
    }
    
    private void renderLiquidInput(final BufferBuilder buf, final PoseStack renderStack, final TextureAtlasSprite tas, final int x, final int y) {
        RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, 28.0f + x * 25.15f, 76.0f + y * 25.15f, 0.0f, 22.3f, 22.3f).tex(tas).draw();
    }
    
    @Override
    public boolean propagateMouseClick(final double mouseX, final double mouseZ) {
        return this.handleBookLookupClick(mouseX, mouseZ);
    }
    
    @Override
    public void postRender(final PoseStack renderStack, final float x, final float y, final float z, final float pTicks, final float mouseX, final float mouseY) {
        this.renderHoverTooltips(renderStack, mouseX, mouseY, z, this.recipe.func_199560_c());
        this.renderInfoStarTooltips(renderStack, x, y, z, mouseX, mouseY, toolTip -> {
            new Component("astralsorcery.journal.recipe.infusion.liquid", new Object[] { this.recipe.getLiquidInput().getAttributes().getDisplayName(new FluidStack(this.recipe.getLiquidInput(), 1000)) });
            final Component translationTextComponent;
            toolTip.add(translationTextComponent);
            new Component("astralsorcery.journal.recipe.infusion.chance.format", new Object[] { this.getInfuserChanceDescription(this.recipe.getConsumptionChance()) });
            final Component translationTextComponent2;
            toolTip.add(translationTextComponent2);
            if (this.recipe.doesConsumeMultipleFluids()) {
                toolTip.add(new Component("astralsorcery.journal.recipe.infusion.multiple"));
            }
            if (!this.recipe.acceptsChaliceInput() && ResearchHelper.getClientProgress().getTierReached().isThisLaterOrEqual(ProgressionTier.TRAIT_CRAFT)) {
                toolTip.add(new Component("astralsorcery.journal.recipe.infusion.no_chalice"));
            }
            if (this.recipe.doesCopyNBTToOutputs()) {
                toolTip.add(new Component("astralsorcery.journal.recipe.infusion.copy_nbt"));
            }
        });
    }
}
