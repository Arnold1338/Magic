package hellfirepvp.astralsorcery.client.util.camera;

public interface ICameraTransformer
{
    int getPriority();
    
    void onClientTick();
    
    ICameraPersistencyFunction getPersistencyFunction();
    
    void onStartTransforming(final float p0);
    
    void onStopTransforming(final float p0);
    
    void transformRenderView(final float p0);
}
