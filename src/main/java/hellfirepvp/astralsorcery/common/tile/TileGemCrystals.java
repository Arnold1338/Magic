package hellfirepvp.astralsorcery.common.tile;

import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.Property;
import hellfirepvp.astralsorcery.common.block.tile.BlockGemCrystalCluster;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import hellfirepvp.astralsorcery.common.tile.base.TileEntityTick;

public class TileGemCrystals extends TileEntityTick
{
    public static final int TICK_GROWTH_CHANCE = 10000;
    
    public TileGemCrystals() {
        super(TileEntityTypesAS.GEM_CRYSTAL_CLUSTER);
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (!this.func_145831_w().func_201670_d()) {
            if (this.getGrowth().getGrowthStage() < 2 && this.doesSeeSky()) {
                this.tryGrowWithChance(10000);
            }
            else if (this.getGrowth().getGrowthStage() == 2 && TileGemCrystals.rand.nextInt(4000) == 0) {
                this.setGrowth(this.getGrowth().shrink());
            }
        }
        else if (this.getGrowth().getGrowthStage() == 2) {
            this.playHarvestEffects();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playHarvestEffects() {
        final Vector3 pos = new Vector3(this).add(0.5, 0.5, 0.5).add(this.func_195044_w().func_191059_e((IBlockReader)this.func_145831_w(), this.func_174877_v()));
        MiscUtils.applyRandomOffset(pos, TileGemCrystals.rand, 0.5f);
        EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).color(VFXColorFunction.constant(this.getGrowth().getDisplayColor())).setScaleMultiplier(0.1f + TileGemCrystals.rand.nextFloat() * 0.05f).setMaxAge(15 + TileGemCrystals.rand.nextInt(5));
    }
    
    public void tryGrowWithChance(int growthChance) {
        final float distribution = DayTimeHelper.getCurrentDaytimeDistribution(this.func_145831_w());
        growthChance *= (int)(1.0f - 0.2f * distribution);
        this.grow(growthChance);
    }
    
    public void grow(final int chance) {
        if (TileGemCrystals.rand.nextInt(Math.max(chance, 1)) == 0) {
            this.setGrowth(this.getGrowth().grow(this.func_145831_w()));
        }
    }
    
    public BlockGemCrystalCluster.GrowthStageType getGrowth() {
        final BlockState current = this.func_145831_w().getBlockState(this.func_174877_v());
        return (BlockGemCrystalCluster.GrowthStageType)current.getValue((Property)BlockGemCrystalCluster.STAGE);
    }
    
    public void setGrowth(final BlockGemCrystalCluster.GrowthStageType stage) {
        final BlockState next = (BlockState)BlocksAS.GEM_CRYSTAL_CLUSTER.defaultBlockState().func_206870_a((Property)BlockGemCrystalCluster.STAGE, (Comparable)stage);
        this.func_145831_w().func_175656_a(this.func_174877_v(), next);
    }
}
