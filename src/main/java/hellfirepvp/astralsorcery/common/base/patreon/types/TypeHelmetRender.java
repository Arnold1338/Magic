package hellfirepvp.astralsorcery.common.base.patreon.types;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import net.minecraft.world.item.ItemStack;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;

public class TypeHelmetRender extends PatreonEffect
{
    private final UUID playerUUID;
    private final ItemStack helmetStack;
    private boolean addedHelmet;
    
    public TypeHelmetRender(final UUID effectUUID, @Nullable final FlareColor flareColor, final UUID playerUUID, final ItemStack helmetStack) {
        super(effectUUID, flareColor);
        this.addedHelmet = false;
        this.playerUUID = playerUUID;
        this.helmetStack = helmetStack.copy();
    }
    
    @Override
    public void attachEventListeners(final IEventBus bus) {
        super.attachEventListeners(bus);
        bus.register((Object)this);
    }
    
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void renderPre(final RenderPlayerEvent.Pre event) {
        final Player player = event.getPlayer();
        if (player.getUUID().equals(this.playerUUID) && player.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            player.getInventory().field_70460_b.set(EquipmentSlot.HEAD.func_188454_b(), (Object)ItemUtils.copyStackWithSize(this.helmetStack, 1));
            this.addedHelmet = true;
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void renderPost(final RenderPlayerEvent.Post event) {
        final Player player = event.getPlayer();
        if (player.getUUID().equals(this.playerUUID) && this.addedHelmet) {
            player.getInventory().field_70460_b.set(EquipmentSlot.HEAD.func_188454_b(), (Object)ItemStack.EMPTY);
            this.addedHelmet = false;
        }
    }
}
