package core.text_extractors;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.javatuples.Pair;

public class ParserPOSTFile {
	public Pair<String, String> getNameAndFilename(String value) {
		Pattern pattern = Pattern.compile("\".*?\"");
		Matcher matcher = pattern.matcher(value);
		
		boolean found = false;
		ArrayList<String> r = new ArrayList<String>();
    while (matcher.find()) {
			int beg = matcher.start();
			int end = matcher.end();
			found = true;
			  
			if ((end - beg) > 2)
				r.add(value.substring(matcher.start()+1, matcher.end()-1));
    }
    
    if(found) {
    	return Pair.fromCollection(r);
    } else {
    	return Pair.with("",  "");
    }
	}
}
