package edu.vt.ece.hw4.backoff;

public class FibonacciBackoff implements Backoff {
    
    private int prev = 0;
    private int curr = 1;

    @Override
    public void backoff() throws InterruptedException {
        int delay = curr;
        Thread.sleep(delay);
        int next = prev + curr;
        prev = curr;
        curr = next;
    }

    @Override
    public void reset() {
        prev = 0;
        curr = 1;
    }
}
