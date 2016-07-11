/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.util.log;

import com.ulta.core.conf.types.LogLevel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static com.ulta.core.conf.UltaConstants.ISDEBUG;
import static com.ulta.core.conf.types.LogLevel.DEBUG;
import static com.ulta.core.conf.types.LogLevel.ERR;



/**
 * Class to log statements.
 * 
 * @author viva
 */
public final class Logger {

	/**
	 * Log file location.
	 */
	public static final String LOG_LOCATION = "/sdcard/UltaLog.txt";
	/**
	 * The Constant TAG.
	 */
	private static final String TAG = "Ulta";

	/**
	 * Instantiates a new logger.
	 */
	private Logger() {

	}

	/**
	 * Log.
	 * 
	 * @param log
	 *            the log
	 */
	public static void Log(final String log) {
		Log(log, DEBUG);
	}

	/**
	 * Log.
	 * 
	 * @param log
	 *            the log
	 */
	public static void Log(final Object log) {
		Log(log.toString());
	}

	/**
	 * Log.
	 * 
	 * @param throwable
	 *            the throwable
	 */
	public static void Log(final Throwable throwable) {
		final StringBuilder buffer = new StringBuilder();
		final PrintStream errStream = new PrintStream(new OutputStream() {
			@Override
			public void write(int oneByte) throws IOException {
				buffer.append((char) oneByte);
			}
		});
		throwable.printStackTrace(errStream);
		Log(buffer.toString(), ERR);
	}

	/**
	 * Log.
	 * 
	 * @param log
	 *            the log
	 * @param level
	 *            the level
	 */
	public static void Log(String log, LogLevel level) {
		switch (level) {
		case DEBUG:
			if (ISDEBUG) {
				android.util.Log.d(TAG, log);
			}
			break;
		case VERBOSE:
			android.util.Log.v(TAG, log);
			break;
		case INFO:
			android.util.Log.i(TAG, log);
			break;
		case WARN:
			android.util.Log.w(TAG, log);
			break;
		case ERR:
			android.util.Log.e(TAG, log);
			break;
		default:
			break;
		}

	}
	
	/**
	 * Method to write log.
	 *
	 * @param fileLogContent the file log content
	 */
	public static void writeLog(String fileLogContent) {
		Logger.Log("[Logger]{writeLog}(content)<>>" + fileLogContent);
		/*FileHandler fh = null;
		String name;
		if (0 == Environment.getExternalStorageState().compareTo( Environment.MEDIA_MOUNTED))
			name = Environment.getExternalStorageDirectory().getAbsolutePath();
		else
			name = Environment.getDataDirectory().getAbsolutePath();
		name += "/response.log";
		try {
			fh = new FileHandler(name, 256 * 1024, 1, true);
			fh.setFormatter(new SimpleFormatter());
			fh.publish(new LogRecord(Level.ALL, fileLogContent));
		} catch (Exception exception) {
			Logger.Log("[Logger]{writeLog}(FileHandler exception)<>>" + exception,LogLevel.ERR);
			return;
		} finally {
			if (fh != null)
				fh.close();
		}*/
	}
}
