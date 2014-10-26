package servlets.protocols;


// troubles on deserialize
public class PathValue {
	//public final UserId
	// FIXME: final is removed, need getters
	public String pageName;
	public String genName;
	public Integer pointPos;
	
	private PathValue() {}
	
	public PathValue(String p, String g, Integer pos) {
		pageName = p;
		genName = g;
		pointPos = pos;
	}
}
