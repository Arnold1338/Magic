package hellfirepvp.astralsorcery.client.util.image;

import java.awt.Graphics2D;
import java.awt.Color;
import org.apache.logging.log4j.util.TriConsumer;
import java.util.Random;
import java.awt.image.BufferedImage;

public class SkyImageGenerator
{
    public static BufferedImage generateStarBackground() {
        final BufferedImage image = createBackground();
        placeRandomly(ImageTemplates.getLargeStar(), image, 200);
        placeRandomly(ImageTemplates.getSmallStar(), image, 500);
        placeRandomly(ImageTemplates.getDot(-2130706433), image, 300);
        placeRandomly(ImageTemplates.getDot(1090519039), image, 600);
        return image;
    }
    
    private static void placeRandomly(final ImageTemplate template, final BufferedImage out, final int count) {
        final Random rand = new Random();
        for (int i = 0; i < count; ++i) {
            final int offsetX = rand.nextInt(out.getWidth() - template.getWidth());
            final int offsetY = rand.nextInt(out.getHeight() - template.getHeight());
            template.place(createColorPlacer(offsetX, offsetY, rand, out));
        }
    }
    
    private static TriConsumer<Integer, Integer, Integer> createColorPlacer(final int offsetX, final int offsetY, final Random rand, final BufferedImage out) {
        return (TriConsumer<Integer, Integer, Integer>)((oX, oY, color) -> {
            final int x = oX + offsetX;
            final int y = oY + offsetY;
            final int newColor = blendAlphaAdditively(out.getRGB(x, y), color, 0.8f + rand.nextFloat() * 0.2f);
            out.setRGB(x, y, convertToABGR(newColor));
        });
    }
    
    private static int convertToABGR(final int color) {
        return (color & 0xFF00FF00) | (color >> 16 & 0xFF) | (color & 0xFF) << 16;
    }
    
    private static int blendAlphaAdditively(final int existing, final int toWrite, final float alphaMulOut) {
        final int existingAlpha = existing >> 24 & 0xFF;
        final int newAlpha = Math.round((toWrite >> 24) * alphaMulOut) & 0xFF;
        final float partNew = newAlpha / 255.0f;
        final float partOld = 1.0f - partNew;
        final int newColorAdd = Math.min(Math.round((existing >> 16 & 0xFF) * partOld + (toWrite >> 16 & 0xFF) * partNew), 255) << 16 | Math.min(Math.round((existing >> 8 & 0xFF) * partOld + (toWrite >> 8 & 0xFF) * partNew), 255) << 8 | Math.min(Math.round((existing & 0xFF) * partOld + (toWrite & 0xFF) * partNew), 255);
        return (newColorAdd & 0xFFFFFF) | (Math.min(existingAlpha + newAlpha, 255) & 0xFF) << 24;
    }
    
    private static BufferedImage createBackground() {
        final BufferedImage image = new BufferedImage(512, 512, 6);
        final Graphics2D draw = image.createGraphics();
        draw.setColor(Color.BLACK);
        draw.fillRect(0, 0, image.getWidth(), image.getHeight());
        return image;
    }
}
