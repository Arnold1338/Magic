package hellfirepvp.astralsorcery.common.registry.internal;

import hellfirepvp.astralsorcery.common.registry.RegistrySounds;
import hellfirepvp.astralsorcery.common.registry.RegistryDataSerializers;
import hellfirepvp.astralsorcery.common.registry.RegistryContainerTypes;
import hellfirepvp.astralsorcery.common.registry.RegistryLoot;
import hellfirepvp.astralsorcery.common.registry.RegistryEnchantments;
import hellfirepvp.astralsorcery.common.registry.RegistryEffects;
import hellfirepvp.astralsorcery.common.registry.RegistryEntities;
import hellfirepvp.astralsorcery.common.registry.RegistryTileEntities;
import hellfirepvp.astralsorcery.common.registry.RegistryBlocks;
import hellfirepvp.astralsorcery.common.registry.RegistryFluids;


import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;
import hellfirepvp.astralsorcery.common.registry.RegistryPerkAttributeReaders;
import hellfirepvp.astralsorcery.common.registry.RegistryPerkCustomModifiers;
import hellfirepvp.astralsorcery.common.registry.RegistryPerkConverters;
import hellfirepvp.astralsorcery.common.registry.RegistryPerkAttributeTypes;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.SourceClassRegistry;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionClassRegistry;
import hellfirepvp.astralsorcery.common.registry.RegistryResearch;
import hellfirepvp.astralsorcery.common.registry.RegistryRecipeSerializers;
import hellfirepvp.astralsorcery.common.registry.RegistryRecipeTypes;
import hellfirepvp.astralsorcery.common.registry.RegistryCrystalProperties;
import hellfirepvp.astralsorcery.common.registry.RegistryCrystalPropertyUsages;
import hellfirepvp.astralsorcery.common.registry.RegistryWorldGeneration;
import hellfirepvp.astralsorcery.common.registry.RegistryStructures;
import hellfirepvp.astralsorcery.common.registry.RegistryEngravingEffects;
import hellfirepvp.astralsorcery.common.registry.RegistryMantleEffects;
import hellfirepvp.astralsorcery.common.registry.RegistryConstellationEffects;
import net.minecraft.world.level.levelgen.structure.Structure;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.AltarRecipeEffect;
import hellfirepvp.astralsorcery.common.crystal.calc.PropertyUsage;
import hellfirepvp.astralsorcery.common.crystal.CrystalProperty;
import net.minecraft.world.level.inventory.MenuType;
import hellfirepvp.astralsorcery.common.perk.reader.PerkAttributeReader;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import hellfirepvp.astralsorcery.common.perk.PerkConverter;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import hellfirepvp.astralsorcery.common.constellation.engraving.EngravingEffect;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProvider;
import hellfirepvp.observerlib.api.ObserverProvider;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import hellfirepvp.observerlib.api.structure.MatchableStructure;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DataSerializerEntry;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.effect.MobEffect;
import net.minecraft.world.level.levelgen.placement.Placement;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.Block;
import java.util.function.Consumer;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;

public class PrimerEventHandler
{
    private final InternalRegistryPrimer registry;
    
    public PrimerEventHandler(final InternalRegistryPrimer registry) {
        this.registry = registry;
    }
    
    public void attachEventHandlers(final IEventBus eventBus) {
        eventBus.addGenericListener((Class)Item.class, (Consumer)this::registerItems);
        eventBus.addGenericListener((Class)Block.class, (Consumer)this::registerBlocks);
        eventBus.addGenericListener((Class)Fluid.class, (Consumer)this::registerFluids);
        eventBus.addGenericListener((Class)BlockEntityType.class, (Consumer)this::registerTiles);
        eventBus.addGenericListener((Class)EntityType.class, (Consumer)this::registerEntities);
        eventBus.addGenericListener((Class)Feature.class, (Consumer)this::registerFeatures);
        eventBus.addGenericListener((Class)Placement.class, (Consumer)this::registerPlacements);
        eventBus.addGenericListener((Class)Effect.class, (Consumer)this::registerEffects);
        eventBus.addGenericListener((Class)Enchantment.class, (Consumer)this::registerEnchantments);
        eventBus.addGenericListener((Class)SoundEvent.class, (Consumer)this::registerSounds);
        eventBus.addGenericListener((Class)GlobalLootModifierSerializer.class, (Consumer)this::registerGlobalLootModifierSerializers);
        eventBus.addGenericListener((Class)IConstellation.class, (Consumer)this::registerConstellations);
        eventBus.addGenericListener((Class)DataSerializerEntry.class, (Consumer)this::registerDataSerializers);
        eventBus.addGenericListener((Class)RecipeSerializer.class, (Consumer)this::registerRecipeSerializers);
        eventBus.addGenericListener((Class)MatchableStructure.class, (Consumer)this::registerStructures);
        eventBus.addGenericListener((Class)StructureType.class, (Consumer)this::registerStructureTypes);
        eventBus.addGenericListener((Class)ObserverProvider.class, (Consumer)this::registerStructureProviders);
        eventBus.addGenericListener((Class)ConstellationEffectProvider.class, (Consumer)this::registerConstellationEffects);
        eventBus.addGenericListener((Class)MantleEffect.class, (Consumer)this::registerMantleEffects);
        eventBus.addGenericListener((Class)EngravingEffect.class, (Consumer)this::registerEngravingEffects);
        eventBus.addGenericListener((Class)PerkAttributeType.class, (Consumer)this::registerPerkAttributeTypes);
        eventBus.addGenericListener((Class)PerkConverter.class, (Consumer)this::registerPerkConverters);
        eventBus.addGenericListener((Class)PerkAttributeModifier.class, (Consumer)this::registerPerkCustomModifiers);
        eventBus.addGenericListener((Class)PerkAttributeReader.class, (Consumer)this::registerPerkAttributeReaders);
        eventBus.addGenericListener((Class)ContainerType.class, (Consumer)this::registerContainerTypes);
        eventBus.addGenericListener((Class)CrystalProperty.class, (Consumer)this::registerCrystalProperties);
        eventBus.addGenericListener((Class)PropertyUsage.class, (Consumer)this::registerCrystalUsages);
        eventBus.addGenericListener((Class)AltarRecipeEffect.class, (Consumer)this::registerAltarRecipeEffects);
        eventBus.addGenericListener((Class)Structure.class, (Consumer)this::registerStructureTemplates);
    }
    
    private void registerRemainingData() {
        RegistryConstellationEffects.init();
        RegistryMantleEffects.init();
        RegistryEngravingEffects.init();
        RegistryStructures.init();
        RegistryWorldGeneration.init();
        RegistryCrystalPropertyUsages.init();
        RegistryCrystalProperties.init();
        RegistryCrystalProperties.initDefaultAttributes();
        RegistryRecipeTypes.init();
        RegistryRecipeSerializers.init();
        RegistryResearch.init();
        TransmissionClassRegistry.setupRegistry();
        SourceClassRegistry.setupRegistry();
        RegistryPerkAttributeTypes.init();
        RegistryPerkConverters.init();
        RegistryPerkCustomModifiers.init();
        RegistryPerkAttributeReaders.init();
    }
    
    private void registerItems(final RegisterEvent<Item> event) {
        RegistryItems.registerItems();
        RegistryItems.registerItemBlocks();
        RegistryItems.registerFluidContainerItems();
        RegistryItems.registerDispenseBehaviors();
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
        this.registerRemainingData();
    }
    
    private void registerBlocks(final RegisterEvent<Block> event) {
        RegistryFluids.registerFluids();
        RegistryBlocks.registerBlocks();
        RegistryBlocks.registerFluidBlocks();
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerFluids(final RegisterEvent<Fluid> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerTiles(final RegisterEvent<BlockEntityType<?>> event) {
        RegistryTileEntities.registerTiles();
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerEntities(final RegisterEvent<EntityType<?>> event) {
        RegistryEntities.init();
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerEffects(final RegisterEvent<Effect> event) {
        RegistryEffects.init();
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerEnchantments(final RegisterEvent<Enchantment> event) {
        RegistryEnchantments.init();
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerGlobalLootModifierSerializers(final RegisterEvent<GlobalLootModifierSerializer<?>> event) {
        RegistryLoot.init();
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerConstellations(final RegisterEvent<IConstellation> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerConstellationEffects(final RegisterEvent<ConstellationEffectProvider> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerMantleEffects(final RegisterEvent<MantleEffect> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerEngravingEffects(final RegisterEvent<EngravingEffect> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerPerkAttributeTypes(final RegisterEvent<PerkAttributeType> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerPerkConverters(final RegisterEvent<PerkConverter> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerPerkCustomModifiers(final RegisterEvent<PerkAttributeModifier> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerPerkAttributeReaders(final RegisterEvent<PerkAttributeReader> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerContainerTypes(final RegisterEvent<ContainerType<?>> event) {
        RegistryContainerTypes.init();
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerCrystalProperties(final RegisterEvent<CrystalProperty> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerCrystalUsages(final RegisterEvent<PropertyUsage> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerAltarRecipeEffects(final RegisterEvent<AltarRecipeEffect> event) {
        RegistryRecipeTypes.initAltarEffects();
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerDataSerializers(final RegisterEvent<DataSerializerEntry> event) {
        RegistryDataSerializers.registerSerializers();
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerRecipeSerializers(final RegisterEvent<RecipeSerializer<?>> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerStructures(final RegisterEvent<MatchableStructure> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerStructureProviders(final RegisterEvent<ObserverProvider> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerStructureTypes(final RegisterEvent<StructureType> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerStructureTemplates(final RegisterEvent<Structure<?>> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerFeatures(final RegisterEvent<Feature<?>> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerPlacements(final RegisterEvent<Placement<?>> event) {
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private void registerSounds(final RegisterEvent<SoundEvent> event) {
        RegistrySounds.init();
        this.fillRegistry(event.getRegistry().getRegistrySuperType(), (net.minecraftforge.registries.IForgeRegistry<Object>)event.getRegistry());
    }
    
    private <T extends Object<T>> void fillRegistry(final Class<T> registrySuperType, final IForgeRegistry<T> forgeRegistry) {
        this.registry.getEntries(registrySuperType).forEach(e -> forgeRegistry.register((Object)e));
    }
}
