package hellfirepvp.astralsorcery.common.constellation.mantle.effect;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraft.world.level.level.LevelAccessor;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraft.world.level.level.Level;
import net.minecraft.world.level.entity.Entity;
import hellfirepvp.astralsorcery.common.entity.EntitySpectralTool;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import net.minecraft.world.level.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.item.armor.ItemMantle;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;

public class MantleEffectPelotrio extends MantleEffect
{
    public static PelotrioConfig CONFIG;
    
    public MantleEffectPelotrio() {
        super(ConstellationsAS.pelotrio);
    }
    
    @Override
    protected void attachEventListeners(final IEventBus bus) {
        super.attachEventListeners(bus);
        bus.addListener((Consumer)this::onHurt);
        bus.addListener((Consumer)this::onBreak);
    }
    
    private void onHurt(final LivingAttackEvent event) {
        final World world = event.getEntityLiving().func_130014_f_();
        if (world.func_201670_d()) {
            return;
        }
        final LivingEntity attacked = event.getEntityLiving();
        final Entity attacker = event.getSource().func_76346_g();
        if (attacker instanceof Player) {
            if (attacked instanceof ServerPlayer && MiscUtils.isPlayerFakeMP((ServerPlayer)attacked)) {
                return;
            }
            final Player player = (Player)attacker;
            if (ItemMantle.getEffect((LivingEntity)player, ConstellationsAS.pelotrio) != null && MantleEffectPelotrio.rand.nextFloat() < (double)MantleEffectPelotrio.CONFIG.chanceSpawnSword.get() && AlignmentChargeHandler.INSTANCE.hasCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectPelotrio.CONFIG.chargeCostPerSword.get()) && world.func_217376_c((Entity)new EntitySpectralTool(world, player.func_233580_cy_().above(), (LivingEntity)player, EntitySpectralTool.ToolTask.createAttackTask()))) {
                AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectPelotrio.CONFIG.chargeCostPerSword.get(), false);
            }
        }
    }
    
    private void onBreak(final BlockEvent.BreakEvent event) {
        final IWorld world = event.getWorld();
        if (world.func_201670_d() || !(world instanceof World)) {
            return;
        }
        final Player player = event.getPlayer();
        if ((!(player instanceof ServerPlayer) || !MiscUtils.isPlayerFakeMP((ServerPlayer)player)) && ItemMantle.getEffect((LivingEntity)player, ConstellationsAS.pelotrio) != null) {
            final BlockState state = event.getState();
            if ((state.getHarvestTool() == ToolType.AXE || !state.func_235783_q_()) && (state.func_235714_a_((ITag)BlockTags.field_200031_h) || state.func_235714_a_((ITag)BlockTags.field_206952_E)) && !player.func_184614_ca().isEmpty() && player.func_184614_ca().getToolTypes().contains(ToolType.AXE)) {
                if (MantleEffectPelotrio.rand.nextFloat() < (double)MantleEffectPelotrio.CONFIG.chanceSpawnAxe.get() && AlignmentChargeHandler.INSTANCE.hasCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectPelotrio.CONFIG.chargeCostPerAxe.get()) && world.func_217376_c((Entity)new EntitySpectralTool((World)world, player.func_233580_cy_(), (LivingEntity)player, EntitySpectralTool.ToolTask.createLogTask()))) {
                    AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectPelotrio.CONFIG.chargeCostPerAxe.get(), false);
                }
                return;
            }
            if ((state.getHarvestTool() == ToolType.PICKAXE || !state.func_235783_q_()) && !player.func_184614_ca().isEmpty() && player.func_184614_ca().getToolTypes().contains(ToolType.PICKAXE) && MantleEffectPelotrio.rand.nextFloat() < (double)MantleEffectPelotrio.CONFIG.chanceSpawnPickaxe.get() && AlignmentChargeHandler.INSTANCE.hasCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectPelotrio.CONFIG.chargeCostPerPickaxe.get()) && world.func_217376_c((Entity)new EntitySpectralTool((World)world, player.func_233580_cy_(), (LivingEntity)player, EntitySpectralTool.ToolTask.createPickaxeTask()))) {
                AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectPelotrio.CONFIG.chargeCostPerPickaxe.get(), false);
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void tickClient(final Player player) {
        super.tickClient(player);
        this.playCapeSparkles(player, 0.15f);
    }
    
    @Override
    protected boolean usesTickMethods() {
        return true;
    }
    
    @Override
    public Config getConfig() {
        return MantleEffectPelotrio.CONFIG;
    }
    
    static {
        MantleEffectPelotrio.CONFIG = new PelotrioConfig();
    }
    
    public static class PelotrioConfig extends Config
    {
        private final double defaultChanceSpawnSword = 0.6;
        private final double defaultChanceSpawnPickaxe = 0.8;
        private final double defaultChanceSpawnAxe = 0.8;
        private final double defaultSpeedSword = 2.3;
        private final double defaultSpeedPick = 1.8;
        private final double defaultSpeedAxe = 1.8;
        private final double defaultSwordDamage = 4.0;
        private final int defaultDurationSword = 100;
        private final int defaultDurationPickaxe = 100;
        private final int defaultDurationAxe = 100;
        private final int defaultTicksPerSwordAttack = 6;
        private final int defaultTicksPerPickaxeBlockBreak = 4;
        private final int defaultTicksPerAxeLogBreak = 2;
        private final int defaultChargeCostPerSword = 250;
        private final int defaultChargeCostPerPickaxe = 250;
        private final int defaultChargeCostPerAxe = 250;
        public ForgeConfigSpec.DoubleValue chanceSpawnSword;
        public ForgeConfigSpec.DoubleValue chanceSpawnPickaxe;
        public ForgeConfigSpec.DoubleValue chanceSpawnAxe;
        public ForgeConfigSpec.DoubleValue speedSword;
        public ForgeConfigSpec.DoubleValue speedPickaxe;
        public ForgeConfigSpec.DoubleValue speedAxe;
        public ForgeConfigSpec.DoubleValue swordDamage;
        public ForgeConfigSpec.IntValue durationSword;
        public ForgeConfigSpec.IntValue durationPickaxe;
        public ForgeConfigSpec.IntValue durationAxe;
        public ForgeConfigSpec.IntValue ticksPerSwordAttack;
        public ForgeConfigSpec.IntValue ticksPerPickaxeBlockBreak;
        public ForgeConfigSpec.IntValue ticksPerAxeLogBreak;
        public ForgeConfigSpec.IntValue chargeCostPerSword;
        public ForgeConfigSpec.IntValue chargeCostPerPickaxe;
        public ForgeConfigSpec.IntValue chargeCostPerAxe;
        
        public PelotrioConfig() {
            super("pelotrio");
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Defines the chance of a spectral sword spawning that fights mobs nearby for a while when you attack a mob.").translation(this.translationKey("chanceSpawnSword"));
            final String s = "chanceSpawnSword";
            this.getClass();
            this.chanceSpawnSword = translation.defineInRange(s, 0.6, 0.0, 1.0);
            final ForgeConfigSpec.Builder translation2 = cfgBuilder.comment("Defines the chance of a spectral pickaxe spawning that's mining for you for a bit when you mine a block.").translation(this.translationKey("chanceSpawnPickaxe"));
            final String s2 = "chanceSpawnPickaxe";
            this.getClass();
            this.chanceSpawnPickaxe = translation2.defineInRange(s2, 0.8, 0.0, 1.0);
            final ForgeConfigSpec.Builder translation3 = cfgBuilder.comment("Defines the chance of a spectral axe spawning that's chopping logs and leaves for you for a bit when you break a log or leaf.").translation(this.translationKey("chanceSpawnAxe"));
            final String s3 = "chanceSpawnAxe";
            this.getClass();
            this.chanceSpawnAxe = translation3.defineInRange(s3, 0.8, 0.0, 1.0);
            final ForgeConfigSpec.Builder translation4 = cfgBuilder.comment("Defines the movement/flying speed of a spawned spectral sword.").translation(this.translationKey("speedSword"));
            final String s4 = "speedSword";
            this.getClass();
            this.speedSword = translation4.defineInRange(s4, 2.3, 0.5, 4.5);
            final ForgeConfigSpec.Builder translation5 = cfgBuilder.comment("Defines the movement/flying speed of a spawned spectral pickaxe.").translation(this.translationKey("speedPickaxe"));
            final String s5 = "speedPickaxe";
            this.getClass();
            this.speedPickaxe = translation5.defineInRange(s5, 1.8, 0.5, 4.5);
            final ForgeConfigSpec.Builder translation6 = cfgBuilder.comment("Defines the movement/flying speed of a spawned spectral axe.").translation(this.translationKey("speedAxe"));
            final String s6 = "speedAxe";
            this.getClass();
            this.speedAxe = translation6.defineInRange(s6, 1.8, 0.5, 4.5);
            final ForgeConfigSpec.Builder translation7 = cfgBuilder.comment("Defines the damage the sword does per attack.").translation(this.translationKey("swordDamage"));
            final String s7 = "swordDamage";
            this.getClass();
            this.swordDamage = translation7.defineInRange(s7, 4.0, 0.1, 32.0);
            final ForgeConfigSpec.Builder translation8 = cfgBuilder.comment("Defines the duration a spawned spectral sword is alive for. It will stay around this amount plus randomly twice this amount of ticks.").translation(this.translationKey("durationSword"));
            final String s8 = "durationSword";
            this.getClass();
            this.durationSword = translation8.defineInRange(s8, 100, 20, 500);
            final ForgeConfigSpec.Builder translation9 = cfgBuilder.comment("Defines the duration a spawned spectral pickaxe is alive for. It will stay around this amount plus randomly twice this amount of ticks.").translation(this.translationKey("durationPickaxe"));
            final String s9 = "durationPickaxe";
            this.getClass();
            this.durationPickaxe = translation9.defineInRange(s9, 100, 20, 500);
            final ForgeConfigSpec.Builder translation10 = cfgBuilder.comment("Defines the duration a spawned spectral axe is alive for. It will stay around this amount plus randomly twice this amount of ticks.").translation(this.translationKey("durationAxe"));
            final String s10 = "durationAxe";
            this.getClass();
            this.durationAxe = translation10.defineInRange(s10, 100, 20, 500);
            final ForgeConfigSpec.Builder translation11 = cfgBuilder.comment("Defines how many ticks are at least between sword attacks the sword makes.").translation(this.translationKey("ticksPerSwordAttack"));
            final String s11 = "ticksPerSwordAttack";
            this.getClass();
            this.ticksPerSwordAttack = translation11.defineInRange(s11, 6, 1, 100);
            final ForgeConfigSpec.Builder translation12 = cfgBuilder.comment("Defines how long a pickaxe needs to break a block.").translation(this.translationKey("ticksPerPickaxeBlockBreak"));
            final String s12 = "ticksPerPickaxeBlockBreak";
            this.getClass();
            this.ticksPerPickaxeBlockBreak = translation12.defineInRange(s12, 4, 1, 100);
            final ForgeConfigSpec.Builder translation13 = cfgBuilder.comment("Defines how long an axe is going to need to break a leaf or log.").translation(this.translationKey("ticksPerAxeLogBreak"));
            final String s13 = "ticksPerAxeLogBreak";
            this.getClass();
            this.ticksPerAxeLogBreak = translation13.defineInRange(s13, 2, 1, 100);
            final ForgeConfigSpec.Builder translation14 = cfgBuilder.comment("Set the amount alignment charge consumed per created spectral sword").translation(this.translationKey("chargeCostPerSword"));
            final String s14 = "chargeCostPerSword";
            this.getClass();
            this.chargeCostPerSword = translation14.defineInRange(s14, 250, 0, 1000);
            final ForgeConfigSpec.Builder translation15 = cfgBuilder.comment("Set the amount alignment charge consumed per created spectral sword").translation(this.translationKey("chargeCostPerPickaxe"));
            final String s15 = "chargeCostPerPickaxe";
            this.getClass();
            this.chargeCostPerPickaxe = translation15.defineInRange(s15, 250, 0, 1000);
            final ForgeConfigSpec.Builder translation16 = cfgBuilder.comment("Set the amount alignment charge consumed per created spectral sword").translation(this.translationKey("chargeCostPerAxe"));
            final String s16 = "chargeCostPerAxe";
            this.getClass();
            this.chargeCostPerAxe = translation16.defineInRange(s16, 250, 0, 1000);
        }
    }
}
