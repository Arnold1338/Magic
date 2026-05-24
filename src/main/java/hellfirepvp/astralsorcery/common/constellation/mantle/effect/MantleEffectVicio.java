package hellfirepvp.astralsorcery.common.constellation.mantle.effect;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import net.minecraft.world.level.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.item.armor.ItemMantle;
import net.minecraft.world.level.item.ItemStack;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.event.PlayerAffectionFlags;
import hellfirepvp.astralsorcery.common.constellation.effect.aoe.CEffectVicio;
import hellfirepvp.astralsorcery.common.event.helper.EventHelperTemporaryFlight;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyMantleFlight;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;

public class MantleEffectVicio extends MantleEffect
{
    public static VicioConfig CONFIG;
    
    public MantleEffectVicio() {
        super(ConstellationsAS.vicio);
    }
    
    @Override
    protected void tickServer(final Player player) {
        super.tickServer(player);
        final PlayerProgress prog = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (prog.getPerkData().hasPerkEffect(p -> p instanceof KeyMantleFlight) && AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectVicio.CONFIG.chargeCost.get(), true)) {
            final boolean prev = player.field_71075_bZ.field_75101_c;
            player.field_71075_bZ.field_75101_c = true;
            if (!prev) {
                player.func_71016_p();
            }
            EventHelperTemporaryFlight.allowFlight(player, 20);
            if (player.field_71075_bZ.field_75100_b && !player.func_233570_aj_() && player.field_70173_aa % 20 == 0 && !PlayerAffectionFlags.isPlayerAffected(player, CEffectVicio.FLAG)) {
                AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectVicio.CONFIG.chargeCost.get(), false);
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void tickClient(final Player player) {
        super.tickClient(player);
        if (player.func_184613_cA() || (!player.func_184812_l_() && !player.func_175149_v() && player.field_71075_bZ.field_75100_b)) {
            if (!Minecraft.func_71410_x().field_71474_y.func_243230_g().func_243193_b()) {
                this.playCapeSparkles(player, 0.1f);
            }
            else {
                this.playCapeSparkles(player, 0.7f);
            }
        }
        else {
            this.playCapeSparkles(player, 0.15f);
        }
    }
    
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    @Override
    protected FXFacingParticle spawnFacingParticle(final Player player, final Vector3 at) {
        if (player.func_184613_cA() || (!player.func_184812_l_() && !player.func_175149_v() && player.field_71075_bZ.field_75100_b)) {
            at.subtract(player.func_213322_ci().func_216372_d(1.5, 1.5, 1.5));
        }
        return super.spawnFacingParticle(player, at);
    }
    
    public static boolean isUsableElytra(final ItemStack elytraStack, final Player wearingEntity) {
        if (elytraStack.getItem() instanceof ItemMantle) {
            final MantleEffect effect = ItemMantle.getEffect((LivingEntity)wearingEntity, ConstellationsAS.vicio);
            PlayerProgress progress;
            if (wearingEntity.func_130014_f_().func_201670_d()) {
                progress = ResearchHelper.getClientProgress();
            }
            else {
                progress = ResearchHelper.getProgress(wearingEntity, LogicalSide.SERVER);
            }
            return effect != null && !progress.getPerkData().hasPerkEffect(p -> p instanceof KeyMantleFlight);
        }
        return false;
    }
    
    @Override
    public Config getConfig() {
        return MantleEffectVicio.CONFIG;
    }
    
    @Override
    protected boolean usesTickMethods() {
        return true;
    }
    
    static {
        MantleEffectVicio.CONFIG = new VicioConfig();
    }
    
    private static class VicioConfig extends Config
    {
        private static final int defaultChargeCost = 100;
        private ForgeConfigSpec.IntValue chargeCost;
        
        public VicioConfig() {
            super("vicio");
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            this.chargeCost = cfgBuilder.comment("Defines the amount of starlight charge consumed per !second! during creative-flight with the vicio mantle.").translation(this.translationKey("chargeCost")).defineInRange("chargeCost", 100, 1, 500);
        }
    }
}
