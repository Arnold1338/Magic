package hellfirepvp.astralsorcery.common.base.patreon.types;

import java.util.EnumSet;
import hellfirepvp.astralsorcery.client.render.ObjModelRender;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import org.joml.Vector3f;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraft.world.level.effect.MobEffects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import java.awt.Color;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import java.util.UUID;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;

public class TypeCelestialWings extends PatreonEffect implements ITickHandler
{
    private final UUID playerUUID;
    
    public TypeCelestialWings(final UUID effectUUID, @Nullable final FlareColor flareColor, final UUID playerUUID) {
        super(effectUUID, flareColor);
        this.playerUUID = playerUUID;
    }
    
    @Override
    public void attachEventListeners(final IEventBus bus) {
        super.attachEventListeners(bus);
        bus.register((Object)this);
    }
    
    @Override
    public void attachTickListeners(final Consumer<ITickHandler> registrar) {
        super.attachTickListeners(registrar);
        registrar.accept((ITickHandler)this);
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final Player player = (Player)context[0];
        final LogicalSide side = (LogicalSide)context[1];
        if (side.isClient() && this.shouldDoEffect(player) && Minecraft.getInstance().player != null && Minecraft.getInstance().player.getUUID().equals(this.playerUUID) && !Minecraft.getInstance().options.func_243230_g().func_243192_a()) {
            this.playEffects(player);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playEffects(final Player player) {
        final float rot = RenderingVectorUtils.interpolateRotation(player.field_70760_ar, player.field_70761_aq, 0.0f);
        float yOffset = 1.3f;
        if (player.func_225608_bj_()) {
            yOffset = 1.0f;
        }
        final float f = Math.abs(ClientScheduler.getSystemClientTick() % 240L - 120.0f) / 120.0f;
        final double offset = Math.cos(f * 2.0f * 3.141592653589793) * 0.03;
        final Vector3 look = new Vector3(1, 0, 0).rotate(Math.toRadians(360.0f - rot), Vector3.RotAxis.Y_AXIS).normalize();
        final Vector3 pos = Vector3.atEntityCorner((Entity)player);
        pos.setY(player.getY() + yOffset + offset);
        for (int i = 0; i < 4; ++i) {
            final double height = -0.1 + Math.min(TypeCelestialWings.rand.nextFloat() * 1.3, TypeCelestialWings.rand.nextFloat() * 1.3);
            final double distance = 1.2000000476837158 - TypeCelestialWings.rand.nextFloat() * 0.6 * (1.0 - Math.max(0.0, height));
            final Vector3 dir = look.clone().rotate(Math.toRadians(180 * (TypeCelestialWings.rand.nextBoolean() ? 1 : 0)), Vector3.RotAxis.Y_AXIS).normalize().multiply(distance);
            final Vector3 at = pos.clone().addY(height).add(dir);
            final Color col = Color.getHSBColor(0.68f, 1.0f, 0.6f - TypeCelestialWings.rand.nextFloat() * 0.5f);
            final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).color(VFXColorFunction.constant(col)).setScaleMultiplier(0.27f + TypeCelestialWings.rand.nextFloat() * 0.1f).alpha(VFXAlphaFunction.FADE_OUT).setMaxAge(25 + TypeCelestialWings.rand.nextInt(20));
            if (TypeCelestialWings.rand.nextInt(4) == 0) {
                p.setScaleMultiplier(0.09f + TypeCelestialWings.rand.nextFloat() * 0.02f).color(VFXColorFunction.WHITE).setMaxAge(10 + TypeCelestialWings.rand.nextInt(8));
            }
            else {
                p.setGravityStrength(3.0E-4f);
            }
        }
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
        this.renderWings(player, event.getMatrixStack(), event.getPartialRenderTick());
    }
    
    @OnlyIn(Dist.CLIENT)
    private void renderWings(final Player player, final PoseStack renderStack, final float pTicks) {
        final float rot = RenderingVectorUtils.interpolateRotation(player.field_70760_ar, player.field_70761_aq, pTicks);
        float yOffset = 1.3f;
        if (player.func_225608_bj_() && !player.field_71075_bZ.field_75100_b) {
            yOffset = 1.0f;
        }
        final float f = Math.abs(ClientScheduler.getSystemClientTick() % 240L - 120.0f) / 120.0f;
        final double offset = Math.cos(f * 2.0f * 3.141592653589793) * 0.03;
        renderStack.popPose();
        renderStack.translate(0.0, yOffset + offset, 0.0);
        renderStack.mulPose(new org.joml.Quaternionf().rotateY((float)Math.toRadians(180.0f - rot)));
        renderStack.translate(0.02f, 0.02f, 0.02f);
        RenderTypesAS.MODEL_CELESTIAL_WINGS.func_228547_a_();
        renderStack.translate(-25.0, 0.0, 0.0);
        ObjModelRender.renderCelestialWings(renderStack);
        renderStack.mulPose(new org.joml.Quaternionf().rotateY((float)Math.toRadians(180.0f)));
        renderStack.translate(-50.0, 0.0, 0.0);
        ObjModelRender.renderCelestialWings(renderStack);
        renderStack.popPose();
        RenderTypesAS.MODEL_CELESTIAL_WINGS.func_228549_b_();
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.PLAYER);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "PatreonEffect - Celestial Wings " + this.playerUUID.toString();
    }
}
