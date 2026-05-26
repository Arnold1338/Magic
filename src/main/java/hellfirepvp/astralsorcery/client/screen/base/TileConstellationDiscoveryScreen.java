package hellfirepvp.astralsorcery.client.screen.base;

import net.minecraft.world.level.Level;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.util.tile.NamedInventoryTile;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class TileConstellationDiscoveryScreen<T extends BlockEntity & NamedInventoryTile, D extends DrawArea> extends ConstellationDiscoveryScreen<D>
{
    private final T tile;
    
    protected TileConstellationDiscoveryScreen(final T tile, final int guiHeight, final int guiWidth) {
        super(tile.getDisplayName(), guiHeight, guiWidth);
        this.tile = tile;
    }
    
    public T getTile() {
        return this.tile;
    }
    
    @Override
    public void func_231023_e_() {
        super.func_231023_e_();
        final Level clWorld = (Level)Minecraft.getInstance().level;
        if (this.tile.func_145837_r() || clWorld == null || !clWorld.dimension().equals(this.tile.getLevel().dimension())) {
            this.init();
        }
    }
}
