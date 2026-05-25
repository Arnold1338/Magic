package hellfirepvp.astralsorcery.common.tile;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.world.level.block.state.Property;
import hellfirepvp.astralsorcery.common.block.tile.BlockCelestialCrystalCluster;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import hellfirepvp.astralsorcery.common.data.config.entry.CraftingConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.block.ore.BlockStarmetalOre;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeTile;
import hellfirepvp.astralsorcery.common.tile.base.TileEntityTick;

public class TileCelestialCrystals extends TileEntityTick implements CrystalAttributeTile
{
    public static final int TICK_GROWTH_CHANCE = 18000;
    private CrystalAttributes attributes;
    
    public TileCelestialCrystals() {
        super(TileEntityTypesAS.CELESTIAL_CRYSTAL_CLUSTER);
        this.attributes = null;
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (!this.getLevel().level()) {
            if (this.getGrowth() < 4 && this.doesSeeSky()) {
                this.tryGrowWithChance(18000);
            }
        }
        else {
            final BlockState downState = this.getLevel().getBlockState(this.getBlockState().renderItem());
            if (downState.getBlock() instanceof BlockStarmetalOre) {
                this.playStarmetalParticles();
            }
            if (this.getGrowth() == 4) {
                this.playFullyGrownParticles();
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playStarmetalParticles() {
        if (TileCelestialCrystals.rand.nextInt(9) == 0) {
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3(this).add(0.1, 0.0, 0.1).add(TileCelestialCrystals.rand.nextFloat() * 0.8, 0.0, TileCelestialCrystals.rand.nextFloat() * 0.8)).color(VFXColorFunction.constant(ColorsAS.DEFAULT_GENERIC_PARTICLE)).setMotion(new Vector3(0.0, 0.02 + TileCelestialCrystals.rand.nextFloat() * 0.05f, 0.0)).setScaleMultiplier(0.1f + TileCelestialCrystals.rand.nextFloat() * 0.15f);
        }
        if (TileCelestialCrystals.rand.nextInt(4) == 0) {
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3(this).addY(0.05).add(TileCelestialCrystals.rand.nextFloat(), 0.0f, TileCelestialCrystals.rand.nextFloat())).color(VFXColorFunction.constant(ColorsAS.DEFAULT_GENERIC_PARTICLE)).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.06f + TileCelestialCrystals.rand.nextFloat() * 0.05f);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playFullyGrownParticles() {
        if (TileCelestialCrystals.rand.nextInt(4) == 0) {
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3(this).add(TileCelestialCrystals.rand.nextFloat(), TileCelestialCrystals.rand.nextFloat(), TileCelestialCrystals.rand.nextFloat())).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.WHITE).setScaleMultiplier(0.1f + TileCelestialCrystals.rand.nextFloat() * 0.15f);
        }
    }
    
    public void tryGrowWithChance(int growthChance) {
        final BlockState downState = this.getLevel().getBlockState(this.getBlockState().renderItem());
        if (downState.getBlock() instanceof BlockStarmetalOre) {
            growthChance *= (int)0.6;
            if (TileCelestialCrystals.rand.nextInt(400) == 0) {
                this.getLevel().func_175656_a(this.getBlockState().renderItem(), CraftingConfig.CONFIG.getStarmetalRevertBlockState());
            }
        }
        final float distribution = DayTimeHelper.getCurrentDaytimeDistribution(this.getLevel());
        growthChance *= (int)(1.0f - 0.5f * distribution);
        this.grow(growthChance);
    }
    
    public void grow(final int chance) {
        if (TileCelestialCrystals.rand.nextInt(Math.max(chance, 1)) == 0) {
            final int stage = this.getGrowth();
            if (stage < 4) {
                this.setGrowth(stage + 1);
            }
        }
    }
    
    public int getGrowth() {
        final BlockState current = this.getLevel().getBlockState(this.getBlockState());
        return (int)current.getValue((Property)BlockCelestialCrystalCluster.STAGE);
    }
    
    public void setGrowth(final int stage) {
        final BlockState next = (BlockState)BlocksAS.CELESTIAL_CRYSTAL_CLUSTER.defaultBlockState().setValue((Property)BlockCelestialCrystalCluster.STAGE, (Comparable)stage);
        this.getLevel().func_175656_a(this.getBlockState(), next);
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        if (this.attributes != null) {
            this.attributes.store(compound);
        }
        else {
            CrystalAttributes.storeNull(compound);
        }
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.attributes = CrystalAttributes.getCrystalAttributes(compound);
    }
    
    @Nullable
    @Override
    public CrystalAttributes getAttributes() {
        return this.attributes;
    }
    
    @Override
    public void setAttributes(@Nullable final CrystalAttributes attributes) {
        this.attributes = attributes;
    }
}
