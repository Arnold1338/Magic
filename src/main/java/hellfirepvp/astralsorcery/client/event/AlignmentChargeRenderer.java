package hellfirepvp.astralsorcery.client.event;

import com.mojang.blaze3d.vertex.BufferBuilder;
import java.util.EnumSet;
import hellfirepvp.astralsorcery.common.item.base.AlignmentChargeRevealer;
import net.minecraftforge.event.TickEvent;
import java.awt.Color;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.MainWindow;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.resource.BlockAtlasTexture;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.systems.RenderSystem;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import hellfirepvp.astralsorcery.common.item.base.AlignmentChargeConsumer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import net.minecraft.world.level.GameType;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class AlignmentChargeRenderer implements ITickHandler
{
    public static final AlignmentChargeRenderer INSTANCE;
    private static final int fadeTicks = 15;
    private static final float visibilityChange = 0.06666667f;
    private int revealTicks;
    private float alphaReveal;
    
    private AlignmentChargeRenderer() {
        this.revealTicks = 0;
        this.alphaReveal = 0.0f;
    }
    
    public void attachEventListeners(final IEventBus bus) {
        bus.addListener(EventPriority.HIGH, (Consumer)this::onRenderOverlay);
    }
    
    private void onRenderOverlay(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {

        }
        if (Minecraft.getInstance().gameMode != null && Minecraft.getInstance().gameMode.func_178889_l() == GameType.SPECTATOR) {

        }
        if (this.alphaReveal <= 0.0f) {

        }
        final PoseStack renderStack = event.getMatrixStack();
        final MainWindow window = event.getWindow();
        final int screenWidth = window.func_198107_o();
        final int screenHeight = window.func_198087_p();
        final int barWidth = 194;
        final int offsetLeft = screenWidth / 2 - barWidth / 2;
        final int offsetTop = screenHeight + 3 - 81;
        final Player player = (Player)Minecraft.getInstance().player;
        float percFilled = AlignmentChargeHandler.INSTANCE.getFilledPercentage(player, LogicalSide.CLIENT);
        boolean hasEnoughCharge = true;
        float usagePerc = 0.0f;
        for (final EquipmentSlot type : EquipmentSlot.values()) {
            final ItemStack equipped = player.getItemBySlot(type);
            if (!equipped.isEmpty() && equipped.getItem() instanceof AlignmentChargeConsumer) {
                final float chargeRequired = ((AlignmentChargeConsumer)equipped.getItem()).getAlignmentChargeCost(player, equipped);
                final float max = AlignmentChargeHandler.INSTANCE.getMaximumCharge(player, LogicalSide.CLIENT);
                usagePerc = Math.min(chargeRequired / max, percFilled);
                hasEnoughCharge = (percFilled > usagePerc);
                percFilled -= usagePerc;

            }
        }
        final Tuple<Float, Float> uvColored = SpritesAS.SPR_OVERLAY_CHARGE.getUVOffset();
        final Tuple<Float, Float> uvColorless = SpritesAS.SPR_OVERLAY_CHARGE.getUVOffset();
        final float width = barWidth * percFilled;
        final float usageWidth = barWidth * usagePerc;
        final float uLengthCharge = SpritesAS.SPR_OVERLAY_CHARGE.getULength() * percFilled;
        final float uLengthUsage = SpritesAS.SPR_OVERLAY_CHARGE_COLORLESS.getULength() * usagePerc;
        final Color usageColor = hasEnoughCharge ? ColorsAS.OVERLAY_CHARGE_USAGE : ColorsAS.OVERLAY_CHARGE_MISSING;
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        SpritesAS.SPR_OVERLAY_CHARGE.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.POSITION_TEX_COLOR, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, (float)offsetLeft, (float)offsetTop, 10.0f, width, 54.0f).color(1.0f, 1.0f, 1.0f, this.alphaReveal).tex((float)uvColored.getA(), (float)uvColored.getB() + 0.002f, uLengthCharge, SpritesAS.SPR_OVERLAY_CHARGE.getVWidth() - 0.002f).draw());
        SpritesAS.SPR_OVERLAY_CHARGE_COLORLESS.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.POSITION_TEX_COLOR, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, offsetLeft + width, (float)offsetTop, 10.0f, usageWidth, 54.0f).color(usageColor.getRed(), usageColor.getGreen(), usageColor.getBlue(), (int)(this.alphaReveal * 255.0f)).tex((float)uvColorless.getA() + uLengthCharge, (float)uvColorless.getB() + 0.002f, uLengthUsage, SpritesAS.SPR_OVERLAY_CHARGE_COLORLESS.getVWidth() - 0.002f).draw());
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
        BlockAtlasTexture.getInstance().bindTexture();
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final Player player = (Player)Minecraft.getInstance().player;
        if (player != null) {
            if (AlignmentChargeHandler.INSTANCE.getFilledPercentage(player, LogicalSide.CLIENT) <= 0.95f) {
                this.revealCharge(20);
            }
            for (final EquipmentSlot slot : EquipmentSlot.values()) {
                final ItemStack stack = player.getItemBySlot(slot);
                if (!stack.isEmpty() && stack.getItem() instanceof AlignmentChargeRevealer && ((AlignmentChargeRevealer)stack.getItem()).shouldReveal(stack)) {
                    this.revealCharge(20);

                }
            }
        }
        --this.revealTicks;
        if (this.revealTicks - 15 < 0) {
            if (this.alphaReveal > 0.0f) {
                this.alphaReveal = Math.max(0.0f, this.alphaReveal - 0.06666667f);
            }
        }
        else if (this.alphaReveal < 1.0f) {
            this.alphaReveal = Math.min(1.0f, this.alphaReveal + 0.06666667f);
        }
    }
    
    public void revealCharge(final int forTicks) {
        this.revealTicks = forTicks;
    }
    
    public void resetChargeReveal() {
        this.revealTicks = 0;
        this.alphaReveal = 0.0f;
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.CLIENT);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "Alignment Charge Renderer";
    }
    
    static {
        INSTANCE = new AlignmentChargeRenderer();
    }
}
