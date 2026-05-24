package hellfirepvp.astralsorcery.common.item;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.data.research.ProgressionTier;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.play.server.PktProgressionUpdate;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.item.ItemUseContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.Item;

public class ItemKnowledgeShare extends Item
{
    public ItemKnowledgeShare() {
        super(new Item.Properties().func_200917_a(1).func_200916_a(CommonProxy.ITEM_GROUP_AS));
    }
    
    public void func_150895_a(final CreativeModeTab group, final NonNullList<ItemStack> items) {
        if (this.func_194125_a(group)) {
            items.add((Object)new ItemStack((ItemLike)this));
            final ItemStack creative = new ItemStack((ItemLike)this);
            this.setCreative(creative);
            items.add((Object)creative);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public void func_77624_a(final ItemStack stack, @Nullable final World world, final List<Component> tooltip, final ITooltipFlag flag) {
        if (isCreative(stack)) {
            tooltip.add((Component)new Component("astralsorcery.misc.knowledge.inscribed.creative").func_240699_a_(ChatFormatting.LIGHT_PURPLE));
            return;
        }
        if (getKnowledge(stack) == null) {
            tooltip.add((Component)new Component("astralsorcery.misc.knowledge.missing").func_240699_a_(ChatFormatting.GRAY));
        }
        else {
            final IFormattableTextComponent name = getKnowledgeOwnerName(stack);
            if (name != null) {
                tooltip.add((Component)new Component("astralsorcery.misc.knowledge.inscribed", new Object[] { name }).func_240699_a_(ChatFormatting.BLUE));
            }
        }
    }
    
    public InteractionResult<ItemStack> func_77659_a(final World world, final Player player, final Hand hand) {
        final ItemStack held = player.func_184586_b(hand);
        if (held.isEmpty() || world.func_201670_d() || !(held.getItem() instanceof ItemKnowledgeShare)) {
            return (InteractionResult<ItemStack>)InteractionResult.func_226248_a_((Object)held);
        }
        if (!isCreative(held) && (player.func_225608_bj_() || getKnowledge(held) == null)) {
            this.tryInscribeKnowledge(held, player);
        }
        else {
            this.tryGiveKnowledge(held, player);
        }
        return (InteractionResult<ItemStack>)InteractionResult.func_226248_a_((Object)held);
    }
    
    public InteractionResult func_195939_a(final ItemUseContext context) {
        final ItemStack stack = context.func_195996_i();
        final Player player = context.func_195999_j();
        if (stack.isEmpty() || player == null || context.func_195991_k().func_201670_d() || !(stack.getItem() instanceof ItemKnowledgeShare)) {
            return InteractionResult.SUCCESS;
        }
        if (!isCreative(stack) && (player.func_225608_bj_() || getKnowledge(stack) == null)) {
            this.tryInscribeKnowledge(stack, player);
        }
        else {
            this.tryGiveKnowledge(stack, player);
        }
        return InteractionResult.SUCCESS;
    }
    
    private void tryGiveKnowledge(final ItemStack stack, final Player player) {
        if (player instanceof ServerPlayer && MiscUtils.isPlayerFakeMP((ServerPlayer)player)) {
            return;
        }
        if (isCreative(stack)) {
            ResearchManager.forceMaximizeAll(player);
            return;
        }
        if (canInscribeKnowledge(stack, player)) {
            return;
        }
        final PlayerProgress progress = getKnowledge(stack);
        if (progress == null) {
            return;
        }
        final ProgressionTier prev = progress.getTierReached();
        if (ResearchHelper.mergeApplyPlayerprogress(progress, player) && progress.getTierReached().isThisLater(prev)) {
            final PktProgressionUpdate pkt = new PktProgressionUpdate(progress.getTierReached());
            PacketChannel.CHANNEL.sendToPlayer(player, pkt);
        }
    }
    
    private void tryInscribeKnowledge(final ItemStack stack, final Player player) {
        if (canInscribeKnowledge(stack, player)) {
            setKnowledge(stack, player, ResearchHelper.getProgress(player, LogicalSide.SERVER));
        }
    }
    
    @Nullable
    public static Player getKnowledgeOwner(final ItemStack stack, final MinecraftServer server) {
        if (isCreative(stack)) {
            return null;
        }
        final CompoundTag compound = NBTHelper.getPersistentData(stack);
        final UUID owner = NBTHelper.getUUID(compound, "knowledgeOwnerUUID", null);
        if (owner == null) {
            return null;
        }
        return (Player)server.getPlayerList().getPlayer(owner);
    }
    
    @Nullable
    public static IFormattableTextComponent getKnowledgeOwnerName(final ItemStack stack) {
        if (isCreative(stack)) {
            return null;
        }
        final CompoundTag compound = NBTHelper.getPersistentData(stack);
        if (!compound.contains("knowledgeOwnerName")) {
            return null;
        }
        return Component.Serializer.func_240643_a_(compound.getString("knowledgeOwnerName"));
    }
    
    @Nullable
    public static PlayerProgress getKnowledge(final ItemStack stack) {
        if (isCreative(stack)) {
            return null;
        }
        final CompoundTag compound = NBTHelper.getPersistentData(stack);
        if (!compound.contains("knowledgeTag")) {
            return null;
        }
        final CompoundTag tag = compound.func_74775_l("knowledgeTag");
        try {
            final PlayerProgress progress = new PlayerProgress();
            progress.loadKnowledge(tag);
            return progress;
        }
        catch (final Exception ignored) {
            return null;
        }
    }
    
    public static boolean canInscribeKnowledge(final ItemStack stack, final Player player) {
        if (isCreative(stack)) {
            return false;
        }
        final CompoundTag compound = NBTHelper.getPersistentData(stack);
        final UUID owner = NBTHelper.getUUID(compound, "knowledgeOwnerUUID", null);
        return owner == null || player.getUUID().equals(owner);
    }
    
    public static void setKnowledge(final ItemStack stack, final Player player, final PlayerProgress progress) {
        if (isCreative(stack) || !progress.isValid()) {
            return;
        }
        final CompoundTag knowledge = new CompoundTag();
        progress.storeKnowledge(knowledge);
        final CompoundTag compound = NBTHelper.getPersistentData(stack);
        compound.putString("knowledgeOwnerName", Component.Serializer.func_150696_a(player.func_145748_c_()));
        compound.putUUID("knowledgeOwnerUUID", player.getUUID());
        compound.put("knowledgeTag", (Tag)knowledge);
    }
    
    public static boolean isCreative(final ItemStack stack) {
        final CompoundTag cmp = NBTHelper.getPersistentData(stack);
        return cmp.contains("creativeKnowledge") && cmp.getBoolean("creativeKnowledge");
    }
    
    private void setCreative(final ItemStack stack) {
        NBTHelper.getPersistentData(stack).putBoolean("creativeKnowledge", true);
    }
}
