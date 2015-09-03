package com.workday.authservertester;
import java.util.Date;

public final class Stopwatch {
    static Date systemTime;
    static int MILLISECONDS_PER_SECOND = 1000;

    private Stopwatch(int seconds) {
    }
    
    public static void start(int seconds) {
        systemTime = new Date();
        long offset = systemTime.getTime() + (seconds * MILLISECONDS_PER_SECOND);
        System.out.println("Check on task at: " + offset);
        while (systemTime.getTime() < offset) {
            systemTime = new Date();
        }
        System.out.println("DONE!");
    }
}
