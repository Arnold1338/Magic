package hellfirepvp.astralsorcery.common.base.patreon.types;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.render.ObjModelRender;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import org.joml.Vector3f;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraft.world.level.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.IEventBus;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;

public class TypeWraithWings extends PatreonEffect
{
    private final UUID playerUUID;
    
    public TypeWraithWings(final UUID effectUUID, @Nullable final FlareColor flareColor, final UUID playerUUID) {
        super(effectUUID, flareColor);
        this.playerUUID = playerUUID;
    }
    
    @Override
    public void attachEventListeners(final IEventBus bus) {
        super.attachEventListeners(bus);
        bus.register((Object)this);
    }
    
    private boolean shouldDoEffect(final Player player) {
        return player.getUUID().equals(this.playerUUID) && !player.isInWater() && !player.func_184613_cA() && !player.hasEffect(Effects.field_76441_p);
    }
    
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    void onRender(final RenderPlayerEvent.Post event) {
        final Player player = event.getPlayer();
        if (!this.shouldDoEffect(player)) {
            return;
        }
        final PoseStack renderStack = event.getMatrixStack();
        final float rot = RenderingVectorUtils.interpolateRotation(player.field_70760_ar, player.field_70761_aq, event.getPartialRenderTick());
        float yOffset = 1.2f;
        if (player.func_225608_bj_() && !player.field_71075_bZ.field_75100_b) {
            yOffset = 1.0f;
        }
        renderStack.popPose();
        final float swimAngle = player.func_205015_b(event.getPartialRenderTick());
        if (swimAngle > 0.0f) {
            final float waterPitch = player.func_70090_H() ? (-90.0f - player.xRot) : -90.0f;
            final float bodySwimAngle = Mth.func_219799_g(swimAngle, 0.0f, waterPitch);
            renderStack.mulPose(Vector3f.field_229181_d_.getMultiBufferSource()180.0f - rot));
            renderStack.mulPose(Vector3f.field_229179_b_.getMultiBufferSource()bodySwimAngle));
            if (player.func_213314_bj()) {
                renderStack.func_227861_a_(0.0, -1.0, 0.3);
            }
        }
        else {
            renderStack.mulPose(Vector3f.field_229181_d_.getMultiBufferSource()180.0f - rot));
        }
        renderStack.func_227861_a_(0.0, (double)yOffset, 0.0);
        renderStack.translate(0.32f, 0.32f, 0.32f);
        RenderTypesAS.MODEL_WRAITH_WINGS.func_228547_a_();
        renderStack.popPose();
        renderStack.func_227861_a_(-2.3, 0.0, 0.8);
        renderStack.mulPose(Vector3f.field_229181_d_.getMultiBufferSource()10.0f));
        ObjModelRender.renderWraithWings(renderStack);
        renderStack.scale();
        renderStack.popPose();
        renderStack.mulPose(Vector3f.field_229181_d_.getMultiBufferSource()180.0f));
        renderStack.func_227861_a_(-2.3, 0.0, -0.8);
        renderStack.mulPose(Vector3f.field_229180_c_.getMultiBufferSource()10.0f));
        ObjModelRender.renderWraithWings(renderStack);
        renderStack.scale();
        RenderTypesAS.MODEL_WRAITH_WINGS.func_228549_b_();
        renderStack.scale();
    }
}
