package doclet_living_doc;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

public class Rundoc {
	public static void main(String[] args) {
		String pathToTestSources = "/usr/src/to_doc";
		File file = new File(pathToTestSources);
		String[] directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		System.out.println(Arrays.toString(directories));
		  String[] params = new String[] {
				  	"-doclet", "doclet_living_doc.ExtractCommentsDoclet", 
				  	"-sourcepath", pathToTestSources,
				  	"-subpackages", String.join(":", directories)
		    };
		    com.sun.tools.javadoc.Main.execute(params);
	}
}
