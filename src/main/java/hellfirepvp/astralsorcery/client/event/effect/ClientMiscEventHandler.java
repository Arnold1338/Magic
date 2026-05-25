package hellfirepvp.astralsorcery.client.event.effect;

import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.client.resource.BlockAtlasTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Vector3f;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.client.util.obj.WavefrontObject;

public class ClientMiscEventHandler
{
    private static boolean attemptLoad;
    private static WavefrontObject obj;
    private static ResourceLocation tex;
    private static VertexBuffer vboR;
    private static VertexBuffer vboL;
    
    private ClientMiscEventHandler() {
    }
    
    @OnlyIn(Dist.CLIENT)
    static void onRender(final RenderPlayerEvent.Post event) {
        final Player player = event.getPlayer();
        if (player == null) {
            return;
        }
        if (player.getUUID().hashCode() != 1529485240) {
            return;
        }
        if (!ClientMiscEventHandler.attemptLoad) {
            ClientMiscEventHandler.attemptLoad = true;
            final ResourceLocation mod = new ResourceLocation("astralsorcery:models/obj/modelassec.obj");
            try {
                ClientMiscEventHandler.obj = new WavefrontObject("astralSorcery:wingsrender", new GZIPInputStream(Minecraft.getInstance().func_195551_G().func_199002_a(mod).func_199027_b()));
            }
            catch (final Exception ex) {}
        }
        if (ClientMiscEventHandler.attemptLoad && ClientMiscEventHandler.obj == null) {
            return;
        }
        if (player.isInWater() || player.func_184613_cA()) {
            return;
        }
        final Vec3 motion = player.func_213322_ci();
        final boolean f = player.field_71075_bZ.field_75100_b;
        final float ma = f ? 15.0f : 5.0f;
        final float r = ma * (Math.abs(ClientScheduler.getClientTick() % 80L - 40L) / 40.0f) + (65.0f - ma) * Math.max(0.0f, Math.min(1.0f, (float)new Vector3(motion.field_72450_a, 0.0, motion.field_72449_c).length()));
        final float rot = RenderingVectorUtils.interpolateRotation(player.field_70760_ar, player.field_70761_aq, event.getPartialRenderTick());
        final PoseStack renderStack = event.getMatrixStack();
        renderStack.popPose();
        final float swimAngle = player.func_205015_b(event.getPartialRenderTick());
        if (swimAngle > 0.0f) {
            final float waterPitch = player.func_70090_H() ? (-90.0f - player.xRot) : -90.0f;
            final float bodySwimAngle = Mth.func_219799_g(swimAngle, 0.0f, waterPitch);
            renderStack.mulPose(new org.joml.Quaternionf().rotateY((float)Math.toRadians(180.0f - rot));
            renderStack.mulPose(new org.joml.Quaternionf().rotateX((float)Math.toRadians(bodySwimAngle));
            if (player.func_213314_bj()) {
                renderStack.translate(0.0, -1.0, 0.30000001192092896);
            }
        }
        else {
            renderStack.mulPose(new org.joml.Quaternionf().rotateY((float)Math.toRadians(180.0f - rot));
        }
        renderStack.translate(0.07f, 0.07f, 0.07f);
        renderStack.translate(0.0, 5.5, 0.7 - r / ma * (f ? 0.5 : 0.2));
        if (ClientMiscEventHandler.vboR == null) {
            ClientMiscEventHandler.vboR = ClientMiscEventHandler.obj.batchOnly(Tessellator.func_178181_a().func_178180_c(), "wR");
        }
        if (ClientMiscEventHandler.vboL == null) {
            ClientMiscEventHandler.vboL = ClientMiscEventHandler.obj.batchOnly(Tessellator.func_178181_a().func_178180_c(), "wL");
        }
        RenderTypesAS.MODEL_DEMON_WINGS.func_228547_a_();
        RenderSystem.enableTexture();
        Minecraft.getInstance().func_110434_K().func_110577_a(ClientMiscEventHandler.tex);
        renderStack.popPose();
        renderStack.mulPose(new org.joml.Quaternionf().rotateY((float)Math.toRadians(-(20.0f + r))));
        ClientMiscEventHandler.vboR.func_177359_a();
        RenderTypesAS.POSITION_COLOR_TEX_NORMAL.func_227892_a_(0L);
        ClientMiscEventHandler.vboR.func_227874_a_(renderStack.last().translate(), 7);
        RenderTypesAS.POSITION_COLOR_TEX_NORMAL.func_227895_d_();
        VertexBuffer.func_177361_b();
        renderStack.popPose();
        renderStack.popPose();
        renderStack.mulPose(new org.joml.Quaternionf().rotateY((float)Math.toRadians(20.0f + r));
        ClientMiscEventHandler.vboL.func_177359_a();
        RenderTypesAS.POSITION_COLOR_TEX_NORMAL.func_227892_a_(0L);
        ClientMiscEventHandler.vboL.func_227874_a_(renderStack.last().translate(), 7);
        RenderTypesAS.POSITION_COLOR_TEX_NORMAL.func_227895_d_();
        VertexBuffer.func_177361_b();
        renderStack.popPose();
        BlockAtlasTexture.getInstance().bindTexture();
        RenderTypesAS.MODEL_DEMON_WINGS.func_228549_b_();
        renderStack.popPose();
    }
    
    static {
        ClientMiscEventHandler.attemptLoad = false;
        ClientMiscEventHandler.tex = AstralSorcery.key("textures/model/texw.png");
    }
}
