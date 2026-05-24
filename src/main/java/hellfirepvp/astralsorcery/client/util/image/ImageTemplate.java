package hellfirepvp.astralsorcery.client.util.image;

import org.apache.logging.log4j.util.TriConsumer;

public interface ImageTemplate
{
    int getWidth();
    
    int getHeight();
    
    void place(final TriConsumer<Integer, Integer, Integer> p0);
    
    public abstract static class Quad implements ImageTemplate
    {
        private final int width;
        private final int height;
        
        protected Quad(final int width, final int height) {
            this.width = width;
            this.height = height;
        }
        
        @Override
        public int getWidth() {
            return this.width;
        }
        
        @Override
        public int getHeight() {
            return this.height;
        }
    }
}
