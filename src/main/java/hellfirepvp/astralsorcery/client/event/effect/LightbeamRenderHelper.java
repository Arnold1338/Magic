package hellfirepvp.astralsorcery.client.event.effect;

import java.util.EnumSet;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightbeam;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileLens;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.data.config.entry.RenderingConfig;
import java.util.Set;
import net.minecraft.core.BlockPos;
import java.util.Map;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.common.data.sync.client.ClientLightConnections;
import hellfirepvp.astralsorcery.common.data.sync.SyncDataHolder;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import java.util.function.Consumer;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class LightbeamRenderHelper implements ITickHandler
{
    private static final LightbeamRenderHelper INSTANCE;
    private int ticksExisted;
    
    private LightbeamRenderHelper() {
        this.ticksExisted = 0;
    }
    
    public static void attachTickListener(final Consumer<ITickHandler> registrar) {
        registrar.accept((ITickHandler)LightbeamRenderHelper.INSTANCE);
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        ++this.ticksExisted;
        if (this.ticksExisted % 48 == 0) {
            this.ticksExisted = 0;
            Entity rView = Minecraft.getInstance().func_175606_aa();
            if (rView == null) {
                rView = (Entity)Minecraft.getInstance().player;
            }
            if (rView != null) {
                final Entity renderView = rView;
                final RegistryKey<Level> dimKey = (RegistryKey<Level>)renderView.level().dimension();
                SyncDataHolder.executeClient(SyncDataHolder.DATA_LIGHT_CONNECTIONS, ClientLightConnections.class, data -> {
                    data.getClientConnections((RegistryKey<Level>)dimKey).entrySet().iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final Map.Entry<BlockPos, Set<BlockPos>> entry = iterator.next();
                        final BlockPos at = entry.getKey();
                        if (renderView.func_70092_e((double)at.getX(), (double)at.getY(), (double)at.getZ()) <= RenderingConfig.CONFIG.getMaxEffectRenderDistanceSq()) {
                            final Vector3 source = new Vector3((Vector3i)at).add(0.5, 0.5, 0.5);
                            Color overlay = null;
                            final TileLens lens = MiscUtils.getTileAt((IBlockReader)renderView.level(), at, TileLens.class, true);
                            if (lens != null && lens.getColorType() != null) {
                                overlay = lens.getColorType().getColor();
                            }
                            entry.getValue().iterator();
                            final Iterator iterator2;
                            while (iterator2.hasNext()) {
                                final BlockPos dst = iterator2.next();
                                final Vector3 to = new Vector3((Vector3i)dst).add(0.5, 0.5, 0.5);
                                final FXLightbeam beam = EffectHelper.of(EffectTemplatesAS.LIGHTBEAM_TRANSFER).spawn(source).setup(to, 0.4, 0.4).setAlphaMultiplier(0.4f);
                                if (overlay != null) {
                                    beam.color(VFXColorFunction.constant(overlay));
                                }
                            }
                        }
                    }
                });
            }
        }
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.CLIENT);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "Lightbeam Render Helper";
    }
    
    static {
        INSTANCE = new LightbeamRenderHelper();
    }
}
