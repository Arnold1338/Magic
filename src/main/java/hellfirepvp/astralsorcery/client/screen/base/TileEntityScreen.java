package hellfirepvp.astralsorcery.client.screen.base;

import net.minecraft.world.level.Level;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.util.tile.NamedInventoryTile;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TileEntityScreen<T extends BlockEntity & NamedInventoryTile> extends WidthHeightScreen
{
    private final T tile;
    
    protected TileEntityScreen(final T tile, final int guiHeight, final int guiWidth) {
        super(tile.getDisplayName(), guiHeight, guiWidth);
        this.tile = tile;
    }
    
    public T getTile() {
        return this.tile;
    }
    
    @Override
    public void func_231023_e_() {
        super.func_231023_e_();
        final World clWorld = (World)Minecraft.func_71410_x().field_71441_e;
        if (this.tile.func_145837_r() || clWorld == null || !clWorld.dimension().equals(this.tile.func_145831_w().dimension())) {
            this.func_231175_as__();
        }
    }
}
