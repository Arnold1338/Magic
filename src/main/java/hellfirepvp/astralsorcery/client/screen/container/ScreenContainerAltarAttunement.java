package hellfirepvp.astralsorcery.client.screen.container;

import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.entity.player.Inventory;
import hellfirepvp.astralsorcery.common.container.ContainerAltarAttunement;
import hellfirepvp.astralsorcery.client.screen.base.ScreenContainerAltar;

public class ScreenContainerAltarAttunement extends ScreenContainerAltar<ContainerAltarAttunement>
{
    public ScreenContainerAltarAttunement(final ContainerAltarAttunement screenContainer, final Inventory inv, final Component name) {
        super(screenContainer, inv, name, 256, 202);
    }
    
    @Override
    public AbstractRenderableTexture getBackgroundTexture() {
        return TexturesAS.TEX_CONTAINER_ALTAR_ATTUNEMENT;
    }
    
    protected void func_230451_b_(final PoseStack renderStack, final int mouseX, final int mouseY) {
        final SimpleAltarRecipe recipe = this.findRecipe(false);
        if (recipe != null) {
            final ItemStack out = recipe.getOutputForRender(this.func_212873_a_().getTileEntity().getInventory());
            renderStack.func_227860_a_();
            renderStack.func_227861_a_(190.0, 35.0, 0.0);
            renderStack.func_227862_a_(2.5f, 2.5f, 1.0f);
            RenderingUtils.renderItemStackGUI(renderStack, out, null);
            renderStack.func_227865_b_();
        }
    }
    
    @Override
    public void renderGuiBackground(final PoseStack renderStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.renderStarlightBar(renderStack, 11, 104, 232, 10);
    }
}
