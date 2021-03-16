package com.example.JAR;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A class to make background tasks simpler
 */
public class Background {

    // The executor service that will be used to run background threads
    static final ExecutorService executor = Executors.newFixedThreadPool(4);

    /**
     * Passes an anonymous function to the background executor
     *
     * @param command an anonymous function
     */
    static void execute(Runnable command) {
        executor.execute(command);
    }
}
