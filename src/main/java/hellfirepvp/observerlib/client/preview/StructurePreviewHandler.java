package hellfirepvp.observerlib.client.preview;

import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class StructurePreviewHandler implements ITickHandler {
    private static final StructurePreviewHandler INSTANCE = new StructurePreviewHandler();
    private StructurePreview currentPreview = null;

    private StructurePreviewHandler() {}

    public static StructurePreviewHandler getInstance() { return INSTANCE; }

    void setStructurePreview(StructurePreview preview) {
        if (currentPreview != null) currentPreview.onRemove();
        currentPreview = preview;
    }

    public void attachEventListeners(IEventBus bus) {
        bus.addListener(EventPriority.HIGH, this::render);
    }

    public void attachTickHandlers(Consumer<ITickHandler> registrar) {
        registrar.accept(this);
    }

    private void render(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
        Level renderWorld = Minecraft.getInstance().level;
        LocalPlayer player = Minecraft.getInstance().player;
        if (renderWorld == null || player == null || currentPreview == null) return;
        if (currentPreview.canRender(renderWorld, player.blockPosition())) {
            currentPreview.render(renderWorld, event.getPoseStack(), player.getEyePosition());
        }
    }

    @Override
    public void tick(TickEvent.Type type, Object... context) {
        Level renderWorld = Minecraft.getInstance().level;
        LocalPlayer player = Minecraft.getInstance().player;
        if (renderWorld == null || player == null) { currentPreview = null; return; }
        if (currentPreview != null) {
            if (!currentPreview.canPersist(renderWorld, player.blockPosition())) {
                currentPreview.onRemove(); currentPreview = null;
            } else {
                currentPreview.tick(renderWorld, player.blockPosition());
            }
        }
    }

    @Override public EnumSet<TickEvent.Type> getHandledTypes() { return EnumSet.of(TickEvent.Type.CLIENT); }
    @Override public boolean canFire(TickEvent.Phase phase) { return phase == TickEvent.Phase.END; }
    @Override public String getName() { return "ObserverLib Structure Preview"; }
}
