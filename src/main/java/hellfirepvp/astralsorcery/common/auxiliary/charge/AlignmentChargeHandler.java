package hellfirepvp.astralsorcery.common.auxiliary.charge;

import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import java.util.EnumSet;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyChargeBalancing;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.network.play.server.PktSyncCharge;
import net.minecraft.util.Mth;
import java.util.HashMap;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraft.world.entity.player.Player;
import java.util.UUID;
import net.minecraftforge.fml.LogicalSide;
import java.util.Map;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class AlignmentChargeHandler implements ITickHandler
{
    public static final AlignmentChargeHandler INSTANCE;
    private static final float MAX_CHARGE = 1000.0f;
    private static final Map<LogicalSide, Map<UUID, Float>> maximumCharge;
    private static final Map<LogicalSide, Map<UUID, Float>> currentCharge;
    
    private AlignmentChargeHandler() {
    }
    
    public void updateMaximum(final Player player, final LogicalSide side) {
        float cap = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, ResearchHelper.getProgress(player, side), PerkAttributeTypesAS.ATTR_TYPE_ALIGNMENT_CHARGE_MAXIMUM, 1000.0f);
        cap = AttributeEvent.postProcessModded(player, PerkAttributeTypesAS.ATTR_TYPE_ALIGNMENT_CHARGE_MAXIMUM, cap);
        cap = Math.max(0.0f, cap);
        AlignmentChargeHandler.maximumCharge.computeIfAbsent(side, s -> new HashMap()).put(player.getUUID(), cap);
        if (this.getCurrentCharge(player, side) > cap) {
            AlignmentChargeHandler.currentCharge.computeIfAbsent(side, s -> new HashMap()).put(player.getUUID(), cap);
        }
    }
    
    public float getMaximumCharge(final Player player, final LogicalSide side) {
        return AlignmentChargeHandler.maximumCharge.computeIfAbsent(side, s -> new HashMap()).computeIfAbsent(player.getUUID(), uuid -> 1000.0f);
    }
    
    public float getCurrentCharge(final Player player, final LogicalSide side) {
        if (player.getVehicle() || player.func_175149_v()) {
            return this.getMaximumCharge(player, side);
        }
        return AlignmentChargeHandler.currentCharge.computeIfAbsent(side, s -> new HashMap()).computeIfAbsent(player.getUUID(), uuid -> 1000.0f);
    }
    
    public float getFilledPercentage(final Player player, final LogicalSide side) {
        if (player.getVehicle() || player.func_175149_v()) {
            return 1.0f;
        }
        final float max = this.getMaximumCharge(player, side);
        final float current = this.getCurrentCharge(player, side);
        return Mth.canEnchant(current / max, 0.0f, 1.0f);
    }
    
    public boolean hasCharge(final Player player, final LogicalSide side, final float charge) {
        if (player.getVehicle() || player.func_175149_v()) {
            return true;
        }
        final float current = this.getCurrentCharge(player, side);
        return current >= charge;
    }
    
    public boolean drainCharge(final Player player, final LogicalSide side, final float charge, final boolean simulate) {
        if (player.getVehicle() || player.func_175149_v()) {
            return true;
        }
        if (!this.hasCharge(player, side, charge)) {
            return false;
        }
        final float current = this.getCurrentCharge(player, side);
        final float result = current - charge;
        if (result < 0.0f) {
            return false;
        }
        if (!simulate) {
            AlignmentChargeHandler.currentCharge.computeIfAbsent(side, s -> new HashMap()).put(player.getUUID(), Mth.canEnchant(result, 0.0f, this.getMaximumCharge(player, side)));
        }
        return true;
    }
    
    @OnlyIn(Dist.CLIENT)
    public void receiveCharge(final PktSyncCharge pkt, final Player player) {
        AlignmentChargeHandler.maximumCharge.computeIfAbsent(LogicalSide.CLIENT, s -> new HashMap()).put(player.getUUID(), pkt.getMaxCharge());
        AlignmentChargeHandler.currentCharge.computeIfAbsent(LogicalSide.CLIENT, s -> new HashMap()).put(player.getUUID(), pkt.getCharge());
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final Player player = (Player)context[0];
        final LogicalSide side = (LogicalSide)context[1];
        float charge = this.getCurrentCharge(player, side);
        final float max = this.getMaximumCharge(player, side);
        if (charge >= max) {
            return;
        }
        final PlayerProgress progress = ResearchHelper.getProgress(player, side);
        float regenPerTick = max / 120.0f;
        final boolean underground = player.level().func_205770_a(Heightmap.Type.WORLD_SURFACE, player.func_233580_cy_()).getY() > player.func_233580_cy_().getY() + 1;
        float dayMultiplier = underground ? 0.85f : (0.3f + 0.7f * DayTimeHelper.getCurrentDaytimeDistribution(player.level()));
        float caveMultiplier = underground ? 0.25f : 1.0f;
        if (progress.getPerkData().hasPerkEffect(p -> p instanceof KeyChargeBalancing)) {
            dayMultiplier = 0.6f + dayMultiplier * 0.4f;
            caveMultiplier = 0.6f + caveMultiplier * 0.4f;
        }
        regenPerTick *= dayMultiplier;
        regenPerTick *= caveMultiplier;
        regenPerTick = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, progress, PerkAttributeTypesAS.ATTR_TYPE_ALIGNMENT_CHARGE_REGENERATION, regenPerTick);
        regenPerTick = AttributeEvent.postProcessModded(player, PerkAttributeTypesAS.ATTR_TYPE_ALIGNMENT_CHARGE_REGENERATION, regenPerTick);
        charge += regenPerTick;
        AlignmentChargeHandler.currentCharge.computeIfAbsent(side, s -> new HashMap()).put(player.getUUID(), Math.min(charge, max));
        PacketChannel.CHANNEL.sendToPlayer(player, new PktSyncCharge(player));
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.PLAYER);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "Alignment Charge Handler";
    }
    
    static {
        INSTANCE = new AlignmentChargeHandler();
        maximumCharge = new HashMap<LogicalSide, Map<UUID, Float>>();
        currentCharge = new HashMap<LogicalSide, Map<UUID, Float>>();
    }
}
