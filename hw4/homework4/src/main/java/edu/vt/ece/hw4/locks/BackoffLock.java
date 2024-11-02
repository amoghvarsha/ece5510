package edu.vt.ece.hw4.locks;

import edu.vt.ece.hw4.backoff.Backoff;

import java.util.concurrent.atomic.AtomicBoolean;

public class BackoffLock implements Lock {

    private final AtomicBoolean state;
    private final Backoff backoff;

    // Constructor accepts a Backoff instance directly
    public BackoffLock(Backoff backoff) {
        this.state = new AtomicBoolean(false);
        this.backoff = backoff;
    }

    @Override
    public void lock() {
        while (true) {
            while (state.get()) {           // Spin until the lock becomes available
            }
            if (!state.getAndSet(true)) {   // Try to acquire the lock
                return;
            } else {                        // Backoff on failure
                try {
                    backoff.backoff();
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public void unlock() {
        backoff.reset();    // Reset the backoff state when unlocking
        state.set(false);   // Release the lock
    }
}
