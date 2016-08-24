package framework.adapters;

import java.util.ArrayList;
import java.util.List;

/**
 * Object which saves all information about specific test.
 */
public class TestInfo {
	private String name;
	private String duration;
	private String stackTrace;
	private String screenshotUrl;
	private String htmlUrl;
	private String ticket;
	private List<String> description;
	private List<String> log;

	private static final String DESCRIPTION_FORMAT = "<a href=\"http://testmanagementtool.com/%s\">%s</a>";

	public List<String> getLog() {
		return log;
	}

	public void setLog(List<String> log) {
		this.log = log;
	}

	public String getName() {
		return name;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public String getScreenshotUrl() {
		return screenshotUrl;
	}

	public void setScreenshotUrl(String screenshotUrl) {
		this.screenshotUrl = screenshotUrl;
	}

	public String getHtmlUrl() {
		return htmlUrl;
	}

	public void setHTMLUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	public List<?> getDescription() {
		return description;
	}

	/** Will add hyperlink to the specified test management tool into test method name at the custom-report.html */
	public void parseDescription(String description) {
		List<String> descript = new ArrayList<String>();
		if (description != null) {
			String[] temp = description.split(";");
			for (String t : temp) {
				t = t.trim();
				int start = t.indexOf(" C");
				if (start > 0) {
					descript.add(String.format(DESCRIPTION_FORMAT, t.substring(start + 2), t.substring(0, start)));
				} else {
					descript.add(t);
				}
			}
		}
		this.description = descript;
	}
}
