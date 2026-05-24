package hellfirepvp.astralsorcery.client.util.camera;

public interface ICameraPersistencyFunction
{
    boolean isExpired();
    
    void setExpired();
    
    void forceStop();
    
    boolean wasForciblyStopped();
}
