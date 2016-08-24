package framework.platform;

/**
 * Enumerable collection of date patterns which can be used for date formatting.
 */
public enum DatePatterns {
	
	YYYY_MM_DD("yyyy-MM-dd"),
	EEEE_MMMMMMMMM_d("EEEE, MMMMMMMMM d"),
	EEEE_MMMMMMMMM_dd("EEEE, MMMMMMMMM dd"),
	MM_DD_YYYY("MM/dd/yyyy"),
	d("dd"),
	MMMMMMMMM_d_yyy("MMMMMMMMM d, yyy"),
	MM_dd_yyyy_HH_mm("MM/dd/yyyy HH:mm"),
	EEEE_MMM_d("EEEE MMM d"),
	MM("MM"),
	YYYY("YYYY");

	private String datePattern;

	private DatePatterns(String datePattern) {
		this.datePattern = datePattern;
	}

	/** Returns specified date pattern. */
	public String getPattern() {
		return datePattern;
	}

}
