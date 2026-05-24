package hellfirepvp.astralsorcery.client.screen.base;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipeContext;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import hellfirepvp.astralsorcery.common.container.ContainerAltarBase;

public abstract class ScreenContainerAltar<T extends ContainerAltarBase> extends ScreenCustomContainer<T>
{
    public ScreenContainerAltar(final T screenContainer, final Inventory inv, final Component name, final int width, final int height) {
        super(screenContainer, inv, name, width, height);
    }
    
    @Nullable
    public SimpleAltarRecipe findRecipe(final boolean ignoreStarlightRequirement) {
        final TileAltar ta = this.func_212873_a_().getTileEntity();
        return RecipeTypesAS.TYPE_ALTAR.findRecipe(new SimpleAltarRecipeContext((Player)Minecraft.getInstance().field_71439_g, LogicalSide.CLIENT, ta).setIgnoreStarlightRequirement(ignoreStarlightRequirement));
    }
    
    @Override
    protected void func_230450_a_(final PoseStack renderStack, final float partialTicks, final int mouseX, final int mouseY) {
        RenderSystem.enableDepthTest();
        this.renderGuiBackground(renderStack, partialTicks, mouseX, mouseY);
        super.func_230450_a_(renderStack, partialTicks, mouseX, mouseY);
    }
    
    protected void renderStarlightBar(final PoseStack renderStack, final int offsetX, final int offsetZ, final int width, final int height) {
        final TileAltar altar = this.func_212873_a_().getTileEntity();
        RenderSystem.disableAlphaTest();
        TexturesAS.TEX_BLACK.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.field_227851_o_, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, (float)(this.field_147003_i + offsetX), (float)(this.field_147009_r + offsetZ), (float)this.func_230927_p_(), (float)width, (float)height).draw());
        float percFilled;
        Color barColor;
        if (altar.hasMultiblock()) {
            percFilled = altar.getAmbientStarlightPercent();
            barColor = Color.WHITE;
        }
        else {
            percFilled = 1.0f;
            barColor = Color.RED;
        }
        if (percFilled > 0.0f) {
            final SpriteSheetResource spriteStarlight = SpritesAS.SPR_STARLIGHT_STORE;
            spriteStarlight.getResource().bindTexture();
            final int tick = altar.getTicksExisted();
            final Tuple<Float, Float> uvOffset = spriteStarlight.getUVOffset(tick);
            RenderingUtils.draw(7, DefaultVertexFormat.field_227851_o_, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, (float)(this.field_147003_i + offsetX), (float)(this.field_147009_r + offsetZ), (float)this.func_230927_p_(), (float)(int)(width * percFilled), (float)height).tex((float)uvOffset.func_76341_a(), (float)uvOffset.func_76340_b(), spriteStarlight.getULength() * percFilled, spriteStarlight.getVLength()).color(barColor).draw());
            if (altar.hasMultiblock()) {
                final SimpleAltarRecipe aar = this.findRecipe(true);
                if (aar != null) {
                    final int req = aar.getStarlightRequirement();
                    final int has = altar.getStoredStarlight();
                    if (has < req) {
                        final int max = altar.getAltarType().getStarlightCapacity();
                        final float percReq = (req - has) / (float)max;
                        final int from = (int)(width * percFilled);
                        final int to = (int)(width * percReq);
                        RenderingUtils.draw(7, DefaultVertexFormat.field_227851_o_, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, (float)(this.field_147003_i + offsetX + from), (float)(this.field_147009_r + offsetZ), (float)this.func_230927_p_(), (float)to, (float)height).tex((float)uvOffset.func_76341_a() + spriteStarlight.getULength() * percFilled, (float)uvOffset.func_76340_b(), spriteStarlight.getULength() * percReq, spriteStarlight.getVLength()).color(0.2f, 0.5f, 1.0f, 0.4f).draw());
                    }
                }
            }
        }
        RenderSystem.enableAlphaTest();
    }
    
    public abstract void renderGuiBackground(final PoseStack p0, final float p1, final int p2, final int p3);
}
