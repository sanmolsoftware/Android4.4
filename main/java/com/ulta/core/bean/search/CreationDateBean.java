package com.ulta.core.bean.search;

import com.ulta.core.bean.UltaBean;


public class CreationDateBean extends UltaBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6215458936172497435L;
	private int date;
	private int day;
	private int hours;
	private int minutes;
	private int month;
	private double nanos;
	private double seconds;
	private double time;
	private double timezoneOffset;
	private double year;
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public double getNanos() {
		return nanos;
	}
	public void setNanos(double nanos) {
		this.nanos = nanos;
	}
	public double getSeconds() {
		return seconds;
	}
	public void setSeconds(double seconds) {
		this.seconds = seconds;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public double getTimezoneOffset() {
		return timezoneOffset;
	}
	public void setTimezoneOffset(double timezoneOffset) {
		this.timezoneOffset = timezoneOffset;
	}
	public double getYear() {
		return year;
	}
	public void setYear(double year) {
		this.year = year;
	}

}
