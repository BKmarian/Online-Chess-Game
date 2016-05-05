import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

abstract class AbstractLogger {
	public static int INFO = 1;
	public static int DEBUG = 2;
	public static int ERROR = 3;
	public static PrintWriter logger;
	protected int level;

	// next element in chain or responsibility
	protected AbstractLogger nextLogger;

	public AbstractLogger() throws FileNotFoundException {
		if (logger == null)
			logger = new PrintWriter(new FileOutputStream(new File("logger.txt")), true);
	}

	public void setNextLogger(AbstractLogger nextLogger) {
		this.nextLogger = nextLogger;
	}

	public void logMessage(int level, String message) {
		if (this.level <= level) {
			print(message);
		}
		if (nextLogger != null) {
			nextLogger.logMessage(level, message);
		}
	}
	public void close() {
		logger.close();
	}
	abstract protected void print(String message);

}

class InfoLogger extends AbstractLogger {

	public InfoLogger(int level) throws FileNotFoundException {
		this.level = level;
	}

	@Override
	protected void print(String message) {
		logger.println("Standard  info::Logger: " + message);
	}
}

class ErrorLogger extends AbstractLogger {

	public ErrorLogger(int level) throws FileNotFoundException {
		this.level = level;
	}

	@Override
	protected void print(String message) {
		logger.println("Error Console::Logger: " + message);
	}
}

class DebugLogger extends AbstractLogger {

	public DebugLogger(int level) throws FileNotFoundException {
		this.level = level;
	}

	@Override
	protected void print(String message) {
		logger.println("Debug::Logger: " + message);
	}
}

class Logger {

	public static AbstractLogger getChainOfLoggers() throws FileNotFoundException {

		AbstractLogger errorLogger = new ErrorLogger(AbstractLogger.ERROR);
		AbstractLogger debugLogger = new DebugLogger(AbstractLogger.DEBUG);
		AbstractLogger infoLogger = new InfoLogger(AbstractLogger.INFO);

		errorLogger.setNextLogger(debugLogger);
		infoLogger.setNextLogger(infoLogger);

		return errorLogger;
	}
}