package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.config.registry.OreBlockRarityRegistry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.tick.PlayerTickPerk;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyStoneEnrichment extends KeyPerk implements PlayerTickPerk
{
    private static final int defaultEnrichmentRadius = 3;
    private static final int defaultChanceToEnrich = 55;
    private static final int defaultChargeCost = 150;
    public static final Config CONFIG;
    
    public KeyStoneEnrichment(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    @Override
    public void onPlayerTick(final Player player, final LogicalSide side) {
        if (side.isServer()) {
            final PlayerProgress prog = ResearchHelper.getProgress(player, side);
            float modChance = (float)(int)KeyStoneEnrichment.CONFIG.chanceToEnrich.get();
            modChance /= PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT);
            if (KeyStoneEnrichment.rand.nextInt(Math.round(Math.max(modChance, 1.0f))) == 0 && AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)KeyStoneEnrichment.CONFIG.chargeCost.get(), true)) {
                float radius = (float)(int)KeyStoneEnrichment.CONFIG.enrichmentRadius.get();
                radius *= PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT);
                final Vector3 vec = Vector3.atEntityCenter((Entity)player).add(KeyStoneEnrichment.rand.nextFloat() * radius * 2.0f - radius, KeyStoneEnrichment.rand.nextFloat() * radius * 2.0f - radius, KeyStoneEnrichment.rand.nextFloat() * radius * 2.0f - radius);
                final World world = player.func_130014_f_();
                final BlockPos pos = vec.toBlockPos();
                if (BlockTags.field_242172_aH.func_230235_a_((Object)world.getBlockState(pos).getBlock())) {
                    final Block block = OreBlockRarityRegistry.STONE_ENRICHMENT.getRandomBlock(KeyStoneEnrichment.rand);
                    if (block != null && world.func_180501_a(pos, block.defaultBlockState(), 11)) {
                        AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)KeyStoneEnrichment.CONFIG.chargeCost.get(), false);
                    }
                }
            }
        }
    }
    
    static {
        CONFIG = new Config("key.stone_enrichment");
    }
    
    public static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.IntValue enrichmentRadius;
        private ForgeConfigSpec.IntValue chanceToEnrich;
        private ForgeConfigSpec.IntValue chargeCost;
        
        private Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.enrichmentRadius = cfgBuilder.comment("Defines the radius where a random position to generate a ore at is checked for").translation(this.translationKey("enrichmentRadius")).defineInRange("enrichmentRadius", 3, 1, 15);
            this.chanceToEnrich = cfgBuilder.comment("Sets the chance (Random.nextInt(chance) == 0) to try to see if a random stone next to the player should get turned into an ore; the lower the more likely").translation(this.translationKey("chanceToEnrich")).defineInRange("chanceToEnrich", 55, 2, 512);
            this.chargeCost = cfgBuilder.comment("Defines the amount of starlight charge consumed per created ore.").translation(this.translationKey("chargeCost")).defineInRange("chargeCost", 150, 1, 500);
        }
    }
}
