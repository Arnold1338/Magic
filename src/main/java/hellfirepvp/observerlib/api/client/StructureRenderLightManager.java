package hellfirepvp.observerlib.api.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StructureRenderLightManager {
    public static int getPackedLightForRendering(BlockAndTintGetter reader, BlockPos pos) {
        int block = reader.getBrightness(LightLayer.BLOCK, pos);
        int sky   = reader.getBrightness(LightLayer.SKY, pos);
        return (sky << 20) | (block << 4);
    }

    public static int getWorldPackedLight(BlockPos pos) {
        if (Minecraft.getInstance().level == null) return 0xF000F0;
        return getPackedLightForRendering(Minecraft.getInstance().level, pos);
    }
}
