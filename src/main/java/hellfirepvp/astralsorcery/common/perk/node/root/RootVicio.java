package hellfirepvp.astralsorcery.common.perk.node.root;

import net.minecraft.stats.StatisticsManager;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.DiminishingMultiplier;
import java.util.HashMap;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import hellfirepvp.astralsorcery.common.perk.tick.PlayerTickPerk;
import hellfirepvp.astralsorcery.common.perk.node.RootPerk;

public class RootVicio extends RootPerk implements PlayerTickPerk
{
    public static final Config CONFIG;
    private final Map<ResourceLocation, Map<UUID, Integer>> moveTrackMap;
    
    public RootVicio(final ResourceLocation name, final float x, final float y) {
        super(name, RootVicio.CONFIG, ConstellationsAS.vicio, x, y);
        this.moveTrackMap = new HashMap<ResourceLocation, Map<UUID, Integer>>();
    }
    
    @Nonnull
    @Override
    protected DiminishingMultiplier createMultiplier() {
        return new DiminishingMultiplier(10000L, 0.065f, 0.003f, 0.2f);
    }
    
    @Override
    public void removePerkLogic(final Player player, final LogicalSide side) {
        super.removePerkLogic(player, side);
        if (side.isServer()) {
            this.moveTrackMap.computeIfAbsent(Stats.field_188100_j, s -> new HashMap()).remove(player.getUUID());
            this.moveTrackMap.computeIfAbsent(Stats.field_188102_l, s -> new HashMap()).remove(player.getUUID());
            this.moveTrackMap.computeIfAbsent(Stats.field_188104_p, s -> new HashMap()).remove(player.getUUID());
            this.moveTrackMap.computeIfAbsent(Stats.field_188110_v, s -> new HashMap()).remove(player.getUUID());
            this.moveTrackMap.computeIfAbsent(Stats.field_75946_m, s -> new HashMap()).remove(player.getUUID());
        }
    }
    
    @Override
    public void clearCaches(final LogicalSide side) {
        super.clearCaches(side);
        if (side.isServer()) {
            this.moveTrackMap.clear();
        }
    }
    
    @Override
    public void onPlayerTick(final Player player, final LogicalSide side) {
        if (!side.isServer() || !(player instanceof ServerPlayer)) {
            return;
        }
        final UUID uuid = player.getUUID();
        final ServerPlayer sPlayer = (ServerPlayer)player;
        final PlayerProgress prog = ResearchHelper.getProgress(player, side);
        final StatisticsManager mgr = (StatisticsManager)sPlayer.func_147099_x();
        final int walked = mgr.func_77444_a(Stats.field_199092_j.func_199076_b((Object)Stats.field_188100_j));
        final int sprint = mgr.func_77444_a(Stats.field_199092_j.func_199076_b((Object)Stats.field_188102_l));
        final int flown = mgr.func_77444_a(Stats.field_199092_j.func_199076_b((Object)Stats.field_188104_p));
        final int elytra = mgr.func_77444_a(Stats.field_199092_j.func_199076_b((Object)Stats.field_188110_v));
        final int swam = mgr.func_77444_a(Stats.field_199092_j.func_199076_b((Object)Stats.field_75946_m));
        final int lastWalked = this.moveTrackMap.computeIfAbsent(Stats.field_188100_j, s -> new HashMap()).computeIfAbsent(uuid, u -> walked);
        final int lastSprint = this.moveTrackMap.computeIfAbsent(Stats.field_188102_l, s -> new HashMap()).computeIfAbsent(uuid, u -> sprint);
        final int lastFly = this.moveTrackMap.computeIfAbsent(Stats.field_188104_p, s -> new HashMap()).computeIfAbsent(uuid, u -> flown);
        final int lastElytra = this.moveTrackMap.computeIfAbsent(Stats.field_188110_v, s -> new HashMap()).computeIfAbsent(uuid, u -> elytra);
        final int lastSwam = this.moveTrackMap.computeIfAbsent(Stats.field_75946_m, s -> new HashMap()).computeIfAbsent(uuid, u -> swam);
        float added = 0.0f;
        if (walked > lastWalked) {
            added += Math.min((float)(walked - lastWalked), 500.0f);
            if (added >= 500.0f) {
                added = 500.0f;
            }
            added *= 0.9f;
            this.moveTrackMap.get(Stats.field_188100_j).put(uuid, walked);
        }
        if (sprint > lastSprint) {
            added += Math.min((float)(sprint - lastSprint), 500.0f);
            if (added >= 500.0f) {
                added = 500.0f;
            }
            added *= 0.8f;
            this.moveTrackMap.get(Stats.field_188102_l).put(uuid, sprint);
        }
        if (flown > lastFly) {
            added += Math.min((float)(flown - lastFly), 500.0f);
            added *= 0.3f;
            this.moveTrackMap.get(Stats.field_188104_p).put(uuid, flown);
        }
        if (elytra > lastElytra) {
            added += Math.min((float)(elytra - lastElytra), 500.0f);
            added *= 0.55f;
            this.moveTrackMap.get(Stats.field_188110_v).put(uuid, elytra);
        }
        if (swam > lastSwam) {
            added += Math.min((float)(swam - lastSwam), 500.0f);
            added *= 1.2f;
            this.moveTrackMap.get(Stats.field_75946_m).put(uuid, swam);
        }
        if (added > 0.0f) {
            added *= 0.02f;
            added *= (float)this.getExpMultiplier();
            added *= this.getDiminishingReturns(player);
            added *= PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT);
            added *= PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EXP);
            added = AttributeEvent.postProcessModded(player, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EXP, added);
            ResearchManager.modifyExp(player, added);
        }
    }
    
    static {
        CONFIG = new Config("root.vicio");
    }
}
