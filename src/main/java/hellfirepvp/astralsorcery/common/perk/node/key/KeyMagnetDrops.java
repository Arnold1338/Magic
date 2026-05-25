package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraft.world.item.ItemStack;
import java.util.Iterator;
import java.util.List;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.damagesource.DamageSource;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.entity.item.ItemEntity;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyMagnetDrops extends KeyPerk
{
    public KeyMagnetDrops(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener(EventPriority.LOW, (Consumer)this::onEntityLoot);
    }
    
    private void onEntityLoot(final LivingDropsEvent event) {
        final DamageSource source = event.getSource();
        if (source.getEnchantments( != null && source.getDirectEntity() instanceof Player) {
            final Player player = (Player)source.getDirectEntity();
            final LogicalSide side = this.getSide((Entity)player);
            final PlayerProgress prog = ResearchHelper.getProgress(player, side);
            if (prog.getPerkData().hasPerkEffect(this)) {
                final List<ItemEntity> remaining = new ArrayList<ItemEntity>();
                for (final ItemEntity drop : event.getDrops()) {
                    final ItemStack remain = ItemUtils.dropItemToPlayer(player, drop.func_92059_d());
                    if (!remain.isEmpty()) {
                        final ItemEntity newDrop = new ItemEntity(drop.level(), drop.getX(), drop.getY(), drop.getZ());
                        newDrop.func_180432_n((Entity)drop);
                        remaining.add(newDrop);
                    }
                }
                event.getDrops().clear();
                event.getDrops().addAll(remaining);
            }
        }
    }
}
