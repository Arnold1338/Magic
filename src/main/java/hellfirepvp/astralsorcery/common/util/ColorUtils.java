package hellfirepvp.astralsorcery.common.util;

import net.minecraft.ChatFormatting;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.IBlockDisplayReader;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.util.Mth;
import java.awt.Color;

public class ColorUtils
{
    public static Color blendColors(final Color color1, final Color color2, final float color1Ratio) {
        return new Color(blendColors(color1.getRGB(), color2.getRGB(), color1Ratio), true);
    }
    
    public static int blendColors(final int color1, final int color2, final float color1Ratio) {
        final float ratio1 = Mth.func_76131_a(color1Ratio, 0.0f, 1.0f);
        final float ratio2 = 1.0f - ratio1;
        final int a1 = (color1 & 0xFF000000) >> 24;
        final int r1 = (color1 & 0xFF0000) >> 16;
        final int g1 = (color1 & 0xFF00) >> 8;
        final int b1 = color1 & 0xFF;
        final int a2 = (color2 & 0xFF000000) >> 24;
        final int r2 = (color2 & 0xFF0000) >> 16;
        final int g2 = (color2 & 0xFF00) >> 8;
        final int b2 = color2 & 0xFF;
        final int a3 = Mth.func_76125_a(Math.round(a1 * ratio1 + a2 * ratio2), 0, 255);
        final int r3 = Mth.func_76125_a(Math.round(r1 * ratio1 + r2 * ratio2), 0, 255);
        final int g3 = Mth.func_76125_a(Math.round(g1 * ratio1 + g2 * ratio2), 0, 255);
        final int b3 = Mth.func_76125_a(Math.round(b1 * ratio1 + b2 * ratio2), 0, 255);
        return a3 << 24 | r3 << 16 | g3 << 8 | b3;
    }
    
    public static Color overlayColor(final Color base, final Color overlay) {
        return new Color(overlayColor(base.getRGB(), overlay.getRGB()), true);
    }
    
    public static int overlayColor(final int base, final int overlay) {
        final int alpha = (base & 0xFF000000) >> 24;
        final int baseR = (base & 0xFF0000) >> 16;
        final int baseG = (base & 0xFF00) >> 8;
        final int baseB = base & 0xFF;
        final int overlayR = (overlay & 0xFF0000) >> 16;
        final int overlayG = (overlay & 0xFF00) >> 8;
        final int overlayB = overlay & 0xFF;
        final int r = Math.round(baseR * (overlayR / 255.0f)) & 0xFF;
        final int g = Math.round(baseG * (overlayG / 255.0f)) & 0xFF;
        final int b = Math.round(baseB * (overlayB / 255.0f)) & 0xFF;
        return alpha << 24 | r << 16 | g << 8 | b;
    }
    
    public static int getOverlayColor(final FluidStack stack) {
        if (stack.isEmpty()) {
            return -1;
        }
        return stack.getFluid().getAttributes().getColor(stack);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static int getOverlayColor(final ItemStack stack) {
        if (stack.isEmpty()) {
            return -1;
        }
        if (!(stack.getItem() instanceof BlockItem)) {
            return Minecraft.getInstance().getItemColors().func_186728_a(stack, 0);
        }
        final BlockState state = ItemUtils.createBlockState(stack);
        if (state == null) {
            return -1;
        }
        return Minecraft.getInstance().func_184125_al().func_228054_a_(state, (IBlockDisplayReader)null, (BlockPos)null, 0);
    }
    
    @Nonnull
    public static MutableComponent getTranslation(final DyeColor color) {
        return (MutableComponent)new Component(String.format("color.minecraft.%s", color.func_176762_d()));
    }
    
    @Nonnull
    public static Color flareColorFromDye(final DyeColor color) {
        return ColorsAS.DYE_COLOR_PARTICLES[color.func_196059_a()];
    }
    
    @Nonnull
    public static ChatFormatting textFormattingForDye(final DyeColor color) {
        switch (color) {
            case WHITE: {
                return ChatFormatting.WHITE;
            }
            case ORANGE: {
                return ChatFormatting.GOLD;
            }
            case MAGENTA: {
                return ChatFormatting.DARK_PURPLE;
            }
            case LIGHT_BLUE: {
                return ChatFormatting.DARK_AQUA;
            }
            case YELLOW: {
                return ChatFormatting.YELLOW;
            }
            case LIME: {
                return ChatFormatting.GREEN;
            }
            case PINK: {
                return ChatFormatting.LIGHT_PURPLE;
            }
            case GRAY: {
                return ChatFormatting.DARK_GRAY;
            }
            case LIGHT_GRAY: {
                return ChatFormatting.GRAY;
            }
            case CYAN: {
                return ChatFormatting.BLUE;
            }
            case PURPLE: {
                return ChatFormatting.DARK_PURPLE;
            }
            case BLUE: {
                return ChatFormatting.DARK_BLUE;
            }
            case BROWN: {
                return ChatFormatting.GOLD;
            }
            case GREEN: {
                return ChatFormatting.DARK_GREEN;
            }
            case RED: {
                return ChatFormatting.DARK_RED;
            }
            case BLACK: {
                return ChatFormatting.DARK_GRAY;
            }
            default: {
                return ChatFormatting.WHITE;
            }
        }
    }
}
