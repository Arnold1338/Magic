package hellfirepvp.astralsorcery.client.screen.journal.page;

import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.network.chat.ITextProperties;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.util.Blending;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.util.block.BlockMatchInformation;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import hellfirepvp.astralsorcery.common.crafting.recipe.BlockTransmutation;

public class RenderPageBlockTransmutation extends RenderPageRecipeTemplate
{
    private final BlockTransmutation recipe;
    private final List<ItemStack> inputOptions;
    
    public RenderPageBlockTransmutation(@Nullable final ResearchNode node, final int nodePage, final BlockTransmutation blockTransmutation) {
        super(node, nodePage);
        this.recipe = blockTransmutation;
        this.inputOptions = blockTransmutation.getInputOptions().stream().map((Function<? super Object, ?>)BlockMatchInformation::getDisplayStack).filter(stack -> !stack.isEmpty()).collect((Collector<? super Object, ?, List<ItemStack>>)Collectors.toList());
    }
    
    @Override
    public void render(final PoseStack renderStack, final float x, final float y, final float z, final float pTicks, final float mouseX, final float mouseY) {
        this.clearFrameRectangles();
        RenderSystem.depthMask(false);
        this.renderRecipeGrid(renderStack, x, y, z, TexturesAS.TEX_GUI_BOOK_GRID_TRANSMUTATION);
        RenderSystem.depthMask(true);
        this.renderExpectedItemStackOutput(renderStack, x + 78.0f, y + 25.0f, z, 1.4f, this.recipe.getOutputDisplay());
        if (this.recipe.getRequiredConstellation() != null) {
            this.renderInfoStar(renderStack, x, y, z, pTicks);
            this.renderRequiredConstellation(renderStack, x, y, z, this.recipe.getRequiredConstellation());
        }
        final float renderX = x + 80.0f;
        final float renderY = y + 73.0f;
        this.renderExpectedIngredientInput(renderStack, renderX, renderY + 80.0f, z, 1.2f, 0L, this.inputOptions);
        SpritesAS.SPR_LIGHTBEAM.bindTexture();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        Blending.ADDITIVE_ALPHA.apply();
        RenderingUtils.draw(7, DefaultVertexFormat.field_227851_o_, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, renderX - 15.0f, renderY + 10.0f, z, 50.0f, 120.0f).tex(SpritesAS.SPR_LIGHTBEAM).draw());
        Blending.DEFAULT.apply();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.disableDepthTest();
        renderStack.func_227860_a_();
        renderStack.func_227861_a_((double)(renderX + 11.0f), (double)(renderY + 11.0f), (double)z);
        renderStack.func_227862_a_(40.0f, 40.0f, 0.0f);
        RenderingUtils.draw(7, DefaultVertexFormat.field_181706_f, buf -> RenderingDrawUtils.renderLightRayFan(renderStack, renderType -> buf, ColorsAS.ROCK_CRYSTAL, this.getNodePage(), 9, 9.0f, 20));
        renderStack.func_227865_b_();
        this.renderItemStack(renderStack, renderX - 4.0f, renderY - 4.0f, z, 1.75f, new ItemStack((ItemLike)BlocksAS.ROCK_COLLECTOR_CRYSTAL));
        RenderSystem.enableDepthTest();
    }
    
    @Override
    public boolean propagateMouseClick(final double mouseX, final double mouseZ) {
        return this.handleBookLookupClick(mouseX, mouseZ);
    }
    
    @Override
    public void postRender(final PoseStack renderStack, final float x, final float y, final float z, final float pTicks, final float mouseX, final float mouseY) {
        this.renderHoverTooltips(renderStack, mouseX, mouseY, z, this.recipe.func_199560_c());
        this.renderInfoStarTooltips(renderStack, x, y, z, mouseX, mouseY, toolTip -> this.addConstellationInfoTooltip(this.recipe.getRequiredConstellation(), toolTip));
    }
}
