package hellfirepvp.astralsorcery.common.constellation.mantle.effect;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import java.util.Iterator;
import java.util.Set;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.util.block.BlockDiscoverer;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.function.Predicate;
import java.awt.Color;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraft.world.level.block.entity.MobSpawnerTileEntity;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;

public class MantleEffectLucerna extends MantleEffect
{
    public static LucernaConfig CONFIG;
    
    public MantleEffectLucerna() {
        super(ConstellationsAS.lucerna);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void tickClient(final Player player) {
        super.tickClient(player);
        this.playCapeSparkles(player, 0.15f);
        if (MantleEffectLucerna.rand.nextBoolean()) {
            this.playEntityHighlight(player);
        }
        if ((boolean)MantleEffectLucerna.CONFIG.findSpawners.get() && MantleEffectLucerna.rand.nextInt(10) == 0) {
            this.playBlockHighlight(player, ColorsAS.MANTLE_LUCERNA_SPAWNER, tileEntity -> tileEntity instanceof MobSpawnerTileEntity);
        }
        if ((boolean)MantleEffectLucerna.CONFIG.findChests.get() && MantleEffectLucerna.rand.nextInt(10) == 0) {
            this.playBlockHighlight(player, ColorsAS.MANTLE_LUCERNA_INVENTORY, tileEntity -> tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent());
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playBlockHighlight(final Player player, final Color highlightColor, final Predicate<BlockEntity> test) {
        float chance = 0.9f;
        final Set<BlockPos> positions = BlockDiscoverer.searchForTileEntitiesAround(player.level(), player.func_233580_cy_(), (int)MantleEffectLucerna.CONFIG.range.get(), test);
        for (final BlockPos pos : positions) {
            if (MantleEffectLucerna.rand.nextFloat() > chance) {

            }
            final Vector3 at = new Vector3((Vec3i)pos).add(MantleEffectLucerna.rand.nextFloat(), MantleEffectLucerna.rand.nextFloat(), MantleEffectLucerna.rand.nextFloat());
            if (at.distance((Entity)player) < 4.0) {

            }
            EffectHelper.of(EffectTemplatesAS.GENERIC_DEPTH_PARTICLE).setOwner(player.getUUID()).spawn(at).color(VFXColorFunction.constant(highlightColor)).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.4f + MantleEffectLucerna.rand.nextFloat() * 0.4f).setMaxAge(30 + MantleEffectLucerna.rand.nextInt(15));
            if (MantleEffectLucerna.rand.nextFloat() > 0.35f) {
                EffectHelper.of(EffectTemplatesAS.GENERIC_DEPTH_PARTICLE).setOwner(player.getUUID()).spawn(at).color(VFXColorFunction.WHITE).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.2f + MantleEffectLucerna.rand.nextFloat() * 0.2f).setMaxAge(20 + MantleEffectLucerna.rand.nextInt(10));
            }
            chance *= 0.9f;
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playEntityHighlight(final Player player) {
        final AABB box = new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0).func_186662_g((double)(int)MantleEffectLucerna.CONFIG.range.get()).func_186670_a(player.func_233580_cy_());
        final List<LivingEntity> entities = player.level().func_217357_a((Class)LivingEntity.class, box);
        for (final LivingEntity entity : entities) {
            if (entity.isAlive() && !entity.equals((Object)player)) {
                if (MantleEffectLucerna.rand.nextInt(8) != 0) {

                }
                final Vector3 atEntity = Vector3.atEntityCorner((Entity)entity);
                if (atEntity.distance((Entity)player) < 2.0) {

                }
                atEntity.add(MantleEffectLucerna.rand.nextFloat() * entity.func_213311_cf(), MantleEffectLucerna.rand.nextFloat() * entity.func_213302_cg(), MantleEffectLucerna.rand.nextFloat() * entity.func_213311_cf());
                EffectHelper.of(EffectTemplatesAS.GENERIC_DEPTH_PARTICLE).setOwner(player.getUUID()).spawn(atEntity).color(VFXColorFunction.constant(this.getAssociatedConstellation().getConstellationColor())).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.4f + MantleEffectLucerna.rand.nextFloat() * 0.4f).setMaxAge(30 + MantleEffectLucerna.rand.nextInt(15));
                if (MantleEffectLucerna.rand.nextFloat() <= 0.35f) {

                }
                EffectHelper.of(EffectTemplatesAS.GENERIC_DEPTH_PARTICLE).setOwner(player.getUUID()).spawn(atEntity).color(VFXColorFunction.WHITE).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.2f + MantleEffectLucerna.rand.nextFloat() * 0.2f).setMaxAge(20 + MantleEffectLucerna.rand.nextInt(10));
            }
        }
    }
    
    @Override
    public Config getConfig() {
        return MantleEffectLucerna.CONFIG;
    }
    
    @Override
    protected boolean usesTickMethods() {
        return true;
    }
    
    static {
        MantleEffectLucerna.CONFIG = new LucernaConfig();
    }
    
    public static class LucernaConfig extends Config
    {
        private final int defaultRange = 48;
        private final boolean defaultFindSpawners = true;
        private final boolean defaultFindChests = true;
        public ForgeConfigSpec.IntValue range;
        public ForgeConfigSpec.BooleanValue findSpawners;
        public ForgeConfigSpec.BooleanValue findChests;
        
        public LucernaConfig() {
            super("lucerna");
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Sets the maximum range of where the lucerna cape effect will get entities (and potentially other stuff given the config option for that is enabled) to highlight.").translation(this.translationKey("range"));
            final String s = "range";
            this.getClass();
            this.range = translation.defineInRange(s, 48, 0, 512);
            final ForgeConfigSpec.Builder translation2 = cfgBuilder.comment("If this is set to true, particles spawned by the lucerna cape effect will also highlight spawners nearby.").translation(this.translationKey("findSpawners"));
            final String s2 = "findSpawners";
            this.getClass();
            this.findSpawners = translation2.define(s2, true);
            final ForgeConfigSpec.Builder translation3 = cfgBuilder.comment("If this is set to true, particles spawned by the lucerna cape effect will also highlight chests nearby.").translation(this.translationKey("findChests"));
            final String s3 = "findChests";
            this.getClass();
            this.findChests = translation3.define(s3, true);
        }
    }
}
