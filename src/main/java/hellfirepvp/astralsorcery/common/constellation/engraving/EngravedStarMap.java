package hellfirepvp.astralsorcery.common.constellation.engraving;

import java.awt.Point;
import net.minecraft.nbt.Tag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.util.Tuple;
import java.util.LinkedList;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import javax.annotation.Nonnull;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.constellation.star.StarLocation;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.Set;
import java.util.Iterator;
import net.minecraft.util.Mth;
import java.util.HashSet;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import net.minecraft.world.level.level.Level;
import hellfirepvp.astralsorcery.common.constellation.DrawnConstellation;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import java.util.Random;

public class EngravedStarMap
{
    private static final Random rand;
    private final Map<ResourceLocation, Float> distributions;
    private final List<DrawnConstellation> drawInformation;
    
    private EngravedStarMap(final Map<ResourceLocation, Float> distributions, final List<DrawnConstellation> drawnConstellations) {
        this.distributions = distributions;
        this.drawInformation = drawnConstellations;
    }
    
    public static EngravedStarMap buildStarMap(final World world, final List<DrawnConstellation> constellations) {
        final float nightPerc = DayTimeHelper.getCurrentDaytimeDistribution(world);
        final Map<DrawnConstellation, List<Rectangle2D.Double>> cstCoordinates = new HashMap<DrawnConstellation, List<Rectangle2D.Double>>();
        for (final DrawnConstellation drawnCst : constellations) {
            cstCoordinates.put(drawnCst, createConstellationOffsets(drawnCst));
        }
        final Map<ResourceLocation, Float> distributionMap = new HashMap<ResourceLocation, Float>();
        for (final DrawnConstellation drawn : cstCoordinates.keySet()) {
            final List<Rectangle2D.Double> positions = cstCoordinates.get(drawn);
            final Set<Rectangle2D.Double> foundPositions = new HashSet<Rectangle2D.Double>();
            for (final DrawnConstellation otherCst : cstCoordinates.keySet()) {
                if (!drawn.equals(otherCst)) {
                    if (drawn.getConstellation().equals(otherCst.getConstellation())) {
                        continue;
                    }
                    final List<Rectangle2D.Double> otherPositions = cstCoordinates.get(otherCst);
                    for (final Rectangle2D.Double starPos : positions) {
                        for (final Rectangle2D.Double otherStarPos : otherPositions) {
                            if (starPos.intersects(otherStarPos)) {
                                foundPositions.add(starPos);
                            }
                        }
                    }
                }
            }
            final IConstellation drawnConstellation = drawn.getConstellation();
            final float percent = 0.1f + 0.9f * Mth.func_76131_a(foundPositions.size() * 1.5f / positions.size() * nightPerc, 0.0f, 1.0f);
            final float existingPercent = distributionMap.getOrDefault(drawnConstellation.getRegistryName(), 0.1f);
            if (percent >= existingPercent) {
                distributionMap.put(drawnConstellation.getRegistryName(), percent);
            }
        }
        return new EngravedStarMap(distributionMap, constellations);
    }
    
    private static List<Rectangle2D.Double> createConstellationOffsets(final DrawnConstellation cst) {
        final float width = 4.6875f;
        final List<Rectangle2D.Double> positions = new ArrayList<Rectangle2D.Double>();
        for (final StarLocation star : cst.getConstellation().getStars()) {
            final double starX = star.x * 1.875f + cst.getPoint().getX() - 15.0 - width / 2.0f;
            final double starY = star.y * 1.875f + cst.getPoint().getY() - 15.0 - width / 2.0f;
            positions.add(new Rectangle2D.Double(starX, starY, width, width));
        }
        return positions;
    }
    
    public boolean canAffect(@Nonnull final ItemStack stack) {
        for (final ResourceLocation key : this.getConstellationKeys()) {
            final IConstellation cst = ConstellationRegistry.getConstellation(key);
            if (cst != null) {
                final EngravingEffect effect = cst.getEngravingEffect();
                if (effect != null && !effect.getApplicableEffects(stack).isEmpty()) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    @Nonnull
    public ItemStack applyEffects(@Nonnull ItemStack stack) {
        final List<Tuple<EngravingEffect.ApplicableEffect, Float>> engravings = new LinkedList<Tuple<EngravingEffect.ApplicableEffect, Float>>();
        final List<IConstellation> constellations = new LinkedList<IConstellation>();
        this.getConstellationKeys().stream().map((Function<? super ResourceLocation, ?>)ConstellationRegistry::getConstellation).filter(Objects::nonNull).forEach(constellations::add);
        for (final IConstellation cst : constellations) {
            final EngravingEffect effect = cst.getEngravingEffect();
            if (effect != null) {
                final List<EngravingEffect.ApplicableEffect> applicable = effect.getApplicableEffects(stack);
                if (applicable.isEmpty()) {
                    continue;
                }
                final float distribution = this.getDistribution(cst);
                for (final EngravingEffect.ApplicableEffect applicableEffect : applicable) {
                    engravings.add((Tuple<EngravingEffect.ApplicableEffect, Float>)new Tuple((Object)applicableEffect, (Object)distribution));
                }
            }
        }
        Tuple<EngravingEffect.ApplicableEffect, Float> tpl = null;
        engravings.sort(Comparator.comparing(tpl -> {
            final EngravingEffect.ApplicableEffect effect3 = (EngravingEffect.ApplicableEffect)tpl.func_76341_a();
            if (effect3 instanceof EngravingEffect.EnchantmentEffect) {
                if (!((EngravingEffect.EnchantmentEffect)effect3).isIgnoreCompatibility()) {
                    return Integer.valueOf(0);
                }
                else {
                    return Integer.valueOf(1);
                }
            }
            else {
                return Integer.valueOf(2);
            }
        }));
        final Iterator<Tuple<EngravingEffect.ApplicableEffect, Float>> iterator3 = engravings.iterator();
        while (iterator3.hasNext()) {
            tpl = iterator3.next();
            final EngravingEffect.ApplicableEffect effect2 = (EngravingEffect.ApplicableEffect)tpl.func_76341_a();
            final float distribution2 = (float)tpl.func_76340_b();
            stack = effect2.apply(stack, distribution2, EngravedStarMap.rand);
        }
        return stack;
    }
    
    public Collection<DrawnConstellation> getDrawnConstellations() {
        return Collections.unmodifiableCollection((Collection<? extends DrawnConstellation>)this.drawInformation);
    }
    
    public Collection<ResourceLocation> getConstellationKeys() {
        return Collections.unmodifiableCollection((Collection<? extends ResourceLocation>)this.distributions.keySet());
    }
    
    public float getDistribution(final IConstellation cst) {
        return this.distributions.getOrDefault(cst.getRegistryName(), 0.0f);
    }
    
    public CompoundTag serialize() {
        final CompoundTag tag = new CompoundTag();
        final ListTag list = new ListTag();
        this.distributions.forEach((constellationKey, percent) -> {
            final CompoundTag cstTag = new CompoundTag();
            NBTHelper.setResourceLocation(cstTag, "cst", constellationKey);
            cstTag.func_74776_a("percent", (float)percent);
            list.add((Object)cstTag);
            return;
        });
        tag.put("distributions", (Tag)list);
        final ListTag listDrawn = new ListTag();
        this.drawInformation.forEach(drawCst -> {
            final CompoundTag cstTag2 = new CompoundTag();
            NBTHelper.setResourceLocation(cstTag2, "cst", drawCst.getConstellation().getRegistryName());
            cstTag2.putInt("x", drawCst.getPoint().x);
            cstTag2.putInt("y", drawCst.getPoint().y);
            listDrawn.add((Object)cstTag2);
            return;
        });
        tag.put("drawInformation", (Tag)listDrawn);
        return tag;
    }
    
    public static EngravedStarMap deserialize(final CompoundTag tag) {
        final Map<ResourceLocation, Float> distributionMap = new HashMap<ResourceLocation, Float>();
        final ListTag list = tag.getList("distributions", 10);
        for (int i = 0; i < list.size(); ++i) {
            final CompoundTag cstTag = list.getCompound(i);
            final ResourceLocation constellationKey = new ResourceLocation(cstTag.getString("cst"));
            final float percent = cstTag.getFloat("percent");
            if (percent > 0.0f) {
                distributionMap.put(constellationKey, percent);
            }
        }
        final List<DrawnConstellation> drawnConstellations = new ArrayList<DrawnConstellation>();
        final ListTag listDrawn = tag.getList("drawInformation", 10);
        for (int j = 0; j < listDrawn.size(); ++j) {
            final CompoundTag cstTag2 = listDrawn.getCompound(j);
            final IConstellation cst = ConstellationRegistry.getConstellation(new ResourceLocation(cstTag2.getString("cst")));
            final Point offset = new Point(cstTag2.getInt("x"), cstTag2.getInt("y"));
            drawnConstellations.add(new DrawnConstellation(offset, cst));
        }
        return new EngravedStarMap(distributionMap, drawnConstellations);
    }
    
    static {
        rand = new Random();
    }
}
