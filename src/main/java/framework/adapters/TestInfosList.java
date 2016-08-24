package framework.adapters;

import java.util.ArrayList;
import java.util.List;

/**
 * List of test info's.
 */
public class TestInfosList {
	private final String className;
	private List<TestInfo> testInfos;

	public TestInfosList(String className) {
		this.className = className;
		this.testInfos = new ArrayList<TestInfo>();
	}

	public String getClassName() {
		return className;
	}

	public List<TestInfo> getTestInfos() {
		return testInfos;
	}

	public void setTestInfos(List<TestInfo> testInfos) {
		this.testInfos = testInfos;
	}
}
