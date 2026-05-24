package hellfirepvp.astralsorcery.client.screen.base;

import com.mojang.blaze3d.vertex.BufferBuilder;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class ScreenCustomContainer<T extends Container> extends ContainerScreen<T> implements IHasContainer<T>
{
    private final int sWidth;
    private final int sHeight;
    
    public ScreenCustomContainer(final T screenContainer, final Inventory inv, final Component name, final int width, final int height) {
        super((Container)screenContainer, inv, name);
        this.sWidth = width;
        this.sHeight = height;
    }
    
    public abstract AbstractRenderableTexture getBackgroundTexture();
    
    protected void func_231160_c_() {
        this.field_146999_f = this.sWidth;
        this.field_147000_g = this.sHeight;
        super.func_231160_c_();
    }
    
    public T func_212873_a_() {
        return (T)this.field_147002_h;
    }
    
    public void func_230430_a_(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        this.func_230446_a_(renderStack);
        super.func_230430_a_(renderStack, mouseX, mouseY, pTicks);
        this.func_230459_a_(renderStack, mouseX, mouseY);
    }
    
    protected void func_230450_a_(final PoseStack renderStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.getBackgroundTexture().bindTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderingUtils.draw(7, DefaultVertexFormats.field_227851_o_, buf -> RenderingGuiUtils.rect((IVertexBuilder)buf, renderStack, (float)this.field_147003_i, (float)this.field_147009_r, (float)this.func_230927_p_(), (float)this.sWidth, (float)this.sHeight).draw());
    }
}
