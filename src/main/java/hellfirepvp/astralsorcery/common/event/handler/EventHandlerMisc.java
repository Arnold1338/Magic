package hellfirepvp.astralsorcery.common.event.handler;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ISeedReader;
import hellfirepvp.astralsorcery.common.capability.ChunkFluidEntry;
import net.minecraft.world.level.effect.MobEffectInstance;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.effect.EffectDropModifier;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.common.capabilities.Capability;
import hellfirepvp.astralsorcery.common.lib.CapabilitiesAS;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.level.ChunkEvent;
import hellfirepvp.astralsorcery.common.GuiType;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.item.ItemTome;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.block.entity.LecternTileEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.item.crystal.ItemCrystalBase;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;

public class EventHandlerMisc
{
    public static void attachListeners(final IEventBus bus) {
        bus.addListener((Consumer)EventHandlerMisc::onSpawnEffectCloud);
        bus.addListener((Consumer)EventHandlerMisc::onPlayerSleepEclipse);
        bus.addListener((Consumer)EventHandlerMisc::onChunkLoad);
        bus.addListener((Consumer)EventHandlerMisc::onLecternOpen);
        bus.addListener((Consumer)EventHandlerMisc::onCrystalToss);
    }
    
    private static void onCrystalToss(final ItemTossEvent event) {
        if (!event.getPlayer().func_130014_f_().func_201670_d()) {
            final ItemStack thrown = event.getEntityItem().func_92059_d();
            if (thrown.getItem() instanceof ItemCrystalBase) {
                event.getEntityItem().func_200216_c(event.getPlayer().getUUID());
            }
        }
    }
    
    private static void onLecternOpen(final PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().func_201670_d()) {
            return;
        }
        final LecternTileEntity lectern = MiscUtils.getTileAt((IBlockReader)event.getWorld(), event.getPos(), LecternTileEntity.class, false);
        if (lectern != null) {
            final ItemStack contained = lectern.func_214033_c();
            if (contained.getItem() instanceof ItemTome) {
                event.setCanceled(true);
                AstralSorcery.getProxy().openGui(event.getPlayer(), GuiType.TOME, new Object[0]);
            }
        }
    }
    
    private static void onChunkLoad(final ChunkEvent.Load event) {
        final IChunk ch = event.getChunk();
        if (ch instanceof Chunk && !event.getWorld().func_201670_d()) {
            ((Chunk)ch).getCapability((Capability)CapabilitiesAS.CHUNK_FLUID).ifPresent(entry -> {
                if (!entry.isInitialized()) {
                    final IWorld w = event.getWorld();
                    if (w instanceof ISeedReader) {
                        long seed = ((ISeedReader)w).func_72905_C();
                        final long chX = event.getChunk().func_76632_l().field_77276_a;
                        final long chZ = event.getChunk().func_76632_l().field_77275_b;
                        seed ^= chX << 32;
                        seed ^= chZ;
                        entry.generate(seed);
                        ((Chunk)ch).func_76630_e();
                    }
                }
            });
        }
    }
    
    private static void onPlayerSleepEclipse(final PlayerSleepInBedEvent event) {
        final WorldContext ctx = SkyHandler.getContext(event.getEntityLiving().func_130014_f_());
        if (ctx != null && ctx.getCelestialEventHandler().getSolarEclipse().isActiveNow() && event.getResultStatus() == null) {
            event.setResult(Player.SleepResult.NOT_POSSIBLE_NOW);
        }
    }
    
    private static void onSpawnEffectCloud(final EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof AreaEffectCloudEntity && MiscUtils.contains(((AreaEffectCloudEntity)event.getEntity()).field_184503_f, effect -> effect.func_188419_a() instanceof EffectDropModifier)) {
            event.setCanceled(true);
        }
    }
}
