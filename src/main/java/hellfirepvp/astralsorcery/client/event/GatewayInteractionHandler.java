package hellfirepvp.astralsorcery.client.event;

import java.util.Iterator;
import hellfirepvp.astralsorcery.common.network.play.client.PktRequestTeleport;
import java.util.List;
import java.util.Collections;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.util.ColorUtils;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.data.world.GatewayCache;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.common.network.play.client.PktRevokeGatewayAccess;
import hellfirepvp.astralsorcery.common.util.PlayerReference;
import java.util.function.Function;
import net.minecraft.util.Tuple;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.MapStream;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.tile.TileCelestialGateway;
import hellfirepvp.astralsorcery.client.event.effect.GatewayUIRenderHandler;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.client.util.GatewayUI;
import java.util.Random;

public class GatewayInteractionHandler
{
    private static final Random rand;
    public static GatewayUI.GatewayEntry focusingEntry;
    public static int focusTicks;
    private static double fovPre;
    
    public static void attachEventListeners(final IEventBus eventBus) {
        eventBus.addListener((Consumer)GatewayInteractionHandler::clientTick);
        eventBus.addListener(EventPriority.LOWEST, (Consumer)GatewayInteractionHandler::renderTick);
        eventBus.addListener((Consumer)GatewayInteractionHandler::onAccessRevoke);
    }
    
    private static void onAccessRevoke(final PlayerInteractEvent.RightClickBlock event) {
        final Player player = event.getPlayer();
        final World world = event.getWorld();
        if (player == null || world == null || !world.func_201670_d() || event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }
        final GatewayUI ui = GatewayUIRenderHandler.getInstance().getCurrentUI();
        if (ui == null) {
            return;
        }
        final GatewayCache.GatewayNode node = ui.getThisGatewayNode();
        if (node == null || !node.isLocked() || node.getOwner() == null || node.getAllowedUsers().isEmpty()) {
            return;
        }
        final TileCelestialGateway gateway = MiscUtils.getTileAt((IBlockReader)world, Vector3.atEntityCorner((Entity)player).toBlockPos(), TileCelestialGateway.class, true);
        if (gateway == null || !gateway.hasMultiblock() || !gateway.doesSeeSky()) {
            return;
        }
        final BlockPos clickedPos = event.getPos();
        MapStream.of(node.getAllowedUsers()).filter(tpl -> TileCelestialGateway.getAllowedUserOffset((int)tpl.func_76341_a()).func_177971_a((Vector3i)node.getPos()).func_177977_b().equals((Object)clickedPos)).findAny().map((Function<? super net.minecraft.util.Tuple<Integer, PlayerReference>, ?>)Tuple::func_76340_b).ifPresent(playerRef -> {
            final PktRevokeGatewayAccess pkt = new PktRevokeGatewayAccess((RegistryKey<World>)world.dimension(), gateway.func_174877_v(), playerRef.getPlayerUUID());
            PacketChannel.CHANNEL.sendToServer(pkt);
        });
    }
    
    private static void clientTick(final TickEvent.ClientTickEvent event) {
        final Player player = (Player)Minecraft.func_71410_x().field_71439_g;
        final World world = (World)Minecraft.func_71410_x().field_71441_e;
        if (player == null || world == null) {
            GatewayInteractionHandler.focusingEntry = null;
            GatewayInteractionHandler.focusTicks = 0;
            return;
        }
        final GatewayUI ui = GatewayUIRenderHandler.getInstance().getCurrentUI();
        if (ui == null) {
            GatewayInteractionHandler.focusingEntry = null;
            GatewayInteractionHandler.focusTicks = 0;
            return;
        }
        final TileCelestialGateway gateway = MiscUtils.getTileAt((IBlockReader)world, Vector3.atEntityCorner((Entity)player).toBlockPos(), TileCelestialGateway.class, true);
        if (gateway == null || !gateway.hasMultiblock() || !gateway.doesSeeSky()) {
            GatewayInteractionHandler.focusingEntry = null;
            GatewayInteractionHandler.focusTicks = 0;
            return;
        }
        final GatewayUI.GatewayEntry entry = GatewayUIRenderHandler.getInstance().findMatchingEntry(MathHelper.func_76142_g(player.field_70177_z), MathHelper.func_76142_g(player.field_70125_A));
        if (entry == null) {
            GatewayInteractionHandler.focusingEntry = null;
            GatewayInteractionHandler.focusTicks = 0;
            return;
        }
        if (!Minecraft.func_71410_x().field_71474_y.field_74313_G.func_151470_d() && !Minecraft.func_71410_x().field_71474_y.field_228046_af_.func_151470_d()) {
            GatewayInteractionHandler.focusingEntry = null;
            GatewayInteractionHandler.focusTicks = 0;
            return;
        }
        if (GatewayInteractionHandler.focusingEntry != null && !entry.equals(GatewayInteractionHandler.focusingEntry)) {
            GatewayInteractionHandler.focusingEntry = null;
            GatewayInteractionHandler.focusTicks = 0;
            return;
        }
        GatewayInteractionHandler.focusingEntry = entry;
        ++GatewayInteractionHandler.focusTicks;
        final Vector3 dir = GatewayInteractionHandler.focusingEntry.getRelativePos().clone().add(ui.getRenderCenter()).subtract(player.func_174824_e(1.0f));
        final Vector3 mov = dir.clone().normalize().multiply(0.25f).negate();
        Vector3 pos = GatewayInteractionHandler.focusingEntry.getRelativePos().clone().add(ui.getRenderCenter());
        final DyeColor nodeColor = GatewayInteractionHandler.focusingEntry.getNode().getColor();
        final Color gatewayColor = ColorUtils.flareColorFromDye((nodeColor == null) ? DyeColor.YELLOW : nodeColor);
        if (GatewayInteractionHandler.focusTicks <= 40) {
            pos = GatewayInteractionHandler.focusingEntry.getRelativePos().clone().multiply(0.8).add(ui.getRenderCenter());
            final float perc = GatewayInteractionHandler.focusTicks / 40.0f;
            List<Vector3> positions = MiscUtils.getCirclePositions(pos, dir.clone().negate(), GatewayInteractionHandler.rand.nextFloat() * 0.2 + 0.4, GatewayInteractionHandler.rand.nextInt(6) + 25);
            for (int i = 0; i < positions.size(); ++i) {
                final float pc = i / (float)positions.size();
                if (pc < perc) {
                    final Color color = MiscUtils.eitherOf(GatewayInteractionHandler.rand, new Color[] { Color.WHITE, gatewayColor, gatewayColor.brighter() });
                    final Vector3 at = positions.get(i);
                    final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_GATEWAY_PARTICLE).spawn(at).setScaleMultiplier(0.08f).color(VFXColorFunction.constant(color));
                    if (GatewayInteractionHandler.rand.nextInt(3) == 0) {
                        final Vector3 to = pos.clone().subtract(at);
                        to.normalize().multiply(0.02);
                        p.setMotion(to).setAlphaMultiplier(0.1f);
                    }
                }
            }
            positions = MiscUtils.getCirclePositions(pos, dir, GatewayInteractionHandler.rand.nextFloat() * 0.2 + 0.4, GatewayInteractionHandler.rand.nextInt(6) + 25);
            Collections.reverse(positions);
            for (int i = 0; i < positions.size(); ++i) {
                final float pc = i / (float)positions.size();
                if (pc < perc) {
                    final Color color = MiscUtils.eitherOf(GatewayInteractionHandler.rand, new Color[] { Color.WHITE, gatewayColor, gatewayColor.brighter() });
                    final Vector3 at = positions.get(i);
                    final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_GATEWAY_PARTICLE).spawn(at).setScaleMultiplier(0.08f).color(VFXColorFunction.constant(color));
                    if (GatewayInteractionHandler.rand.nextInt(3) == 0) {
                        final Vector3 to = pos.clone().subtract(at);
                        to.normalize().multiply(0.02);
                        p.setMotion(to).setAlphaMultiplier(0.1f);
                    }
                }
            }
        }
        else {
            for (final Vector3 v : MiscUtils.getCirclePositions(pos, dir, GatewayInteractionHandler.rand.nextFloat() * 0.3 + 0.2, GatewayInteractionHandler.rand.nextInt(20) + 30)) {
                final Color color2 = MiscUtils.eitherOf(GatewayInteractionHandler.rand, new Color[] { Color.WHITE, gatewayColor, gatewayColor.brighter() });
                final Vector3 m = mov.clone().multiply(0.5 + GatewayInteractionHandler.rand.nextFloat() * 0.5);
                EffectHelper.of(EffectTemplatesAS.GENERIC_GATEWAY_PARTICLE).spawn(v).setScaleMultiplier(0.1f).setMotion(m).color(VFXColorFunction.constant(color2));
            }
        }
        if (GatewayInteractionHandler.focusTicks > 95) {
            Minecraft.func_71410_x().field_71439_g.func_226284_e_(false);
            final PktRequestTeleport pkt = new PktRequestTeleport(GatewayInteractionHandler.focusingEntry.getNodeDimension(), GatewayInteractionHandler.focusingEntry.getNode().getPos());
            PacketChannel.CHANNEL.sendToServer(pkt);
            GatewayInteractionHandler.focusingEntry = null;
            GatewayInteractionHandler.focusTicks = 0;
        }
    }
    
    private static void renderTick(final TickEvent.RenderTickEvent event) {
        final GatewayUI ui = GatewayUIRenderHandler.getInstance().getCurrentUI();
        if (ui == null) {
            return;
        }
        if (event.phase == TickEvent.Phase.START) {
            GatewayInteractionHandler.fovPre = Minecraft.func_71410_x().field_71474_y.field_74334_X;
            if (GatewayInteractionHandler.focusTicks < 80) {
                return;
            }
            float percDone = 1.0f - (GatewayInteractionHandler.focusTicks - 80.0f + event.renderTickTime) / 15.0f;
            percDone = (float)Math.pow(percDone, 2.4000000953674316);
            final float targetFov = 10.0f;
            final double diff = GatewayInteractionHandler.fovPre - targetFov;
            Minecraft.func_71410_x().field_71474_y.field_74334_X = Math.max(targetFov, targetFov + diff * percDone);
        }
        else {
            Minecraft.func_71410_x().field_71474_y.field_74334_X = GatewayInteractionHandler.fovPre;
        }
    }
    
    static {
        rand = new Random();
        GatewayInteractionHandler.focusingEntry = null;
        GatewayInteractionHandler.focusTicks = 0;
        GatewayInteractionHandler.fovPre = 0.0;
    }
}
