package hellfirepvp.astralsorcery.common.registry;

import net.minecraft.world.level.Level;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.render.entity.RenderEntityGrapplingHook;
import hellfirepvp.astralsorcery.client.render.entity.RenderEntityItemHighlighted;
import hellfirepvp.astralsorcery.client.render.entity.RenderEntitySpectralTool;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import hellfirepvp.astralsorcery.client.render.entity.RenderEntityEmpty;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import hellfirepvp.astralsorcery.common.entity.technical.EntityGrapplingHook;
import hellfirepvp.astralsorcery.common.entity.technical.EntityObservatoryHelper;
import hellfirepvp.astralsorcery.common.entity.item.EntityStarmetal;
import hellfirepvp.astralsorcery.common.entity.item.EntityCrystal;
import hellfirepvp.astralsorcery.common.entity.item.EntityItemExplosionResistant;
import net.minecraft.world.entity.item.ItemEntity;
import hellfirepvp.astralsorcery.common.entity.item.EntityItemHighlighted;
import hellfirepvp.astralsorcery.common.entity.EntitySpectralTool;
import hellfirepvp.astralsorcery.common.entity.EntityFlare;
import hellfirepvp.astralsorcery.common.entity.EntityIlluminationSpark;
import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import hellfirepvp.astralsorcery.common.entity.EntityNocturnalSpark;

public class RegistryEntities
{
    private RegistryEntities() {
    }
    
    public static void init() {
        EntityTypesAS.NOCTURNAL_SPARK = register("nocturnal_spark", (EntityType.Builder<EntityNocturnalSpark>)EntityType.Builder.func_220322_a((EntityType.IFactory)EntityNocturnalSpark.factory(), MobCategory.MISC).func_200705_b().func_220320_c().setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).setTrackingRange(32).setCustomClientFactory((spawnEntity, world) -> new EntityNocturnalSpark(world)).func_220321_a(0.1f, 0.1f));
        EntityTypesAS.ILLUMINATION_SPARK = register("illumination_spark", (EntityType.Builder<EntityIlluminationSpark>)EntityType.Builder.func_220322_a((EntityType.IFactory)EntityIlluminationSpark.factory(), MobCategory.MISC).func_200705_b().func_220320_c().setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).setTrackingRange(32).setCustomClientFactory((spawnEntity, world) -> new EntityIlluminationSpark(world)).func_220321_a(0.1f, 0.1f));
        EntityTypesAS.FLARE = register("flare", (EntityType.Builder<EntityFlare>)EntityType.Builder.func_220322_a((EntityType.IFactory)EntityFlare.factory(), MobCategory.MISC).func_220320_c().setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setCustomClientFactory((spawnEntity, world) -> new EntityFlare(world)).func_220321_a(0.4f, 0.4f));
        EntityTypesAS.SPECTRAL_TOOL = register("spectral_tool", (EntityType.Builder<EntitySpectralTool>)EntityType.Builder.func_220322_a((EntityType.IFactory)EntitySpectralTool.factory(), MobCategory.MISC).func_200705_b().func_220320_c().setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).setTrackingRange(32).setCustomClientFactory((spawnEntity, world) -> new EntitySpectralTool(world)).func_220321_a(0.6f, 0.8f));
        EntityTypesAS.ITEM_HIGHLIGHT = register("item_highlighted", (EntityType.Builder<EntityItemHighlighted>)EntityType.Builder.func_220322_a((EntityType.IFactory)EntityItemHighlighted.factoryHighlighted(), MobCategory.MISC).func_200705_b().setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).setTrackingRange(16).setCustomClientFactory((spawnEntity, world) -> new EntityItemHighlighted(EntityTypesAS.ITEM_HIGHLIGHT, world)).func_220321_a(0.25f, 0.25f));
        EntityTypesAS.ITEM_EXPLOSION_RESISTANT = register("item_explosion_resistant", (EntityType.Builder<EntityItemExplosionResistant>)EntityType.Builder.func_220322_a((EntityType.IFactory)EntityItemExplosionResistant.factoryExplosionResistant(), MobCategory.MISC).func_200705_b().setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).setTrackingRange(16).setCustomClientFactory((spawnEntity, world) -> new EntityItemExplosionResistant(EntityTypesAS.ITEM_EXPLOSION_RESISTANT, world)).func_220321_a(0.25f, 0.25f));
        EntityTypesAS.ITEM_CRYSTAL = register("item_crystal", (EntityType.Builder<EntityCrystal>)EntityType.Builder.func_220322_a((EntityType.IFactory)EntityCrystal.factoryCrystal(), MobCategory.MISC).func_200705_b().setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).setTrackingRange(16).setCustomClientFactory((spawnEntity, world) -> new EntityCrystal(EntityTypesAS.ITEM_CRYSTAL, world)).func_220321_a(0.5f, 0.5f));
        EntityTypesAS.ITEM_STARMETAL_INGOT = register("item_starmetal", (EntityType.Builder<EntityStarmetal>)EntityType.Builder.func_220322_a((EntityType.IFactory)EntityStarmetal.factoryStarmetalIngot(), MobCategory.MISC).func_200705_b().setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).setTrackingRange(16).setCustomClientFactory((spawnEntity, world) -> new EntityStarmetal(EntityTypesAS.ITEM_STARMETAL_INGOT, world)).func_220321_a(0.5f, 0.5f));
        EntityTypesAS.OBSERVATORY_HELPER = register("observatory_helper", (EntityType.Builder<EntityObservatoryHelper>)EntityType.Builder.func_220322_a((EntityType.IFactory)EntityObservatoryHelper.factory(), MobCategory.MISC).func_200705_b().setUpdateInterval(1).func_220320_c().setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setCustomClientFactory((spawnEntity, world) -> new EntityObservatoryHelper(world)).func_220321_a(0.0f, 0.0f));
        EntityTypesAS.GRAPPLING_HOOK = register("grappling_hook", (EntityType.Builder<EntityGrapplingHook>)EntityType.Builder.func_220322_a((EntityType.IFactory)EntityGrapplingHook.factory(), MobCategory.MISC).func_200705_b().setUpdateInterval(1).func_220320_c().setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setCustomClientFactory((spawnEntity, world) -> new EntityGrapplingHook(world)).func_220321_a(0.1f, 0.1f));
    }
    
    public static void initAttributes(final EntityAttributeCreationEvent event) {
        event.put((EntityType)EntityTypesAS.FLARE, EntityFlare.createAttributes().func_233813_a_());
        event.put((EntityType)EntityTypesAS.SPECTRAL_TOOL, EntitySpectralTool.createAttributes().func_233813_a_());
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void initClient() {
        RenderingRegistry.registerEntityRenderingHandler((EntityType)EntityTypesAS.NOCTURNAL_SPARK, (IRenderFactory)new RenderEntityEmpty.Factory());
        RenderingRegistry.registerEntityRenderingHandler((EntityType)EntityTypesAS.ILLUMINATION_SPARK, (IRenderFactory)new RenderEntityEmpty.Factory());
        RenderingRegistry.registerEntityRenderingHandler((EntityType)EntityTypesAS.FLARE, (IRenderFactory)new RenderEntityEmpty.Factory());
        RenderingRegistry.registerEntityRenderingHandler((EntityType)EntityTypesAS.SPECTRAL_TOOL, (IRenderFactory)new RenderEntitySpectralTool.Factory());
        RenderingRegistry.registerEntityRenderingHandler((EntityType)EntityTypesAS.ITEM_HIGHLIGHT, (IRenderFactory)new RenderEntityItemHighlighted.Factory());
        RenderingRegistry.registerEntityRenderingHandler((EntityType)EntityTypesAS.ITEM_EXPLOSION_RESISTANT, (IRenderFactory)new RenderEntityItemHighlighted.Factory());
        RenderingRegistry.registerEntityRenderingHandler((EntityType)EntityTypesAS.ITEM_CRYSTAL, (IRenderFactory)new RenderEntityItemHighlighted.Factory());
        RenderingRegistry.registerEntityRenderingHandler((EntityType)EntityTypesAS.ITEM_STARMETAL_INGOT, manager -> new ItemRenderer(manager, Minecraft.getInstance().func_175599_af()));
        RenderingRegistry.registerEntityRenderingHandler((EntityType)EntityTypesAS.OBSERVATORY_HELPER, (IRenderFactory)new RenderEntityEmpty.Factory());
        RenderingRegistry.registerEntityRenderingHandler((EntityType)EntityTypesAS.GRAPPLING_HOOK, (IRenderFactory)new RenderEntityGrapplingHook.Factory());
    }
    
    private static <E extends Entity> EntityType<E> register(final String name, final EntityType.Builder<E> typeBuilder) {
        final EntityType<E> type = (EntityType<E>)typeBuilder.func_206830_a(AstralSorcery.key(name).toString());
        type.setRegistryName(AstralSorcery.key(name));
        AstralSorcery.getProxy().getRegistryPrimer().register(type);
        return type;
    }
}
