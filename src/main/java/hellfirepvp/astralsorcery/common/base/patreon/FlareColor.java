package hellfirepvp.astralsorcery.common.base.patreon;

import java.util.Locale;
import hellfirepvp.astralsorcery.client.resource.AssetLoader;
import hellfirepvp.astralsorcery.client.resource.query.SpriteQuery;
import java.awt.Color;

public enum FlareColor
{
    BLUE(1407743, 12703999), 
    DARK_RED(16713529, 16733525), 
    DAWN(16732550, 15293511), 
    GOLD(16748822, 16773742), 
    GREEN(6029111, 6553507), 
    MAGENTA(16547836, 16762623), 
    RED(16715563, 16715609), 
    WHITE(12582911, 16777215), 
    YELLOW(16777045, 16631583), 
    ELDRITCH(6423168, 11412223), 
    DARK_GREEN(50689, 2293647), 
    FIRE(16728070, 16750848), 
    WATER(9035775, 5798621), 
    EARTH(13665853, 13546386), 
    AIR(16777169, 11721405), 
    STANDARD(10033337, 6184406);
    
    public final Color color1;
    public final Color color2;
    
    private FlareColor(final int c1, final int c2) {
        this.color1 = new Color(c1);
        this.color2 = new Color(c2);
    }
    
    public SpriteQuery getSpriteQuery() {
        return new SpriteQuery(AssetLoader.TextureLocation.EFFECT, 1, 48, new String[] { "patreonflares", this.name().toLowerCase(Locale.ROOT) });
    }
}
