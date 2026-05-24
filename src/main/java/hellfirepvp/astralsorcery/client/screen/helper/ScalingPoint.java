package hellfirepvp.astralsorcery.client.screen.helper;

public class ScalingPoint
{
    private float posX;
    private float posY;
    private float scaledX;
    private float scaledY;
    
    private ScalingPoint() {
    }
    
    public static ScalingPoint createPoint(final float posX, final float posY, final float scale, final boolean arePositionsScaled) {
        final ScalingPoint sp = new ScalingPoint();
        if (arePositionsScaled) {
            sp.updateScaledPos(posX, posY, scale);
        }
        else {
            sp.updatePos(posX, posY, scale);
        }
        return sp;
    }
    
    public void updatePos(final float posX, final float posY, final float scale) {
        this.posX = posX;
        this.posY = posY;
        this.scaledX = scale * this.getPosX();
        this.scaledY = scale * this.getPosY();
    }
    
    public void updateScaledPos(final float scaledX, final float scaledY, final float scale) {
        this.scaledX = scaledX;
        this.scaledY = scaledY;
        this.posX = this.scaledX / scale;
        this.posY = this.scaledY / scale;
    }
    
    public float getPosY() {
        return this.posY;
    }
    
    public float getPosX() {
        return this.posX;
    }
    
    public float getScaledPosX() {
        return this.scaledX;
    }
    
    public float getScaledPosY() {
        return this.scaledY;
    }
    
    public void rescale(final float newScale) {
        this.scaledX = this.getPosX() * newScale;
        this.scaledY = this.getPosY() * newScale;
    }
}
