package hellfirepvp.astralsorcery.common.registry;

import com.google.common.collect.Lists;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.block.tile.BlockCelestialCrystalCluster;
import hellfirepvp.astralsorcery.common.block.tile.BlockGemCrystalCluster;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.ToolAction;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlockProperties;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.util.NameUtil;
import net.minecraft.world.item.BlockItem;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.block.Block;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.level.block.DispenserBlock;
import hellfirepvp.astralsorcery.common.util.dispenser.FluidContainerDispenseBehavior;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.common.item.crystal.ItemAttunedCelestialCrystal;
import hellfirepvp.astralsorcery.common.item.crystal.ItemCelestialCrystal;
import hellfirepvp.astralsorcery.common.item.crystal.ItemAttunedRockCrystal;
import hellfirepvp.astralsorcery.common.item.crystal.ItemRockCrystal;
import hellfirepvp.astralsorcery.common.item.lens.ItemColoredLensSpectral;
import hellfirepvp.astralsorcery.common.item.lens.ItemColoredLensPush;
import hellfirepvp.astralsorcery.common.item.lens.ItemColoredLensRegeneration;
import hellfirepvp.astralsorcery.common.item.lens.ItemColoredLensDamage;
import hellfirepvp.astralsorcery.common.item.lens.ItemColoredLensGrowth;
import hellfirepvp.astralsorcery.common.item.lens.ItemColoredLensBreak;
import hellfirepvp.astralsorcery.common.item.lens.ItemColoredLensFire;
import hellfirepvp.astralsorcery.common.item.useables.ItemShiftingStarVicio;
import hellfirepvp.astralsorcery.common.item.useables.ItemShiftingStarEvorsio;
import hellfirepvp.astralsorcery.common.item.useables.ItemShiftingStarDiscidia;
import hellfirepvp.astralsorcery.common.item.useables.ItemShiftingStarArmara;
import hellfirepvp.astralsorcery.common.item.useables.ItemShiftingStarAevitas;
import hellfirepvp.astralsorcery.common.item.useables.ItemShiftingStar;
import hellfirepvp.astralsorcery.common.item.dust.ItemIlluminationPowder;
import hellfirepvp.astralsorcery.common.item.dust.ItemNocturnalPowder;
import hellfirepvp.astralsorcery.common.item.useables.ItemPerkSeal;
import hellfirepvp.astralsorcery.common.item.armor.ItemMantle;
import hellfirepvp.astralsorcery.common.item.ItemInfusedGlass;
import hellfirepvp.astralsorcery.common.item.ItemHandTelescope;
import hellfirepvp.astralsorcery.common.item.wand.ItemBlinkWand;
import hellfirepvp.astralsorcery.common.item.wand.ItemGrappleWand;
import hellfirepvp.astralsorcery.common.item.wand.ItemExchangeWand;
import hellfirepvp.astralsorcery.common.item.wand.ItemArchitectWand;
import hellfirepvp.astralsorcery.common.item.wand.ItemIlluminationWand;
import hellfirepvp.astralsorcery.common.item.ItemLinkingTool;
import hellfirepvp.astralsorcery.common.item.ItemResonator;
import hellfirepvp.astralsorcery.common.item.ItemChisel;
import hellfirepvp.astralsorcery.common.item.wand.ItemWand;
import hellfirepvp.astralsorcery.common.item.ItemKnowledgeShare;
import hellfirepvp.astralsorcery.common.item.ItemEnchantmentAmulet;
import hellfirepvp.astralsorcery.common.item.ItemConstellationPaper;
import hellfirepvp.astralsorcery.common.item.ItemTome;
import hellfirepvp.astralsorcery.common.item.tool.ItemInfusedCrystalSword;
import hellfirepvp.astralsorcery.common.item.tool.ItemInfusedCrystalShovel;
import hellfirepvp.astralsorcery.common.item.tool.ItemInfusedCrystalPickaxe;
import hellfirepvp.astralsorcery.common.item.tool.ItemInfusedCrystalAxe;
import hellfirepvp.astralsorcery.common.item.tool.ItemCrystalSword;
import hellfirepvp.astralsorcery.common.item.tool.ItemCrystalShovel;
import hellfirepvp.astralsorcery.common.item.tool.ItemCrystalPickaxe;
import hellfirepvp.astralsorcery.common.item.tool.ItemCrystalAxe;
import hellfirepvp.astralsorcery.common.item.gem.ItemPerkGemNight;
import hellfirepvp.astralsorcery.common.item.gem.ItemPerkGemDay;
import hellfirepvp.astralsorcery.common.item.gem.ItemPerkGemSky;
import hellfirepvp.astralsorcery.common.item.ItemStardust;
import hellfirepvp.astralsorcery.common.item.ItemStarmetalIngot;
import hellfirepvp.astralsorcery.common.item.ItemParchment;
import hellfirepvp.astralsorcery.common.item.ItemGlassLens;
import hellfirepvp.astralsorcery.common.item.ItemResonatingGem;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.item.ItemAquamarine;
import hellfirepvp.astralsorcery.common.item.base.client.ItemDynamicColor;
import java.util.List;

public class RegistryItems
{
    private static final List<ItemDynamicColor> colorItems;
    
    private RegistryItems() {
    }
    
    public static void registerItems() {
        ItemsAS.AQUAMARINE = registerItem(new ItemAquamarine());
        ItemsAS.RESONATING_GEM = registerItem(new ItemResonatingGem());
        ItemsAS.GLASS_LENS = registerItem(new ItemGlassLens());
        ItemsAS.PARCHMENT = registerItem(new ItemParchment());
        ItemsAS.STARMETAL_INGOT = registerItem(new ItemStarmetalIngot());
        ItemsAS.STARDUST = registerItem(new ItemStardust());
        ItemsAS.PERK_GEM_SKY = registerItem(new ItemPerkGemSky());
        ItemsAS.PERK_GEM_DAY = registerItem(new ItemPerkGemDay());
        ItemsAS.PERK_GEM_NIGHT = registerItem(new ItemPerkGemNight());
        ItemsAS.CRYSTAL_AXE = registerItem(new ItemCrystalAxe());
        ItemsAS.CRYSTAL_PICKAXE = registerItem(new ItemCrystalPickaxe());
        ItemsAS.CRYSTAL_SHOVEL = registerItem(new ItemCrystalShovel());
        ItemsAS.CRYSTAL_SWORD = registerItem(new ItemCrystalSword());
        ItemsAS.INFUSED_CRYSTAL_AXE = registerItem(new ItemInfusedCrystalAxe());
        ItemsAS.INFUSED_CRYSTAL_PICKAXE = registerItem(new ItemInfusedCrystalPickaxe());
        ItemsAS.INFUSED_CRYSTAL_SHOVEL = registerItem(new ItemInfusedCrystalShovel());
        ItemsAS.INFUSED_CRYSTAL_SWORD = registerItem(new ItemInfusedCrystalSword());
        ItemsAS.TOME = registerItem(new ItemTome());
        ItemsAS.CONSTELLATION_PAPER = registerItem(new ItemConstellationPaper());
        ItemsAS.ENCHANTMENT_AMULET = registerItem(new ItemEnchantmentAmulet());
        ItemsAS.KNOWLEDGE_SHARE = registerItem(new ItemKnowledgeShare());
        ItemsAS.WAND = registerItem(new ItemWand());
        ItemsAS.CHISEL = registerItem(new ItemChisel());
        ItemsAS.RESONATOR = registerItem(new ItemResonator());
        ItemsAS.LINKING_TOOL = registerItem(new ItemLinkingTool());
        ItemsAS.ILLUMINATION_WAND = registerItem(new ItemIlluminationWand());
        ItemsAS.ARCHITECT_WAND = registerItem(new ItemArchitectWand());
        ItemsAS.EXCHANGE_WAND = registerItem(new ItemExchangeWand());
        ItemsAS.GRAPPLE_WAND = registerItem(new ItemGrappleWand());
        ItemsAS.BLINK_WAND = registerItem(new ItemBlinkWand());
        ItemsAS.HAND_TELESCOPE = registerItem(new ItemHandTelescope());
        ItemsAS.INFUSED_GLASS = registerItem(new ItemInfusedGlass());
        ItemsAS.MANTLE = registerItem(new ItemMantle());
        ItemsAS.PERK_SEAL = registerItem(new ItemPerkSeal());
        ItemsAS.NOCTURNAL_POWDER = registerItem(new ItemNocturnalPowder());
        ItemsAS.ILLUMINATION_POWDER = registerItem(new ItemIlluminationPowder());
        ItemsAS.SHIFTING_STAR = registerItem(new ItemShiftingStar());
        ItemsAS.SHIFTING_STAR_AEVITAS = registerItem(new ItemShiftingStarAevitas());
        ItemsAS.SHIFTING_STAR_ARMARA = registerItem(new ItemShiftingStarArmara());
        ItemsAS.SHIFTING_STAR_DISCIDIA = registerItem(new ItemShiftingStarDiscidia());
        ItemsAS.SHIFTING_STAR_EVORSIO = registerItem(new ItemShiftingStarEvorsio());
        ItemsAS.SHIFTING_STAR_VICIO = registerItem(new ItemShiftingStarVicio());
        ItemsAS.COLORED_LENS_FIRE = registerItem(new ItemColoredLensFire());
        ItemsAS.COLORED_LENS_BREAK = registerItem(new ItemColoredLensBreak());
        ItemsAS.COLORED_LENS_GROWTH = registerItem(new ItemColoredLensGrowth());
        ItemsAS.COLORED_LENS_DAMAGE = registerItem(new ItemColoredLensDamage());
        ItemsAS.COLORED_LENS_REGENERATION = registerItem(new ItemColoredLensRegeneration());
        ItemsAS.COLORED_LENS_PUSH = registerItem(new ItemColoredLensPush());
        ItemsAS.COLORED_LENS_SPECTRAL = registerItem(new ItemColoredLensSpectral());
        ItemsAS.ROCK_CRYSTAL = registerItem(new ItemRockCrystal());
        ItemsAS.ATTUNED_ROCK_CRYSTAL = registerItem(new ItemAttunedRockCrystal());
        ItemsAS.CELESTIAL_CRYSTAL = registerItem(new ItemCelestialCrystal());
        ItemsAS.ATTUNED_CELESTIAL_CRYSTAL = registerItem(new ItemAttunedCelestialCrystal());
    }
    
    public static void registerItemBlocks() {
        RegistryBlocks.ITEM_BLOCKS.forEach(RegistryItems::registerItemBlock);
    }
    
    public static void registerFluidContainerItems() {
        RegistryFluids.FLUID_HOLDER_ITEMS.forEach(RegistryItems::registerItem);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void registerColors(final ColorHandlerEvent.Item itemColorEvent) {
        RegistryItems.colorItems.forEach(item -> itemColorEvent.getItemColors().func_199877_a(item::getColor, new ItemLike[] { (ItemLike)item }));
    }
    
    public static void registerDispenseBehaviors() {
        DispenserBlock.func_199774_a((ItemLike)ItemsAS.BUCKET_LIQUID_STARLIGHT, (DispenseItemBehavior)FluidContainerDispenseBehavior.getInstance());
    }
    
    public static void registerItemProperties() {
        ItemModelsProperties.func_239418_a_((Item)ItemsAS.INFUSED_GLASS, new ResourceLocation("engraved"), (stack, world, entity) -> (ItemInfusedGlass.getEngraving(stack) != null) ? 1.0f : 0.0f);
        ItemModelsProperties.func_239418_a_((Item)ItemsAS.KNOWLEDGE_SHARE, new ResourceLocation("written"), (stack, world, entity) -> (ItemKnowledgeShare.isCreative(stack) || ItemKnowledgeShare.getKnowledge(stack) != null) ? 1.0f : 0.0f);
        ItemModelsProperties.func_239418_a_((Item)ItemsAS.RESONATOR, new ResourceLocation("upgrade"), (stack, world, entity) -> {
            if (!(entity instanceof Player)) {
                return ItemResonator.ResonatorUpgrade.STARLIGHT.ordinal() / (float)ItemResonator.ResonatorUpgrade.values().length;
            }
            final ItemResonator.ResonatorUpgrade current = ItemResonator.getCurrentUpgrade((Player)entity, stack);
            return current.ordinal() / (float)ItemResonator.ResonatorUpgrade.values().length;
        });
        ItemModelsProperties.func_239418_a_(Item.func_150898_a((Block)BlocksAS.CELESTIAL_CRYSTAL_CLUSTER), new ResourceLocation("stage"), (stack, world, entity) -> stack.getDamageValue() / (float)BlockCelestialCrystalCluster.STAGE.getPossibleValues().size());
        ItemModelsProperties.func_239418_a_(Item.func_150898_a((Block)BlocksAS.GEM_CRYSTAL_CLUSTER), new ResourceLocation("stage"), (stack, world, entity) -> stack.getDamageValue() / (float)BlockGemCrystalCluster.STAGE.getPossibleValues().size());
    }
    
    private static void registerItemBlock(final CustomItemBlock block) {
        final BlockItem itemBlock = block.createItemBlock(buildItemBlockProperties((Block)block));

        AstralSorcery.getProxy().getRegistryPrimer().register(itemBlock);
    }
    
    private static <T extends Item> T registerItem(final T item) {
        final ResourceLocation name = NameUtil.fromClass(item, "Item");

        AstralSorcery.getProxy().getRegistryPrimer().register(item);
        if (item instanceof ItemDynamicColor) {
            RegistryItems.colorItems.add((ItemDynamicColor)item);
        }
        return item;
    }
    
    private static Item.Properties buildItemBlockProperties(final Block block) {
        final Item.Properties props = new Item.Properties();
        props.hasModifier(CommonProxy.ITEM_GROUP_AS);
        if (block instanceof CustomItemBlockProperties) {
            final CreativeModeTab group = ((CustomItemBlockProperties)block).getItemGroup();
            if (group != null) {
                props.hasModifier(group);
            }
            if (!((CustomItemBlockProperties)block).canItemBeRepaired()) {
                props.setNoRepair();
            }
            props.func_208103_a(((CustomItemBlockProperties)block).getItemRarity());
            props.func_200917_a(((CustomItemBlockProperties)block).getItemMaxStackSize());
            props.func_200915_b(((CustomItemBlockProperties)block).getItemMaxDamage());
            props.func_200919_a(((CustomItemBlockProperties)block).getContainerItem());
            props.setISTER((Supplier)((CustomItemBlockProperties)block).getItemTEISR());
            ((CustomItemBlockProperties)block).getItemToolLevels().forEach(props::addToolType);
        }
        return props;
    }
    
    static {
        colorItems = Lists.newArrayList();
    }
}
