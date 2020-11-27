package com.example.JAR;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Background {

    static final ExecutorService executor = Executors.newFixedThreadPool(4);

    static void execute(Runnable command) {
        executor.execute(command);
    }
}
