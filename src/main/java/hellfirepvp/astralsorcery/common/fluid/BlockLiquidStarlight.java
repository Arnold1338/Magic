package hellfirepvp.astralsorcery.common.fluid;

import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.Random;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.ForgeEventFactory;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.world.level.block.Blocks;
import hellfirepvp.astralsorcery.common.data.config.entry.CraftingConfig;
import net.minecraft.world.level.IBlockDisplayReader;
import net.minecraft.world.level.material.EmptyFluid;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelReader;
import hellfirepvp.astralsorcery.common.crafting.nojson.LiquidStarlightCraftingRegistry;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.effect.MobEffectInstance;
import net.minecraft.world.level.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.Property;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.FlowingFluid;
import java.util.function.Supplier;
import net.minecraft.world.level.block.FlowingFluidBlock;

public class BlockLiquidStarlight extends FlowingFluidBlock
{
    public BlockLiquidStarlight(final Supplier<? extends FlowingFluid> fluidSupplier) {
        super((Supplier)fluidSupplier, AbstractBlock.Properties.func_200945_a(Material.field_151586_h).func_200942_a().func_235838_a_(state -> 15).func_200943_b(100.0f).func_222380_e());
    }
    
    public void func_196262_a(final BlockState state, final Level world, final BlockPos pos, final Entity entity) {
        super.func_196262_a(state, world, pos, entity);
        if ((int)state.getValue((Property)BlockLiquidStarlight.field_176367_b) != 0) {
            return;
        }
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).func_195064_c(new MobEffectInstance(Effects.field_76439_r, 300, 0, true, true));
        }
        else if (entity instanceof ItemEntity) {
            LiquidStarlightCraftingRegistry.tryCraft((ItemEntity)entity, pos);
            if (!world.level().isClientSide() && ((ItemEntity)entity).func_92059_d().isEmpty()) {
                entity.func_70106_y();
            }
        }
    }
    
    public void func_220082_b(final BlockState state, final Level worldIn, final BlockPos pos, final BlockState oldState, final boolean isMoving) {
        if (this.reactWithNeighbors(worldIn, pos, state)) {
            worldIn.func_205219_F_().func_205360_a(pos, (Object)state.getFluidState().func_206886_c(), this.getFluid().func_205569_a((IWorldReader)worldIn));
        }
    }
    
    public void func_220069_a(final BlockState state, final Level worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos, final boolean isMoving) {
        if (this.reactWithNeighbors(worldIn, pos, state)) {
            worldIn.func_205219_F_().func_205360_a(pos, (Object)state.getFluidState().func_206886_c(), this.getFluid().func_205569_a((IWorldReader)worldIn));
        }
    }
    
    private boolean reactWithNeighbors(final Level world, final BlockPos pos, final BlockState state) {
        for (final Direction dir : Direction.values()) {
            final FluidState otherState = world.func_204610_c(pos.func_177972_a(dir));
            Fluid otherFluid = otherState.func_206886_c();
            if (otherFluid instanceof FlowingFluid) {
                otherFluid = ((FlowingFluid)otherFluid).func_210198_f();
            }
            if (!(otherFluid instanceof EmptyFluid)) {
                if (!otherFluid.equals(this.getFluid())) {
                    final boolean isHot = otherFluid.getAttributes().getTemperature((IBlockDisplayReader)world, pos.func_177972_a(dir)) > 600;
                    BlockState generate;
                    if (isHot) {
                        if (CraftingConfig.CONFIG.liquidStarlightInteractionSand.get()) {
                            generate = Blocks.field_150354_m.defaultBlockState();
                            if ((boolean)CraftingConfig.CONFIG.liquidStarlightInteractionAquamarine.get() && world.field_73012_v.nextInt(800) == 0) {
                                generate = BlocksAS.AQUAMARINE_SAND_ORE.defaultBlockState();
                            }
                        }
                        else {
                            generate = Blocks.field_150347_e.defaultBlockState();
                        }
                    }
                    else if (CraftingConfig.CONFIG.liquidStarlightInteractionIce.get()) {
                        generate = Blocks.field_150403_cj.defaultBlockState();
                    }
                    else {
                        generate = Blocks.field_150347_e.defaultBlockState();
                    }
                    world.func_175656_a(pos, ForgeEventFactory.fireFluidPlaceBlockEvent((IWorld)world, pos, pos, generate));
                }
            }
        }
        return true;
    }
    
    @OnlyIn(Dist.CLIENT)
    public void func_180655_c(final BlockState state, final Level world, final BlockPos pos, final Random rand) {
        final Integer level = (Integer)state.getValue((Property)BlockLiquidStarlight.field_176367_b);
        final double percHeight = 1.0 - (level + 1.0) / 8.0;
        playLiquidStarlightBlockEffect(rand, new Vector3((Vec3i)pos).addY(percHeight * rand.nextFloat()), 1.0f);
        playLiquidStarlightBlockEffect(rand, new Vector3((Vec3i)pos).addY(percHeight * rand.nextFloat()), 1.0f);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playLiquidStarlightBlockEffect(final Random rand, final Vector3 at, final float blockSize) {
        if (rand.nextInt(3) == 0) {
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at.clone().add(0.5 + rand.nextFloat() * (blockSize / 2.0f) * (rand.nextBoolean() ? 1 : -1), 0.0, 0.5 + rand.nextFloat() * (blockSize / 2.0f) * (rand.nextBoolean() ? 1 : -1))).setScaleMultiplier(0.1f + rand.nextFloat() * 0.06f).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.constant(ColorsAS.ROCK_CRYSTAL));
        }
    }
}
