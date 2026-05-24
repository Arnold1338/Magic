package hellfirepvp.astralsorcery.common.integration;

import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import hellfirepvp.astralsorcery.common.item.ItemResonator;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.Optional;
import hellfirepvp.astralsorcery.AstralSorcery;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.helpers.IStackHelper;
import hellfirepvp.astralsorcery.common.container.ContainerAltarTrait;
import hellfirepvp.astralsorcery.common.container.ContainerAltarConstellation;
import hellfirepvp.astralsorcery.common.container.ContainerAltarAttunement;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import hellfirepvp.astralsorcery.common.container.ContainerAltarBase;
import hellfirepvp.astralsorcery.common.integration.jei.TieredAltarRecipeTransferHandler;
import hellfirepvp.astralsorcery.common.container.ContainerAltarDiscovery;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.world.item.crafting.Recipe;
import java.util.Collection;
import java.util.Comparator;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import hellfirepvp.astralsorcery.common.integration.jei.CategoryWell;
import hellfirepvp.astralsorcery.common.integration.jei.CategoryTransmutation;
import hellfirepvp.astralsorcery.common.integration.jei.CategoryLiquidInteraction;
import hellfirepvp.astralsorcery.common.integration.jei.CategoryInfuser;
import hellfirepvp.astralsorcery.common.block.tile.BlockAltar;
import hellfirepvp.astralsorcery.common.integration.jei.CategoryAltar;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.world.level.block.Block;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.world.item.Item;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.integration.jei.JEICategory;
import java.util.List;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.IModPlugin;

@JeiPlugin
public class IntegrationJEI implements IModPlugin
{
    public static final List<JEICategory<?>> CATEGORIES;
    public static final ResourceLocation CATEGORY_ALTAR_ATTUNEMENT;
    public static final ResourceLocation CATEGORY_ALTAR_CONSTELLATION;
    public static final ResourceLocation CATEGORY_ALTAR_DISCOVERY;
    public static final ResourceLocation CATEGORY_ALTAR_TRAIT;
    public static final ResourceLocation CATEGORY_INFUSER;
    public static final ResourceLocation CATEGORY_LIQUID_INTERACTION;
    public static final ResourceLocation CATEGORY_TRANSMUTATION;
    public static final ResourceLocation CATEGORY_WELL;
    public static IJeiRuntime runtime;
    
    public void registerItemSubtypes(final ISubtypeRegistration registry) {
        registry.useNbtForSubtypes(new Item[] { ItemsAS.ATTUNED_ROCK_CRYSTAL, ItemsAS.ATTUNED_CELESTIAL_CRYSTAL, Item.func_150898_a((Block)BlocksAS.ROCK_COLLECTOR_CRYSTAL), Item.func_150898_a((Block)BlocksAS.CELESTIAL_COLLECTOR_CRYSTAL), Item.func_150898_a((Block)BlocksAS.CELESTIAL_CRYSTAL_CLUSTER), Item.func_150898_a((Block)BlocksAS.GEM_CRYSTAL_CLUSTER) });
        registry.registerSubtypeInterpreter((Item)ItemsAS.RESONATOR, stack -> ItemResonator.getUpgrades(stack).stream().map((Function<? super Object, ?>)ItemResonator.ResonatorUpgrade::getAppendix).collect((Collector<? super Object, ?, String>)Collectors.joining(",")));
        registry.registerSubtypeInterpreter((Item)ItemsAS.MANTLE, stack -> Optional.ofNullable(ItemsAS.MANTLE.getConstellation(stack)).map((Function<? super IConstellation, ? extends String>)IConstellation::getSimpleName).orElse("none"));
    }
    
    public void registerCategories(final IRecipeCategoryRegistration registry) {
        final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        if (IntegrationJEI.CATEGORIES.isEmpty()) {
            IntegrationJEI.CATEGORIES.add(new CategoryAltar(IntegrationJEI.CATEGORY_ALTAR_DISCOVERY, "altar_discovery", BlocksAS.ALTAR_DISCOVERY, guiHelper));
            IntegrationJEI.CATEGORIES.add(new CategoryAltar(IntegrationJEI.CATEGORY_ALTAR_ATTUNEMENT, "altar_attunement", BlocksAS.ALTAR_ATTUNEMENT, guiHelper));
            IntegrationJEI.CATEGORIES.add(new CategoryAltar(IntegrationJEI.CATEGORY_ALTAR_CONSTELLATION, "altar_constellation", BlocksAS.ALTAR_CONSTELLATION, guiHelper));
            IntegrationJEI.CATEGORIES.add(new CategoryAltar(IntegrationJEI.CATEGORY_ALTAR_TRAIT, "altar_trait", BlocksAS.ALTAR_RADIANCE, guiHelper));
            IntegrationJEI.CATEGORIES.add(new CategoryInfuser(guiHelper));
            IntegrationJEI.CATEGORIES.add(new CategoryLiquidInteraction(guiHelper));
            IntegrationJEI.CATEGORIES.add(new CategoryTransmutation(guiHelper));
            IntegrationJEI.CATEGORIES.add(new CategoryWell(guiHelper));
        }
        IntegrationJEI.CATEGORIES.forEach(xva$0 -> rec$.addRecipeCategories(new IRecipeCategory[] { xva$0 }));
    }
    
    public void registerRecipes(final IRecipeRegistration registry) {
        IntegrationJEI.CATEGORIES.forEach(category -> {
            final List<? extends Recipe<?>> recipes = category.getRecipes();
            recipes.sort(Comparator.comparing(recipe -> recipe.func_199560_c().toString()));
            registry.addRecipes((Collection)recipes, category.getUid());
        });
    }
    
    public void registerRecipeCatalysts(final IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst((Object)new ItemStack((ItemLike)BlocksAS.ALTAR_DISCOVERY), new ResourceLocation[] { IntegrationJEI.CATEGORY_ALTAR_DISCOVERY });
        registry.addRecipeCatalyst((Object)new ItemStack((ItemLike)BlocksAS.ALTAR_ATTUNEMENT), new ResourceLocation[] { IntegrationJEI.CATEGORY_ALTAR_ATTUNEMENT });
        registry.addRecipeCatalyst((Object)new ItemStack((ItemLike)BlocksAS.ALTAR_CONSTELLATION), new ResourceLocation[] { IntegrationJEI.CATEGORY_ALTAR_CONSTELLATION });
        registry.addRecipeCatalyst((Object)new ItemStack((ItemLike)BlocksAS.ALTAR_RADIANCE), new ResourceLocation[] { IntegrationJEI.CATEGORY_ALTAR_TRAIT });
        registry.addRecipeCatalyst((Object)new ItemStack((ItemLike)BlocksAS.INFUSER), new ResourceLocation[] { IntegrationJEI.CATEGORY_INFUSER });
        registry.addRecipeCatalyst((Object)new ItemStack((ItemLike)BlocksAS.CHALICE), new ResourceLocation[] { IntegrationJEI.CATEGORY_LIQUID_INTERACTION });
        registry.addRecipeCatalyst((Object)new ItemStack((ItemLike)BlocksAS.LENS), new ResourceLocation[] { IntegrationJEI.CATEGORY_TRANSMUTATION });
        registry.addRecipeCatalyst((Object)new ItemStack((ItemLike)BlocksAS.WELL), new ResourceLocation[] { IntegrationJEI.CATEGORY_WELL });
    }
    
    public void registerRecipeTransferHandlers(final IRecipeTransferRegistration registry) {
        final IStackHelper stackHelper = registry.getJeiHelpers().getStackHelper();
        final IRecipeTransferHandlerHelper transferHelper = registry.getTransferHelper();
        registry.addRecipeTransferHandler((IRecipeTransferHandler)new TieredAltarRecipeTransferHandler((Class<ContainerAltarBase>)ContainerAltarDiscovery.class, stackHelper, transferHelper, 9), IntegrationJEI.CATEGORY_ALTAR_DISCOVERY);
        registry.addRecipeTransferHandler((IRecipeTransferHandler)new TieredAltarRecipeTransferHandler((Class<ContainerAltarBase>)ContainerAltarAttunement.class, stackHelper, transferHelper, 13), IntegrationJEI.CATEGORY_ALTAR_DISCOVERY);
        registry.addRecipeTransferHandler((IRecipeTransferHandler)new TieredAltarRecipeTransferHandler((Class<ContainerAltarBase>)ContainerAltarConstellation.class, stackHelper, transferHelper, 21), IntegrationJEI.CATEGORY_ALTAR_DISCOVERY);
        registry.addRecipeTransferHandler((IRecipeTransferHandler)new TieredAltarRecipeTransferHandler((Class<ContainerAltarBase>)ContainerAltarTrait.class, stackHelper, transferHelper, 25), IntegrationJEI.CATEGORY_ALTAR_DISCOVERY);
        registry.addRecipeTransferHandler((IRecipeTransferHandler)new TieredAltarRecipeTransferHandler((Class<ContainerAltarBase>)ContainerAltarAttunement.class, stackHelper, transferHelper, 13), IntegrationJEI.CATEGORY_ALTAR_ATTUNEMENT);
        registry.addRecipeTransferHandler((IRecipeTransferHandler)new TieredAltarRecipeTransferHandler((Class<ContainerAltarBase>)ContainerAltarConstellation.class, stackHelper, transferHelper, 21), IntegrationJEI.CATEGORY_ALTAR_ATTUNEMENT);
        registry.addRecipeTransferHandler((IRecipeTransferHandler)new TieredAltarRecipeTransferHandler((Class<ContainerAltarBase>)ContainerAltarTrait.class, stackHelper, transferHelper, 25), IntegrationJEI.CATEGORY_ALTAR_ATTUNEMENT);
        registry.addRecipeTransferHandler((IRecipeTransferHandler)new TieredAltarRecipeTransferHandler((Class<ContainerAltarBase>)ContainerAltarConstellation.class, stackHelper, transferHelper, 21), IntegrationJEI.CATEGORY_ALTAR_CONSTELLATION);
        registry.addRecipeTransferHandler((IRecipeTransferHandler)new TieredAltarRecipeTransferHandler((Class<ContainerAltarBase>)ContainerAltarTrait.class, stackHelper, transferHelper, 25), IntegrationJEI.CATEGORY_ALTAR_CONSTELLATION);
        registry.addRecipeTransferHandler((IRecipeTransferHandler)new TieredAltarRecipeTransferHandler((Class<ContainerAltarBase>)ContainerAltarTrait.class, stackHelper, transferHelper, 25), IntegrationJEI.CATEGORY_ALTAR_TRAIT);
    }
    
    public void onRuntimeAvailable(final IJeiRuntime jeiRuntime) {
        IntegrationJEI.runtime = jeiRuntime;
    }
    
    public ResourceLocation getPluginUid() {
        return AstralSorcery.key("jei_integration");
    }
    
    static {
        CATEGORIES = new ArrayList<JEICategory<?>>();
        CATEGORY_ALTAR_ATTUNEMENT = AstralSorcery.key("altar_attunement");
        CATEGORY_ALTAR_CONSTELLATION = AstralSorcery.key("altar_constellation");
        CATEGORY_ALTAR_DISCOVERY = AstralSorcery.key("altar_discovery");
        CATEGORY_ALTAR_TRAIT = AstralSorcery.key("altar_trait");
        CATEGORY_INFUSER = AstralSorcery.key("infuser");
        CATEGORY_LIQUID_INTERACTION = AstralSorcery.key("interaction");
        CATEGORY_TRANSMUTATION = AstralSorcery.key("transmutation");
        CATEGORY_WELL = AstralSorcery.key("well");
        IntegrationJEI.runtime = null;
    }
}
