package net.catchpole.B9.codec.transcoder;

public interface FieldInterceptor<V> {
    V intercept(V currentValue, V newValue);
}
