package hellfirepvp.astralsorcery.common.constellation;

import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import java.awt.Color;
import java.util.function.Supplier;
import net.minecraft.world.item.crafting.Ingredient;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Iterator;
import java.awt.Point;
import java.util.List;
import hellfirepvp.astralsorcery.common.constellation.star.StarConnection;
import hellfirepvp.astralsorcery.common.constellation.star.StarLocation;
import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.client.util.word.RandomWordGenerator;
import java.util.Random;

public class ConstellationGenerator
{
    public static GeneratedConstellation generateRandom(final long seed) {
        final Random sRandom = new Random(seed);
        final int stars = 5 + ((sRandom.nextFloat() > 0.6f) ? 1 : 0);
        return generateRandom(seed, stars);
    }
    
    public static GeneratedConstellation generateRandom(final long seed, final int stars) {
        final Random sRandom = new Random(seed);
        final String name = RandomWordGenerator.getGenerator().generateWord(seed, (sRandom.nextFloat() > 0.6f) ? 7 : 6);
        final GeneratedConstellation cst = new GeneratedConstellation(name);
        final List<StarLocation> tmpStars = Lists.newArrayList();
        final List<StarConnection> tmpConnections = Lists.newArrayList();
        for (int i = 0; i < stars; ++i) {
            final Point newPoint = pickStarPoint(sRandom, tmpStars, 6.0f);
            tmpStars.add(new StarLocation(newPoint.x, newPoint.y));
        }
        final Iterator<StarLocation> it = tmpStars.iterator();
        while (it.hasNext()) {
            final StarLocation sl = it.next();
            final StarLocation other = findConnection(sRandom, sl, tmpStars, tmpConnections);
            if (other == null) {
                it.remove();
            }
            else {
                tmpConnections.add(new StarConnection(sl, other));
            }
        }
        tmpStars.forEach(s -> cst.addStar(s.x, s.y));
        tmpConnections.forEach(c -> {
            if (cst.getStars().contains(c.to) && cst.getStars().contains(c.from)) {
                cst.addConnection(c.from, c.to);
            }
            return;
        });
        return cst;
    }
    
    private static StarLocation findConnection(final Random rand, final StarLocation sl, final List<StarLocation> stars, final List<StarConnection> existingConnections) {
        final List<StarLocation> others = Lists.newArrayList((Iterable)stars);
        others.remove(sl);
        if (others.isEmpty()) {
            return null;
        }
        Collections.shuffle(others, rand);
    Label_0042:
        for (final StarLocation other : others) {
            final StarConnection conn = new StarConnection(sl, other);
            for (final StarConnection otherConnection : existingConnections) {
                if (intersect(conn, otherConnection)) {
                    continue Label_0042;
                }
            }
            return other;
        }
        return null;
    }
    
    private static boolean intersect(final StarConnection sc1, final StarConnection sc2) {
        return isIntersecting(sc1, sc2.from.asPoint()) || isIntersecting(sc1, sc2.to.asPoint());
    }
    
    private static boolean isIntersecting(final StarConnection part, final Point p) {
        final StarConnection originPart = new StarConnection(new StarLocation(0, 0), new StarLocation(part.to.x - part.from.x, part.to.y - part.from.y));
        final Point originOffset = new Point(p.x - part.from.x, p.y - part.from.y);
        return cross(originPart.to.asPoint(), originOffset) < 0;
    }
    
    private static int cross(final Point p1, final Point p2) {
        return p1.x * p2.y - p2.x * p1.y;
    }
    
    private static Point pickStarPoint(final Random rand, final List<StarLocation> occupied, final float minDst) {
        Point opt = null;
    Label_0000:
        while (true) {
            opt = new Point(rand.nextInt(25), rand.nextInt(25));
            opt.translate(3, 3);
            for (final StarLocation other : occupied) {
                if (opt.distance(other.asPoint()) < minDst) {
                    continue Label_0000;
                }
            }
            break;
        }
        return opt;
    }
    
    public static class GeneratedConstellation extends BaseConstellation
    {
        private final String localizedName;
        
        private GeneratedConstellation(final String localizedName) {
            this.localizedName = localizedName;
        }
        
        public int getSortingId() {
            return 0;
        }
        
        public String getSimpleName() {
            return this.localizedName;
        }
        
        public String getTranslationKey() {
            return this.getSimpleName();
        }
        
        public List<Ingredient> getConstellationSignatureItems() {
            return Collections.emptyList();
        }
        
        public IConstellation addSignatureItem(final Supplier<Ingredient> ingredient) {
            return this;
        }
        
        public Color getConstellationColor() {
            return ColorsAS.CONSTELLATION_TYPE_WEAK;
        }
        
        public boolean canDiscover(final Player player, final PlayerProgress progress) {
            return true;
        }
        
        public int compareTo(final IConstellation o) {
            return 0;
        }
    }
}
