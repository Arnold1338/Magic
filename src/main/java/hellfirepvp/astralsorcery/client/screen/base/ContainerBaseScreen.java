package hellfirepvp.astralsorcery.client.screen.base;

import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import hellfirepvp.astralsorcery.common.container.ContainerTileEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class ContainerBaseScreen<T extends BlockEntity, C extends ContainerTileEntity<T>> extends ContainerScreen<C>
{
    public ContainerBaseScreen(final C screenContainer, final Inventory inv, final Component titleIn) {
        super((Container)screenContainer, inv, titleIn);
    }
    
    public void func_230430_a_(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        this.func_230446_a_(renderStack);
        super.func_230430_a_(renderStack, mouseX, mouseY, pTicks);
        this.func_230459_a_(renderStack, mouseX, mouseY);
    }
    
    public void func_231023_e_() {
        super.func_231023_e_();
        final BlockEntity te = ((ContainerTileEntity)this.field_147002_h).getTileEntity();
        if (te.func_145837_r() || !((ContainerTileEntity)this.field_147002_h).func_75145_c((Player)Minecraft.getInstance().player)) {
            this.func_231175_as__();
        }
    }
}
