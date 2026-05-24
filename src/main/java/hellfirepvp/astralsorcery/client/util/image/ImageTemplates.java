package hellfirepvp.astralsorcery.client.util.image;

import org.apache.logging.log4j.util.TriConsumer;

public class ImageTemplates
{
    public static final int TR_0 = -1;
    public static final int TR_1 = -1056964609;
    public static final int TR_2 = -2130706433;
    public static final int TR_3 = 1090519039;
    
    public static ImageTemplate getLargeStar() {
        return new ImageTemplate.Quad(7, 7) {
            @Override
            public void place(final TriConsumer<Integer, Integer, Integer> setColor) {
                setColor.accept((Object)3, (Object)0, (Object)1090519039);
                setColor.accept((Object)0, (Object)3, (Object)1090519039);
                setColor.accept((Object)6, (Object)3, (Object)1090519039);
                setColor.accept((Object)3, (Object)6, (Object)1090519039);
                setColor.accept((Object)2, (Object)2, (Object)1090519039);
                setColor.accept((Object)4, (Object)2, (Object)1090519039);
                setColor.accept((Object)2, (Object)4, (Object)1090519039);
                setColor.accept((Object)4, (Object)4, (Object)1090519039);
                setColor.accept((Object)3, (Object)1, (Object)(-2130706433));
                setColor.accept((Object)1, (Object)3, (Object)(-2130706433));
                setColor.accept((Object)3, (Object)5, (Object)(-2130706433));
                setColor.accept((Object)5, (Object)3, (Object)(-2130706433));
                setColor.accept((Object)3, (Object)2, (Object)(-1056964609));
                setColor.accept((Object)2, (Object)3, (Object)(-1056964609));
                setColor.accept((Object)3, (Object)4, (Object)(-1056964609));
                setColor.accept((Object)4, (Object)3, (Object)(-1056964609));
                setColor.accept((Object)3, (Object)3, (Object)(-1));
            }
        };
    }
    
    public static ImageTemplate getSmallStar() {
        return new ImageTemplate.Quad(3, 3) {
            @Override
            public void place(final TriConsumer<Integer, Integer, Integer> setColor) {
                setColor.accept((Object)1, (Object)0, (Object)1090519039);
                setColor.accept((Object)0, (Object)1, (Object)1090519039);
                setColor.accept((Object)1, (Object)2, (Object)1090519039);
                setColor.accept((Object)2, (Object)1, (Object)1090519039);
                setColor.accept((Object)1, (Object)1, (Object)(-1056964609));
            }
        };
    }
    
    public static ImageTemplate getDot(final int color) {
        return new ImageTemplate.Quad(1, 1) {
            @Override
            public void place(final TriConsumer<Integer, Integer, Integer> setColor) {
                setColor.accept((Object)0, (Object)0, (Object)color);
            }
        };
    }
}
