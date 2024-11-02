package edu.vt.ece.hw4;

import edu.vt.ece.hw4.barriers.Barrier;
import edu.vt.ece.hw4.barriers.TTASBarrier;
import edu.vt.ece.hw4.bench.*;
import edu.vt.ece.hw4.locks.ALock;
import edu.vt.ece.hw4.locks.BackoffLock;
import edu.vt.ece.hw4.locks.Lock;
import edu.vt.ece.hw4.locks.MCSLock;

import static edu.vt.ece.hw4.utils.DebugConfig.DEBUG;

import edu.vt.ece.hw4.backoff.BackoffFactory;

public class Benchmark {

    private static final String MODE      = "normal";
    private static final int THREAD_COUNT = 2;
    private static final int TOTAL_ITERS  = 64;

    private static final String ALOCK       = "ALock";
    private static final String BACKOFFLOCK = "BackoffLock";
    private static final String MCSLOCK     = "MCSLock";

    public static void main(String[] args) {

        try {
            if (DEBUG)
                printArgs(args);

            String mode      = (args.length <= 0 ? MODE         : args[0]);
            String lockClass = (args.length <= 1 ? ALOCK        : args[1]);
            int threadCount  = (args.length <= 2 ? THREAD_COUNT : Integer.parseInt(args[2]));
            int totalIters   = (args.length <= 3 ? TOTAL_ITERS  : Integer.parseInt(args[3]));
                    
            if (threadCount <= 0 || totalIters <= 0) {
                throw new IllegalArgumentException("Thread count and Total Iterations must be greater than zero.");
            }

            int iters = totalIters / threadCount;

            for (int i = 0; i < 5; i++) {
                run(args, mode, lockClass, threadCount, iters);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printArgs(String[] args) {
        System.out.println("\n************ Command-Line Arguments ************");
        if (args.length == 0) {
            System.out.println("No arguments provided.");
        } else {
            for (int i = 0; i < args.length; i++) {
                System.out.println((i + 1) + ". " + args[i]);
            }
        }
        System.out.println("************************************************\n");
    }

    private static Lock createBackoffLock(String[] args) throws IllegalArgumentException {
        if (args.length <= 4) {
            throw new IllegalArgumentException("Please specify the backoff strategy for BackoffLock.");
        }

        String backoffStrategy = args[4].trim();
        if (backoffStrategy.equals("Fixed") || backoffStrategy.equals("Polynomial")) {
            if (args.length <= 5) {
                throw new IllegalArgumentException("Please specify the parameter for " + backoffStrategy + " backoff strategy.");
            }
            int parameter = Integer.parseInt(args[5]);
            return new BackoffLock(BackoffFactory.getBackoff(backoffStrategy, parameter));
        } else {
            return new BackoffLock(BackoffFactory.getBackoff(backoffStrategy));
        }
    }

    private static Lock createLock(String[] args, int threadCount) throws IllegalArgumentException {
        String lockClass = args[1].trim();
        
        switch (lockClass) {
            case ALOCK:
                return new ALock(threadCount);
            case BACKOFFLOCK:
                return createBackoffLock(args);
            case MCSLOCK:
                return new MCSLock();
            default:
                throw new IllegalArgumentException("Unknown Lock: " + lockClass);
        }
    }

    private static void run(String[] args, String mode, String lockClass, int threadCount, int iters) throws Exception {
        Lock lock = createLock(args, threadCount);
        mode = mode.trim().toLowerCase();

        switch (mode) {
            case "normal":
                final Counter counter = new SharedCounter(0, lock);
                runNormal(counter, threadCount, iters);
                break;
            case "empty":
                runEmptyCS(lock, threadCount, iters);
                break;
            case "long":
                runLongCS(lock, threadCount, iters);
                break;
            case "barrier":
                //Barrier b = new TTASBarrier();
                throw new UnsupportedOperationException("Complete barrier mode");
            default:
                throw new IllegalArgumentException("Unknown Mode: " + mode);
        }
    }

    private static void runNormal(Counter counter, int threadCount, int iters) throws Exception {
        final TestThread[] threads = new TestThread[threadCount];
        TestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new TestThread(counter, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }

    private static void runEmptyCS(Lock lock, int threadCount, int iters) throws Exception {

        final EmptyCSTestThread[] threads = new EmptyCSTestThread[threadCount];
        EmptyCSTestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new EmptyCSTestThread(lock, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }

    static void runLongCS(Lock lock, int threadCount, int iters) throws Exception {
        final Counter counter = new Counter(0);
        final LongCSTestThread[] threads = new LongCSTestThread[threadCount];
        LongCSTestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new LongCSTestThread(lock, counter, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }
}