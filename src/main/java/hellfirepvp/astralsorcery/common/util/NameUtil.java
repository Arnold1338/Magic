package hellfirepvp.astralsorcery.common.util;

import hellfirepvp.astralsorcery.AstralSorcery;
import com.google.common.base.CaseFormat;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

public class NameUtil
{
    public static ResourceLocation prefixPath(final ResourceLocation key, final String prefix) {
        return new ResourceLocation(key.func_110624_b(), prefix + key.addTransientModifier());
    }
    
    public static ResourceLocation suffixPath(final ResourceLocation key, final String suffix) {
        return new ResourceLocation(key.func_110624_b(), key.addTransientModifier() + suffix);
    }
    
    public static ResourceLocation fromClass(final Object object) {
        return fromClass(object, null);
    }
    
    public static ResourceLocation fromClass(final Class<?> clazz) {
        return fromClass(clazz, null);
    }
    
    public static ResourceLocation fromClass(final Object object, @Nullable final String cutPrefix) {
        return fromClass(object, cutPrefix, null);
    }
    
    public static ResourceLocation fromClass(final Class<?> clazz, @Nullable final String cutPrefix) {
        return fromClass(clazz, cutPrefix, null);
    }
    
    public static ResourceLocation fromClass(final Object object, @Nullable final String cutPrefix, @Nullable final String cutSuffix) {
        return fromClass(object.getClass(), cutPrefix, cutSuffix);
    }
    
    public static ResourceLocation fromClass(final Class<?> clazz, @Nullable final String cutPrefix, @Nullable final String cutSuffix) {
        String name = clazz.getSimpleName();
        if (clazz.getEnclosingClass() != null) {
            name = clazz.getEnclosingClass().getSimpleName() + name;
        }
        if (cutPrefix != null && name.startsWith(cutPrefix)) {
            name = name.substring(cutPrefix.length());
        }
        if (cutSuffix != null && name.endsWith(cutSuffix)) {
            name = name.substring(0, name.length() - cutSuffix.length());
        }
        name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
        return AstralSorcery.key(name);
    }
}
