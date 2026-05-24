package hellfirepvp.astralsorcery.common.block.base;

import net.minecraft.client.renderer.blockentity.ItemStackTileEntityRenderer;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.Collections;
import net.minecraftforge.common.ToolAction;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.world.level.item.Rarity;
import net.minecraft.world.level.item.CreativeModeTab;
import javax.annotation.Nullable;
import net.minecraft.world.level.item.Item;

public interface CustomItemBlockProperties extends CustomItemBlock
{
    default int getItemMaxStackSize() {
        return (this.getItemMaxDamage() > 0) ? 1 : 64;
    }
    
    default int getItemMaxDamage() {
        return 0;
    }
    
    @Nullable
    default Item getContainerItem() {
        return null;
    }
    
    @Nullable
    default CreativeModeTab getItemGroup() {
        return null;
    }
    
    @Nonnull
    default Rarity getItemRarity() {
        return Rarity.COMMON;
    }
    
    default boolean canItemBeRepaired() {
        return false;
    }
    
    @Nonnull
    default Map<ToolType, Integer> getItemToolLevels() {
        return Collections.emptyMap();
    }
    
    @Nullable
    default Supplier<Callable<ItemStackTileEntityRenderer>> getItemTEISR() {
        return null;
    }
}
