package hellfirepvp.astralsorcery.common.network.play.server;

import java.util.Random;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import java.util.Optional;
import net.minecraftforge.fml.network.NetworkEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktShootEntity extends ASPacket<PktShootEntity>
{
    private int entityId;
    private Vector3 motionVector;
    private boolean hasEffect;
    private float effectLength;
    
    public PktShootEntity() {
        this.entityId = -1;
        this.motionVector = null;
        this.hasEffect = false;
        this.effectLength = 0.0f;
    }
    
    public PktShootEntity(final int entityId, final Vector3 motionVector) {
        this.entityId = -1;
        this.motionVector = null;
        this.hasEffect = false;
        this.effectLength = 0.0f;
        this.entityId = entityId;
        this.motionVector = motionVector;
    }
    
    public PktShootEntity setEffectLength(final float length) {
        this.hasEffect = true;
        this.effectLength = length;
        return this;
    }
    
    @Nonnull
    @Override
    public Encoder<PktShootEntity> encoder() {
        return (packet, buffer) -> {
            buffer.writeInt(packet.entityId);
            ByteBufUtils.writeOptional(buffer, packet.motionVector, ByteBufUtils::writeVector);
            buffer.writeBoolean(packet.hasEffect);
            buffer.writeFloat(packet.effectLength);
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktShootEntity> decoder() {
        return (Decoder<PktShootEntity>)(buffer -> {
            final PktShootEntity shootEntity = new PktShootEntity(buffer.readInt(), ByteBufUtils.readOptional(buffer, ByteBufUtils::readVector));
            shootEntity.hasEffect = buffer.readBoolean();
            shootEntity.effectLength = buffer.readFloat();
            return shootEntity;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktShootEntity> handler() {
        return new Handler<PktShootEntity>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktShootEntity packet, final NetworkEvent.Context context) {
                context.enqueueWork(() -> {
                    final Optional world = (Optional)Optional.ofNullable(Minecraft.getInstance().level);
                    final Entity entity = world.map(w -> w.getEntityById(packet.entityId)).orElse(null);
                    if (entity != null) {
                        entity.func_213317_d(packet.motionVector.toVector3d());
                        if (packet.hasEffect) {
                            final Vector3 origin = Vector3.atEntityCenter(entity).setY(entity.getY() + entity.func_213302_cg());
                            final Vector3 look = new Vector3(entity.func_70040_Z()).normalize().multiply(packet.effectLength * 18.0f);
                            final Vector3 motionReverse = look.clone().normalize().multiply(-0.4 * packet.effectLength);
                            final Vector3 perp = look.clone().perpendicular().normalize().multiply(6.0f);
                            for (int i = 0; i < 300; ++i) {
                                final Vector3 at = look.clone().multiply(0.5f + PktShootEntity.rand.nextFloat() * 2.0f).add(perp.clone().rotate(PktShootEntity.rand.nextFloat() * 360.0f, look).multiply(0.5f + PktShootEntity.rand.nextFloat())).add(origin);
                                final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).alpha(VFXAlphaFunction.FADE_OUT).setAlphaMultiplier(0.75f).setScaleMultiplier(0.7f + PktShootEntity.rand.nextFloat() * 0.35f).setMaxAge(20 + PktShootEntity.rand.nextInt(15));
                                if (PktShootEntity.rand.nextBoolean()) {
                                    p.color(VFXColorFunction.WHITE).setScaleMultiplier(0.3f + PktShootEntity.rand.nextFloat() * 0.15f);
                                }
                                else {
                                    p.color(VFXColorFunction.constant(ColorsAS.CONSTELLATION_VICIO));
                                }
                                p.setMotion(motionReverse);
                            }
                        }
                    }
                });
            }
            
            @Override
            public void handle(final PktShootEntity packet, final NetworkEvent.Context context, final LogicalSide side) {
            }
        };
    }
}
