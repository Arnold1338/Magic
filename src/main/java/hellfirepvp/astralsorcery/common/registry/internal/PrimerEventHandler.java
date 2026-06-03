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
import net.minecraft.world.inventory.MenuType;
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

import hellfirepvp.astralsorcery.common.constellation.IConstellation;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.effect.MobEffect;

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
                eventBus.addGenericListener((Class)Effect.class, (Consumer)this::registerEffects);
        eventBus.addGenericListener((Class)Enchantment.class, (Consumer)this::registerEnchantments);
        eventBus.addGenericListener((Class)SoundEvent.class, (Consumer)this::registerSounds);
        eventBus.addGenericListener((Class)net.minecraftforge.common.loot.IGlobalLootModifier.class, (Consumer)this::registerGlobalLootModifierSerializers);
        eventBus.addGenericListener((Class)IConstellation.class, (Consumer)this::registerConstellations);
        eventBus.addGenericListener((Class)net.minecraft.network.syncher.EntityDataSerializer.class, (Consumer)this::registerDataSerializers);
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
    
    private void registerItems(final RegisterEvent event) {
        RegistryItems.registerItems();
        RegistryItems.registerItemBlocks();
        RegistryItems.registerFluidContainerItems();
        RegistryItems.registerDispenseBehaviors();
        this.fillRegistryFromEvent(event);
        this.registerRemainingData();
    }
    
    private void registerBlocks(final RegisterEvent event) {
        RegistryFluids.registerFluids();
        RegistryBlocks.registerBlocks();
        RegistryBlocks.registerFluidBlocks();
        this.fillRegistryFromEvent(event);
    }
    
    private void registerFluids(final RegisterEvent event) {
        this.fillRegistryFromEvent(event);
    }
    
    private void registerTiles(final RegisterEvent event) {
        RegistryTileEntities.registerTiles();
        this.fillRegistryFromEvent(event);
    }
    
    private void registerEntities(final RegisterEvent event) {
        RegistryEntities.init();
        this.fillRegistryFromEvent(event);
    }
    
    private void registerEffects(final RegisterEvent event) {
        RegistryEffects.init();
        this.fillRegistryFromEvent(event);
    }
    
    private void registerEnchantments(final RegisterEvent event) {
        RegistryEnchantments.init();
        this.fillRegistryFromEvent(event);
    }
    
    private void registerGlobalLootModifierSerializers(final RegisterEvent event) {
        RegistryLoot.init();
        this.fillRegistryFromEvent(event);
    }
    
    private void registerConstellations(final RegisterEvent event) {
        this.fillRegistryFromEvent(event);
    }
    
    private void registerConstellationEffects(final RegisterEvent event) {
        this.fillRegistryFromEvent(event);
    }
    
    private void registerMantleEffects(final RegisterEvent event) {
        this.fillRegistryFromEvent(event);
    }
    
    private void registerEngravingEffects(final RegisterEvent event) {
        this.fillRegistryFromEvent(event);
    }
    
    private void registerPerkAttributeTypes(final RegisterEvent event) {
        this.fillRegistryFromEvent(event);
    }
    
    private void registerPerkConverters(final RegisterEvent event) {
        this.fillRegistryFromEvent(event);
    }
    
    private void registerPerkCustomModifiers(final RegisterEvent event) {
        this.fillRegistryFromEvent(event);
    }
    
    private void registerPerkAttributeReaders(final RegisterEvent event) {
        this.fillRegistryFromEvent(event);
    }
    
    private void registerContainerTypes(final RegisterEvent event) {
        RegistryContainerTypes.init();
        this.fillRegistryFromEvent(event);
    }
    
    private void registerCrystalProperties(final RegisterEvent event) {
        this.fillRegistryFromEvent(event);
    }
    
    private void registerCrystalUsages(final RegisterEvent event) {
        this.fillRegistryFromEvent(event);
    }
    
    private void registerAltarRecipeEffects(final RegisterEvent event) {
        RegistryRecipeTypes.initAltarEffects();
        this.fillRegistryFromEvent(event);
    }
    
    private void registerDataSerializers(final RegisterEvent event) {
        RegistryDataSerializers.registerSerializers();
        this.fillRegistryFromEvent(event);
    }
    
    private void registerRecipeSerializers(final RegisterEvent event) {
        this.fillRegistryFromEvent(event);
    }
    
    private void registerStructures(final RegisterEvent event) {
        this.fillRegistryFromEvent(event);
    }
    
    private void registerStructureProviders(final RegisterEvent event) {
        this.fillRegistryFromEvent(event);
    }
    
    private void registerStructureTypes(final RegisterEvent event) {
        this.fillRegistryFromEvent(event);
    }
    
    private void registerStructureTemplates(final RegisterEvent event) {
        this.fillRegistryFromEvent(event);
    }
    
    private void registerFeatures(final RegisterEvent event) {
        this.fillRegistryFromEvent(event);
    }
    private void registerSounds(final RegisterEvent event) {
        RegistrySounds.init();
        this.fillRegistryFromEvent(event);
    }
    
    private <T extends Object<T>> void fillRegistry(final Class<T> registrySuperType, final IForgeRegistry<T> forgeRegistry) {
        this.registry.getEntries(registrySuperType).forEach(e -> forgeRegistry.register((Object)e));
    }
}
