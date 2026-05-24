package hellfirepvp.astralsorcery.common.perk;

import net.minecraft.util.Mth;
import java.util.HashMap;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.tuple.Pair;
import java.util.function.Supplier;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import java.util.Map;

public class PerkAttributeLimiter
{
    private static final LimitRange ANY;
    private static final Map<PerkAttributeType, LimitRange> LIMITS;
    
    private PerkAttributeLimiter() {
    }
    
    public static void attachListeners(final IEventBus bus) {
        bus.addListener(EventPriority.HIGH, (Consumer)PerkAttributeLimiter::onModded);
        bus.addListener(EventPriority.HIGH, (Consumer)PerkAttributeLimiter::onVanilla);
    }
    
    public static void limit(final PerkAttributeType type, final Supplier<Double> min, final Supplier<Double> max) {
        PerkAttributeLimiter.LIMITS.put(type, new LimitRange((Supplier)min, (Supplier)max));
    }
    
    public static boolean hasLimit(final PerkAttributeType type) {
        return PerkAttributeLimiter.LIMITS.containsKey(type);
    }
    
    @Nonnull
    public static Pair<Double, Double> getLimit(final PerkAttributeType type) {
        return PerkAttributeLimiter.LIMITS.getOrDefault(type, PerkAttributeLimiter.ANY).asPair();
    }
    
    private static void onModded(final AttributeEvent.PostProcessModded event) {
        if (hasLimit(event.getType())) {
            event.setValue(PerkAttributeLimiter.LIMITS.getOrDefault(event.getType(), PerkAttributeLimiter.ANY).limit(event.getValue()));
        }
    }
    
    private static void onVanilla(final AttributeEvent.PostProcessVanilla event) {
        final PerkAttributeType type = event.resolveAttributeType();
        if (type != null) {
            event.setValue(PerkAttributeLimiter.LIMITS.getOrDefault(type, PerkAttributeLimiter.ANY).limit(event.getValue()));
        }
    }
    
    static {
        ANY = new AcceptAll();
        LIMITS = new HashMap<PerkAttributeType, LimitRange>();
    }
    
    private static class LimitRange
    {
        private final Supplier<Double> min;
        private final Supplier<Double> max;
        
        private LimitRange(final Supplier<Double> min, final Supplier<Double> max) {
            this.min = min;
            this.max = max;
        }
        
        protected double limit(final double value) {
            return Mth.func_151237_a(value, (double)this.min.get(), (double)this.max.get());
        }
        
        private Pair<Double, Double> asPair() {
            return (Pair<Double, Double>)Pair.of((Object)this.min.get(), (Object)this.max.get());
        }
    }
    
    private static class AcceptAll extends LimitRange
    {
        private AcceptAll() {
            super(() -> Double.MIN_VALUE, () -> Double.MAX_VALUE);
        }
        
        @Override
        protected double limit(final double value) {
            return value;
        }
    }
}
