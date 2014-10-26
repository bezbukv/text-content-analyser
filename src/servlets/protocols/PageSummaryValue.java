package servlets.protocols;

import java.util.List;

public class PageSummaryValue {
	public final String pageName;
	public final List<String> genNames;
	
	private PageSummaryValue(String pageName, List<String> genNames) {
		this.pageName = pageName;
		this.genNames = genNames;
	}
	
	public static PageSummaryValue create(String pageName, List<String> genNames) {
		return new PageSummaryValue(pageName, genNames);
	}
}
