package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.level.phys.shapes.Shapes;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.StateContainer;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.phys.HitResult;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.ColorUtils;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.Property;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesMisc;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.EnumProperty;
import net.minecraft.world.level.block.Block;

public class BlockFlareLight extends Block
{
    public static final EnumProperty<DyeColor> COLOR;
    private static final VoxelShape SHAPE;
    
    public BlockFlareLight() {
        super(PropertiesMisc.defaultAir().func_235838_a_(state -> 15));
        this.func_180632_j((BlockState)((BlockState)this.func_176194_O().func_177621_b()).func_206870_a((Property)BlockFlareLight.COLOR, (Comparable)DyeColor.YELLOW));
    }
    
    @OnlyIn(Dist.CLIENT)
    public void func_180655_c(final BlockState state, final World world, final BlockPos pos, final Random rand) {
        final Color c = ColorUtils.flareColorFromDye((DyeColor)state.getValue((Property)BlockFlareLight.COLOR));
        for (int i = 0; i < 2; ++i) {
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3((Vector3i)pos).add(0.5, 0.2, 0.5).add(rand.nextFloat() * 0.1 * (rand.nextBoolean() ? 1 : -1), rand.nextFloat() * 0.1 * (rand.nextBoolean() ? 1 : -1), rand.nextFloat() * 0.1 * (rand.nextBoolean() ? 1 : -1))).setScaleMultiplier(0.4f + rand.nextFloat() * 0.1f).setAlphaMultiplier(0.35f).setMotion(new Vector3(0.0f, rand.nextFloat() * 0.01f, 0.0f)).color(VFXColorFunction.constant(c)).setMaxAge(50 + rand.nextInt(20));
        }
        if (rand.nextBoolean()) {
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3((Vector3i)pos).add(0.5, 0.3, 0.5).add(rand.nextFloat() * 0.02 * (rand.nextBoolean() ? 1 : -1), rand.nextFloat() * 0.1 * (rand.nextBoolean() ? 1 : -1), rand.nextFloat() * 0.02 * (rand.nextBoolean() ? 1 : -1))).setScaleMultiplier(0.15f + rand.nextFloat() * 0.1f).setMotion(new Vector3(0.0f, rand.nextFloat() * 0.01f, 0.0f)).color(VFXColorFunction.WHITE).setMaxAge(25 + rand.nextInt(10));
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean addDestroyEffects(final BlockState state, final World world, final BlockPos pos, final ParticleEngine manager) {
        return true;
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean addHitEffects(final BlockState state, final World worldObj, final HitResult target, final ParticleEngine manager) {
        return true;
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean addRunningEffects(final BlockState state, final World world, final BlockPos pos, final Entity entity) {
        return true;
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean addLandingEffects(final BlockState state1, final ServerLevel worldserver, final BlockPos pos, final BlockState state2, final LivingEntity entity, final int numberOfParticles) {
        return true;
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader world, final BlockPos pos, final CollisionContext ctx) {
        return BlockFlareLight.SHAPE;
    }
    
    public boolean isAir(final BlockState state, final IBlockReader world, final BlockPos pos) {
        return false;
    }
    
    public boolean canBeReplacedByLogs(final BlockState state, final IWorldReader world, final BlockPos pos) {
        return true;
    }
    
    public boolean canBeReplacedByLeaves(final BlockState state, final IWorldReader world, final BlockPos pos) {
        return true;
    }
    
    protected void func_206840_a(final StateContainer.Builder<Block, BlockState> ct) {
        ct.func_206894_a(new Property[] { (Property)BlockFlareLight.COLOR });
    }
    
    public RenderShape func_149645_b(final BlockState state) {
        return RenderShape.INVISIBLE;
    }
    
    static {
        COLOR = EnumProperty.func_177709_a("color", (Class)DyeColor.class);
        SHAPE = VoxelShapes.func_197873_a(0.375, 0.1875, 0.375, 0.625, 0.4375, 0.625);
    }
}
