package hellfirepvp.astralsorcery.client.effect;

import java.util.Objects;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import javax.annotation.Nullable;
import java.util.function.Supplier;
import java.util.HashMap;
import java.util.Map;
import hellfirepvp.astralsorcery.client.effect.function.RefreshFunction;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.Random;

public abstract class EntityComplexFX
{
    protected static final Random rand;
    private static long counter;
    private final long id;
    protected int age;
    protected int maxAge;
    protected int ageRefreshCount;
    protected Vector3 pos;
    private RefreshFunction refreshFunction;
    private Map<String, Object> customData;
    protected boolean removeRequested;
    private boolean flagRemoved;
    
    protected EntityComplexFX(final Vector3 pos) {
        this.age = 0;
        this.maxAge = 40;
        this.ageRefreshCount = 0;
        this.refreshFunction = RefreshFunction.DESPAWN;
        this.customData = new HashMap<String, Object>();
        this.removeRequested = false;
        this.flagRemoved = true;
        this.id = EntityComplexFX.counter;
        ++EntityComplexFX.counter;
        this.pos = pos;
    }
    
    public final long getId() {
        return this.id;
    }
    
    public <T extends EntityComplexFX> T setMaxAge(final int maxAge) {
        this.maxAge = maxAge;
        return (T)this;
    }
    
    public int getMaxAge() {
        return this.maxAge;
    }
    
    public int getAge() {
        return this.age;
    }
    
    public <T extends EntityComplexFX> T move(final Vector3 change) {
        this.setPosition(this.getPosition().add(change));
        return (T)this;
    }
    
    public Vector3 getPosition() {
        return this.pos.clone();
    }
    
    public <T extends EntityComplexFX> T setPosition(final Vector3 pos) {
        this.pos = pos.clone();
        return (T)this;
    }
    
    public <T extends EntityComplexFX> T addPosition(final Vector3 offset) {
        this.pos.add(offset);
        return (T)this;
    }
    
    public <T extends EntityComplexFX> T refresh(final RefreshFunction<?> refreshFunction) {
        this.refreshFunction = refreshFunction;
        return (T)this;
    }
    
    public <T> T getOrCreateData(final String str, final Supplier<T> defaultProvider) {
        return (T)this.customData.computeIfAbsent(str, s -> defaultProvider.get());
    }
    
    @Nullable
    public <T> T getData(final String str) {
        return (T)this.customData.get(str);
    }
    
    public void tick() {
        ++this.age;
        if (this.canRemove() && this.refreshFunction.shouldRefresh(this) && RenderingUtils.canEffectExist(this)) {
            this.resetLifespan();
            ++this.ageRefreshCount;
        }
    }
    
    public void resetLifespan() {
        this.age = 0;
    }
    
    public int getAgeRefreshCount() {
        return this.ageRefreshCount;
    }
    
    public boolean canRemove() {
        return this.age >= this.maxAge || this.removeRequested;
    }
    
    public void requestRemoval() {
        this.removeRequested = true;
    }
    
    public boolean isRemoved() {
        return this.flagRemoved;
    }
    
    public void flagAsRemoved() {
        this.flagRemoved = true;
        this.removeRequested = false;
    }
    
    public void setActive() {
        this.flagRemoved = false;
        this.removeRequested = false;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final EntityComplexFX that = (EntityComplexFX)o;
        return this.id == that.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    
    static {
        rand = new Random();
        EntityComplexFX.counter = 0L;
    }
}
