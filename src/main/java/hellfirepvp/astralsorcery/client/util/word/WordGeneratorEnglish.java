package hellfirepvp.astralsorcery.client.util.word;

import java.util.Random;

public class WordGeneratorEnglish extends RandomWordGenerator
{
    private static String[] vows;
    private static String[] cons;
    
    @Override
    public String generateWord(final long seed, final int length) {
        final Random sRand = new Random(seed);
        boolean toggleVowel = sRand.nextFloat() > 0.8f;
        final StringBuilder word = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            final String[] sel = toggleVowel ? WordGeneratorEnglish.vows : WordGeneratorEnglish.cons;
            word.append(sel[sRand.nextInt(sel.length)]);
            toggleVowel = !toggleVowel;
        }
        return word.toString();
    }
    
    static {
        WordGeneratorEnglish.vows = new String[] { "a", "e", "i", "o", "u" };
        WordGeneratorEnglish.cons = new String[] { "b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "ph", "qu", "r", "s", "t", "v", "w", "x", "y", "z", "tt", "ch", "sh" };
    }
}
