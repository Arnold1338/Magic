package hellfirepvp.astralsorcery.client.util.draw;

public class BufferBatchHelper
{
    public static BufferContext make() {
        return make(2097152);
    }
    
    public static BufferContext make(final int size) {
        return new BufferContext(size);
    }
}
