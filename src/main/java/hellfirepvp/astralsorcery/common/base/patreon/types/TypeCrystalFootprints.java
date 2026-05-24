package hellfirepvp.astralsorcery.common.base.patreon.types;

import java.util.EnumSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXCrystal;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import java.awt.Color;
import java.util.UUID;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;

public class TypeCrystalFootprints extends PatreonEffect implements ITickHandler
{
    private final UUID playerUUID;
    private final Color color;
    
    public TypeCrystalFootprints(final UUID effectUUID, @Nullable final FlareColor flareColor, final UUID playerUUID, final Color color) {
        super(effectUUID, flareColor);
        this.playerUUID = playerUUID;
        this.color = color;
    }
    
    @Override
    public void attachTickListeners(final Consumer<ITickHandler> registrar) {
        super.attachTickListeners(registrar);
        registrar.accept((ITickHandler)this);
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final Player player = (Player)context[0];
        final LogicalSide side = (LogicalSide)context[1];
        if (side.isClient() && this.shouldDoEffect(player) && TypeCrystalFootprints.rand.nextInt(3) == 0) {
            this.spawnFootprint(player);
        }
    }
    
    private boolean shouldDoEffect(final Player player) {
        return player.getUUID().equals(this.playerUUID) && !player.func_70644_a(Effects.field_76441_p) && player.func_233570_aj_();
    }
    
    @OnlyIn(Dist.CLIENT)
    private void spawnFootprint(final Player player) {
        final Vector3 pos = Vector3.atEntityCorner((Entity)player).subtract(player.func_213311_cf() / 2.0f, 0.1, player.func_213311_cf() / 2.0f).add(player.func_213311_cf() * TypeCrystalFootprints.rand.nextFloat(), 0.0f, player.func_213311_cf() * TypeCrystalFootprints.rand.nextFloat());
        if (player.func_130014_f_().isEmptyBlock(pos.toBlockPos())) {
            return;
        }
        EffectHelper.of(EffectTemplatesAS.CRYSTAL).spawn(pos).rotation(TypeCrystalFootprints.rand.nextFloat() * 35.0f * (TypeCrystalFootprints.rand.nextBoolean() ? 1 : -1), TypeCrystalFootprints.rand.nextFloat() * 35.0f * (TypeCrystalFootprints.rand.nextBoolean() ? 1 : -1), TypeCrystalFootprints.rand.nextFloat() * 35.0f * (TypeCrystalFootprints.rand.nextBoolean() ? 1 : -1)).color(VFXColorFunction.constant(this.color)).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.025f + TypeCrystalFootprints.rand.nextFloat() * 0.03f).setMaxAge(60 + TypeCrystalFootprints.rand.nextInt(30));
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.PLAYER);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "PatreonEffect - Crystal Footprints " + this.playerUUID.toString();
    }
}
