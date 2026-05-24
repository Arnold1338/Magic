package hellfirepvp.astralsorcery.common.integration.jei;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.inventory.AbstractContainerMenu;
import javax.annotation.Nullable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.network.packets.PacketJei;
import mezz.jei.network.Network;
import java.util.List;
import mezz.jei.network.packets.PacketRecipeTransfer;
import hellfirepvp.astralsorcery.common.util.MapStream;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Collection;
import mezz.jei.transfer.RecipeTransferUtil;
import java.util.Map;
import net.minecraft.world.level.item.ItemStack;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import net.minecraft.world.level.inventory.Slot;
import java.util.HashMap;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import mezz.jei.util.Translator;
import mezz.jei.config.ServerInfo;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import net.minecraft.world.level.entity.player.Player;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.helpers.IStackHelper;
import org.apache.logging.log4j.Logger;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import hellfirepvp.astralsorcery.common.container.ContainerAltarBase;

public class TieredAltarRecipeTransferHandler<C extends ContainerAltarBase> implements IRecipeTransferHandler<C>
{
    private static final Logger LOGGER;
    private final Class<C> containerClass;
    private final IStackHelper stackHelper;
    private final IRecipeTransferHandlerHelper handlerHelper;
    private final int maxListSize;
    
    public TieredAltarRecipeTransferHandler(final Class<C> containerClass, final IStackHelper stackHelper, final IRecipeTransferHandlerHelper handlerHelper, final int maxListSize) {
        this.containerClass = containerClass;
        this.stackHelper = stackHelper;
        this.handlerHelper = handlerHelper;
        this.maxListSize = maxListSize;
    }
    
    public Class<C> getContainerClass() {
        return this.containerClass;
    }
    
    @Nullable
    public IRecipeTransferError transferRecipe(final C container, final IRecipeLayout recipeLayout, final Player player, final boolean maxTransfer, final boolean doTransfer) {
        if (!ServerInfo.isJeiOnServer()) {
            final String tooltipMessage = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.no.server");
            return this.handlerHelper.createUserErrorWithTooltip(tooltipMessage);
        }
        if (!this.containerClass.isAssignableFrom(container.getClass())) {
            return this.handlerHelper.createInternalError();
        }
        final IRecipeCategory<?> category = (IRecipeCategory<?>)recipeLayout.getRecipeCategory();
        if (!(category instanceof CategoryAltar)) {
            return this.handlerHelper.createInternalError();
        }
        final AltarType recipeTier = ((CategoryAltar)category).getAltarType();
        final AltarType altarTier = container.getTileEntity().getAltarType();
        final Map<Integer, Slot> inventorySlots = new HashMap<Integer, Slot>();
        for (final Slot slot : container.field_75151_b.subList(0, 36)) {
            inventorySlots.put(slot.field_75222_d, slot);
        }
        final Map<Integer, Slot> craftingSlots = new HashMap<Integer, Slot>();
        for (final Slot slot2 : container.field_75151_b.subList(36, 36 + this.maxListSize)) {
            craftingSlots.put(slot2.field_75222_d, slot2);
        }
        int inputCount = 0;
        final IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();
        final Map<Integer, IGuiIngredient<ItemStack>> itemStacks = new HashMap<Integer, IGuiIngredient<ItemStack>>();
        for (final Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>> entry : itemStackGroup.getGuiIngredients().entrySet()) {
            if (entry.getKey() < 25) {
                itemStacks.put(entry.getKey(), (IGuiIngredient<ItemStack>)entry.getValue());
            }
        }
        for (final IGuiIngredient<ItemStack> ingredient : itemStacks.values()) {
            if (ingredient.isInput() && !ingredient.getAllIngredients().isEmpty()) {
                ++inputCount;
            }
        }
        if (inputCount > craftingSlots.size()) {
            TieredAltarRecipeTransferHandler.LOGGER.error("Recipe Transfer does not work for container {}", (Object)container.getClass());
            return this.handlerHelper.createInternalError();
        }
        final Map<Integer, ItemStack> availableItemStacks = new HashMap<Integer, ItemStack>();
        int filledCraftSlotCount = 0;
        int emptySlotCount = 0;
        for (final Slot slot3 : craftingSlots.values()) {
            final ItemStack stack = slot3.func_75211_c();
            if (!stack.isEmpty()) {
                if (!slot3.func_82869_a(player)) {
                    TieredAltarRecipeTransferHandler.LOGGER.error("Recipe Transfer does not work for container {}. Player can't move item out of Crafting Slot number {}", (Object)container.getClass(), (Object)slot3.field_75222_d);
                    return this.handlerHelper.createInternalError();
                }
                ++filledCraftSlotCount;
                availableItemStacks.put(slot3.field_75222_d, stack.copy());
            }
        }
        for (final Slot slot3 : inventorySlots.values()) {
            final ItemStack stack = slot3.func_75211_c();
            if (!stack.isEmpty()) {
                availableItemStacks.put(slot3.field_75222_d, stack.copy());
            }
            else {
                ++emptySlotCount;
            }
        }
        if (filledCraftSlotCount - inputCount > emptySlotCount) {
            final String message = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.inventory.full");
            return this.handlerHelper.createUserErrorWithTooltip(message);
        }
        final RecipeTransferUtil.MatchingItemsResult matchingItemsResult = RecipeTransferUtil.getMatchingItems(this.stackHelper, (Map)availableItemStacks, (Map)itemStacks);
        if (matchingItemsResult.missingItems.size() > 0) {
            final String message2 = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.missing");
            return this.handlerHelper.createUserErrorForSlots(message2, (Collection)matchingItemsResult.missingItems);
        }
        final List<Integer> craftingSlotIndexes = new ArrayList<Integer>(craftingSlots.keySet());
        Collections.sort(craftingSlotIndexes);
        final List<Integer> inventorySlotIndexes = new ArrayList<Integer>(inventorySlots.keySet());
        Collections.sort(inventorySlotIndexes);
        final Map<Integer, Integer> matchIndices = MapStream.of((Map<Integer, Integer>)matchingItemsResult.matchingItems).mapKey(container::translateIndex).toMap();
        for (final Map.Entry<Integer, Integer> entry2 : matchIndices.entrySet()) {
            final int craftNumber = entry2.getKey();
            final int slotNumber = craftingSlotIndexes.get(craftNumber);
            if (slotNumber < 0 || slotNumber >= container.field_75151_b.size()) {
                TieredAltarRecipeTransferHandler.LOGGER.error("Recipes Transfer references slot {} outside of the inventory's size {}", (Object)slotNumber, (Object)container.field_75151_b.size());
                return this.handlerHelper.createInternalError();
            }
        }
        if (doTransfer) {
            final PacketRecipeTransfer packet = new PacketRecipeTransfer((Map)matchIndices, (List)craftingSlotIndexes, (List)inventorySlotIndexes, maxTransfer, true);
            Network.sendPacketToServer((PacketJei)packet);
        }
        return null;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
