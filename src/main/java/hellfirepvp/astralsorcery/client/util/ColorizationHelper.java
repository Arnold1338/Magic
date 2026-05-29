package hellfirepvp.astralsorcery.client.util;

import java.util.HashMap;
import net.minecraftforge.resource.VanillaResourceType;
import net.minecraftforge.resource.SelectiveReloadStateHandler;
import net.minecraft.util.Unit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.util.profiling.IProfiler;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.client.util.color.ColorThief;
import net.minecraftforge.fluids.FluidStack;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import hellfirepvp.astralsorcery.common.util.ColorUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import java.awt.Color;
import java.util.Optional;
import net.minecraft.world.item.Item;
import java.util.Map;

public class ColorizationHelper
{
    private static final Map<Item, Optional<Color>> itemColors;
    private static final Map<Fluid, Optional<Color>> fluidColors;
    
    private ColorizationHelper() {
    }
    
    @Nonnull
    public static Optional<Color> getColor(final ItemStack stack) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }
        final Item i = stack.getItem();
        if (!ColorizationHelper.itemColors.containsKey(i)) {
            final TextureAtlasSprite tas = RenderingUtils.getParticleTexture(stack);
            if (tas != null) {
                ColorizationHelper.itemColors.put(i, getDominantColor(tas));
            }
            else {
                ColorizationHelper.itemColors.put(i, Optional.empty());
            }
        }
        return ColorizationHelper.itemColors.get(i).map(c -> ColorUtils.overlayColor(c, new Color(ColorUtils.getOverlayColor(stack))));
    }
    
    @Nonnull
    public static Optional<Color> getColor(final FluidStack stack) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }
        final Fluid fluid = stack.getFluid();
        if (!ColorizationHelper.fluidColors.containsKey(fluid)) {
            final TextureAtlasSprite tas = RenderingUtils.getParticleTexture(stack);
            if (tas != null) {
                ColorizationHelper.fluidColors.put(fluid, getDominantColor(tas));
            }
            else {
                ColorizationHelper.fluidColors.put(fluid, Optional.empty());
            }
        }
        return ColorizationHelper.fluidColors.get(fluid).map(c -> ColorUtils.overlayColor(c, new Color(ColorUtils.getOverlayColor(stack))));
    }
    
    private static Optional<Color> getDominantColor(final TextureAtlasSprite tas) {
        if (tas == null) {
            return Optional.empty();
        }
        try {
            final BufferedImage extractedImage = extractImage(tas);
            final int[] dominantColor = ColorThief.getColor(extractedImage);
            final int color = (dominantColor[0] & 0xFF) << 16 | (dominantColor[1] & 0xFF) << 8 | (dominantColor[2] & 0xFF);
            return Optional.of(new Color(color));
        }
        catch (final Exception exc) {
            AstralSorcery.log.error("Item Colorization Helper: Ignoring non-resolvable image " + tas.func_195668_m().toString());
            exc.printStackTrace();
            return Optional.empty();
        }
    }
    
    @Nullable
    private static BufferedImage extractImage(final TextureAtlasSprite tas) {
        final int w = tas.func_94211_a();
        final int h = tas.func_94216_b();
        final int count = tas.func_110970_k();
        if (w <= 0 || h <= 0 || count <= 0) {
            return null;
        }
        final BufferedImage bufferedImage = new BufferedImage(w, h * count, 6);
        for (int i = 0; i < count; ++i) {
            final int[] pxArray = new int[tas.func_94211_a() * tas.func_94216_b()];
            for (int xx = 0; xx < tas.func_94211_a(); ++xx) {
                for (int zz = 0; zz < tas.func_94216_b(); ++zz) {
                    final int argb = tas.getPixelRGBA(0, xx, zz + i * tas.func_94216_b());
                    pxArray[zz * tas.func_94211_a() + xx] = ((argb & 0xFF00FF00) | (argb & 0xFF0000) >> 16 | (argb & 0xFF) << 16);
                }
            }
            bufferedImage.setRGB(0, i * h, w, h, pxArray, 0, w);
        }
        return bufferedImage;
    }
    
    public static PreparableReloadListener onReload() {
        return (stage, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> stage.func_216872_a((Object)Unit.INSTANCE).thenRunAsync(() -> {
            if (!(!SelectiveReloadStateHandler.INSTANCE.get().test(VanillaResourceType.TEXTURES))) {
                ColorizationHelper.itemColors.clear();
                ColorizationHelper.fluidColors.clear();
            }
        });
    }
    
    static {
        itemColors = new HashMap<Item, Optional<Color>>();
        fluidColors = new HashMap<Fluid, Optional<Color>>();
    }
}
