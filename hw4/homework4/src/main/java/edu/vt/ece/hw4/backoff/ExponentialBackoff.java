package edu.vt.ece.hw4.backoff;

public class ExponentialBackoff implements Backoff {
    
    private int attempt = 0;

    public ExponentialBackoff() {
        // Default constructor
    }

    @Override
    public void backoff() throws InterruptedException {
        int delay = (int) Math.pow(2, attempt);
        Thread.sleep(delay);
        attempt++;
    }

    @Override
    public void reset() {
        attempt = 0;
    }
}
