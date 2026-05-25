package hellfirepvp.astralsorcery.client.event;

import com.mojang.blaze3d.vertex.BufferBuilder;
import java.util.EnumSet;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.item.base.PerkExperienceRevealer;
import net.minecraft.world.level.InteractionHand;
import net.minecraftforge.event.TickEvent;
import hellfirepvp.astralsorcery.common.data.research.PlayerPerkData;
import net.minecraft.world.entity.player.Player;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.resource.BlockAtlasTexture;
import net.minecraft.client.gui.Font;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import net.minecraft.network.chat.FormattedCharSequence;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class PerkExperienceRenderer implements ITickHandler
{
    public static final PerkExperienceRenderer INSTANCE;
    private static final int fadeTicks = 15;
    private static final float visibilityChange = 0.06666667f;
    private int revealTicks;
    private float visibilityReveal;
    
    private PerkExperienceRenderer() {
        this.revealTicks = 0;
        this.visibilityReveal = 0.0f;
    }
    
    public void attachEventListeners(final IEventBus bus) {
        bus.addListener(EventPriority.HIGH, (Consumer)this::onRenderOverlay);
    }
    
    private void onRenderOverlay(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        if (this.visibilityReveal <= 0.0f) {
            return;
        }
        if (!ResearchHelper.getClientProgress().isAttuned()) {
            return;
        }
        final PoseStack renderStack = event.getMatrixStack();
        final Player player = (Player)Minecraft.getInstance().player;
        final float frameHeight = 128.0f;
        final float frameWidth = 32.0f;
        final float frameOffsetX = 0.0f;
        final float frameOffsetY = 5.0f;
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        TexturesAS.TEX_OVERLAY_EXP_FRAME.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.POSITION_TEX_COLOR, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, frameOffsetX, frameOffsetY, 10.0f, frameWidth, frameHeight).color(1.0f, 1.0f, 1.0f, this.visibilityReveal * 0.9f).draw());
        final PlayerPerkData perkData = ResearchHelper.getClientProgress().getPerkData();
        final float perc = perkData.getPercentToNextLevel(player, LogicalSide.CLIENT);
        final float expHeight = 78.0f * perc;
        final float expWidth = 32.0f;
        final float expOffsetX = 0.0f;
        final float expOffsetY = 27.5f + (1.0f - perc) * 78.0f;
        TexturesAS.TEX_OVERLAY_EXP_BAR.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.POSITION_TEX_COLOR, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, expOffsetX, expOffsetY, 10.0f, expWidth, expHeight).color(1.0f, 0.9f, 0.0f, this.visibilityReveal * 0.9f).tex(0.0f, 0.0f, 1.0f, 1.0f - perc).draw());
        final String strLevel = String.valueOf(perkData.getPerkLevel(player, LogicalSide.CLIENT));
        final Component txtLevel = new Component(strLevel);
        final int strLength = Minecraft.getInstance().font.func_238414_a_((FormattedCharSequence)txtLevel);
        renderStack.popPose();
        renderStack.translate((double)(15.0f - strLength / 2.0f), 94.0, 20.0);
        renderStack.translate(1.2f, 1.2f, 1.0f);
        int c = 14540253;
        c |= (int)(255.0f * this.visibilityReveal) << 24;
        if (this.visibilityReveal > 1.0E-5) {
            RenderingDrawUtils.renderStringAt((FormattedCharSequence)txtLevel, renderStack, null, c, true);
        }
        renderStack.popPose();
        BlockAtlasTexture.getInstance().bindTexture();
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final Player player = (Player)Minecraft.getInstance().player;
        if (player != null) {
            ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (!held.isEmpty() && held.getItem() instanceof PerkExperienceRevealer && ((PerkExperienceRevealer)held.getItem()).shouldReveal(held)) {
                this.revealExperience(20);
            }
            held = player.getItemInHand(InteractionHand.OFF_HAND);
            if (!held.isEmpty() && held.getItem() instanceof PerkExperienceRevealer && ((PerkExperienceRevealer)held.getItem()).shouldReveal(held)) {
                this.revealExperience(20);
            }
        }
        --this.revealTicks;
        if (this.revealTicks - 15 < 0) {
            if (this.visibilityReveal > 0.0f) {
                this.visibilityReveal = Math.max(0.0f, this.visibilityReveal - 0.06666667f);
            }
        }
        else if (this.visibilityReveal < 1.0f) {
            this.visibilityReveal = Math.min(1.0f, this.visibilityReveal + 0.06666667f);
        }
    }
    
    public void revealExperience(final int forTicks) {
        this.revealTicks = forTicks;
    }
    
    public void resetReveal() {
        this.revealTicks = 0;
        this.visibilityReveal = 0.0f;
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.CLIENT);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "Perk Experience Renderer";
    }
    
    static {
        INSTANCE = new PerkExperienceRenderer();
    }
}
