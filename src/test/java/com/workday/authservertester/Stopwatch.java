package com.workday.authservertester;
import java.util.Date;

public class Stopwatch {
	Date systemTime;
	int timeToWait;
	int MILLISECONDS_PER_SECOND = 1000;

	public Stopwatch(int seconds) {
		systemTime = new Date();
		long offset = systemTime.getTime() + (seconds * MILLISECONDS_PER_SECOND);
		System.out.println("Check on task at: " + offset);
		while (systemTime.getTime() < offset) {
			systemTime = new Date();
		}
		System.out.println("DONE!");
	}
}