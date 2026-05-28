package hellfirepvp.astralsorcery.common.crafting.custom;

import hellfirepvp.astralsorcery.common.block.tile.BlockCelestialGateway;
import net.minecraft.world.level.block.Block;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.item.wand.ItemIlluminationWand;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import net.minecraft.world.item.crafting.SpecialRecipeSerializer;
import net.minecraft.world.level.Container;
import javax.annotation.Nullable;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.CraftingInventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import java.util.function.BiConsumer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import java.util.function.Supplier;
import net.minecraft.world.item.crafting.SpecialRecipe;

public class RecipeDyeableChangeColor extends SpecialRecipe
{
    private final Supplier<RecipeSerializer<?>> serializer;
    private final Item targetItem;
    private final BiConsumer<ItemStack, DyeColor> colorFn;
    
    public RecipeDyeableChangeColor(final ResourceLocation idIn, final Supplier<RecipeSerializer<?>> serializer, final Item targetItem, final BiConsumer<ItemStack, DyeColor> colorFn) {
        super(idIn);
        this.serializer = serializer;
        this.targetItem = targetItem;
        this.colorFn = colorFn;
    }
    
    public boolean matches(final CraftingInventory inv, final Level worldIn) {
        return this.tryFindValidRecipeAndDye(inv) != null;
    }
    
    public ItemStack getCraftingResult(final CraftingInventory inv) {
        final Tuple<DyeColor, ItemStack> itemColorTpl = this.tryFindValidRecipeAndDye(inv);
        if (itemColorTpl == null) {
            return ItemStack.EMPTY;
        }
        final ItemStack out = ItemUtils.copyStackWithSize((ItemStack)itemColorTpl.getB(), 1);
        this.colorFn.accept(out, (DyeColor)itemColorTpl.getA());
        return out;
    }
    
    @Nullable
    private Tuple<DyeColor, ItemStack> tryFindValidRecipeAndDye(final CraftingInventory inv) {
        ItemStack itemFound = ItemStack.EMPTY;
        DyeColor dyeColorFound = null;
        int nonEmptyItemsFound = 0;
        for (int slot = 0; slot < inv.func_70302_i_(); ++slot) {
            final ItemStack in = inv.func_70301_a(slot);
            if (!in.isEmpty()) {
                ++nonEmptyItemsFound;
                if (in.getItem().equals(this.targetItem)) {
                    itemFound = in;
                }
                else {
                    final DyeColor color = DyeColor.getColor(in);
                    if (color != null) {
                        dyeColorFound = color;
                    }
                }
            }
        }
        if (itemFound.isEmpty() || dyeColorFound == null || nonEmptyItemsFound != 2) {
            return null;
        }
        return (Tuple<DyeColor, ItemStack>)new Tuple((Object)dyeColorFound, (Object)itemFound);
    }
    
    public boolean func_194133_a(final int width, final int height) {
        return width * height >= 2;
    }
    
    public RecipeSerializer<?> func_199559_b() {
        return this.serializer.get();
    }
    
    public static class IlluminationWandColorSerializer extends SpecialRecipeSerializer<RecipeDyeableChangeColor>
    {
        public IlluminationWandColorSerializer() {
            super(id -> new RecipeDyeableChangeColor(id, () -> RecipeSerializersAS.CUSTOM_CHANGE_WAND_COLOR_SERIALIZER, (Item)ItemsAS.ILLUMINATION_WAND, ItemIlluminationWand::setConfiguredColor));
            this.setRegistryName(RecipeSerializersAS.CUSTOM_CHANGE_WAND_COLOR);
        }
    }
    
    public static class CelestialGatewayColorSerializer extends SpecialRecipeSerializer<RecipeDyeableChangeColor>
    {
        public CelestialGatewayColorSerializer() {
            super(id -> new RecipeDyeableChangeColor(id, () -> RecipeSerializersAS.CUSTOM_CHANGE_GATEWAY_COLOR_SERIALIZER, Item.func_150898_a((Block)BlocksAS.GATEWAY), BlockCelestialGateway::setColor));
            this.setRegistryName(RecipeSerializersAS.CUSTOM_CHANGE_GATEWAY_COLOR);
        }
    }
}
