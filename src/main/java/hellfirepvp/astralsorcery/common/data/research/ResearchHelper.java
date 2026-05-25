package hellfirepvp.astralsorcery.common.data.research;

import java.util.HashMap;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.play.server.PktProgressionUpdate;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.CompoundTag;
import java.io.IOException;
import java.io.File;
import com.google.common.io.Files;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.network.play.server.PktSyncKnowledge;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import java.util.UUID;
import java.util.Map;

public class ResearchHelper
{
    private static PlayerProgress clientProgress;
    private static final Map<UUID, PlayerProgress> playerProgressServer;
    
    @Nonnull
    public static PlayerProgress getProgress(@Nullable final Player player, final LogicalSide side) {
        if (side.isClient()) {
            return getClientProgress();
        }
        if (player instanceof ServerPlayer) {
            return getProgressServer((ServerPlayer)player);
        }
        return new PlayerProgressTestAccess();
    }
    
    @Nonnull
    public static PlayerProgress getClientProgress() {
        return ResearchHelper.clientProgress;
    }
    
    @Nonnull
    private static PlayerProgress getProgressServer(final ServerPlayer player) {
        if (MiscUtils.isPlayerFakeMP(player)) {
            return new PlayerProgressTestAccess();
        }
        return getProgress(player.getUUID());
    }
    
    @Nonnull
    private static PlayerProgress getProgress(final UUID uuid) {
        PlayerProgress progress = ResearchHelper.playerProgressServer.get(uuid);
        if (progress == null) {
            loadPlayerKnowledge(uuid);
            progress = ResearchHelper.playerProgressServer.get(uuid);
        }
        if (progress == null) {
            progress = new PlayerProgress();
        }
        return progress;
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void updateClientResearch(@Nullable final PktSyncKnowledge pkt) {
        ResearchHelper.clientProgress = new PlayerProgress();
        if (pkt != null) {
            ResearchHelper.clientProgress.receive(pkt);
        }
    }
    
    public static void loadPlayerKnowledge(final ServerPlayer p) {
        if (!MiscUtils.isPlayerFakeMP(p)) {
            loadPlayerKnowledge(p.getUUID());
        }
    }
    
    private static void loadPlayerKnowledge(final UUID pUUID) {
        File playerFile = getPlayerFile(pUUID);
        try {
            load_unsafe(pUUID, playerFile);
        }
        catch (final Exception e) {
            AstralSorcery.log.warn("Unable to load progress from default progress file. Attempting loading backup.");
            AstralSorcery.log.warn("Erroneous file: " + playerFile.getName());
            e.printStackTrace();
            playerFile = getPlayerBackupFile(pUUID);
            try {
                load_unsafe(pUUID, playerFile);
                Files.copy(playerFile, getPlayerFile(pUUID));
            }
            catch (final Exception e2) {
                AstralSorcery.log.warn("Unable to load progress from backup progress file. Copying relevant files to error files.");
                AstralSorcery.log.warn("Erroneous file: " + playerFile.getName());
                e2.printStackTrace();
                final File plOriginal = getPlayerFile(pUUID);
                final File plBackup = getPlayerBackupFile(pUUID);
                try {
                    Files.copy(plOriginal, new File(plOriginal.getParent(), plOriginal.getName() + ".lerror"));
                    Files.copy(plBackup, new File(plBackup.getParent(), plBackup.getName() + ".lerror"));
                    AstralSorcery.log.warn("Copied progression files to error files. In case you would like to try me (HellFirePvP) to maybe see what i can do about maybe recovering the files,");
                    AstralSorcery.log.warn("send them over to me at the issue tracker https://github.com/HellFirePvP/AstralSorcery/issues - 90% that i won't be able to do anything, but reporting it would still be great.");
                }
                catch (final IOException e3) {
                    AstralSorcery.log.warn("Unable to copy files to error-files.");
                    AstralSorcery.log.warn("I've had enough. I can't even access or open the files apparently. I'm giving up.");
                    e3.printStackTrace();
                }
                plOriginal.delete();
                plBackup.delete();
                informPlayersAboutProgressionLoss(pUUID);
                load_unsafeFromNBT(pUUID, null);
                savePlayerKnowledge(pUUID, true);
            }
        }
    }
    
    private static void load_unsafe(final UUID pUUID, final File playerFile) throws Exception {
        final CompoundTag compound = NbtIo.func_74797_a(playerFile);
        load_unsafeFromNBT(pUUID, compound);
    }
    
    private static void load_unsafeFromNBT(final UUID pUUID, @Nullable final CompoundTag compound) {
        final PlayerProgress progress = new PlayerProgress();
        if (compound != null && !compound.isEmpty()) {
            progress.load(compound);
        }
        progress.forceGainResearch(ResearchProgression.DISCOVERY);
        ResearchHelper.playerProgressServer.put(pUUID, progress);
    }
    
    private static void informPlayersAboutProgressionLoss(final UUID pUUID) {
        final MinecraftServer server = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            final ServerPlayer player = server.getPlayerList().getPlayer(pUUID);
            if (player != null) {
                player.func_145747_a((Component)new Component("AstralSorcery: Your progression could not be loaded and can't be recovered from backup. Please contact an administrator to lookup what went wrong and/or potentially recover your data from a backup.").withStyle(ChatFormatting.RED)), Util.NIL_UUID);
            }
            final String resolvedName = (player != null) ? player.func_146103_bH().getName() : (pUUID.toString() + " (Not online)");
            for (final String opName : server.getPlayerList().func_152606_n()) {
                final Player pl = (Player)server.getPlayerList().func_152612_a(opName);
                if (pl != null) {
                    pl.func_145747_a((Component)new Component("AstralSorcery: The progression of " + resolvedName + " could not be loaded and can't be recovered from backup. Error files might be created from the unloadable progression files, check the console for additional information!").withStyle(ChatFormatting.RED)), Util.NIL_UUID);
                }
            }
        }
    }
    
    public static void sendConstellationDiscoveryMessage(final CommandSource src, final IConstellation cst) {
        src.func_145747_a((Component)new Component("astralsorcery.progress.constellation.discover.chat", new Object[] { cst.getConstellationName().withStyle(ChatFormatting.GRAY)) }).withStyle(ChatFormatting.BLUE)), Util.NIL_UUID);
    }
    
    public static void sendConstellationMemorizationMessage(final CommandSource src, final PlayerProgress progress, final IConstellation cst) {
        src.func_145747_a((Component)new Component("astralsorcery.progress.constellation.seen.chat", new Object[] { cst.getConstellationName().withStyle(ChatFormatting.GRAY)) }).withStyle(ChatFormatting.BLUE)), Util.NIL_UUID);
        if (progress.getSeenConstellations().size() == 1) {
            src.func_145747_a((Component)new Component("astralsorcery.progress.constellation.seen.track").withStyle(ChatFormatting.BLUE)), Util.NIL_UUID);
        }
    }
    
    public static boolean mergeApplyPlayerprogress(final PlayerProgress toMergeFrom, final Player player) {
        final PlayerProgress progress = getProgress(player, LogicalSide.SERVER);
        if (!progress.isValid()) {
            return false;
        }
        progress.acceptMergeFrom(toMergeFrom);
        ResearchSyncHelper.pushProgressToClientUnsafe(progress, player);
        savePlayerKnowledge(player);
        return true;
    }
    
    public static void wipeKnowledge(final ServerPlayer p) {
        ResearchManager.resetPerks((Player)p);
        wipeFile(p);
        ResearchHelper.playerProgressServer.remove(p.getUUID());
        final PktProgressionUpdate pkt = new PktProgressionUpdate();
        PacketChannel.CHANNEL.sendToPlayer((Player)p, pkt);
        final PktSyncKnowledge pk = new PktSyncKnowledge((byte)1);
        PacketChannel.CHANNEL.sendToPlayer((Player)p, pk);
        loadPlayerKnowledge(p);
        ResearchSyncHelper.pushProgressToClientUnsafe(getProgressServer(p), (Player)p);
    }
    
    private static void wipeFile(final ServerPlayer player) {
        getPlayerFile((Player)player).delete();
        ResearchIOThread.cancelSave(player.getUUID());
    }
    
    public static void savePlayerKnowledge(final Player p) {
        if (p instanceof ServerPlayer && !MiscUtils.isPlayerFakeMP((ServerPlayer)p)) {
            savePlayerKnowledge(p.getUUID(), false);
        }
    }
    
    private static void savePlayerKnowledge(final UUID pUUID, final boolean force) {
        if (ResearchHelper.playerProgressServer.get(pUUID) == null) {
            return;
        }
        final PlayerProgress progress = ResearchHelper.playerProgressServer.get(pUUID);
        if (force) {
            ResearchIOThread.saveNow(pUUID, progress);
        }
        else {
            ResearchIOThread.saveProgress(pUUID, progress.copy());
        }
    }
    
    public static void saveAndClearServerCache() {
        ResearchHelper.playerProgressServer.clear();
    }
    
    public static File getPlayerFile(final Player player) {
        return getPlayerFile(player.getUUID());
    }
    
    public static File getPlayerFile(final UUID pUUID) {
        final File f = new File(getPlayerDirectory(), pUUID.toString() + ".astral");
        if (!f.exists()) {
            try {
                NbtIo.func_74795_b(new CompoundTag(), f);
            }
            catch (final IOException ex) {}
        }
        return f;
    }
    
    public static boolean doesPlayerFileExist(final Player player) {
        return new File(getPlayerDirectory(), player.getUUID().toString() + ".astral").exists();
    }
    
    public static File getPlayerBackupFile(final Player player) {
        return getPlayerBackupFile(player.getUUID());
    }
    
    public static File getPlayerBackupFile(final UUID pUUID) {
        final File f = new File(getPlayerDirectory(), pUUID.toString() + ".astralback");
        if (!f.exists()) {
            try {
                NbtIo.func_74795_b(new CompoundTag(), f);
            }
            catch (final IOException ex) {}
        }
        return f;
    }
    
    private static File getPlayerDirectory() {
        final File pDir = new File(AstralSorcery.getProxy().getASServerDataDirectory(), "playerdata");
        if (!pDir.exists()) {
            pDir.mkdirs();
        }
        return pDir;
    }
    
    static {
        ResearchHelper.clientProgress = new PlayerProgressTestAccess();
        playerProgressServer = new HashMap<UUID, PlayerProgress>();
    }
}
