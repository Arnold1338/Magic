package hellfirepvp.astralsorcery.client.util.word;

import java.util.Random;

public class WordGeneratorChinese extends RandomWordGenerator
{
    private static String[] a;
    private static String[] b;
    private static String suffix;
    
    @Override
    public String generateWord(final long seed, final int length) {
        final Random r = new Random(seed);
        final StringBuilder word = new StringBuilder();
        word.append(WordGeneratorChinese.a[r.nextInt(WordGeneratorChinese.a.length)]);
        word.append(WordGeneratorChinese.b[r.nextInt(WordGeneratorChinese.b.length)]);
        word.append(WordGeneratorChinese.suffix);
        return word.toString();
    }
    
    static {
        WordGeneratorChinese.a = new String[] { "\u4e7e", "\u9707", "\u574e", "\u826e", "\u5764", "\u5dfd", "\u79bb", "\u5151" };
        WordGeneratorChinese.b = new String[] { "\u89d2", "\u4ea2", "\u6c10", "\u623f", "\u5fc3", "\u5c3e", "\u7b95", "\u6597", "\u725b", "\u5973", "\u865a", "\u5371", "\u5ba4", "\u58c1", "\u594e", "\u5a04", "\u80c3", "\u6634", "\u6bd5", "\u89dc", "\u53c2", "\u4e95", "\u9b3c", "\u67f3", "\u661f", "\u5f20", "\u7ffc", "\u8f78" };
        WordGeneratorChinese.suffix = "\u5ea7";
    }
}
