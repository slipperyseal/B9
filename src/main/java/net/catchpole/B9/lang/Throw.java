package net.catchpole.B9.lang;

public class Throw {
    public static RuntimeException unchecked(Throwable throwable) {
        if (throwable == null) {
            throw new NullPointerException();
        }
        Throw.<RuntimeException>abyss(throwable);
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void abyss(Throwable t) throws T {
        throw (T) t;
    }
}
