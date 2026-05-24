package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.constellation.star.StarLocation;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import net.minecraft.world.level.level.LevelAccessor;
import hellfirepvp.astralsorcery.common.base.MoonPhase;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import net.minecraft.world.level.level.Level;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.constellation.Constellation;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.world.level.item.Items;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.world.level.level.block.Blocks;
import net.minecraftforge.common.Tags;
import hellfirepvp.astralsorcery.common.lib.TagsAS;
import net.minecraft.world.level.item.Item;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.ItemTags;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;

public class RegistryConstellations
{
    public static void init() {
        buildConstellations();
        registerSignatureItems();
    }
    
    private static void registerSignatureItems() {
        ConstellationsAS.aevitas.addSignatureItem((ITag<Item>)ItemTags.field_200037_g);
        ConstellationsAS.aevitas.addSignatureItem((ITag<Item>)TagsAS.Items.DUSTS_STARDUST);
        ConstellationsAS.aevitas.addSignatureItem((ITag<Item>)Tags.Items.SEEDS_WHEAT);
        ConstellationsAS.aevitas.addSignatureItem((ItemLike)Blocks.field_196608_cF);
        ConstellationsAS.discidia.addSignatureItem((ItemLike)Items.field_151145_ak);
        ConstellationsAS.discidia.addSignatureItem((ITag<Item>)Tags.Items.INGOTS_IRON);
        ConstellationsAS.discidia.addSignatureItem((ITag<Item>)ItemTags.field_219776_M);
        ConstellationsAS.discidia.addSignatureItem((ITag<Item>)Tags.Items.DUSTS_REDSTONE);
        ConstellationsAS.armara.addSignatureItem((ITag<Item>)Tags.Items.INGOTS_IRON);
        ConstellationsAS.armara.addSignatureItem((ITag<Item>)Tags.Items.LEATHER);
        ConstellationsAS.armara.addSignatureItem((ItemLike)Items.field_151119_aD);
        ConstellationsAS.armara.addSignatureItem((ITag<Item>)Tags.Items.DUSTS_GLOWSTONE);
        ConstellationsAS.vicio.addSignatureItem((ITag<Item>)Tags.Items.FEATHERS);
        ConstellationsAS.vicio.addSignatureItem((ItemLike)Items.field_151102_aT);
        ConstellationsAS.vicio.addSignatureItem((ItemLike)Items.field_151007_F);
        ConstellationsAS.vicio.addSignatureItem((ITag<Item>)ItemTags.field_206964_G);
        ConstellationsAS.evorsio.addSignatureItem((ITag<Item>)Tags.Items.COBBLESTONE);
        ConstellationsAS.evorsio.addSignatureItem((ItemLike)Items.field_151145_ak);
        ConstellationsAS.evorsio.addSignatureItem((ITag<Item>)Tags.Items.GUNPOWDER);
        ConstellationsAS.evorsio.addSignatureItem((ItemLike)Blocks.field_150335_W);
        ConstellationsAS.lucerna.addSignatureItem((ITag<Item>)Tags.Items.DUSTS_GLOWSTONE);
        ConstellationsAS.lucerna.addSignatureItem((ItemLike)Blocks.field_150478_aa);
        ConstellationsAS.lucerna.addSignatureItem((ITag<Item>)Tags.Items.DUSTS_REDSTONE);
        ConstellationsAS.lucerna.addSignatureItem((ITag<Item>)ItemTags.field_219775_L);
        ConstellationsAS.mineralis.addSignatureItem((ITag<Item>)Tags.Items.ORES_IRON);
        ConstellationsAS.mineralis.addSignatureItem((ITag<Item>)Tags.Items.INGOTS_GOLD);
        ConstellationsAS.mineralis.addSignatureItem((ITag<Item>)Tags.Items.INGOTS_IRON);
        ConstellationsAS.mineralis.addSignatureItem((ITag<Item>)Tags.Items.DUSTS_REDSTONE);
        ConstellationsAS.horologium.addSignatureItem((ITag<Item>)TagsAS.Items.DUSTS_STARDUST);
        ConstellationsAS.horologium.addSignatureItem((ITag<Item>)Tags.Items.GEMS_DIAMOND);
        ConstellationsAS.horologium.addSignatureItem((ITag<Item>)Tags.Items.ENDER_PEARLS);
        ConstellationsAS.horologium.addSignatureItem((ItemLike)Items.field_151113_aN);
        ConstellationsAS.octans.addSignatureItem((ITag<Item>)ItemTags.field_206964_G);
        ConstellationsAS.octans.addSignatureItem((ItemLike)Items.field_151112_aM);
        ConstellationsAS.octans.addSignatureItem((ITag<Item>)Tags.Items.DYES_BLUE);
        ConstellationsAS.octans.addSignatureItem((ItemLike)Items.field_151119_aD);
        ConstellationsAS.bootes.addSignatureItem((ITag<Item>)Tags.Items.CROPS_WHEAT);
        ConstellationsAS.bootes.addSignatureItem((ITag<Item>)Tags.Items.BONES);
        ConstellationsAS.bootes.addSignatureItem((ItemLike)Items.field_151034_e);
        ConstellationsAS.bootes.addSignatureItem((ITag<Item>)Tags.Items.LEATHER);
        ConstellationsAS.fornax.addSignatureItem((ItemLike)Items.field_151044_h);
        ConstellationsAS.fornax.addSignatureItem((ITag<Item>)Tags.Items.DUSTS_REDSTONE);
        ConstellationsAS.fornax.addSignatureItem((ITag<Item>)Tags.Items.INGOTS_IRON);
        ConstellationsAS.fornax.addSignatureItem((ITag<Item>)Tags.Items.GUNPOWDER);
        ConstellationsAS.pelotrio.addSignatureItem((ItemLike)Items.field_151078_bh);
        ConstellationsAS.pelotrio.addSignatureItem((ItemLike)Items.field_151065_br);
        ConstellationsAS.pelotrio.addSignatureItem((ItemLike)Items.field_151034_e);
        ConstellationsAS.pelotrio.addSignatureItem((ITag<Item>)Tags.Items.EGGS);
        ConstellationsAS.gelu.addSignatureItem((ItemLike)Items.field_151126_ay);
        ConstellationsAS.gelu.addSignatureItem((ItemLike)Blocks.field_150432_aD);
        ConstellationsAS.gelu.addSignatureItem((ITag<Item>)Tags.Items.GEMS_QUARTZ);
        ConstellationsAS.gelu.addSignatureItem((ITag<Item>)Tags.Items.FEATHERS);
        ConstellationsAS.ulteria.addSignatureItem((ITag<Item>)TagsAS.Items.INGOTS_STARMETAL);
        ConstellationsAS.ulteria.addSignatureItem((ITag<Item>)Tags.Items.LEATHER);
        ConstellationsAS.ulteria.addSignatureItem((ITag<Item>)Tags.Items.GEMS_DIAMOND);
        ConstellationsAS.ulteria.addSignatureItem((ITag<Item>)Tags.Items.RODS_BLAZE);
        ConstellationsAS.alcara.addSignatureItem((ITag<Item>)Tags.Items.CROPS_NETHER_WART);
        ConstellationsAS.alcara.addSignatureItem((ITag<Item>)Tags.Items.ENDER_PEARLS);
        ConstellationsAS.alcara.addSignatureItem((ItemLike)Blocks.field_150425_aM);
        ConstellationsAS.alcara.addSignatureItem((ITag<Item>)ItemTags.field_219775_L);
        ConstellationsAS.vorux.addSignatureItem((ItemLike)Items.field_151065_br);
        ConstellationsAS.vorux.addSignatureItem((ItemLike)ItemsAS.NOCTURNAL_POWDER);
        ConstellationsAS.vorux.addSignatureItem((ITag<Item>)Tags.Items.GUNPOWDER);
        ConstellationsAS.vorux.addSignatureItem((ItemLike)Items.field_196154_dH);
    }
    
    private static void buildConstellations() {
        ConstellationsAS.discidia = new Constellation.Major("discidia", ColorsAS.CONSTELLATION_DISCIDIA);
        StarLocation sl1 = ConstellationsAS.discidia.addStar(9, 25);
        StarLocation sl2 = ConstellationsAS.discidia.addStar(6, 18);
        StarLocation sl3 = ConstellationsAS.discidia.addStar(13, 16);
        StarLocation sl4 = ConstellationsAS.discidia.addStar(15, 9);
        StarLocation sl5 = ConstellationsAS.discidia.addStar(22, 8);
        StarLocation sl6 = ConstellationsAS.discidia.addStar(27, 2);
        ConstellationsAS.discidia.addConnection(sl1, sl2);
        ConstellationsAS.discidia.addConnection(sl2, sl3);
        ConstellationsAS.discidia.addConnection(sl1, sl3);
        ConstellationsAS.discidia.addConnection(sl3, sl4);
        ConstellationsAS.discidia.addConnection(sl3, sl5);
        ConstellationsAS.discidia.addConnection(sl4, sl5);
        ConstellationsAS.discidia.addConnection(sl5, sl6);
        register(ConstellationsAS.discidia);
        ConstellationsAS.armara = new Constellation.Major("armara", ColorsAS.CONSTELLATION_ARMARA);
        sl1 = ConstellationsAS.armara.addStar(26, 26);
        sl2 = ConstellationsAS.armara.addStar(24, 15);
        sl3 = ConstellationsAS.armara.addStar(16, 22);
        sl4 = ConstellationsAS.armara.addStar(20, 5);
        sl5 = ConstellationsAS.armara.addStar(15, 4);
        sl6 = ConstellationsAS.armara.addStar(8, 12);
        StarLocation sl7 = ConstellationsAS.armara.addStar(5, 18);
        ConstellationsAS.armara.addConnection(sl1, sl2);
        ConstellationsAS.armara.addConnection(sl2, sl3);
        ConstellationsAS.armara.addConnection(sl1, sl3);
        ConstellationsAS.armara.addConnection(sl2, sl4);
        ConstellationsAS.armara.addConnection(sl4, sl5);
        ConstellationsAS.armara.addConnection(sl5, sl6);
        ConstellationsAS.armara.addConnection(sl3, sl6);
        ConstellationsAS.armara.addConnection(sl6, sl7);
        register(ConstellationsAS.armara);
        ConstellationsAS.vicio = new Constellation.Major("vicio", ColorsAS.CONSTELLATION_VICIO);
        sl1 = ConstellationsAS.vicio.addStar(13, 11);
        sl2 = ConstellationsAS.vicio.addStar(26, 10);
        sl3 = ConstellationsAS.vicio.addStar(23, 4);
        sl4 = ConstellationsAS.vicio.addStar(4, 6);
        sl5 = ConstellationsAS.vicio.addStar(5, 20);
        sl6 = ConstellationsAS.vicio.addStar(12, 25);
        ConstellationsAS.vicio.addConnection(sl1, sl2);
        ConstellationsAS.vicio.addConnection(sl2, sl3);
        ConstellationsAS.vicio.addConnection(sl1, sl3);
        ConstellationsAS.vicio.addConnection(sl1, sl4);
        ConstellationsAS.vicio.addConnection(sl4, sl5);
        ConstellationsAS.vicio.addConnection(sl5, sl6);
        ConstellationsAS.vicio.addConnection(sl6, sl1);
        register(ConstellationsAS.vicio);
        ConstellationsAS.aevitas = new Constellation.Major("aevitas", ColorsAS.CONSTELLATION_AEVITAS);
        sl1 = ConstellationsAS.aevitas.addStar(13, 4);
        sl2 = ConstellationsAS.aevitas.addStar(15, 18);
        sl3 = ConstellationsAS.aevitas.addStar(10, 27);
        sl4 = ConstellationsAS.aevitas.addStar(27, 24);
        sl5 = ConstellationsAS.aevitas.addStar(24, 19);
        sl6 = ConstellationsAS.aevitas.addStar(25, 10);
        sl7 = ConstellationsAS.aevitas.addStar(3, 6);
        ConstellationsAS.aevitas.addConnection(sl1, sl2);
        ConstellationsAS.aevitas.addConnection(sl2, sl3);
        ConstellationsAS.aevitas.addConnection(sl3, sl4);
        ConstellationsAS.aevitas.addConnection(sl4, sl5);
        ConstellationsAS.aevitas.addConnection(sl5, sl6);
        ConstellationsAS.aevitas.addConnection(sl6, sl1);
        ConstellationsAS.aevitas.addConnection(sl1, sl7);
        register(ConstellationsAS.aevitas);
        ConstellationsAS.evorsio = new Constellation.Major("evorsio", ColorsAS.CONSTELLATION_EVORSIO);
        sl1 = ConstellationsAS.evorsio.addStar(27, 17);
        sl2 = ConstellationsAS.evorsio.addStar(19, 23);
        sl3 = ConstellationsAS.evorsio.addStar(25, 27);
        sl4 = ConstellationsAS.evorsio.addStar(22, 12);
        sl5 = ConstellationsAS.evorsio.addStar(13, 3);
        sl6 = ConstellationsAS.evorsio.addStar(16, 11);
        sl7 = ConstellationsAS.evorsio.addStar(6, 5);
        ConstellationsAS.evorsio.addConnection(sl1, sl2);
        ConstellationsAS.evorsio.addConnection(sl1, sl3);
        ConstellationsAS.evorsio.addConnection(sl1, sl4);
        ConstellationsAS.evorsio.addConnection(sl4, sl5);
        ConstellationsAS.evorsio.addConnection(sl4, sl6);
        ConstellationsAS.evorsio.addConnection(sl6, sl7);
        register(ConstellationsAS.evorsio);
        ConstellationsAS.lucerna = new Constellation.Weak("lucerna", ColorsAS.CONSTELLATION_LUCERNA);
        sl1 = ConstellationsAS.lucerna.addStar(19, 4);
        sl2 = ConstellationsAS.lucerna.addStar(25, 14);
        sl3 = ConstellationsAS.lucerna.addStar(22, 22);
        sl4 = ConstellationsAS.lucerna.addStar(15, 25);
        sl5 = ConstellationsAS.lucerna.addStar(8, 23);
        sl6 = ConstellationsAS.lucerna.addStar(4, 12);
        ConstellationsAS.lucerna.addConnection(sl1, sl2);
        ConstellationsAS.lucerna.addConnection(sl2, sl3);
        ConstellationsAS.lucerna.addConnection(sl3, sl4);
        ConstellationsAS.lucerna.addConnection(sl4, sl5);
        ConstellationsAS.lucerna.addConnection(sl5, sl6);
        register(ConstellationsAS.lucerna);
        ConstellationsAS.mineralis = new Constellation.Weak("mineralis", ColorsAS.CONSTELLATION_MINERALIS);
        sl1 = ConstellationsAS.mineralis.addStar(17, 2);
        sl2 = ConstellationsAS.mineralis.addStar(19, 10);
        sl3 = ConstellationsAS.mineralis.addStar(13, 7);
        sl4 = ConstellationsAS.mineralis.addStar(15, 15);
        sl5 = ConstellationsAS.mineralis.addStar(22, 19);
        sl6 = ConstellationsAS.mineralis.addStar(11, 25);
        sl7 = ConstellationsAS.mineralis.addStar(18, 28);
        ConstellationsAS.mineralis.addConnection(sl1, sl2);
        ConstellationsAS.mineralis.addConnection(sl1, sl3);
        ConstellationsAS.mineralis.addConnection(sl4, sl2);
        ConstellationsAS.mineralis.addConnection(sl4, sl3);
        ConstellationsAS.mineralis.addConnection(sl4, sl5);
        ConstellationsAS.mineralis.addConnection(sl4, sl6);
        ConstellationsAS.mineralis.addConnection(sl7, sl5);
        ConstellationsAS.mineralis.addConnection(sl7, sl6);
        register(ConstellationsAS.mineralis);
        ConstellationsAS.horologium = new Constellation.WeakSpecial("horologium", ColorsAS.CONSTELLATION_HOROLOGIUM) {
            @Override
            public boolean doesShowUp(final World world, final long day) {
                final WorldContext ctx = SkyHandler.getContext(world);
                return ctx != null && ctx.getCelestialEventHandler().getSolarEclipse().isActiveDay();
            }
            
            @Override
            public float getDistribution(final World world, final long day, final boolean showsUp) {
                return showsUp ? 1.0f : 0.25f;
            }
        };
        sl1 = ConstellationsAS.horologium.addStar(28, 6);
        sl2 = ConstellationsAS.horologium.addStar(22, 10);
        sl3 = ConstellationsAS.horologium.addStar(16, 6);
        sl4 = ConstellationsAS.horologium.addStar(10, 4);
        sl5 = ConstellationsAS.horologium.addStar(6, 8);
        sl6 = ConstellationsAS.horologium.addStar(3, 27);
        ConstellationsAS.horologium.addConnection(sl1, sl2);
        ConstellationsAS.horologium.addConnection(sl2, sl3);
        ConstellationsAS.horologium.addConnection(sl3, sl4);
        ConstellationsAS.horologium.addConnection(sl4, sl5);
        ConstellationsAS.horologium.addConnection(sl5, sl6);
        register(ConstellationsAS.horologium);
        ConstellationsAS.octans = new Constellation.Weak("octans", ColorsAS.CONSTELLATION_OCTANS);
        sl1 = ConstellationsAS.octans.addStar(25, 25);
        sl2 = ConstellationsAS.octans.addStar(17, 5);
        sl3 = ConstellationsAS.octans.addStar(11, 10);
        sl4 = ConstellationsAS.octans.addStar(4, 6);
        ConstellationsAS.octans.addConnection(sl1, sl2);
        ConstellationsAS.octans.addConnection(sl1, sl3);
        ConstellationsAS.octans.addConnection(sl2, sl3);
        ConstellationsAS.octans.addConnection(sl3, sl4);
        register(ConstellationsAS.octans);
        ConstellationsAS.bootes = new Constellation.Weak("bootes", ColorsAS.CONSTELLATION_BOOTES);
        sl1 = ConstellationsAS.bootes.addStar(9, 22);
        sl2 = ConstellationsAS.bootes.addStar(3, 14);
        sl3 = ConstellationsAS.bootes.addStar(22, 27);
        sl4 = ConstellationsAS.bootes.addStar(16, 5);
        sl5 = ConstellationsAS.bootes.addStar(26, 3);
        sl6 = ConstellationsAS.bootes.addStar(24, 11);
        ConstellationsAS.bootes.addConnection(sl1, sl2);
        ConstellationsAS.bootes.addConnection(sl1, sl3);
        ConstellationsAS.bootes.addConnection(sl1, sl4);
        ConstellationsAS.bootes.addConnection(sl1, sl6);
        ConstellationsAS.bootes.addConnection(sl4, sl5);
        ConstellationsAS.bootes.addConnection(sl5, sl6);
        register(ConstellationsAS.bootes);
        ConstellationsAS.fornax = new Constellation.Weak("fornax", ColorsAS.CONSTELLATION_FORNAX);
        sl1 = ConstellationsAS.fornax.addStar(18, 29);
        sl2 = ConstellationsAS.fornax.addStar(28, 18);
        sl3 = ConstellationsAS.fornax.addStar(21, 13);
        sl4 = ConstellationsAS.fornax.addStar(16, 18);
        sl5 = ConstellationsAS.fornax.addStar(19, 6);
        sl6 = ConstellationsAS.fornax.addStar(13, 2);
        sl7 = ConstellationsAS.fornax.addStar(9, 21);
        final StarLocation sl8 = ConstellationsAS.fornax.addStar(2, 17);
        ConstellationsAS.fornax.addConnection(sl1, sl2);
        ConstellationsAS.fornax.addConnection(sl2, sl3);
        ConstellationsAS.fornax.addConnection(sl3, sl4);
        ConstellationsAS.fornax.addConnection(sl4, sl1);
        ConstellationsAS.fornax.addConnection(sl3, sl5);
        ConstellationsAS.fornax.addConnection(sl5, sl6);
        ConstellationsAS.fornax.addConnection(sl4, sl7);
        ConstellationsAS.fornax.addConnection(sl7, sl8);
        register(ConstellationsAS.fornax);
        ConstellationsAS.pelotrio = new Constellation.WeakSpecial("pelotrio", ColorsAS.CONSTELLATION_PELOTRIO) {
            @Override
            public boolean doesShowUp(final World world, final long day) {
                final MoonPhase phase = MoonPhase.fromWorld((IWorld)world);
                return phase == MoonPhase.NEW || phase == MoonPhase.FULL;
            }
            
            @Override
            public float getDistribution(final World world, final long day, final boolean showingUp) {
                if (showingUp) {
                    return 1.0f;
                }
                final MoonPhase current = MoonPhase.fromWorld((IWorld)world);
                if (current == MoonPhase.WANING_1_2 || current == MoonPhase.WAXING_1_2) {
                    return 0.3f;
                }
                return 0.65f;
            }
        };
        sl1 = ConstellationsAS.pelotrio.addStar(17, 24);
        sl2 = ConstellationsAS.pelotrio.addStar(27, 25);
        sl3 = ConstellationsAS.pelotrio.addStar(22, 8);
        sl4 = ConstellationsAS.pelotrio.addStar(14, 14);
        sl5 = ConstellationsAS.pelotrio.addStar(8, 29);
        sl6 = ConstellationsAS.pelotrio.addStar(3, 8);
        sl7 = ConstellationsAS.pelotrio.addStar(9, 10);
        ConstellationsAS.pelotrio.addConnection(sl1, sl2);
        ConstellationsAS.pelotrio.addConnection(sl2, sl3);
        ConstellationsAS.pelotrio.addConnection(sl3, sl4);
        ConstellationsAS.pelotrio.addConnection(sl4, sl1);
        ConstellationsAS.pelotrio.addConnection(sl1, sl5);
        ConstellationsAS.pelotrio.addConnection(sl5, sl6);
        ConstellationsAS.pelotrio.addConnection(sl6, sl7);
        ConstellationsAS.pelotrio.addConnection(sl7, sl4);
        register(ConstellationsAS.pelotrio);
        ConstellationsAS.gelu = new Constellation.Minor("gelu", ColorsAS.CONSTELLATION_GELU, new MoonPhase[] { MoonPhase.NEW, MoonPhase.WAXING_1_4, MoonPhase.WAXING_1_2 }) {
            @Override
            public void affectConstellationEffect(final ConstellationEffectProperties properties) {
                properties.multiplyPotency(1.4f);
                properties.multiplySize(2.0);
            }
        };
        sl1 = ConstellationsAS.gelu.addStar(21, 28);
        sl2 = ConstellationsAS.gelu.addStar(27, 13);
        sl3 = ConstellationsAS.gelu.addStar(29, 4);
        sl4 = ConstellationsAS.gelu.addStar(12, 14);
        sl5 = ConstellationsAS.gelu.addStar(3, 12);
        sl6 = ConstellationsAS.gelu.addStar(9, 21);
        ConstellationsAS.gelu.addConnection(sl1, sl2);
        ConstellationsAS.gelu.addConnection(sl2, sl3);
        ConstellationsAS.gelu.addConnection(sl3, sl4);
        ConstellationsAS.gelu.addConnection(sl4, sl5);
        ConstellationsAS.gelu.addConnection(sl5, sl6);
        ConstellationsAS.gelu.addConnection(sl6, sl1);
        register(ConstellationsAS.gelu);
        ConstellationsAS.ulteria = new Constellation.Minor("ulteria", ColorsAS.CONSTELLATION_ULTERIA, new MoonPhase[] { MoonPhase.WANING_1_2, MoonPhase.WANING_3_4, MoonPhase.NEW }) {
            @Override
            public void affectConstellationEffect(final ConstellationEffectProperties properties) {
                properties.multiplyEffectAmplifier(2.0f);
                properties.multiplyPotency(0.5f);
                properties.multiplySize(0.5);
            }
        };
        sl1 = ConstellationsAS.ulteria.addStar(4, 28);
        sl2 = ConstellationsAS.ulteria.addStar(7, 22);
        sl3 = ConstellationsAS.ulteria.addStar(9, 14);
        sl4 = ConstellationsAS.ulteria.addStar(21, 15);
        sl5 = ConstellationsAS.ulteria.addStar(15, 8);
        sl6 = ConstellationsAS.ulteria.addStar(24, 3);
        ConstellationsAS.ulteria.addConnection(sl1, sl2);
        ConstellationsAS.ulteria.addConnection(sl2, sl3);
        ConstellationsAS.ulteria.addConnection(sl3, sl4);
        ConstellationsAS.ulteria.addConnection(sl3, sl5);
        ConstellationsAS.ulteria.addConnection(sl4, sl5);
        ConstellationsAS.ulteria.addConnection(sl5, sl6);
        register(ConstellationsAS.ulteria);
        ConstellationsAS.alcara = new Constellation.Minor("alcara", ColorsAS.CONSTELLATION_ALCARA, new MoonPhase[] { MoonPhase.WANING_1_2, MoonPhase.WAXING_1_2 }) {
            @Override
            public void affectConstellationEffect(final ConstellationEffectProperties properties) {
                properties.setCorrupted(true);
                properties.multiplySize(1.2000000476837158);
            }
        };
        sl1 = ConstellationsAS.alcara.addStar(24, 27);
        sl2 = ConstellationsAS.alcara.addStar(17, 16);
        sl3 = ConstellationsAS.alcara.addStar(12, 9);
        sl4 = ConstellationsAS.alcara.addStar(23, 9);
        sl5 = ConstellationsAS.alcara.addStar(21, 2);
        sl6 = ConstellationsAS.alcara.addStar(10, 21);
        sl7 = ConstellationsAS.alcara.addStar(3, 20);
        ConstellationsAS.alcara.addConnection(sl1, sl2);
        ConstellationsAS.alcara.addConnection(sl2, sl3);
        ConstellationsAS.alcara.addConnection(sl2, sl4);
        ConstellationsAS.alcara.addConnection(sl4, sl5);
        ConstellationsAS.alcara.addConnection(sl5, sl3);
        ConstellationsAS.alcara.addConnection(sl2, sl6);
        ConstellationsAS.alcara.addConnection(sl6, sl7);
        ConstellationsAS.alcara.addConnection(sl7, sl3);
        register(ConstellationsAS.alcara);
        ConstellationsAS.vorux = new Constellation.Minor("vorux", ColorsAS.CONSTELLATION_VORUX, new MoonPhase[] { MoonPhase.FULL, MoonPhase.WAXING_3_4, MoonPhase.WANING_3_4 }) {
            @Override
            public void affectConstellationEffect(final ConstellationEffectProperties properties) {
                properties.multiplyEffectAmplifier(0.8f);
                properties.multiplyPotency(5.0f);
                properties.multiplySize(1.2000000476837158);
            }
        };
        sl1 = ConstellationsAS.vorux.addStar(7, 29);
        sl2 = ConstellationsAS.vorux.addStar(15, 12);
        sl3 = ConstellationsAS.vorux.addStar(23, 7);
        sl4 = ConstellationsAS.vorux.addStar(28, 14);
        sl5 = ConstellationsAS.vorux.addStar(3, 6);
        ConstellationsAS.vorux.addConnection(sl1, sl2);
        ConstellationsAS.vorux.addConnection(sl2, sl3);
        ConstellationsAS.vorux.addConnection(sl3, sl4);
        ConstellationsAS.vorux.addConnection(sl2, sl5);
        register(ConstellationsAS.vorux);
    }
    
    private static void register(final IConstellation cst) {
        AstralSorcery.getProxy().getRegistryPrimer().register(cst);
    }
}
