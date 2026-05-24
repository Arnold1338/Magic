package hellfirepvp.astralsorcery.common.util.reflection;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.lang.reflect.Constructor;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import java.lang.reflect.Field;
import net.minecraft.world.level.entity.item.ItemEntity;
import java.util.function.BiConsumer;

public class ReflectionHelper
{
    private static BiConsumer<ItemEntity, Boolean> itemEntitySkipPhysicRenderer;
    
    public static void setSkipItemPhysicsRender(final ItemEntity entity) {
        if (ReflectionHelper.itemEntitySkipPhysicRenderer == null) {
            ReflectionHelper.itemEntitySkipPhysicRenderer = getFieldSetter(ItemEntity.class, "skipPhysicRenderer", Field::setBoolean);
        }
        ReflectionHelper.itemEntitySkipPhysicRenderer.accept(entity, true);
    }
    
    private static <T, V> BiConsumer<T, V> getFieldSetter(final Class<T> owningClass, final String fieldName, final FieldSetter<T, V> fieldSetter) {
        final Field field = findField(owningClass, fieldName);
        if (field == null) {
            return (object, value) -> {};
        }
        return (object, value) -> {
            try {
                fieldSetter.setFieldValue(field, object, value);
            }
            catch (final Exception ex) {}
        };
    }
    
    @Nullable
    private static <T> Field findField(final Class<T> owningClass, final String fieldName) {
        try {
            return ObfuscationReflectionHelper.findField((Class)owningClass, fieldName);
        }
        catch (final Exception ignored) {
            return null;
        }
    }
    
    private static Function<Object[], Object> resolveConstructor(final Class<?> owningClass, final Class<?>... parameters) {
        return (Function<Object[], Object>)(invokeParams -> {
            try {
                final Constructor<?> ctor = owningClass.getDeclaredConstructor((Class<?>[])parameters);
                ctor.setAccessible(true);
                return ctor.newInstance(invokeParams);
            }
            catch (final Exception e) {
                throw new ReflectionException("Failed to resolve/call Constructor!", e);
            }
        });
    }
    
    private static BiFunction<Object, Object[], Object> resolveMethod(final Class<?> owningClass, final String methodName, final Class<?>... parameters) {
        return (BiFunction<Object, Object[], Object>)((owningObject, invokeParams) -> {
            try {
                final Method m = owningClass.getDeclaredMethod(methodName, (Class[])parameters);
                m.setAccessible(true);
                return m.invoke(owningObject, invokeParams);
            }
            catch (final Exception e) {
                throw new ReflectionException("Failed to resolve/call Method!", e);
            }
        });
    }
    
    private interface FieldSetter<T, V>
    {
        void setFieldValue(final Field p0, final T p1, final V p2) throws IllegalAccessException;
    }
}
