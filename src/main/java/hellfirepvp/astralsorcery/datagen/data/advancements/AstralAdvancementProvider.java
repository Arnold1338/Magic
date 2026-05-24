package hellfirepvp.astralsorcery.datagen.data.advancements;

import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import hellfirepvp.astralsorcery.common.advancement.instance.PerkLevelInstance;
import hellfirepvp.astralsorcery.common.advancement.AttuneCrystalTrigger;
import hellfirepvp.astralsorcery.common.advancement.AttuneSelfTrigger;
import hellfirepvp.astralsorcery.common.advancement.instance.ConstellationInstance;
import hellfirepvp.astralsorcery.common.advancement.DiscoverConstellationTrigger;
import hellfirepvp.astralsorcery.common.advancement.instance.AltarRecipeInstance;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.criterion.TickTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.advancements.FrameType;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.network.chat.Component;
import net.minecraft.advancements.Advancement;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import java.util.Set;
import java.nio.file.Path;
import java.io.IOException;
import com.google.gson.JsonElement;
import net.minecraft.data.IDataProvider;
import com.google.common.collect.Sets;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.DataGenerator;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import net.minecraft.data.AdvancementProvider;

public class AstralAdvancementProvider extends AdvancementProvider
{
    private static final Logger LOGGER;
    private static final Gson GSON;
    private final DataGenerator generator;
    
    public AstralAdvancementProvider(final DataGenerator generator) {
        super(generator);
        this.generator = generator;
    }
    
    public void func_200398_a(final DirectoryCache cache) {
        final Path path = this.generator.func_200391_b();
        final Set<ResourceLocation> set = Sets.newHashSet();
        final Consumer<Advancement> registrar = advancement -> {
            if (!set.add(advancement.func_192067_g())) {
                throw new IllegalStateException("Duplicate advancement " + advancement.func_192067_g());
            }
            else {
                final Path outPath = this.getPath(path, advancement);
                try {
                    IDataProvider.func_218426_a(AstralAdvancementProvider.GSON, cache, (JsonElement)advancement.func_192075_a().func_200273_b(), outPath);
                }
                catch (final IOException ioexception) {
                    AstralAdvancementProvider.LOGGER.error("Couldn't save advancement {}", (Object)outPath, (Object)ioexception);
                }
                return;
            }
        };
        this.registerAdvancements(registrar);
    }
    
    private Path getPath(final Path base, final Advancement advancement) {
        return base.resolve(String.format("data/%s/advancements/%s.json", advancement.func_192067_g().func_110624_b(), advancement.func_192067_g().func_110623_a()));
    }
    
    private Component title(final String key) {
        return new Component(String.format("advancements.astralsorcery.%s.title", key));
    }
    
    private Component description(final String key) {
        return new Component(String.format("advancements.astralsorcery.%s.desc", key));
    }
    
    private void registerAdvancements(final Consumer<Advancement> registrar) {
        final Advancement root = Advancement.Builder.func_200278_a().func_203902_a((ItemLike)ItemsAS.TOME, (Component)this.title("root"), (Component)this.description("root"), AstralSorcery.key("textures/block/black_marble_raw.png"), FrameType.TASK, false, false, false).func_200275_a("astralsorcery_present", (AbstractCriterionTriggerInstance)new TickTrigger.Instance(EntityPredicate.AndPredicate.field_234582_a_)).func_203904_a((Consumer)registrar, AstralSorcery.key("root").toString());
        final Advancement foundRockCrystals = Advancement.Builder.func_200278_a().func_203905_a(root).func_203902_a((ItemLike)ItemsAS.ROCK_CRYSTAL, (Component)this.title("rock_crystals"), (Component)this.description("rock_crystals"), (ResourceLocation)null, FrameType.TASK, true, true, false).func_200275_a("rock_crystal_in_inventory", (AbstractCriterionTriggerInstance)InventoryChangeTrigger.Instance.func_203922_a(new ItemLike[] { (ItemLike)ItemsAS.ROCK_CRYSTAL })).func_203904_a((Consumer)registrar, AstralSorcery.key("rock_crystals").toString());
        final Advancement foundCelestialCrystals = Advancement.Builder.func_200278_a().func_203905_a(foundRockCrystals).func_203902_a((ItemLike)ItemsAS.CELESTIAL_CRYSTAL, (Component)this.title("celestial_crystals"), (Component)this.description("celestial_crystals"), (ResourceLocation)null, FrameType.TASK, true, true, false).func_200275_a("celestial_crystal_in_inventory", (AbstractCriterionTriggerInstance)InventoryChangeTrigger.Instance.func_203922_a(new ItemLike[] { (ItemLike)ItemsAS.CELESTIAL_CRYSTAL })).func_203904_a((Consumer)registrar, AstralSorcery.key("celestial_crystals").toString());
        final Advancement craftAltarT2 = Advancement.Builder.func_200278_a().func_203905_a(foundRockCrystals).func_203902_a((ItemLike)BlocksAS.ALTAR_ATTUNEMENT, (Component)this.title("craft_t2_altar"), (Component)this.description("craft_t2_altar"), (ResourceLocation)null, FrameType.TASK, true, true, false).func_200275_a("altar_craft_t2_altar", (AbstractCriterionTriggerInstance)AltarRecipeInstance.withOutput((ItemLike)BlocksAS.ALTAR_ATTUNEMENT)).func_203904_a((Consumer)registrar, AstralSorcery.key("craft_t2_altar").toString());
        final Advancement craftAltarT3 = Advancement.Builder.func_200278_a().func_203905_a(craftAltarT2).func_203902_a((ItemLike)BlocksAS.ALTAR_CONSTELLATION, (Component)this.title("craft_t3_altar"), (Component)this.description("craft_t3_altar"), (ResourceLocation)null, FrameType.TASK, true, true, false).func_200275_a("altar_craft_t3_altar", (AbstractCriterionTriggerInstance)AltarRecipeInstance.withOutput((ItemLike)BlocksAS.ALTAR_CONSTELLATION)).func_203904_a((Consumer)registrar, AstralSorcery.key("craft_t3_altar").toString());
        final Advancement craftAltarT4 = Advancement.Builder.func_200278_a().func_203905_a(craftAltarT3).func_203902_a((ItemLike)BlocksAS.ALTAR_CONSTELLATION, (Component)this.title("craft_t4_altar"), (Component)this.description("craft_t4_altar"), (ResourceLocation)null, FrameType.CHALLENGE, true, true, false).func_200275_a("altar_craft_t3_altar", (AbstractCriterionTriggerInstance)AltarRecipeInstance.withOutput((ItemLike)BlocksAS.ALTAR_RADIANCE)).func_203904_a((Consumer)registrar, AstralSorcery.key("craft_t4_altar").toString());
        final Advancement findAnyConstellation = Advancement.Builder.func_200278_a().func_203905_a(root).func_203902_a((ItemLike)BlocksAS.TELESCOPE, (Component)this.title("find_constellation"), (Component)this.description("find_constellation"), (ResourceLocation)null, FrameType.TASK, true, true, false).func_200275_a("any_constellation_discovered", (AbstractCriterionTriggerInstance)ConstellationInstance.any(DiscoverConstellationTrigger.ID)).func_203904_a((Consumer)registrar, AstralSorcery.key("find_constellation").toString());
        final Advancement findWeakConstellation = Advancement.Builder.func_200278_a().func_203905_a(findAnyConstellation).func_203902_a((ItemLike)BlocksAS.TELESCOPE, (Component)this.title("find_weak_constellation"), (Component)this.description("find_weak_constellation"), (ResourceLocation)null, FrameType.TASK, true, true, false).func_200275_a("weak_constellation_discovered", (AbstractCriterionTriggerInstance)ConstellationInstance.anyWeak(DiscoverConstellationTrigger.ID)).func_203904_a((Consumer)registrar, AstralSorcery.key("find_weak_constellation").toString());
        final Advancement findMinorConstellation = Advancement.Builder.func_200278_a().func_203905_a(findWeakConstellation).func_203902_a((ItemLike)BlocksAS.OBSERVATORY, (Component)this.title("find_minor_constellation"), (Component)this.description("find_minor_constellation"), (ResourceLocation)null, FrameType.TASK, true, true, false).func_200275_a("minor_constellation_discovered", (AbstractCriterionTriggerInstance)ConstellationInstance.anyMinor(DiscoverConstellationTrigger.ID)).func_203904_a((Consumer)registrar, AstralSorcery.key("find_minor_constellation").toString());
        final Advancement attuneSelf = Advancement.Builder.func_200278_a().func_203905_a(findAnyConstellation).func_203902_a((ItemLike)BlocksAS.ATTUNEMENT_ALTAR, (Component)this.title("attune_self"), (Component)this.description("attune_self"), (ResourceLocation)null, FrameType.TASK, true, true, false).func_200275_a("attune_self", (AbstractCriterionTriggerInstance)ConstellationInstance.any(AttuneSelfTrigger.ID)).func_203904_a((Consumer)registrar, AstralSorcery.key("attune_self").toString());
        final Advancement attuneCrystal = Advancement.Builder.func_200278_a().func_203905_a(attuneSelf).func_203902_a((ItemLike)BlocksAS.RITUAL_PEDESTAL, (Component)this.title("attune_crystal"), (Component)this.description("attune_crystal"), (ResourceLocation)null, FrameType.TASK, true, true, false).func_200275_a("attune_crystal", (AbstractCriterionTriggerInstance)ConstellationInstance.anyWeak(AttuneCrystalTrigger.ID)).func_203904_a((Consumer)registrar, AstralSorcery.key("attune_crystal").toString());
        final Advancement attuneCrystalTrait = Advancement.Builder.func_200278_a().func_203905_a(attuneCrystal).func_203902_a((ItemLike)BlocksAS.RITUAL_PEDESTAL, (Component)this.title("attune_trait"), (Component)this.description("attune_trait"), (ResourceLocation)null, FrameType.TASK, true, true, false).func_200275_a("attune_trait", (AbstractCriterionTriggerInstance)ConstellationInstance.anyMinor(AttuneCrystalTrigger.ID)).func_203904_a((Consumer)registrar, AstralSorcery.key("attune_trait").toString());
        final Advancement perkLevelSmall = Advancement.Builder.func_200278_a().func_203905_a(attuneSelf).func_203902_a((ItemLike)BlocksAS.SPECTRAL_RELAY, (Component)this.title("perk_level_small"), (Component)this.description("perk_level_small"), (ResourceLocation)null, FrameType.TASK, true, true, false).func_200275_a("gain_perk_level_small", (AbstractCriterionTriggerInstance)PerkLevelInstance.reachLevel(10)).func_203904_a((Consumer)registrar, AstralSorcery.key("perk_level_small").toString());
        final Advancement perkLevelMedium = Advancement.Builder.func_200278_a().func_203905_a(perkLevelSmall).func_203902_a((ItemLike)BlocksAS.SPECTRAL_RELAY, (Component)this.title("perk_level_medium"), (Component)this.description("perk_level_medium"), (ResourceLocation)null, FrameType.TASK, true, true, false).func_200275_a("gain_perk_level_medium", (AbstractCriterionTriggerInstance)PerkLevelInstance.reachLevel(25)).func_203904_a((Consumer)registrar, AstralSorcery.key("perk_level_medium").toString());
        final Advancement perkLevelLarge = Advancement.Builder.func_200278_a().func_203905_a(perkLevelMedium).func_203902_a((ItemLike)BlocksAS.SPECTRAL_RELAY, (Component)this.title("perk_level_large"), (Component)this.description("perk_level_large"), (ResourceLocation)null, FrameType.CHALLENGE, true, true, false).func_200275_a("gain_perk_level_large", (AbstractCriterionTriggerInstance)PerkLevelInstance.reachLevel(40)).func_203904_a((Consumer)registrar, AstralSorcery.key("perk_level_large").toString());
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }
}
