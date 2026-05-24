package hellfirepvp.astralsorcery.common.perk.node.socket;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.List;
import hellfirepvp.astralsorcery.common.data.research.PlayerPerkData;
import net.minecraft.network.chat.Component;
import java.util.ArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.IFormattableTextComponent;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;

public interface GemSocketPerk
{
    public static final String SOCKET_DATA_KEY = "socketedItem";
    
    default boolean hasItem(final Player player, final LogicalSide side) {
        return this.hasItem(player, side, null);
    }
    
    default boolean hasItem(final Player player, final LogicalSide side, @Nullable final CompoundTag data) {
        return !this.getContainedItem(player, side, data).isEmpty();
    }
    
    default ItemStack getContainedItem(final Player player, final LogicalSide side) {
        return this.getContainedItem(player, side, null);
    }
    
    default ItemStack getContainedItem(final Player player, final LogicalSide side, @Nullable final CompoundTag dataOvr) {
        if (!(this instanceof AbstractPerk)) {
            throw new UnsupportedOperationException("Cannot do perk-specific socketing logic on something that's not a perk!");
        }
        final CompoundTag data = (dataOvr != null) ? dataOvr : ((AbstractPerk)this).getPerkData(player, side);
        if (data == null) {
            return ItemStack.field_190927_a;
        }
        final ItemStack stack = NBTHelper.getStack(data, "socketedItem");
        return (stack != null) ? stack : ItemStack.field_190927_a;
    }
    
    default boolean setContainedItem(final Player player, final LogicalSide side, final ItemStack stack) {
        return this.setContainedItem(player, side, null, stack);
    }
    
    default <T extends AbstractPerk & GemSocketPerk> boolean setContainedItem(final Player player, final LogicalSide side, @Nullable final CompoundTag dataOvr, final ItemStack stack) {
        if (!(this instanceof AbstractPerk)) {
            throw new UnsupportedOperationException("Cannot do perk-specific socketing logic on something that's not a perk!");
        }
        final T thisPerk = (T)this;
        final PlayerProgress prog = ResearchHelper.getProgress(player, side);
        if (!prog.getPerkData().hasPerkEffect(thisPerk)) {
            return false;
        }
        final boolean useLiveData = dataOvr == null;
        CompoundTag data = dataOvr;
        if (useLiveData) {
            data = ((AbstractPerk)this).getPerkData(player, side);
        }
        if (data == null) {
            return false;
        }
        final CompoundTag prev = data.func_74737_b();
        if (stack.isEmpty()) {
            final ItemStack existing = NBTHelper.getStack(data, "socketedItem");
            if (!existing.isEmpty() && existing.getItem() instanceof GemSocketItem) {
                ((GemSocketItem)existing.getItem()).onExtract(existing, thisPerk, player, prog);
            }
            data.func_82580_o("socketedItem");
        }
        else {
            if (stack.getItem() instanceof GemSocketItem) {
                ((GemSocketItem)stack.getItem()).onInsert(stack, thisPerk, player, prog);
            }
            NBTHelper.setStack(data, "socketedItem", stack);
        }
        if (useLiveData) {
            ResearchManager.setPerkData(player, thisPerk, prev, data);
        }
        return true;
    }
    
    default void dropItemToPlayer(final Player player) {
        this.dropItemToPlayer(player, null);
    }
    
    default void dropItemToPlayer(final Player player, @Nullable CompoundTag data) {
        if (!(this instanceof AbstractPerk)) {
            throw new UnsupportedOperationException("Cannot do perk-specific socketing logic on something that's not a perk!");
        }
        if (player.func_130014_f_().func_201670_d()) {
            return;
        }
        final boolean updateData = data == null;
        if (updateData) {
            data = ((AbstractPerk)this).getPerkData(player, LogicalSide.SERVER);
        }
        if (data == null) {
            return;
        }
        final CompoundTag prev = data.func_74737_b();
        final ItemStack contained = this.getContainedItem(player, LogicalSide.SERVER, data);
        if (!contained.isEmpty() && !player.func_191521_c(contained)) {
            ItemUtils.dropItem(player.func_130014_f_(), player.func_226277_ct_(), player.func_226278_cu_(), player.func_226281_cx_(), contained);
        }
        this.setContainedItem(player, LogicalSide.SERVER, data, ItemStack.field_190927_a);
        if (updateData) {
            ResearchManager.setPerkData(player, (AbstractPerk)this, prev, data);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    default <T extends AbstractPerk & GemSocketPerk> void addTooltipInfo(final Collection<IFormattableTextComponent> tooltip) {
        if (!(this instanceof AbstractPerk)) {
            return;
        }
        final T thisPerk = (T)this;
        final PlayerProgress prog = ResearchHelper.getClientProgress();
        if (!prog.isValid()) {
            return;
        }
        final PlayerPerkData perkData = prog.getPerkData();
        final ItemStack contained = this.getContainedItem((Player)Minecraft.func_71410_x().field_71439_g, LogicalSide.CLIENT);
        if (contained.isEmpty()) {
            tooltip.add(new Component("perk.info.astralsorcery.gem.empty").func_240699_a_(ChatFormatting.GRAY));
            if (perkData.hasPerkEffect(thisPerk)) {
                tooltip.add(new Component("perk.info.astralsorcery.gem.content.empty").func_240699_a_(ChatFormatting.GRAY));
                final boolean has = !ItemUtils.findItemsIndexedInPlayerInventory((Player)Minecraft.func_71410_x().field_71439_g, stack -> {
                    if (stack.isEmpty() || !(stack.getItem() instanceof GemSocketItem)) {
                        return false;
                    }
                    else {
                        final GemSocketItem item2 = (GemSocketItem)stack.getItem();
                        return item2.canBeInserted(stack, thisPerk, (Player)Minecraft.func_71410_x().field_71439_g, ResearchHelper.getClientProgress(), LogicalSide.CLIENT);
                    }
                }).isEmpty();
                if (!has) {
                    tooltip.add(new Component("perk.info.astralsorcery.gem.content.empty.none").func_240699_a_(ChatFormatting.RED));
                }
            }
        }
        else {
            if (contained.getItem() instanceof GemSocketItem) {
                final GemSocketItem item = (GemSocketItem)contained.getItem();
                final List<IFormattableTextComponent> additionalToolTip = new ArrayList<IFormattableTextComponent>();
                item.addTooltip(contained, thisPerk, additionalToolTip);
                if (!additionalToolTip.isEmpty()) {
                    tooltip.addAll(additionalToolTip);
                    tooltip.add((IFormattableTextComponent)new Component(""));
                }
            }
            tooltip.add(new Component("perk.info.astralsorcery.gem.content.item", new Object[] { contained.func_200301_q() }).func_240699_a_(ChatFormatting.GRAY));
            if (perkData.hasPerkEffect(thisPerk)) {
                tooltip.add(new Component("perk.info.astralsorcery.gem.remove").func_240699_a_(ChatFormatting.GRAY));
            }
        }
    }
}
