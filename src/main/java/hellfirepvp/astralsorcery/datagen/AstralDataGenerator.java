package hellfirepvp.astralsorcery.datagen;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraft.data.DataGenerator;
import hellfirepvp.astralsorcery.datagen.assets.AstralBlockStateMappingProvider;
import hellfirepvp.astralsorcery.datagen.data.perks.AstralPerkTreeProvider;
import hellfirepvp.astralsorcery.datagen.data.recipes.AstralRecipeProvider;
import hellfirepvp.astralsorcery.datagen.data.loot.AstralLootTableProvider;
import hellfirepvp.astralsorcery.datagen.data.tags.AstralItemTagsProvider;
import hellfirepvp.astralsorcery.datagen.data.tags.AstralBlockTagsProvider;
import net.minecraft.data.IDataProvider;
import hellfirepvp.astralsorcery.datagen.data.advancements.AstralAdvancementProvider;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralDataGenerator
{
    @SubscribeEvent
    public static void gather(final GatherDataEvent event) {
        if (!AstralSorcery.isDoingDataGeneration()) {

        }
        final DataGenerator gen = event.getGenerator();
        final ExistingFileHelper fileHelper = event.getExistingFileHelper();
        if (event.includeServer()) {
            gen.func_200390_a((IDataProvider)new AstralAdvancementProvider(gen));
            final BlockTagsProvider blockTagGen = new AstralBlockTagsProvider(gen, fileHelper);
            gen.func_200390_a((IDataProvider)blockTagGen);
            gen.func_200390_a((IDataProvider)new AstralItemTagsProvider(gen, blockTagGen, fileHelper));
            gen.func_200390_a((IDataProvider)new AstralLootTableProvider(gen));
            gen.func_200390_a((IDataProvider)new AstralRecipeProvider(gen));
            gen.func_200390_a((IDataProvider)new AstralPerkTreeProvider(gen));
        }
        if (event.includeClient()) {
            gen.func_200390_a((IDataProvider)new AstralBlockStateMappingProvider(gen, fileHelper));
        }
    }
}
