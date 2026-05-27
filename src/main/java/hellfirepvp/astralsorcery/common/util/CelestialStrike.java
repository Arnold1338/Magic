package hellfirepvp.astralsorcery.common.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightning;
import hellfirepvp.astralsorcery.client.effect.vfx.FXSpritePlane;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightbeam;
import java.util.Random;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ISeedReader;
import hellfirepvp.astralsorcery.common.util.world.SkyCollectionHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.damagesource.DamageSource;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.util.EntityPredicates;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

public class CelestialStrike
{
    private static final AABB EMPTY;
    
    private CelestialStrike() {
    }
    
    public static void play(@Nullable final LivingEntity attacker, final ServerLevel world, final Vector3 at, final Vector3 displayPosition) {
        final double radius = 16.0;
        final List<LivingEntity> livingEntities = world.func_175647_a((Class)LivingEntity.class, CelestialStrike.EMPTY.func_72314_b(radius, radius / 2.0, radius).func_186670_a(at.toBlockPos()), EntityPredicates.field_94557_a);
        if (attacker != null) {
            livingEntities.remove(attacker);
        }
        DamageSource ds = CommonProxy.DAMAGE_SOURCE_STELLAR;
        if (attacker != null) {
            ds = DamageSource.func_76358_a(attacker);
            if (attacker instanceof Player) {
                ds = DamageSource.func_76365_a((Player)attacker);
            }
        }
        float dmg = 25.0f;
        dmg += SkyCollectionHelper.getSkyNoiseDistribution((ISeedReader)world, at.toBlockPos()) * 10.0f;
        for (final LivingEntity living : livingEntities) {
            if (living instanceof Player) {
                if (living.func_175149_v() || ((Player)living).getVehicle()) {
                    continue;
                }
                if (attacker != null && living.func_184191_r((Entity)attacker)) {
                    continue;
                }
            }
            float dstPerc = (float)(Vector3.atEntityCenter((Entity)living).distance(at) / radius);
            dstPerc = 1.0f - Mth.canEnchant(dstPerc, 0.0f, 1.0f);
            final float dmgDealt = dstPerc * dmg;
            if (dmgDealt > 0.5) {
                DamageUtil.attackEntityFrom((Entity)living, ds, dmgDealt);
                if (attacker == null) {
                    continue;
                }
                final int fireAspectLevel = EnchantmentHelper.func_185284_a(Enchantments.field_77334_n, attacker);
                if (fireAspectLevel <= 0 || living.func_70027_ad()) {
                    continue;
                }
                living.setAge(fireAspectLevel * 4);
            }
        }
        final PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.Type.CELESTIAL_STRIKE).addData(buf -> ByteBufUtils.writeVector(buf, displayPosition));
        PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos((Level)world, (Vec3i)at.toBlockPos(), 96.0));
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playEffect(final PktPlayEffect effect) {
        final Random r = new Random();
        final Vector3 vec = ByteBufUtils.readVector(effect.getExtraData());
        final Vector3 effectPos = vec.clone();
        EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(effectPos.clone().addY(-4.0)).setup(effectPos.clone().addY(16.0), 9.0, 6.0).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.WHITE).setAlphaMultiplier(1.0f).setMaxAge(25);
        effectPos.add(r.nextFloat() - r.nextFloat(), 0.0f, r.nextFloat() - r.nextFloat());
        EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(effectPos.clone().addY(-4.0)).setup(effectPos.clone().addY(16.0).addY(r.nextFloat() * 2.0f), 9.0, 6.0).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.constant(ColorsAS.EFFECT_BLUE_LIGHT)).setAlphaMultiplier(1.0f).setMaxAge(24 + r.nextInt(6));
        effectPos.add(r.nextFloat() - r.nextFloat(), 0.0f, r.nextFloat() - r.nextFloat());
        EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(effectPos.clone().addY(-4.0)).setup(effectPos.clone().addY(16.0).addY(r.nextFloat() * 2.0f), 9.0, 6.0).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.constant(ColorsAS.EFFECT_BLUE_DARK)).setAlphaMultiplier(1.0f).setMaxAge(24 + r.nextInt(6));
        final AbstractRenderableTexture tex = MiscUtils.eitherOf(r, new AbstractRenderableTexture[] { TexturesAS.TEX_SMOKE_1, TexturesAS.TEX_SMOKE_2, TexturesAS.TEX_SMOKE_3, TexturesAS.TEX_SMOKE_4 });
        EffectHelper.of(EffectTemplatesAS.TEXTURE_SPRITE).spawn(vec.clone().addY(0.10000000149011612)).setAxis(Vector3.RotAxis.Y_AXIS.clone().negate()).setSprite(tex).setNoRotation(r.nextFloat() * 360.0f).setAlphaMultiplier(0.4f).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(17.0f).setMaxAge(30 + r.nextInt(10));
        for (int i = 0; i < 43; ++i) {
            final Vector3 randTo = new Vector3(r.nextDouble() * 9.0 - r.nextDouble() * 9.0, r.nextDouble() * 5.0, r.nextDouble() * 9.0 - r.nextDouble() * 9.0);
            randTo.add(vec.clone());
            final FXLightning lightning = EffectHelper.of(EffectTemplatesAS.LIGHTNING).spawn(vec.clone()).makeDefault(randTo);
            lightning.color(MiscUtils.eitherOf(r, (VFXColorFunction[])new VFXColorFunction[] { VFXColorFunction.constant(Color.WHITE), VFXColorFunction.constant(ColorsAS.EFFECT_BLUE_LIGHT), VFXColorFunction.constant(ColorsAS.EFFECT_BLUE_DARK) }));
        }
        for (int i = 0; i < 40; ++i) {
            final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(vec.clone().add((r.nextFloat() - r.nextFloat()) * 4.0f, r.nextFloat() * 9.0f, (r.nextFloat() - r.nextFloat()) * 4.0f)).setGravityStrength(-0.005f).setScaleMultiplier(0.85f).setMaxAge(14 + r.nextInt(6));
            p.color(MiscUtils.eitherOf(r, (VFXColorFunction[])new VFXColorFunction[] { VFXColorFunction.constant(Color.WHITE), VFXColorFunction.constant(ColorsAS.EFFECT_BLUE_LIGHT), VFXColorFunction.constant(ColorsAS.EFFECT_BLUE_DARK) }));
        }
        List<Vector3> circle = MiscUtils.getCirclePositions(vec, Vector3.RotAxis.Y_AXIS, 7.5f + r.nextFloat(), 200 + r.nextInt(40));
        for (final Vector3 at : circle) {
            final Vector3 dir = at.clone().subtract(vec).normalize().multiply(0.3 + 0.4 * r.nextFloat());
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).setAlphaMultiplier(0.4f).setMotion(dir).color(VFXColorFunction.constant(ColorsAS.EFFECT_BLUE_LIGHT)).setScaleMultiplier(1.2f).setMaxAge(14 + r.nextInt(6));
        }
        circle = MiscUtils.getCirclePositions(vec, Vector3.RotAxis.Y_AXIS, 7.5f + r.nextFloat(), 100 + r.nextInt(40));
        for (final Vector3 at : circle) {
            final Vector3 dir = at.clone().subtract(vec).normalize().multiply(0.2 + 0.1 * r.nextFloat());
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).setAlphaMultiplier(0.4f).setMotion(dir).color(VFXColorFunction.constant(ColorsAS.EFFECT_BLUE_DARK)).setScaleMultiplier(1.5f).setMaxAge(14 + r.nextInt(6));
        }
    }
    
    static {
        EMPTY = new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }
}
