package hellfirepvp.astralsorcery.client.screen.helper;

public final class ScreenRenderBoundingBox
{
    private final double lx;
    private final double ly;
    private final double hx;
    private final double hy;
    
    public ScreenRenderBoundingBox(final double lx, final double ly, final double hx, final double hy) {
        this.lx = lx;
        this.ly = ly;
        this.hx = hx;
        this.hy = hy;
    }
    
    public boolean isInBox(final double currX, final double currY) {
        return currX >= this.lx && currX <= this.hx && currY >= this.ly && currY <= this.hy;
    }
    
    public double getMidX() {
        return this.getWidth() / 2.0;
    }
    
    public double getMidY() {
        return this.getHeight() / 2.0;
    }
    
    public double getLowerX() {
        return this.lx;
    }
    
    public double getLowerY() {
        return this.ly;
    }
    
    public double getWidth() {
        return this.hx - this.lx;
    }
    
    public double getHeight() {
        return this.hy - this.ly;
    }
    
    @Override
    public String toString() {
        return "ScreenRenderBoundingBox{" + this.lx + "," + this.ly + " to " + this.hx + "," + this.hy + "}";
    }
}
