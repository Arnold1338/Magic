package hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.Random;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;

public interface IFocusEffect
{
    @Nonnull
    default Color getFocusColor(final IConstellation cst, final Random rand) {
        Color c = Color.WHITE;
        if (cst != null && rand.nextInt(4) == 0) {
            if (rand.nextInt(3) == 0) {
                c = cst.getConstellationColor().brighter();
            }
            else {
                c = cst.getConstellationColor();
            }
        }
        return c;
    }
}
