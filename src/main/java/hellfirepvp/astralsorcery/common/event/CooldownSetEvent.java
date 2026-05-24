package hellfirepvp.astralsorcery.common.event;

import net.minecraft.world.level.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class CooldownSetEvent extends Event
{
    private final Player player;
    private final int originalCooldown;
    private int cooldown;
    
    public CooldownSetEvent(final Player player, final int originalCooldown) {
        this.player = player;
        this.originalCooldown = originalCooldown;
        this.setCooldown(this.getOriginalCooldown());
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public int getOriginalCooldown() {
        return this.originalCooldown;
    }
    
    public void setCooldown(final int cooldown) {
        this.cooldown = cooldown;
    }
    
    public int getResultCooldown() {
        return this.cooldown;
    }
}
