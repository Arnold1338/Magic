package hellfirepvp.astralsorcery.common.network.play.server;

import hellfirepvp.astralsorcery.common.tile.TileFountain;
import hellfirepvp.astralsorcery.common.tile.TileChalice;
import hellfirepvp.astralsorcery.common.tile.TileCelestialGateway;
import hellfirepvp.astralsorcery.common.tile.TileTreeBeacon;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;
import hellfirepvp.astralsorcery.common.starlight.network.handler.BlockTransmutationHandler;
import hellfirepvp.astralsorcery.common.tile.TileInfuser;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import hellfirepvp.astralsorcery.common.util.CelestialStrike;
import hellfirepvp.astralsorcery.common.item.lens.ItemColoredLensFire;
import hellfirepvp.astralsorcery.common.auxiliary.BlockBreakHelper;
import hellfirepvp.astralsorcery.common.util.time.TimeStopEffectHelper;
import hellfirepvp.astralsorcery.common.constellation.effect.aoe.CEffectAevitas;
import hellfirepvp.astralsorcery.common.item.wand.ItemWand;
import hellfirepvp.astralsorcery.client.util.MiscPlayEffect;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktPlayEffect extends ASPacket<PktPlayEffect>
{
    private Type type;
    private Consumer<FriendlyByteBuf> encoder;
    private FriendlyByteBuf data;
    
    public PktPlayEffect() {
        this.encoder = (buf -> {});
        this.data = null;
    }
    
    public PktPlayEffect(final Type type) {
        this.encoder = (buf -> {});
        this.data = null;
        this.type = type;
    }
    
    public PktPlayEffect addData(final Consumer<FriendlyByteBuf> encoder) {
        this.encoder = this.encoder.andThen(encoder);
        return this;
    }
    
    public FriendlyByteBuf getExtraData() {
        return this.data;
    }
    
    @Nonnull
    @Override
    public Encoder<PktPlayEffect> encoder() {
        return (packet, buffer) -> {
            ByteBufUtils.writeEnumValue(buffer, packet.type);
            packet.encoder.accept(buffer);
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktPlayEffect> decoder() {
        return (Decoder<PktPlayEffect>)(buffer -> {
            final Type type = ByteBufUtils.readEnumValue(buffer, Type.class);
            final PktPlayEffect pkt = new PktPlayEffect(type);
            final ByteBuf buf = Unpooled.buffer(buffer.readableBytes());
            buffer.readBytes(buf);
            pkt.data = new FriendlyByteBuf(buf);
            return pkt;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktPlayEffect> handler() {
        return new Handler<PktPlayEffect>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void handleClient(final PktPlayEffect packet, final NetworkEvent.Context context) {
                context.enqueueWork(() -> packet.type.runEffect().accept(packet));
            }
            
            @Override
            public void handle(final PktPlayEffect packet, final NetworkEvent.Context context, final LogicalSide side) {
            }
        };
    }
    
    public enum Type
    {
        LIGHTNING, 
        BEAM_BREAK, 
        BLOCK_EFFECT, 
        BLOCK_EFFECT_TUMBLE, 
        ROCK_CRYSTAL_COLUMN, 
        ROCK_CRYSTAL_SPARKS, 
        SMALL_CRYSTAL_BREAK, 
        GEM_CRYSTAL_BREAK, 
        CROP_GROWTH, 
        MELT_BLOCK, 
        CELESTIAL_STRIKE, 
        ALTAR_RECIPE_FINISH, 
        INFUSER_RECIPE_FINISH, 
        BLOCK_TRANSMUTATION_TICK, 
        TIME_FREEZE_EFFECT, 
        LIQUID_FOUNTAIN, 
        CONSTELLATION_EFFECT_PING, 
        BLOCK_HARVEST_DRAW, 
        GATEWAY_REVOKE_EFFECT, 
        LIQUID_INTERACTION_LINE, 
        FOUNTAIN_TRANSITION_SEGMENT, 
        FOUNTAIN_REPLACE_EFFECT;
        
        @OnlyIn(Dist.CLIENT)
        private Consumer<PktPlayEffect> runEffect() {
            switch (this) {
                case LIGHTNING: {
                    return MiscPlayEffect::fireLightning;
                }
                case ROCK_CRYSTAL_COLUMN: {
                    return ItemWand::playEffect;
                }
                case ROCK_CRYSTAL_SPARKS: {
                    return ItemWand::playUndergroundEffect;
                }
                case SMALL_CRYSTAL_BREAK: {
                    return MiscPlayEffect::catalystBurst;
                }
                case GEM_CRYSTAL_BREAK: {
                    return MiscPlayEffect::gemCrystalBurst;
                }
                case CROP_GROWTH: {
                    return CEffectAevitas::playParticles;
                }
                case TIME_FREEZE_EFFECT: {
                    return TimeStopEffectHelper::playEntityParticles;
                }
                case BEAM_BREAK: {
                    return BlockBreakHelper::blockBreakAnimation;
                }
                case BLOCK_EFFECT: {
                    return MiscPlayEffect::playBlockEffects;
                }
                case BLOCK_EFFECT_TUMBLE: {
                    return MiscPlayEffect::playTumbleBlockEffects;
                }
                case MELT_BLOCK: {
                    return ItemColoredLensFire::playParticles;
                }
                case CELESTIAL_STRIKE: {
                    return CelestialStrike::playEffect;
                }
                case ALTAR_RECIPE_FINISH: {
                    return TileAltar::finishCraftingEffects;
                }
                case INFUSER_RECIPE_FINISH: {
                    return TileInfuser::finishCraftingEffects;
                }
                case BLOCK_TRANSMUTATION_TICK: {
                    return BlockTransmutationHandler::playTransmutation;
                }
                case LIQUID_FOUNTAIN: {
                    return MiscPlayEffect::liquidFountain;
                }
                case CONSTELLATION_EFFECT_PING: {
                    return ConstellationEffect::playConstellationPing;
                }
                case BLOCK_HARVEST_DRAW: {
                    return TileTreeBeacon::playDrawParticles;
                }
                case GATEWAY_REVOKE_EFFECT: {
                    return TileCelestialGateway::playAccessRevokeEffect;
                }
                case LIQUID_INTERACTION_LINE: {
                    return TileChalice::drawLiquidLine;
                }
                case FOUNTAIN_TRANSITION_SEGMENT: {
                    return TileFountain::playTransitionEffect;
                }
                case FOUNTAIN_REPLACE_EFFECT: {
                    return TileFountain::replaceEffect;
                }
                default: {
                    return pkt -> {};
                }
            }
        }
    }
}
