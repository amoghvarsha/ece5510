package edu.vt.ece.hw4.backoff;

public class BackoffFactory {

    public static Backoff getBackoff(String name) {
        switch (name) {
            case "Exponential":
                return new ExponentialBackoff();
            case "Fibonacci":
                return new FibonacciBackoff();
            case "Linear":
                return new LinearBackoff();
            default:
                throw new IllegalArgumentException("Unknown backoff strategy: " + name);
        }
    }

    public static Backoff getBackoff(String name, int x) {
        switch (name) {
            case "Fixed":
                return new FixedBackoff(x);
            case "Polynomial":
                return new PolynomialBackoff(x);
            default:
                throw new IllegalArgumentException("Unknown backoff strategy: " + name);
        }
    }
}
