package hellfirepvp.astralsorcery.common.util;

import javax.annotation.Nullable;

public interface ServerLifecycleListener
{
    void onServerStart();
    
    void onServerStop();
    
    default ServerLifecycleListener stop(final Runnable onStop) {
        return wrap(null, onStop);
    }
    
    default ServerLifecycleListener start(final Runnable onStart) {
        return wrap(onStart, null);
    }
    
    default ServerLifecycleListener wrap(@Nullable final Runnable onStart, @Nullable final Runnable onStop) {
        return new ServerLifecycleListener() {
            @Override
            public void onServerStart() {
                if (onStart != null) {
                    onStart.run();
                }
            }
            
            @Override
            public void onServerStop() {
                if (onStop != null) {
                    onStop.run();
                }
            }
        };
    }
}
