package hellfirepvp.astralsorcery.common.base.patreon;

import java.util.Objects;
import hellfirepvp.astralsorcery.common.base.patreon.entity.PatreonFlare;
import hellfirepvp.astralsorcery.common.base.patreon.entity.PatreonPartialEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.Random;

public class PatreonEffect
{
    protected static final Random rand;
    private final FlareColor flareColor;
    private final UUID effectUUID;
    
    public PatreonEffect(final UUID effectUUID, @Nullable final FlareColor flareColor) {
        this.effectUUID = effectUUID;
        this.flareColor = flareColor;
    }
    
    @Nullable
    public FlareColor getFlareColor() {
        return this.flareColor;
    }
    
    public boolean hasPartialEntity() {
        return this.getFlareColor() != null;
    }
    
    public UUID getEffectUUID() {
        return this.effectUUID;
    }
    
    public void initialize() {
    }
    
    public void attachEventListeners(final IEventBus bus) {
    }
    
    public void attachTickListeners(final Consumer<ITickHandler> registrar) {
    }
    
    @OnlyIn(Dist.CLIENT)
    public void doClientEffect(final Player player) {
    }
    
    @Nullable
    public PatreonPartialEntity createEntity(final UUID playerUUID) {
        if (this.hasPartialEntity()) {
            return new PatreonFlare(this.getEffectUUID(), playerUUID);
        }
        return null;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final PatreonEffect that = (PatreonEffect)o;
        return Objects.equals(this.effectUUID, that.effectUUID);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.effectUUID);
    }
    
    static {
        rand = new Random();
    }
}
