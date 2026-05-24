package hellfirepvp.astralsorcery.common.base.patreon.types;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import java.util.Iterator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import net.minecraft.world.level.IBlockDisplayReader;
import hellfirepvp.astralsorcery.client.util.LightmapUtil;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.util.Blending;
import hellfirepvp.astralsorcery.client.resource.BlockAtlasTexture;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Player;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import java.util.Map;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;

public class TypeBlockRing extends PatreonEffect
{
    private final UUID playerUUID;
    private final float distance;
    private final float rotationAngle;
    private final int repetition;
    private final int rotationSpeed;
    private final float rotationPart;
    private final Map<BlockPos, BlockState> pattern;
    
    public TypeBlockRing(final UUID sessionEffectId, final FlareColor chosenColor, final UUID playerUUID, final float distance, final float rotationAngle, final int repeats, final int tickRotationSpeed, final Map<BlockPos, BlockState> pattern) {
        super(sessionEffectId, chosenColor);
        this.playerUUID = playerUUID;
        this.distance = distance;
        this.rotationAngle = rotationAngle;
        this.repetition = repeats;
        this.rotationSpeed = tickRotationSpeed;
        this.rotationPart = 360.0f / this.rotationSpeed;
        this.pattern = pattern;
    }
    
    @Override
    public void attachEventListeners(final IEventBus bus) {
        super.attachEventListeners(bus);
        bus.register((Object)this);
    }
    
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderLast(final RenderWorldLastEvent event) {
        final Player pl = (Player)Minecraft.getInstance().field_71439_g;
        if (Minecraft.getInstance().field_71474_y.func_243230_g().func_243192_a() && pl != null && pl.getUUID().equals(this.playerUUID)) {
            final PoseStack renderStack = event.getMatrixStack();
            int alpha = 88;
            if (pl.field_70125_A >= 35.0f) {
                alpha *= (int)Math.max(0.0f, (55.0f - pl.field_70125_A) / 20.0f);
            }
            if (Minecraft.func_238218_y_()) {
                RenderSystem.clear(256, Minecraft.field_142025_a);
            }
            renderStack.func_227860_a_();
            renderStack.func_227861_a_(0.0, -0.5, 0.0);
            renderStack.func_227862_a_(0.5f, 0.5f, 0.5f);
            this.renderRingAt(renderStack, pl, alpha, event.getPartialTicks());
            renderStack.func_227865_b_();
        }
    }
    
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderPost(final RenderPlayerEvent.Post ev) {
        final Player player = ev.getPlayer();
        if (!player.getUUID().equals(this.playerUUID)) {
            return;
        }
        this.renderRingAt(ev.getMatrixStack(), player, 88, ev.getPartialRenderTick());
    }
    
    @OnlyIn(Dist.CLIENT)
    private void renderRingAt(final PoseStack renderStack, final Player player, final int alphaMultiplier, final float pTicks) {
        float addedRotationAngle = 0.0f;
        if (this.rotationSpeed > 1) {
            final float rot = (float)(ClientScheduler.getSystemClientTick() % this.rotationSpeed);
            addedRotationAngle = rot / this.rotationSpeed * 360.0f + this.rotationPart * pTicks;
        }
        RenderSystem.enableTexture();
        BlockAtlasTexture.getInstance().bindTexture();
        RenderSystem.disableAlphaTest();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        Blending.ADDITIVE_ALPHA.apply();
        for (int rotation = 0; rotation < 360; rotation += 360 / this.repetition) {
            for (final BlockPos offset : this.pattern.keySet()) {
                final BlockState state = this.pattern.get(offset);
                final TextureAtlasSprite tas = RenderingUtils.getParticleTexture(state, offset);
                if (tas == null) {
                    continue;
                }
                final float angle = offset.getZ() * this.rotationAngle + rotation + addedRotationAngle;
                final Vector3 dir = new Vector3(offset.getX() - this.distance, (float)offset.getY(), 0.0f);
                dir.rotate(Math.toRadians(angle), Vector3.RotAxis.Y_AXIS);
                dir.multiply(new Vector3(0.2f, 0.1f, 0.2f));
                renderStack.func_227860_a_();
                renderStack.func_227861_a_(dir.getX(), dir.getY(), dir.getZ());
                renderStack.func_227862_a_(0.09f, 0.09f, 0.09f);
                RenderingUtils.draw(7, DefaultVertexFormat.field_227852_q_, buf -> RenderingDrawUtils.renderTexturedCubeCentralColorLighted((VertexConsumer)buf, renderStack, tas.func_94209_e(), tas.func_94206_g(), tas.func_94212_f() - tas.func_94209_e(), tas.func_94210_h() - tas.func_94206_g(), 255, 255, 255, alphaMultiplier, LightmapUtil.getPackedLightCoords((IBlockDisplayReader)player.func_130014_f_(), player.func_233580_cy_())));
                renderStack.func_227865_b_();
            }
        }
        Blending.DEFAULT.apply();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableTexture();
    }
}
