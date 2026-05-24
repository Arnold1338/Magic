package hellfirepvp.observerlib.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class SimpleBossInfo extends LerpingBossEvent {
    private SimpleBossInfo(UUID id, Component name, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay) {
        // 1.20.1 LerpingBossEvent constructor: (UUID, Component, float, BossBarColor, BossBarOverlay, boolean, boolean, boolean)
        super(id, name, 1.0f, color, overlay, false, false, false);
    }

    public static Builder newBuilder(Component text, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay) {
        return newBuilder(UUID.randomUUID(), text, color, overlay);
    }

    public static Builder newBuilder(UUID id, Component text, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay) {
        return new Builder(id, text, color, overlay);
    }

    @SuppressWarnings("unchecked")
    public boolean displayInfo() {
        if (Minecraft.getInstance().level == null) return false;
        try {
            BossHealthOverlay overlay = Minecraft.getInstance().gui.getBossOverlay();
            Field mapField = BossHealthOverlay.class.getDeclaredField("events");
            mapField.setAccessible(true);
            Map<UUID, LerpingBossEvent> map = (Map<UUID, LerpingBossEvent>) mapField.get(overlay);
            return !map.containsKey(getId()) && map.put(getId(), this) == null;
        } catch (Exception e) { return false; }
    }

    @SuppressWarnings("unchecked")
    public boolean removeInfo() {
        try {
            BossHealthOverlay overlay = Minecraft.getInstance().gui.getBossOverlay();
            Field mapField = BossHealthOverlay.class.getDeclaredField("events");
            mapField.setAccessible(true);
            Map<UUID, LerpingBossEvent> map = (Map<UUID, LerpingBossEvent>) mapField.get(overlay);
            return map.remove(getId()) != null;
        } catch (Exception e) { return false; }
    }

    public static class Builder {
        private final UUID id;
        private final Component text;
        private final BossEvent.BossBarColor color;
        private final BossEvent.BossBarOverlay overlay;
        private Builder(UUID id, Component text, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay) {
            this.id = id; this.text = text; this.color = color; this.overlay = overlay;
        }
        public SimpleBossInfo build() { return new SimpleBossInfo(id, text, color, overlay); }
    }
}
