package framework;

import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utilitarian class which describes formatting of logs.
 */
public class Logger extends Reporter {

	protected final static org.slf4j.Logger logger = LoggerFactory.getLogger(Logger.class);

	/** Used to print out information into INFO log level. */
	public static void info(String log) {
		logger.info(log);
		log(log + "</br>");
	}

	/** Used to print out information into ERROR log level. */
	public static void err(String log) {
		logger.error(log);
	}

	/**
	 * Used to print out information about known issues.
	 * <p>
	 *    ! <b>Must be placed right before the line on which test will fail</b> !
	 * @param log
     */
	public static void knownIssue(String log) {
		logger.error(log);
		log("BUG:" + log + "</br>");
	}

	/** Provides information about failed test to custom report. */
	public static String getReasonForFailedTest(ITestResult result) {
		int num = getOutput(result).size();
		String reason;
		try {
			reason = getOutput(result).get(num - 1).split("BUG:")[1].split("</br>")[0];
		} catch (Exception e) {
			reason = "N/A";
		}
		return reason;
	}

	/** Prints out list of failed tests linked to known issues. */
	public static List<String> getTestInfoLogsList(ITestResult result) {
		return getOutput(result).stream().filter(u -> !u.contains("BUG")).collect(Collectors.toList());
	}

	/** Used to print out information into DEBUG log level*/
	public static void debug(String log) {
		logger.debug(log);
	}
}
