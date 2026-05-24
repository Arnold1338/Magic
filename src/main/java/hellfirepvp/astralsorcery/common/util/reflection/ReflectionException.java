package hellfirepvp.astralsorcery.common.util.reflection;

public class ReflectionException extends RuntimeException
{
    public ReflectionException() {
    }
    
    public ReflectionException(final String message) {
        super(message);
    }
    
    public ReflectionException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ReflectionException(final Throwable cause) {
        super(cause);
    }
    
    public ReflectionException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
