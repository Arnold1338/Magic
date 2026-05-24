package hellfirepvp.astralsorcery.client.screen.container;

import com.mojang.blaze3d.vertex.BufferBuilder;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.util.RenderingConstellationUtils;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.client.util.Blending;
import com.mojang.blaze3d.systems.RenderSystem;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import java.util.Random;
import hellfirepvp.astralsorcery.common.container.ContainerAltarTrait;
import hellfirepvp.astralsorcery.client.screen.base.ScreenContainerAltar;

public class ScreenContainerAltarRadiance extends ScreenContainerAltar<ContainerAltarTrait>
{
    private static final Random rand;
    
    public ScreenContainerAltarRadiance(final ContainerAltarTrait screenContainer, final Inventory inv, final Component name) {
        super(screenContainer, inv, name, 255, 202);
    }
    
    @Override
    public AbstractRenderableTexture getBackgroundTexture() {
        return TexturesAS.TEX_CONTAINER_ALTAR_RADIANCE;
    }
    
    protected void func_230451_b_(final PoseStack renderStack, final int mouseX, final int mouse) {
        final SimpleAltarRecipe recipe = this.findRecipe(false);
        if (recipe != null) {
            final ItemStack out = recipe.getOutputForRender(this.func_212873_a_().getTileEntity().getInventory());
            renderStack.func_227860_a_();
            renderStack.func_227861_a_(190.0, 35.0, 0.0);
            renderStack.func_227862_a_(2.5f, 2.5f, 1.0f);
            RenderingUtils.renderItemStackGUI(renderStack, out, null);
            renderStack.func_227865_b_();
        }
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        RenderSystem.disableDepthTest();
        final float pTicks = Minecraft.getInstance().func_184121_ak();
        TexturesAS.TEX_STAR_1.bindTexture();
        ScreenContainerAltarRadiance.rand.setSeed(-8604827917233251694L);
        for (int i = 0; i < 18; ++i) {
            final int x = ScreenContainerAltarRadiance.rand.nextInt(54);
            final int y = ScreenContainerAltarRadiance.rand.nextInt(54);
            final float brightness = 0.3f + RenderingConstellationUtils.stdFlicker(ClientScheduler.getClientTick(), pTicks, 10 + ScreenContainerAltarRadiance.rand.nextInt(20)) * 0.6f;
            RenderingUtils.draw(7, DefaultVertexFormat.field_227851_o_, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, (float)(15 + x), (float)(39 + y), (float)this.func_230927_p_(), 5.0f, 5.0f).color(brightness, brightness, brightness, brightness).draw());
        }
        final TileAltar altar = this.func_212873_a_().getTileEntity();
        final IConstellation c = altar.getFocusedConstellation();
        if (c != null && altar.hasMultiblock() && ResearchHelper.getClientProgress().hasConstellationDiscovered(c)) {
            ScreenContainerAltarRadiance.rand.setSeed(7061404134423019785L);
            RenderingConstellationUtils.renderConstellationIntoGUI(c.getConstellationColor(), c, renderStack, 16.0f, 41.0f, (float)this.func_230927_p_(), 58.0f, 58.0f, 2.0, () -> 0.2f + 0.8f * RenderingConstellationUtils.conCFlicker(Minecraft.getInstance().field_71441_e.func_72820_D(), pTicks, 5 + ScreenContainerAltarRadiance.rand.nextInt(5)), true, false);
        }
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }
    
    @Override
    public void renderGuiBackground(final PoseStack renderStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.renderStarlightBar(renderStack, 11, 104, 232, 10);
    }
    
    static {
        rand = new Random();
    }
}
