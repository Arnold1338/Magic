package hellfirepvp.astralsorcery.common.base.patreon.types;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.render.ObjModelRender;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import com.mojang.math.Vector3f;
import net.minecraft.util.math.MathHelper;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraft.world.effect.MobEffects;
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
        return player.getUUID().equals(this.playerUUID) && !player.func_184218_aH() && !player.func_184613_cA() && !player.func_70644_a(Effects.field_76441_p);
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
        renderStack.func_227860_a_();
        final float swimAngle = player.func_205015_b(event.getPartialRenderTick());
        if (swimAngle > 0.0f) {
            final float waterPitch = player.func_70090_H() ? (-90.0f - player.field_70125_A) : -90.0f;
            final float bodySwimAngle = MathHelper.func_219799_g(swimAngle, 0.0f, waterPitch);
            renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(180.0f - rot));
            renderStack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(bodySwimAngle));
            if (player.func_213314_bj()) {
                renderStack.func_227861_a_(0.0, -1.0, 0.3);
            }
        }
        else {
            renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(180.0f - rot));
        }
        renderStack.func_227861_a_(0.0, (double)yOffset, 0.0);
        renderStack.func_227862_a_(0.32f, 0.32f, 0.32f);
        RenderTypesAS.MODEL_WRAITH_WINGS.func_228547_a_();
        renderStack.func_227860_a_();
        renderStack.func_227861_a_(-2.3, 0.0, 0.8);
        renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(10.0f));
        ObjModelRender.renderWraithWings(renderStack);
        renderStack.func_227865_b_();
        renderStack.func_227860_a_();
        renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(180.0f));
        renderStack.func_227861_a_(-2.3, 0.0, -0.8);
        renderStack.func_227863_a_(Vector3f.field_229180_c_.func_229187_a_(10.0f));
        ObjModelRender.renderWraithWings(renderStack);
        renderStack.func_227865_b_();
        RenderTypesAS.MODEL_WRAITH_WINGS.func_228549_b_();
        renderStack.func_227865_b_();
    }
}
