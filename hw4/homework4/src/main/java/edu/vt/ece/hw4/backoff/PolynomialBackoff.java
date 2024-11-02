package edu.vt.ece.hw4.backoff;

public class PolynomialBackoff implements Backoff {

    private int attempt = 1;
    private final int exponent;

    public PolynomialBackoff(int exponent) {
        this.exponent = exponent;
    }

    @Override
    public void backoff() throws InterruptedException {
        int delay = (int) Math.pow(attempt, exponent); // Calculate delay as a^e
        Thread.sleep(delay);
        attempt++;
    }

    @Override
    public void reset() {
        attempt = 1;
    }
}
