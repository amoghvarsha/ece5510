package edu.vt.ece.hw4.backoff;

public class LinearBackoff implements Backoff {
    
    private int attempt = 1;

    public LinearBackoff() {
        // Default constructor
    }

    @Override
    public void backoff() throws InterruptedException {
        Thread.sleep(attempt);
        attempt++;
    }

    @Override
    public void reset() {
        attempt = 1;
    }
}
