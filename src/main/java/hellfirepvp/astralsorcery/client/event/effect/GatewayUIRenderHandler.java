package hellfirepvp.astralsorcery.client.event.effect;

import hellfirepvp.astralsorcery.common.util.PlayerReference;
import com.mojang.blaze3d.vertex.BufferBuilder;
import java.util.EnumSet;
import net.minecraftforge.event.TickEvent;
import javax.annotation.Nullable;
import java.util.Iterator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import hellfirepvp.astralsorcery.client.util.Blending;
import java.util.Random;
import net.minecraft.world.item.DyeColor;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.util.ColorUtils;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.awt.Color;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.data.world.GatewayCache;
import net.minecraft.network.chat.ITextProperties;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.util.RenderingConstellationUtils;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import net.minecraft.world.level.phys.BlockHitResult;
import net.minecraft.world.level.phys.HitResult;
import net.minecraft.world.level.entity.player.Player;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.level.entity.Entity;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraft.world.level.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileCelestialGateway;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.Level;
import hellfirepvp.astralsorcery.client.util.GatewayUI;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class GatewayUIRenderHandler implements ITickHandler
{
    private static final GatewayUIRenderHandler INSTANCE;
    private GatewayUI currentUI;
    
    private GatewayUIRenderHandler() {
        this.currentUI = null;
    }
    
    public static GatewayUIRenderHandler getInstance() {
        return GatewayUIRenderHandler.INSTANCE;
    }
    
    public GatewayUI getOrCreateUI(final World world, final BlockPos pos, final Vector3 renderPos) {
        if (this.currentUI == null || !this.currentUI.getDimType().equals(world.dimension()) || !this.currentUI.getPos().equals((Object)pos)) {
            this.currentUI = GatewayUI.create(world, pos, renderPos, 5.5);
        }
        if (this.currentUI != null) {
            this.currentUI.refreshView();
        }
        return this.currentUI;
    }
    
    public GatewayUI getCurrentUI() {
        return this.currentUI;
    }
    
    private boolean validate() {
        if (this.currentUI == null) {
            return true;
        }
        final World world = (World)Minecraft.func_71410_x().field_71441_e;
        final TileCelestialGateway gateway;
        if (world == null || this.currentUI.getVisibleTicks() <= 0 || !this.currentUI.getDimType().equals(world.dimension()) || (gateway = MiscUtils.getTileAt((IBlockReader)world, this.currentUI.getPos(), TileCelestialGateway.class, true)) == null || !gateway.doesSeeSky() || !gateway.hasMultiblock()) {
            this.currentUI = null;
        }
        return this.currentUI == null;
    }
    
    void render(final RenderWorldLastEvent event) {
        if (this.validate()) {
            return;
        }
        final float pTicks = event.getPartialTicks();
        final PoseStack renderStack = event.getMatrixStack();
        final Vector3 renderOffset = this.currentUI.getRenderCenter();
        final Player player = (Player)Minecraft.func_71410_x().field_71439_g;
        final double dst = renderOffset.distance(Vector3.atEntityCorner((Entity)player).addY(1.5));
        if (dst > 3.0) {
            return;
        }
        if (Minecraft.func_238218_y_()) {
            RenderSystem.clear(256, Minecraft.field_142025_a);
        }
        this.renderGatewayShieldOverlay(renderStack, renderOffset, dst, pTicks);
        this.renderGatewayFocusedEntry(renderStack, renderOffset, pTicks);
        this.renderGatewayAllowedPlayers(renderStack, renderOffset, dst, pTicks);
    }
    
    private void renderGatewayAllowedPlayers(final PoseStack renderStack, final Vector3 renderOffset, final double distance, final float pTicks) {
        final GatewayCache.GatewayNode node = this.currentUI.getThisGatewayNode();
        if (node == null || !node.isLocked() || node.getOwner() == null || node.getAllowedUsers().isEmpty()) {
            return;
        }
        final UUID currentUUID = (Minecraft.func_71410_x().field_71439_g != null) ? Minecraft.func_71410_x().field_71439_g.getUUID() : null;
        final HitResult mouseOverRtr = Minecraft.func_71410_x().field_71476_x;
        BlockPos blockSelected;
        if (mouseOverRtr != null && mouseOverRtr.func_216346_c() == HitResult.Type.BLOCK && mouseOverRtr instanceof BlockHitResult) {
            blockSelected = ((BlockHitResult)mouseOverRtr).func_216350_a().above();
        }
        else {
            blockSelected = null;
        }
        final Color c = ColorsAS.CONSTELLATION_TYPE_MAJOR;
        final float alpha = Mth.func_76131_a(1.0f - (float)(distance / 2.0), 0.0f, 1.0f);
        node.getAllowedUsers().forEach((index, playerRef) -> {
            final BlockPos drawPos = TileCelestialGateway.getAllowedUserOffset(index).func_177971_a((Vector3i)node.getPos());
            final Vector3 at = new Vector3((Vector3i)drawPos).add(0.5, 0.001, 0.5).subtract(RenderingVectorUtils.getStandardTranslationRemovalVector(pTicks));
            final IConstellation cst = this.getCurrentUI().getGeneratedConstellation(playerRef.getPlayerUUID());
            if (cst != null) {
                RenderingConstellationUtils.renderConstellationIntoWorldFlat(c, cst, renderStack, at, 1.2, 1.0, alpha);
                final UUID targetUUID = playerRef.getPlayerUUID();
                if ((node.getOwner().getPlayerUUID().equals(currentUUID) || targetUUID.equals(currentUUID)) && drawPos.equals((Object)blockSelected)) {
                    RenderingUtils.renderInWorldText((ITextProperties)playerRef.getPlayerName(), c, 0.020833334f, at.clone().addY(0.2), renderStack, pTicks, true);
                }
            }
        });
    }
    
    private void renderGatewayFocusedEntry(final PoseStack renderStack, final Vector3 renderOffset, final float pTicks) {
        final Player player = (Player)Minecraft.func_71410_x().field_71439_g;
        final GatewayUI.GatewayEntry entry = this.findMatchingEntry(Mth.func_76142_g(player.field_70177_z), Mth.func_76142_g(player.field_70125_A));
        if (entry != null) {
            final Component display = entry.getNode().getDisplayName();
            if (display != null && !display.getString().isEmpty()) {
                final Vector3 at = entry.getRelativePos().clone().add(renderOffset).addY(0.4000000059604645).subtract(RenderingVectorUtils.getStandardTranslationRemovalVector(pTicks));
                Color c = ColorsAS.CONSTELLATION_SINGLE_STAR;
                final DyeColor nodeColor = entry.getNode().getColor();
                if (nodeColor != null) {
                    c = ColorUtils.flareColorFromDye(nodeColor);
                }
                RenderingUtils.renderInWorldText((ITextProperties)display, c, at, renderStack, pTicks, true);
            }
        }
    }
    
    private void renderGatewayShieldOverlay(final PoseStack renderStack, final Vector3 renderOffset, final double distance, final float pTicks) {
        final float alpha = Mth.func_76131_a(1.0f - (float)(distance / 2.0), 0.0f, 1.0f);
        final Color c = ColorsAS.CONSTELLATION_SINGLE_STAR;
        final int red = c.getRed();
        final int green = c.getGreen();
        final int blue = c.getBlue();
        long seed = -6376616654017193693L;
        seed |= (long)this.currentUI.getPos().getX() << 48;
        seed |= (long)this.currentUI.getPos().getY() << 24;
        seed |= this.currentUI.getPos().getZ();
        final Random rand = new Random(seed);
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        TexturesAS.TEX_STAR_1.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.field_227851_o_, buf -> {
            for (int i = 0; i < 300; ++i) {
                final Vector3 at = Vector3.random(rand).normalize().multiply(this.currentUI.getSphereRadius() * 0.9).add(renderOffset);
                if (at.getY() >= this.currentUI.getPos().getY()) {
                    final float a = RenderingConstellationUtils.conCFlicker(ClientScheduler.getClientTick(), pTicks, rand.nextInt(7) + 6);
                    final float a2 = a * alpha;
                    RenderingDrawUtils.renderFacingFullQuadVB((VertexConsumer)buf, renderStack, at.getX(), at.getY(), at.getZ(), 0.07f, rand.nextFloat(), 255, 255, 255, (int)(a2 * 255.0f));
                }
            }
            this.currentUI.getGatewayEntries().iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final GatewayUI.GatewayEntry entry = iterator.next();
                int r = red;
                int g = green;
                int b = blue;
                DyeColor nodeColor = entry.getNode().getColor();
                if (nodeColor != null) {
                    if (nodeColor == DyeColor.BLACK) {
                        nodeColor = DyeColor.GRAY;
                    }
                    final Color ovr = ColorUtils.flareColorFromDye(nodeColor);
                    r = ovr.getRed();
                    g = ovr.getGreen();
                    b = ovr.getBlue();
                }
                final float a3 = RenderingConstellationUtils.conCFlicker(ClientScheduler.getClientTick(), pTicks, rand.nextInt(7) + 6);
                final float a4 = 0.4f + 0.6f * a3;
                final float a5 = a4 * alpha;
                RenderingDrawUtils.renderFacingFullQuadVB((VertexConsumer)buf, renderStack, renderOffset.getX() + entry.getRelativePos().getX(), renderOffset.getY() + entry.getRelativePos().getY(), renderOffset.getZ() + entry.getRelativePos().getZ(), 0.16f, 0.0f, r, g, b, (int)(a5 * 255.0f));
            }
            return;
        });
        RenderSystem.depthMask(true);
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
    }
    
    @Nullable
    public GatewayUI.GatewayEntry findMatchingEntry(final float yaw, final float pitch) {
        final float matchAccurancy = 4.0f;
        for (final GatewayUI.GatewayEntry entry : this.currentUI.getGatewayEntries()) {
            if (Math.abs(entry.getPitch() - pitch) < matchAccurancy && (Math.abs(entry.getYaw() - yaw) <= matchAccurancy || Math.abs(entry.getYaw() - yaw - 360.0f) <= matchAccurancy)) {
                return entry;
            }
        }
        return null;
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        if (this.currentUI != null) {
            this.currentUI.decrementVisibleTicks();
        }
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.CLIENT);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "GatewayUI Render Handler";
    }
    
    static {
        INSTANCE = new GatewayUIRenderHandler();
    }
}
