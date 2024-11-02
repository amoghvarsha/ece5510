package edu.vt.ece.hw4.backoff;

public class FixedBackoff implements Backoff {
    
    private final int fixedDelay;

    public FixedBackoff(int fixedDelay) {
        this.fixedDelay = fixedDelay;
    }

    @Override
    public void backoff() throws InterruptedException {
        Thread.sleep(fixedDelay);
    }

    @Override
    public void reset() {
        // No state to reset in this case
    }
}

