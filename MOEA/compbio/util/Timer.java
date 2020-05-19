/*
 * @(#)Timer.java 1.0 June 2010 Copyright (c) 2010 Peter Troshin JRONN version:
 * 3.1 This library is free software; you can redistribute it and/or modify it
 * under the terms of the Apache License version 2 as published by the Apache
 * Software Foundation This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Apache License
 * for more details. A copy of the license is in apache_license.txt. It is also
 * available here: see: http://www.apache.org/licenses/LICENSE-2.0.txt Any
 * republication or derived work distributed in source code form must include
 * this copyright and license notice.
 */
package util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * A simple timer, calculates the time interval between two events. Keeps two
 * counters, one for long time intervals, to measure time between the start and
 * end of the application for instance, and another for short events, to measure
 * how long it took to reach a next block of code. This class is immutable and
 * as such thread safe.
 * 
 * @author Peter Troshin
 * @version 1.0 September 2010
 * 
 */
public class Timer {

	private long checkPoint;
	private final long startTime;
	private TimeUnit reportTimeUnit;

	/*
	 * Place to write the statistics to, handy for the clients
	 */
	private PrintWriter statWriter;

	/**
	 * Constructs the new instance of Timer, initiate default time reporting
	 * unit to seconds.
	 */
	public Timer() {
		startTime = System.nanoTime();
		checkPoint = startTime;
		// set default time unit for reporting
		reportTimeUnit = TimeUnit.SECONDS;
	}

	/**
	 * Coping constructor
	 * 
	 * @param timer
	 */
	public Timer(Timer timer) {
		this();
		this.reportTimeUnit = timer.reportTimeUnit;
		this.statWriter = timer.getStatWriter();
	}

	public Timer(OutputStream statistics) throws IOException {
		this();
		setStatOutput(statistics);
	}

	public synchronized void setStatOutput(OutputStream statistics) {
		if (statistics != null) {
			statWriter = new PrintWriter(new OutputStreamWriter(statistics),
					true);
		} else {
			statWriter = new PrintWriter(new NullOutputStream());
		}
	}

	/**
	 * A plate to write the execution statistics to.
	 * 
	 * @return PrintWriter
	 * @throws IllegalStateException
	 *             if default Timer constructor was used instead of output
	 *             stream or file constructors
	 */
	public PrintWriter getStatWriter() {
		if (statWriter == null) {
			throw new IllegalStateException("Must use file or "
					+ "Output stream constructor is writer is required!");
		}
		return statWriter;
	}

	public void print(String statMessage) {
		getStatWriter().print(statMessage);
	}

	public void println(String statMessage) {
		getStatWriter().println(statMessage);
	}

	/**
	 * Make an instance of Timer which would report time in time unit provided
	 * 
	 * @param reportIn
	 *            - the time unit to report time in e.g.
	 *            {@code TimeUnit.SECONDS}
	 * @see TimeUnit
	 */
	public Timer(final TimeUnit reportIn) {
		this();
		reportTimeUnit = reportIn;
	}

	/**
	 * Convenience factory for micro seconds timer (10-E6 sec) functionally
	 * equivalent to new Timer(TimeUnit.MICROSECONDS).
	 * 
	 * @return Timer instance
	 */
	public static Timer getMicroSecondsTimer() {
		return new Timer(TimeUnit.MICROSECONDS);
	}

	/**
	 * Convenience factory for minutes timer functionally equivalent to new
	 * Timer(TimeUnit.MINUTES).
	 * 
	 * @return Timer instance
	 */
	public static Timer getMinutesTimer() {
		return new Timer(TimeUnit.MINUTES);
	}

	/**
	 * Convenience factory for milliseconds timer (10-E3 sec) functionally
	 * equivalent to new Timer(TimeUnit.MILLISECONDS).
	 * 
	 * @return Timer instance
	 */
	public static Timer getMilliSecondsTimer() {
		return new Timer(TimeUnit.MILLISECONDS);
	}

	private void checkPoint() {
		checkPoint = System.nanoTime();
	}

	/**
	 * Calculates the amount of time passed from previous call of this method in
	 * the time unit supplied as a parameter
	 * 
	 * @param tunit
	 * @return the amount of time passed from the last call of this method
	 */
	public long getStepTime(final TimeUnit tunit) {
		final long duration = tunit.convert(System.nanoTime() - checkPoint,
				TimeUnit.NANOSECONDS);
		checkPoint();
		return duration;
	}

	/**
	 * Calculates the amount of time passed from the last call of this method
	 * The reporting units are the time units provided in the time of the
	 * {@code Timer} instantiation. Default timer instance reports in seconds.
	 * 
	 * {@link Timer#getStepTime()}
	 * 
	 * @return the amount of time passed from previous call of this method
	 */
	public long getStepTime() {
		return getStepTime(reportTimeUnit);
	}

	/**
	 * Calculates the amount of time passed from the constructing of this
	 * instance. Time is reported in the time units provided during
	 * initialization of the Timer instance.
	 * 
	 * @return the amount of time passed from initiation of this instance
	 */
	public long getTotalTime() {
		return getTotalTime(reportTimeUnit);
	}

	/**
	 * Calculates the amount of time passed from the constructing of this
	 * instance convert this amount into the time units requested
	 * 
	 * @return the time passed from initiation of this instance in the time unit
	 *         requested
	 */
	public long getTotalTime(final TimeUnit tunit) {
		return tunit.convert(System.nanoTime() - startTime,
				TimeUnit.NANOSECONDS);
	}
}
