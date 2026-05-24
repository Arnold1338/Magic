package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import net.minecraft.world.level.block.Blocks;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import net.minecraft.world.level.LevelAccessor;
import hellfirepvp.astralsorcery.common.auxiliary.CropHelper;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.tick.PlayerTickPerk;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyGrowables extends KeyPerk implements PlayerTickPerk
{
    private static final float defaultChanceToBonemeal = 0.3f;
    private static final int defaultRadius = 3;
    private static final int defaultChargeCost = 120;
    public static final Config CONFIG;
    
    public KeyGrowables(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    @Override
    public void onPlayerTick(final Player player, final LogicalSide side) {
        if (!side.isServer()) {
            return;
        }
        final PlayerProgress prog = ResearchHelper.getProgress(player, side);
        final float cChance = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, ((Double)KeyGrowables.CONFIG.chanceToBonemeal.get()).floatValue());
        if (KeyGrowables.rand.nextFloat() < cChance && AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)KeyGrowables.CONFIG.chargeCost.get(), true)) {
            final float fRadius = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, (float)(int)KeyGrowables.CONFIG.radius.get());
            final int rRadius = Math.max(Mth.func_76141_d(fRadius), 1);
            final BlockPos pos = player.func_233580_cy_().offset(KeyGrowables.rand.nextInt(rRadius * 2) + 1 - rRadius, KeyGrowables.rand.nextInt(rRadius * 2) + 1 - rRadius, KeyGrowables.rand.nextInt(rRadius * 2) + 1 - rRadius);
            final World w = player.func_130014_f_();
            final CropHelper.GrowablePlant plant = CropHelper.wrapPlant((IWorld)w, pos);
            PktPlayEffect pkt = null;
            if (plant != null) {
                if (plant.tryGrow((IWorld)w, KeyGrowables.rand)) {
                    AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)KeyGrowables.CONFIG.chargeCost.get(), false);
                    pkt = new PktPlayEffect(PktPlayEffect.Type.CROP_GROWTH).addData(buf -> ByteBufUtils.writeVector(buf, new Vector3((Vector3i)pos)));
                }
            }
            else {
                final BlockState at = w.getBlockState(pos);
                if (at.getBlock().equals(Blocks.field_150346_d) && w.isEmptyBlock(pos.above()) && w.func_175656_a(pos, Blocks.field_196658_i.defaultBlockState())) {
                    AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)KeyGrowables.CONFIG.chargeCost.get(), false);
                    pkt = new PktPlayEffect(PktPlayEffect.Type.CROP_GROWTH).addData(buf -> ByteBufUtils.writeVector(buf, new Vector3((Vector3i)pos)));
                }
            }
            if (pkt != null) {
                PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos(w, (Vector3i)pos, 16.0));
            }
        }
    }
    
    static {
        CONFIG = new Config("key.growable");
    }
    
    public static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.DoubleValue chanceToBonemeal;
        private ForgeConfigSpec.IntValue radius;
        private ForgeConfigSpec.IntValue chargeCost;
        
        private Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.chanceToBonemeal = cfgBuilder.comment("Sets the chance to try to see if a random plant near the player gets bonemeal'd.").translation(this.translationKey("chanceToBonemeal")).defineInRange("chanceToBonemeal", 0.30000001192092896, 0.0, 1.0);
            this.radius = cfgBuilder.comment("Defines the radius around which the perk effect should apply around the player.").translation(this.translationKey("radius")).defineInRange("radius", 3, 1, 16);
            this.chargeCost = cfgBuilder.comment("Defines the amount of starlight charge consumed per growth-attempt.").translation(this.translationKey("chargeCost")).defineInRange("chargeCost", 120, 1, 500);
        }
    }
}
