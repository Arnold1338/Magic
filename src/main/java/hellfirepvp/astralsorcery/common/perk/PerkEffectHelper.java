package hellfirepvp.astralsorcery.common.perk;

import hellfirepvp.astralsorcery.common.perk.source.AttributeModifierProvider;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import hellfirepvp.astralsorcery.common.perk.source.AttributeConverterProvider;
import java.util.Collection;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.perk.source.ModifierManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.data.research.PlayerPerkData;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.perk.source.ModifierSource;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.network.play.server.PktSyncPerkActivity;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.server.level.ServerPlayer;

public class PerkEffectHelper
{
    private PerkEffectHelper() {
    }
    
    public static void onPlayerConnectEvent(final ServerPlayer player) {
        modifyAllPerks((Player)player, LogicalSide.SERVER, Action.ADD);
        final CompoundTag asData = NBTHelper.getPersistentData((Entity)player);
        if (asData.func_150297_b("health", 5)) {
            player.func_70606_j(asData.getFloat("health"));
        }
        PacketChannel.CHANNEL.sendToPlayer((Player)player, new PktSyncPerkActivity(PktSyncPerkActivity.Type.UNLOCKALL));
    }
    
    public static void onPlayerDisconnectEvent(final ServerPlayer player) {
        modifyAllPerks((Player)player, LogicalSide.SERVER, Action.REMOVE);
        NBTHelper.getPersistentData((Entity)player).func_74776_a("health", player.getMaxHealth());
    }
    
    public static void onPlayerCloneEvent(final ServerPlayer original, final ServerPlayer newPlayer) {
        modifyAllPerks((Player)original, LogicalSide.SERVER, Action.REMOVE);
        modifyAllPerks((Player)newPlayer, LogicalSide.SERVER, Action.ADD);
        PerkCooldownHelper.removeAllCooldowns((Player)original, LogicalSide.SERVER);
        PacketChannel.CHANNEL.sendToPlayer((Player)newPlayer, new PktSyncPerkActivity(PktSyncPerkActivity.Type.UNLOCKALL));
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void clientChangePerkData(final AbstractPerk perk, final CompoundTag oldData, final CompoundTag newData) {
        final Player player = (Player)Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.CLIENT);
        final PlayerPerkData perkData = progress.getPerkData();
        if (!perkData.hasPerkAllocation(perk)) {
            return;
        }
        perkData.updatePerkData(perk, oldData);
        modifySource(player, LogicalSide.CLIENT, perk, Action.REMOVE);
        perkData.updatePerkData(perk, newData);
        modifySource(player, LogicalSide.CLIENT, perk, Action.ADD);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void clientClearAllPerks() {
        final Player player = (Player)Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        final PlayerProgress progress = ResearchHelper.getProgress(player, LogicalSide.CLIENT);
        if (!progress.isValid()) {
            return;
        }
        final PerkAttributeMap attr = PerkAttributeHelper.getOrCreateMap(player, LogicalSide.CLIENT);
        for (final ModifierSource source : ModifierManager.getAppliedModifiers(player, LogicalSide.CLIENT)) {
            if (source instanceof AbstractPerk) {
                removeSource(attr, player, LogicalSide.CLIENT, source);
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void clientRefreshAllPerks() {
        final Player player = (Player)Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        modifyAllPerks(player, LogicalSide.CLIENT, Action.ADD);
        PerkCooldownHelper.removeAllCooldowns(player, LogicalSide.CLIENT);
    }
    
    private static void modifyAllPerks(final Player player, final LogicalSide side, final Action action) {
        ResearchHelper.getProgress(player, side).getPerkData().getEffectGrantingPerks().forEach(perk -> modifySource(player, side, perk, action));
    }
    
    public static void updateSource(final Player player, final LogicalSide side, final ModifierSource oldSource, final ModifierSource newSource) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, side);
        if (!progress.isValid()) {
            return;
        }
        final PerkAttributeMap attributeMap = PerkAttributeHelper.getOrCreateMap(player, side);
        attributeMap.write(() -> {
            if (ModifierManager.isModifierApplied(player, side, oldSource)) {
                removeSource(attributeMap, player, side, oldSource);
            }
            if (!ModifierManager.isModifierApplied(player, side, newSource) && newSource.canApplySource(player, side)) {
                applySource(attributeMap, player, side, newSource);
            }
        });
    }
    
    public static <T extends ModifierSource> void modifySources(final Player player, final LogicalSide side, final Collection<T> sources, final Action action) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, side);
        if (!progress.isValid()) {
            return;
        }
        final PerkAttributeMap attributeMap = PerkAttributeHelper.getOrCreateMap(player, side);
        for (final T src : sources) {
            if (action.isRemove()) {
                if (!ModifierManager.isModifierApplied(player, side, src)) {
                    continue;
                }
                attributeMap.write(() -> removeSource(attributeMap, player, side, src));
            }
            else {
                if (ModifierManager.isModifierApplied(player, side, src) || !src.canApplySource(player, side)) {
                    continue;
                }
                attributeMap.write(() -> applySource(attributeMap, player, side, src));
            }
        }
    }
    
    public static void modifySource(final Player player, final LogicalSide side, final ModifierSource source, final Action action) {
        final PlayerProgress progress = ResearchHelper.getProgress(player, side);
        if (!progress.isValid()) {
            return;
        }
        final PerkAttributeMap attributeMap = PerkAttributeHelper.getOrCreateMap(player, side);
        if (action.isRemove()) {
            if (ModifierManager.isModifierApplied(player, side, source)) {
                attributeMap.write(() -> removeSource(attributeMap, player, side, source));
            }
        }
        else if (!ModifierManager.isModifierApplied(player, side, source) && source.canApplySource(player, side)) {
            attributeMap.write(() -> applySource(attributeMap, player, side, source));
        }
    }
    
    private static void applySource(final PerkAttributeMap attrMap, final Player player, final LogicalSide side, final ModifierSource add) {
        final Collection<ModifierSource> sources = ModifierManager.getAppliedModifiers(player, side);
        sources.forEach(source -> {
            removeModifiers(source, attrMap, player, side);
            ModifierManager.removeModifier(player, side, source);
            return;
        });
        if (add instanceof AttributeConverterProvider) {
            ((AttributeConverterProvider)add).getConverters(player, side, false).forEach(c -> attrMap.applyConverter(player, c));
        }
        final Collection<PerkAttributeModifier> newModifiers = applyModifiers(add, attrMap, player, side);
        sources.forEach(source -> {
            applyModifiers(source, attrMap, player, side);
            ModifierManager.addModifier(player, side, source);
            return;
        });
        ModifierManager.addModifier(player, side, add);
        newModifiers.forEach(mod -> mod.getAttributeType().onApply(player, side, add));
    }
    
    private static Collection<PerkAttributeModifier> applyModifiers(final ModifierSource source, final PerkAttributeMap attrMap, final Player player, final LogicalSide side) {
        final Collection<PerkAttributeModifier> addedModifiers = new ArrayList<PerkAttributeModifier>();
        if (source instanceof AttributeModifierProvider) {
            for (final PerkAttributeModifier modifier : ((AttributeModifierProvider)source).getModifiers(player, side, false)) {
                addedModifiers.addAll(attrMap.applyModifier(player, modifier, source));
            }
        }
        return addedModifiers;
    }
    
    private static void removeSource(final PerkAttributeMap attrMap, final Player player, final LogicalSide side, final ModifierSource remove) {
        ModifierManager.removeModifier(player, side, remove);
        final Collection<ModifierSource> sources = ModifierManager.getAppliedModifiers(player, side);
        sources.forEach(source -> {
            removeModifiers(source, attrMap, player, side);
            ModifierManager.removeModifier(player, side, source);
            return;
        });
        final Collection<PerkAttributeModifier> removedModifiers = removeModifiers(remove, attrMap, player, side);
        if (remove instanceof AttributeConverterProvider) {
            ((AttributeConverterProvider)remove).getConverters(player, side, false).forEach(c -> attrMap.removeConverter(player, c));
        }
        sources.forEach(source -> {
            applyModifiers(source, attrMap, player, side);
            ModifierManager.addModifier(player, side, source);
            return;
        });
        final PerkAttributeMap map = PerkAttributeHelper.getOrCreateMap(player, side);
        removedModifiers.forEach(mod -> mod.getAttributeType().onRemove(player, side, !map.hasModifiers(mod.getAttributeType()), remove));
    }
    
    private static Collection<PerkAttributeModifier> removeModifiers(final ModifierSource source, final PerkAttributeMap attrMap, final Player player, final LogicalSide side) {
        final Collection<PerkAttributeModifier> removedModifiers = new ArrayList<PerkAttributeModifier>();
        if (source instanceof AttributeModifierProvider) {
            for (final PerkAttributeModifier modifier : ((AttributeModifierProvider)source).getModifiers(player, side, false)) {
                removedModifiers.addAll(attrMap.removeModifier(player, modifier, source));
            }
        }
        return removedModifiers;
    }
    
    public enum Action
    {
        ADD, 
        REMOVE;
        
        private boolean isRemove() {
            return this == Action.REMOVE;
        }
    }
}
