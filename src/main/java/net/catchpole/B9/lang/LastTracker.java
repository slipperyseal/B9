package net.catchpole.B9.lang;

public class LastTracker<T> {
    private T last;

    public T getLast(T current) {
        try {
            return this.last;
        } finally {
            this.last = current;
        }
    }

    public T getLast() {
        return last;
    }
}
