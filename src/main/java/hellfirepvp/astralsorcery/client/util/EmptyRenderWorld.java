package hellfirepvp.astralsorcery.client.util;

import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LightType;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.core.BlockPos;
import hellfirepvp.observerlib.api.client.StructureRenderLightManager;
import net.minecraft.world.level.lighting.WorldLightManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.core.Direction;
import java.util.function.Supplier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.IBlockDisplayReader;

public class EmptyRenderWorld implements IBlockDisplayReader
{
    private final Biome biome;
    
    public EmptyRenderWorld(final Supplier<Biome> biomeSupplier) {
        this.biome = biomeSupplier.get();
    }
    
    @OnlyIn(Dist.CLIENT)
    public float func_230487_a_(final Direction direction, final boolean b) {
        return 1.0f;
    }
    
    public WorldLightManager func_225524_e_() {
        return (WorldLightManager)new StructureRenderLightManager(this.func_201572_C());
    }
    
    public int func_225525_a_(final BlockPos blockPosIn, final ColorResolver colorResolverIn) {
        return colorResolverIn.getColor(this.biome, (double)blockPosIn.getX(), (double)blockPosIn.getZ());
    }
    
    public int func_226658_a_(final LightType lightType, final BlockPos blockPos) {
        return this.func_201572_C();
    }
    
    @Nullable
    public BlockEntity func_175625_s(final BlockPos blockPos) {
        return null;
    }
    
    public BlockState func_180495_p(final BlockPos blockPos) {
        return Blocks.field_150350_a.defaultBlockState();
    }
    
    public FluidState func_204610_c(final BlockPos blockPos) {
        return Fluids.field_204541_a.func_207188_f();
    }
}
