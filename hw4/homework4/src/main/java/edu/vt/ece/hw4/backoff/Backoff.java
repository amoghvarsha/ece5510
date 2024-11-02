package edu.vt.ece.hw4.backoff;

public interface Backoff {
    void backoff() throws InterruptedException;
    void reset();
}
