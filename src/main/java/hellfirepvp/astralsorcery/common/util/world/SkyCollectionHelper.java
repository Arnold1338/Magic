package hellfirepvp.astralsorcery.common.util.world;

import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.ISeedReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Random;

public class SkyCollectionHelper
{
    private static final int accuracy = 32;
    private static final Random sharedRand;
    
    @OnlyIn(Dist.CLIENT)
    public static Optional<Float> getSkyNoiseDistributionClient(final RegistryKey<Level> dim, final BlockPos pos) {
        return WorldSeedCache.getSeedIfPresent(dim).map(seed -> getDistributionInternal(seed, pos));
    }
    
    public static float getSkyNoiseDistribution(final ISeedReader world, final BlockPos pos) {
        return getDistributionInternal(MiscUtils.getRandomWorldSeed(world), pos);
    }
    
    private static float getDistributionInternal(final long seed, final BlockPos pos) {
        final BlockPos lowerAnchorPoint = new BlockPos((int)Math.floor(pos.getX() / 32.0f) * 32, 0, (int)Math.floor(pos.getZ() / 32.0f) * 32);
        final float layer0 = getNoiseDistribution(seed, lowerAnchorPoint, lowerAnchorPoint.offset(32, 0, 0), lowerAnchorPoint.offset(0, 0, 32), lowerAnchorPoint.offset(32, 0, 32), pos);
        return layer0 * layer0;
    }
    
    private static float getNoiseDistribution(final long seed, final BlockPos lXlZ, final BlockPos hXlZ, final BlockPos lXhZ, final BlockPos hXhZ, final BlockPos exact) {
        final float nll = getNoise(seed, lXlZ.getX(), lXlZ.getZ());
        final float nhl = getNoise(seed, hXlZ.getX(), hXlZ.getZ());
        final float nlh = getNoise(seed, lXhZ.getX(), lXhZ.getZ());
        final float nhh = getNoise(seed, hXhZ.getX(), hXhZ.getZ());
        final float xPart = Math.abs((exact.getX() - lXlZ.getX()) / 32.0f);
        final float zPart = Math.abs((exact.getZ() - lXlZ.getZ()) / 32.0f);
        return cosInterpolate(cosInterpolate(nll, nhl, xPart), cosInterpolate(nlh, nhh, xPart), zPart);
    }
    
    private static float cosInterpolate(final float l, final float h, final float partial) {
        final float t2 = (1.0f - Mth.func_76134_b((float)(partial * 3.141592653589793))) / 2.0f;
        return l * (1.0f - t2) + h * t2;
    }
    
    private static float getNoise(final long seed, final int posX, final int posZ) {
        SkyCollectionHelper.sharedRand.setSeed(simple_hash(new int[] { (int)seed, (int)(seed >> 32), posX, posZ }, 4));
        SkyCollectionHelper.sharedRand.nextLong();
        return SkyCollectionHelper.sharedRand.nextFloat();
    }
    
    private static int simple_hash(final int[] is, final int count) {
        int hash = 80238287;
        for (int i = 0; i < count; ++i) {
            hash = (hash << 4 ^ hash >> 28 ^ is[i] * 5449 % 130651);
        }
        return hash % 75327403;
    }
    
    static {
        sharedRand = new Random();
    }
}
