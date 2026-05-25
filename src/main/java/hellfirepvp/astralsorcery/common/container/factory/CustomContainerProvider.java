package hellfirepvp.astralsorcery.common.container.factory;

import java.util.function.Consumer;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.inventory.MenuType;
import net.minecraft.world.inventory.INamedContainerProvider;
import net.minecraft.world.level.inventory.AbstractContainerMenu;

public abstract class CustomContainerProvider<C extends Container> implements INamedContainerProvider
{
    private final ContainerType<C> type;
    
    public CustomContainerProvider(final ContainerType<C> type) {
        this.type = type;
    }
    
    public Component func_145748_c_() {
        final ResourceLocation key = this.type.getRegistryName();
        return (Component)new Component("screen.%s.%s", new Object[] { key.func_110624_b(), key.addTransientModifier() });
    }
    
    @Nonnull
    public abstract C createMenu(final int p0, final Inventory p1, final Player p2);
    
    protected abstract void writeExtraData(final FriendlyByteBuf p0);
    
    public void openFor(final ServerPlayer player) {
        NetworkHooks.openGui(player, (INamedContainerProvider)this, (Consumer)this::writeExtraData);
    }
}
