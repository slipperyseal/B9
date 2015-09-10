package net.catchpole.B9.event;

/**
 * A Trigger will relay trigger events to a TriggerListener via a privately managed thread.
 * Events will be ignored if the TriggerListener is still processing when new events arrive.
 * The purpose of the trigger is to decouple and serialize notifications such as timing events from a single processing loop.
 * Exceptions thrown from the TriggerListener will be eaten and will not terminate the thread.
 * Calling dispose() will interrupt the TriggerListener thread and allow it to terminate.
 */
public class Trigger implements TriggerListener {
    private final TriggerListener triggerListener;
    private final Thread thread = new Thread(new TriggerRunnable());
    private final Object lock = new Object();
    private volatile boolean run = true;

    public Trigger(TriggerListener triggerListener) {
        this.triggerListener = triggerListener;
        this.thread.start();
    }

    public void trigger() {
        synchronized (lock) {
            lock.notify();
        }
    }

    public void dispose() {
        this.run = false;
        thread.interrupt();
    }

    class TriggerRunnable implements Runnable {
        public void run() {
            while (run) {
                try {
                    synchronized (lock) {
                        lock.wait();
                    }
                    triggerListener.trigger();
                } catch (Throwable t) {
                    if (!run) {
                        return;
                    }
                }
            }
        }
    }
}
