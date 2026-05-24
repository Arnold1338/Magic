package hellfirepvp.astralsorcery.common.container;

import javax.annotation.Nullable;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class ContainerTileEntity<T extends BlockEntity> extends Container
{
    private final T te;
    
    protected ContainerTileEntity(final T tileEntity, @Nullable final ContainerType<?> type, final int windowId) {
        super((ContainerType)type, windowId);
        this.te = tileEntity;
    }
    
    public T getTileEntity() {
        return this.te;
    }
}
