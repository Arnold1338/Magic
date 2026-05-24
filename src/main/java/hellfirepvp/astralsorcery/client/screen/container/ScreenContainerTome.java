package hellfirepvp.astralsorcery.client.screen.container;

import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.entity.player.Inventory;
import hellfirepvp.astralsorcery.common.container.ContainerTome;
import hellfirepvp.astralsorcery.client.screen.base.ScreenCustomContainer;

public class ScreenContainerTome extends ScreenCustomContainer<ContainerTome>
{
    public ScreenContainerTome(final ContainerTome screenContainer, final Inventory inv, final Component name) {
        super(screenContainer, inv, name, 176, 166);
    }
    
    @Override
    public AbstractRenderableTexture getBackgroundTexture() {
        return TexturesAS.TEX_CONTAINER_TOME_STORAGE;
    }
    
    protected void func_230451_b_(final PoseStack matrixStack, final int x, final int y) {
    }
}
