package hellfirepvp.astralsorcery.client.util.word;

import com.google.common.collect.Maps;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import java.util.Map;

public abstract class RandomWordGenerator
{
    public static Map<String, RandomWordGenerator> localizedProviders;
    private static RandomWordGenerator fallback;
    
    @Nonnull
    public static RandomWordGenerator getGenerator() {
        String lang = Minecraft.func_71410_x().field_71474_y.field_74363_ab;
        if (lang == null) {
            return RandomWordGenerator.fallback;
        }
        lang = lang.toLowerCase();
        RandomWordGenerator gen;
        if ((gen = RandomWordGenerator.localizedProviders.get(lang)) == null) {
            gen = RandomWordGenerator.fallback;
        }
        return gen;
    }
    
    public abstract String generateWord(final long p0, final int p1);
    
    public static void init() {
        RandomWordGenerator.fallback = new WordGeneratorEnglish();
        RandomWordGenerator.localizedProviders.put("en_us", RandomWordGenerator.fallback);
        RandomWordGenerator.localizedProviders.put("zh_cn", new WordGeneratorChinese());
    }
    
    static {
        RandomWordGenerator.localizedProviders = Maps.newHashMap();
    }
}
